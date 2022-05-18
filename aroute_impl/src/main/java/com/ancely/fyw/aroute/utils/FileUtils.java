package com.ancely.fyw.aroute.utils;

/*
 *  @项目名：  Componentization
 *  @包名：    com.ancely.fyw.aroute.utils
 *  @文件名:   FileUtils
 *  @创建者:   admin
 *  @创建时间:  2022/5/18 9:49
 *  @描述：    文件相关操作
 */

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtils {
    public static final String TAG = "ImageExt";
    public static String ALBUM_DIR = Environment.DIRECTORY_PICTURES;

    /**
     * 保存图片Stream到相册的Pictures文件夹
     *
     * @param context      上下文
     * @param fileName     文件名。 需要携带后缀
     * @param relativePath 相对于Pictures的路径
     */
    public static void saveToAlbum(InputStream inputStream, Context context, String fileName, String relativePath) throws IOException {
        ContentResolver resolver = context.getContentResolver();
        OutputFileTaker outputFileTaker = new OutputFileTaker();
        fileName = "IMG_" + System.currentTimeMillis() + fileName;
        Uri imageUri = insertMediaImage(resolver, fileName, relativePath, outputFileTaker);
        if (imageUri == null) {
            Log.w(TAG, "insert: error: uri == null");
            return;
        }
        OutputStream outputStream = outputStream(resolver, imageUri);
        if (outputStream == null) {
            return;
        }
        if (inputStream == null) {
            return;
        }
        copyTo(inputStream, outputStream);
        finishPending(imageUri, context, resolver, outputFileTaker.getFile());
    }

    private static void copyTo(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] buffer = new byte[8 * 1024];
        int bytes = inputStream.read(buffer);
        while (bytes >= 0) {
            outputStream.write(buffer, 0, bytes);
            bytes = inputStream.read(buffer);
        }
        outputStream.flush();
        outputStream.close();
        inputStream.close();
    }

    private static Uri insertMediaImage(ContentResolver resolver, String fileName, String relativePath, OutputFileTaker outputFileTaker) {
        // 图片信息
        ContentValues contentValues = new ContentValues();
        String mimeType = getMimeType(fileName);
        if (!TextUtils.isEmpty(mimeType)) {
            contentValues.put(MediaStore.Images.Media.MIME_TYPE, mimeType);
        }
        long date = System.currentTimeMillis() / 1000L;
        contentValues.put(MediaStore.Images.Media.DATE_ADDED, date);
        contentValues.put(MediaStore.Images.Media.DATE_MODIFIED, date);

        // 保存的位置
        Uri collection;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            String path;
            if (!TextUtils.isEmpty(relativePath)) {
                path = ALBUM_DIR + File.separator + relativePath;
            } else {
                path = ALBUM_DIR;
            }
            contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
            contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, path);
            contentValues.put(MediaStore.Images.Media.IS_PENDING, 1);
            collection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
            // 高版本不用查重直接插入，会自动重命名

        } else {
            File pictureFile = Environment.getExternalStoragePublicDirectory(ALBUM_DIR);
            File saveDir;
            if (!TextUtils.isEmpty(relativePath)) {
                saveDir = new File(pictureFile, relativePath);
            } else {
                saveDir = pictureFile;
            }
            if (!saveDir.exists() && !saveDir.mkdirs()) {
                Log.e(TAG, "save: error: can't create Pictures directory");
                return null;
            }
            // 文件路径查重，重复的话在文件名后拼接数字
            File imageFile = new File(saveDir, fileName);
            String imageFileName = imageFile.getName();
            String fileNameWithoutExtension;
            String fileExtension;
            int index = imageFileName.lastIndexOf(".");
            if (index == -1) {
                fileNameWithoutExtension = imageFileName;
                fileExtension = imageFileName;
            } else {
                fileNameWithoutExtension = imageFileName.substring(0, index);
                fileExtension = imageFileName.substring(index + 1);
            }
            Uri queryUri = queryMediaImage28(resolver, imageFile.getAbsolutePath());
            int suffix = 1;
            while (queryUri != null) {
                String newName = fileNameWithoutExtension + (suffix++) + "." + fileExtension;
                imageFile = new File(saveDir, newName);
                queryUri = queryMediaImage28(resolver, imageFile.getAbsolutePath());
            }

            contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, imageFile.getName());
            // 保存路径
            String imagePath = imageFile.getAbsolutePath();
            Log.v(TAG, "save file: " + imagePath);
            contentValues.put(MediaStore.Images.Media.DATA, imagePath);
            outputFileTaker.setFile(imageFile);
            collection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        }
        // 插入图片信息
        return resolver.insert(collection, contentValues);
    }

    private static Uri queryMediaImage28(ContentResolver resolver, String imagePath) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) return null;
        File imageFile = new File(imagePath);
        if (imageFile.canRead() && imageFile.exists()) {
            Log.v(TAG, "query: path: $imagePath exists");
            // 文件已存在，返回一个file://xxx的uri
            return Uri.fromFile(imageFile);
        }

        // 保存的位置
        // 查询是否已经存在相同图片
        Cursor query = resolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA},
                MediaStore.Images.Media.DATA + " == ?",
                new String[]{imagePath}, null);

        if (query == null) {
            return null;
        }
        Uri uri = null;
        while (query.moveToNext()) {
            int idColumn = query.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
            long id = query.getLong(idColumn);
            uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
            Log.v(TAG, "query: path: $imagePath exists uri: $existsUri");
        }
        query.close();
        return uri;
    }

    private static String getMimeType(String fileName) {
        String fileN = fileName.toLowerCase();
        if (fileN.endsWith(".png")) {
            return "image/png";
        } else if (fileN.endsWith(".jpg")) {
            return "image/jpeg";
        } else if (fileN.endsWith(".webp")) {
            return "image/webp";
        } else if (fileN.endsWith(".gif")) {
            return "image/gif";
        } else {
            return "";
        }
    }

    private static void finishPending(Uri imageUri, Context context, ContentResolver resolver, File outputFile) {
        ContentValues imageValues = new ContentValues();
        if (Build.VERSION.SDK_INT < 29) {
            if (outputFile != null) {
                imageValues.put(MediaStore.Images.Media.SIZE, outputFile.length());
            }

            resolver.update(imageUri, imageValues, null, null);
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, imageUri);
            context.sendBroadcast(intent);
        } else {
            // Android Q添加了IS_PENDING状态，为0时其他应用才可见
            imageValues.put(MediaStore.Images.Media.IS_PENDING, 0);
            resolver.update(imageUri, imageValues, null, null);
        }

    }


    private static OutputStream outputStream(ContentResolver resolver, Uri uri) {
        try {
            return resolver.openOutputStream(uri);
        } catch (FileNotFoundException e) {
            Log.e(TAG, "save: open stream error: " + e.getMessage());
        }
        return null;
    }

    public static class OutputFileTaker {
        private File file;

        public OutputFileTaker() {
        }

        public File getFile() {
            return file;
        }

        public OutputFileTaker(@Nullable File file) {
            this.file = file;
        }

        public void setFile(File file) {
            this.file = file;
        }
    }
}
