package com.ancely.fyw.aroute.utils;

import android.util.Log;

import java.util.Formatter;

public class LogUtils {

    private static boolean IsDebug = true;

    public static void setIsDebug(boolean isDebug) {
        IsDebug = isDebug;
    }

    public static boolean isIsDebug() {
        return IsDebug;
    }

    private static String getNameFromTrace(StackTraceElement[] traceElements, int place) {
        StringBuilder builder = new StringBuilder();
        if (traceElements != null && traceElements.length > place) {
            StackTraceElement traceElement = traceElements[place];
            builder.append(traceElement.getMethodName());
            builder.append("(").append(traceElement.getFileName()).append(":").append(traceElement.getLineNumber()).append(")");
        }
        return builder.toString();
    }

    private static String getContent(String msg, int place, Object... args) {
        try {
            String sourceLinks = getNameFromTrace(Thread.currentThread().getStackTrace(), place);
            return sourceLinks + String.format(msg, args);
        } catch (Throwable throwable) {
            return msg;
        }
    }

    public static void e(String tag, String content, Object... args) {
        if (!IsDebug) {
            return;
        }
        Log.e(tag, getString(4) + ": " + content);
//        for (int i = 0; i < Thread.currentThread().getStackTrace().length; i++) {
//            String realContent = getContent(content,i,args);
//            Log.e(tag, realContent);
//        }
    }

    public static void i(String tag, String content, Object... args) {
        if (!IsDebug) {
            return;
        }
        Log.i(tag, getString(4) + ": " + content);
    }

    public static String getString(int space) {
        StackTraceElement targetElement = Thread.currentThread().getStackTrace()[space];
        return new Formatter()
                .format("%s, %s(%s:%d)",
                        Thread.currentThread().getName(),
                        targetElement.getMethodName(),
                        targetElement.getFileName(),
                        targetElement.getLineNumber())
                .toString();
    }
}
