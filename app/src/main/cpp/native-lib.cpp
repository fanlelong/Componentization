
#include <jni.h>
#include <string>

#include <android/native_window_jni.h>
#include <zconf.h>

#include <android/bitmap.h>
#include <android/log.h>
#include <malloc.h>
#include <csetjmp>
#include <sys/stat.h>

extern "C" {
#include <hafuman/jpeglib.h>
#include <hafuman/turbojpeg.h>
}
void writeImg(uint8_t *data, const char *path, jint quality, int w, int h);

extern "C"
JNIEXPORT void JNICALL
Java_com_ancely_ffmpeg_PlayerManager_native_1compress(JNIEnv *env, jobject instance, jobject bitmap,
                                                      jstring path_, jint quality) {
    const char *path = env->GetStringUTFChars(path_, 0);


    // 图片解析成一个像素数组
    AndroidBitmapInfo bitmapInfo;
    AndroidBitmap_getInfo(env, bitmap, &bitmapInfo);
    uint8_t *pixels;
    AndroidBitmap_lockPixels(env, bitmap, (void **) &pixels);
    int h = bitmapInfo.height;
    int w = bitmapInfo.width;

    //开一块内存用来保存rgb信息
    uint8_t *data, *tmpData;
    data = (uint8_t *) malloc(w * h * 3);
    tmpData = data;
    uint8_t r, g, b;
    int color;//一个int 4个字节 相当于argb用来存放一个argb
    //遍历一个像素点 将rgb 提取出来放到一个数组里面
    for (int i = 0; i < h; ++i) {
        for (int j = 0; j < w; ++j) {
            color = *((int *) pixels);
//            r = ((color & 0x00FF0000) >> 16);
//            g = ((color & 0x0000FF00) >> 8);
//            b = ((color & 0x000000FF));
            r = (color >> 16) & 0xff;//颜色是16进制 0x FFFFFF 16转为二进制  F为15 1111 1111 1111 1111 1111 1111
            g = (color >> 8) & 0xff;
            b = color & 0xff;
            *data = b;
            *(data + 1) = g;
            *(data + 2) = r;
            data += 3;
            pixels += 4;
        }
    }

    writeImg(tmpData, path, quality, w, h);
    free(tmpData);

    AndroidBitmap_unlockPixels(env, bitmap);
    env->ReleaseStringUTFChars(path_, path);
}

void writeImg(uint8_t *data, const char *path, jint quality, int w, int h) {
    struct jpeg_compress_struct jpeg_struct;
    //    设置错误处理信息
    jpeg_error_mgr err;
    jpeg_struct.err = jpeg_std_error(&err);
//    给结构体分配内存
    jpeg_create_compress(&jpeg_struct);

    FILE *file = fopen(path, "wb");
//    设置输出路径
    jpeg_stdio_dest(&jpeg_struct, file);

    jpeg_struct.image_width = w;
    jpeg_struct.image_height = h;
    //采取哈夫曼编码   在skia 源码中    jpeg_struct.arith_code=TRUE
    jpeg_struct.arith_code = FALSE;
//优化编码
    jpeg_struct.optimize_coding = TRUE;
    jpeg_struct.in_color_space = JCS_RGB;

    jpeg_struct.input_components = 3;
//    其他的设置默认
    jpeg_set_defaults(&jpeg_struct);
    jpeg_set_quality(&jpeg_struct, quality, true);
    //------------------------jpeg的初始化

    jpeg_start_compress(&jpeg_struct, TRUE);

    JSAMPROW row_pointer[1];
//    一行的rgb
    int row_stride = w * 3;
    while (jpeg_struct.next_scanline < h) {
        row_pointer[0] = &data[jpeg_struct.next_scanline * row_stride];

//        这个函数一调用    jpeg_struct.next_scanline ++
        jpeg_write_scanlines(&jpeg_struct, row_pointer, 1);
    }
    jpeg_finish_compress(&jpeg_struct);
    jpeg_destroy_compress(&jpeg_struct);
    fclose(file);
}

typedef unsigned char uchar;

typedef struct tjp_info {
    int outwidth;
    int outheight;
    unsigned long jpg_size;
} tjp_info_t;


/*读取文件到内存*/
uchar *read_file2buffer(const char *filepath, tjp_info_t *tinfo) {
    FILE *fd;
    struct stat fileinfo;
    stat(filepath, &fileinfo);
    tinfo->jpg_size = fileinfo.st_size;

    fd = fopen(filepath, "rb");
    if (NULL == fd) {
        printf("file not open\n");
        return NULL;
    }
    uchar *data = (uchar *) malloc(sizeof(uchar) * fileinfo.st_size);
    fread(data, 1, fileinfo.st_size, fd);
    fclose(fd);
    return data;
}

