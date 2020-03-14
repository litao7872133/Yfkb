package com.yfkk.cardbag.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * 文件工具类
 * <p>
 * 建议App加载的图片，和下载的资源文件，放在外部缓存对应的文件夹下。如：getExternalFilePath("img")
 * 建议App内下载的文件（如安装包，聊天传的文件），放在外部短期缓存目录下。getExternalCachePath()
 * 建议App缓存的数据模型，序列化对象放在内部缓存中；如getInternalCachePath(),getInternalFilesPath()
 * <p>
 * Created by litao on 2018/1/24.
 */

public class FileUtils {

    /**
     * 外部缓存路径(存储短期数据，对应系统功能的清除缓存)
     * 不需要动态申请权限
     *
     * @return "/storage/emulated/0/Android/data/com.lishen.foster/cache/"
     */
    public static String getExternalCachePath(Context context) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            //外部存储可用
            return context.getExternalCacheDir().getPath();
        } else {
            //外部存储不可用
            return context.getCacheDir().getPath();
        }
    }

    /**
     * 外部文件路径(存储较长时间数据)
     * 不需要动态申请权限
     *
     * @param dirName files文件夹下，新建目录
     * @return "/storage/emulated/0/Android/data/com.lishen.foster/files/img"
     */
    public static String getExternalFilePath(Context context, String dirName) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            //外部存储可用
            return context.getExternalFilesDir(dirName).getPath();
        } else {
            //外部存储不可用
            return context.getFilesDir().getPath() + File.separator + dirName;
        }
    }

    /**
     * 内部文件路径(数据比较安全，不适用大量数据存储)
     * 不需要动态申请权限
     *
     * @return "/data/user/0/com.lishen.foster/files"
     */
    public static String getInternalFilesPath(Context context) {
        return context.getFilesDir().getPath();
    }

    /**
     * 内部缓存路径(数据比较安全，不适用大量数据存储)
     * 不需要动态申请权限
     *
     * @return "/data/user/0/com.lishen.foster/cache"
     */
    public static String getInternalCachePath(Context context) {
        return context.getCacheDir().getPath();
    }

    /**
     * 外部公共下载路径
     * 需要动态申请权限
     *
     * @return "/storage/emulated/0/Download"
     */
    public static String getExternalPublicDirDownload() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
    }

    /**
     * 外部公共图片路径
     * 需要动态申请权限
     *
     * @return "/storage/emulated/0/Pictures"
     */
    public static String getExternalPublicDirImg() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath();
    }

    /**
     * 通过路径获取uri
     *
     * @param filePath
     * @return
     */
    public static Uri getUri(Context context, String filePath) {

        if (!FileUtils.isFileExist(filePath)) {
            try {
                new File(filePath).createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // 通过FileProvider创建一个content类型的Uri
            return FileProvider.getUriForFile(context, "com.bonbvo.guardian.FileProvider",
                    new File(filePath));
        } else {
            return Uri.fromFile(new File(filePath));
        }
    }


    /**
     * 文件是否存在
     *
     * @param path
     * @return
     */
    public static boolean isFileExist(String path) {
        File file = new File(path);
        return file.exists();
    }

    /**
     * 删除文件或是整个目录
     *
     * @param file
     */
    public static void deleteFile(File file) {
        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                File files[] = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    deleteFile(files[i]);
                }
                file.delete();//如要保留文件夹，只删除文件，请注释这行
            }
        }
    }

    /**
     * 复制文件
     *
     * @param srcFile 源文件
     * @param newFile 新文件
     * @return 实际复制的字节数，如果文件、目录不存在、文件为null或者发生IO异常，返回-1
     * @author: litao
     */
    public static long copyFile(String srcFile, String newFile) {
        long copySizes = 0;

        if (!isFileExist(srcFile) || newFile == null) {
            copySizes = -1;
        }
        try {
            File file = new File(newFile);
            if (file.isDirectory()) {
                file.delete();
            }
            if (file.isFile()) {
                file.delete();
            }
            if (!file.isFile()) {
                file.getParentFile().mkdirs();// 创建目录
                file.createNewFile();
            }

            FileChannel fcin = new FileInputStream(srcFile).getChannel();
            FileChannel fcout = new FileOutputStream(newFile).getChannel();

            long size = fcin.size();
            fcin.transferTo(0, fcin.size(), fcout);

            fcin.close();
            fcout.close();
            copySizes = size;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return copySizes;
    }


    /**
     * 某路径是否存在，不存在则创建 返回 true: 文件夹存在，或创建成功 false: 不存在
     */
    public static boolean openOrCreatDir(String path) {
        File file = new File(path);
        file.isDirectory();
        if (false == file.exists()) {
            return file.mkdirs();
        }
        return true;
    }

    /**
     * 获取文件指定文件的指定单位的大小
     *
     * @param filePath 文件路径
     * @return double值的大小
     */
    public static double getFileOrFilesSize(String filePath) {
        File file = new File(filePath);
        long blockSize = 0;
        try {
            if (file.isDirectory()) {
                blockSize = getFileSizes(file);
            } else {
                blockSize = getFileSize(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return FormetFileSize(blockSize);
    }

    /**
     * 获取指定文件夹
     *
     * @param f
     * @return
     * @throws Exception
     */
    private static long getFileSizes(File f) throws Exception {
        long size = 0;
        File flist[] = f.listFiles();
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                size = size + getFileSizes(flist[i]);
            } else {
                size = size + getFileSize(flist[i]);
            }
        }
        return size;
    }

    /**
     * 获取指定文件大小
     *
     * @param file
     * @return
     * @throws Exception
     */
    private static long getFileSize(File file) throws Exception {
        long size = 0;
        if (file.exists()) {
            FileInputStream fis = null;
            fis = new FileInputStream(file);
            size = fis.available();
        } else {
            file.createNewFile();
        }
        return size;
    }


    /**
     * 获取文件名后缀，不包括"."
     *
     * @param fileName
     * @return
     */
    public static String getSuffix(String fileName) {
        if (null == fileName)
            return null;

        final int index = fileName.lastIndexOf(".");

        if (-1 == index || (fileName.length() - 1) == index || 0 == index) {
            return null;
        }

        return fileName.substring(index + 1);
    }

    /**
     * 获取文件名
     *
     * @return
     */
    public static String getFileNameByUrl(String url) {
        if (null == url) {
            return null;
        }
        int pos1 = url.lastIndexOf('/');
        int pos2 = url.lastIndexOf('\\');
        int pos = Math.max(pos1, pos2);
        if (pos < 0) {
            return url;
        } else {
            return url.substring(pos + 1);
        }
    }


    /**
     * 解压一个压缩文档 到指定位置
     *
     * @param zipFileString 压缩包的名字
     * @param outPathString 指定的路径
     * @throws Exception
     */
    public static void UnZipFolder(String zipFileString, String outPathString) throws Exception {
        // 删除目录后创建,在某些手机上可能出现文件被占用无法创建的问题,所以直接解压覆盖
        // 先删除已有目录
//        FileUtils.deleteFile(new File(outPathString));

        ZipInputStream inZip = new ZipInputStream(new FileInputStream(zipFileString));
        ZipEntry zipEntry;
        String szName = "";

        while ((zipEntry = inZip.getNextEntry()) != null) {
            szName = zipEntry.getName();
            if (zipEntry.isDirectory()) {
                // get the folder name of the widget
                szName = szName.substring(0, szName.length() - 1);
                File folder = new File(outPathString + File.separator + szName);
                folder.mkdirs();

            } else {
                File file = new File(outPathString + File.separator + szName);
                file.createNewFile();
                // get the output stream of the file
                FileOutputStream out = new FileOutputStream(file);
                int len;
                byte[] buffer = new byte[1024];
                // read (len) bytes into buffer
                while ((len = inZip.read(buffer)) != -1) {
                    // write (len) byte from buffer at the position 0
                    out.write(buffer, 0, len);
                    out.flush();
                }
                out.close();
            }
        }//end of while

        inZip.close();

    }

    /**
     * 转换文件大小,指定转换的类型
     *
     * @param fileS
     * @return
     */
    private static double FormetFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        double fileSizeLong = Double.valueOf(df.format((double) fileS / 1048576));
        return fileSizeLong;
    }

    /**
     * 安装
     * @param context
     * @param filePath
     */
    public static void install(Context context, String filePath) {
        // 手机下载目录
        File file = new File(filePath);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // 7.0 版本，不能直接通过路径获取Uri，需要使用FileProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // 此处的authority需要和manifest里面保持一致
            Uri apkUri = FileProvider.getUriForFile(context, "com.bonbvo.guardian.FileProvider", file);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(file),
                    "application/vnd.android.package-archive");
        }
        context.startActivity(intent);
    }

}