/*写内存到文件*/
void write_buffer2file(const char *filename, uchar *buffer, int size) {
    FILE *fd = fopen(filename, "wb");
    if (NULL == fd) {
        return;
    }
    fwrite(buffer, 1, size, fd);
    fclose(fd);
}

/*图片解压缩*/
uchar *tjpeg_decompress(uchar *jpg_buffer, tjp_info_t *tinfo, jint optionSize) {
    tjhandle handle = NULL;
    int img_width, img_height, img_subsamp, img_colorspace;
    int flags = 0, pixelfmt = TJPF_RGB;
    /*创建一个turbojpeg句柄*/
    handle = tjInitDecompress();
    if (NULL == handle) {
        return NULL;
    }
    /*获取jpg图片相关信息但并不解压缩*/
    int ret = tjDecompressHeader3(handle, jpg_buffer, tinfo->jpg_size, &img_width, &img_height,
                                  &img_subsamp, &img_colorspace);
    if (0 != ret) {
        tjDestroy(handle);
        return NULL;
    }
    /*输出图片信息*/
    printf("jpeg width:%d\n", img_width);
    printf("jpeg height:%d\n", img_height);
    printf("jpeg subsamp:%d\n", img_subsamp);
    printf("jpeg colorspace:%d\n", img_colorspace);
    /*计算1/4缩放后的图像大小,若不缩放，那么直接将上面的尺寸赋值给输出尺寸*/
    tjscalingfactor sf;
    sf.num = 1;
    sf.denom =optionSize;
    tinfo->outwidth = TJSCALED(img_width, sf);
    tinfo->outheight = TJSCALED(img_height, sf);
    printf("w:%d,h:%d\n", tinfo->outwidth, tinfo->outheight);
    /*解压缩时，tjDecompress2（）会自动根据设置的大小进行缩放，但是设置的大小要在它的支持范围，如1/2 1/4等*/
    flags |= 0;
    int size = tinfo->outwidth * tinfo->outheight * 3;
    uchar *rgb_buffer = (uchar *) malloc(sizeof(uchar) * size);
    ret = tjDecompress2(handle, jpg_buffer, tinfo->jpg_size, rgb_buffer, tinfo->outwidth, 0,
                        tinfo->outheight, pixelfmt, flags);
    if (0 != ret) {
        tjDestroy(handle);
        return NULL;
    }
    tjDestroy(handle);
    return rgb_buffer;
}

/*压缩图片*/
int tjpeg_compress(uchar *rgb_buffer, tjp_info_t *tinfo, int quality, uchar **outjpg_buffer,
                   unsigned long *outjpg_size) {
    tjhandle handle = NULL;
    int flags = 0;
    int subsamp = TJSAMP_422;
    int pixelfmt = TJPF_RGB;
/*创建一个turbojpeg句柄*/
    handle = tjInitCompress();
    if (NULL == handle) {
        return -1;
    }
/*将rgb图或灰度图等压缩成jpeg格式图片*/
    int ret = tjCompress2(handle, rgb_buffer, tinfo->outwidth, 0, tinfo->outheight, pixelfmt,
                          outjpg_buffer, outjpg_size, subsamp, quality, flags);
    if (0 != ret) {
        tjDestroy(handle);
        return -1;
    }
    tjDestroy(handle);
    return 0;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_ancely_ffmpeg_PlayerManager_native_1compress_1from_1path(JNIEnv *env, jobject instance,
                                                                  jstring srcPath_,
                                                                  jstring decPath_, jint quality,
                                                                  jint optionSize) {
    const char *srcPath = env->GetStringUTFChars(srcPath_, 0);
    const char *decPath = env->GetStringUTFChars(decPath_, 0);

    tjp_info_t tinfo;
    /*读图像*/
    uchar *jpeg_buffer = read_file2buffer(srcPath,&tinfo);
    if (NULL == jpeg_buffer) {
        return;
    }

    /*解压缩*/
    uchar *rgb = tjpeg_decompress(jpeg_buffer, &tinfo, optionSize);
    if (NULL == rgb) {
        printf("error\n");
        free(jpeg_buffer);
        return ;
    }
    uchar *outjpeg=NULL;
    unsigned long outjpegsize;
    /*压缩*/
    tjpeg_compress(rgb,&tinfo,quality,&outjpeg,&outjpegsize);
    write_buffer2file(decPath,outjpeg,outjpegsize);
    free(jpeg_buffer);
    free(rgb);

    env->ReleaseStringUTFChars(srcPath_, srcPath);
    env->ReleaseStringUTFChars(decPath_, decPath);
}
