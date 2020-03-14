package com.yfkk.cardbag.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

import com.soundcloud.android.crop.Crop;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.yfkk.cardbag.MainApplication;
import com.yfkk.cardbag.config.Config;

import java.io.File;

import rx.functions.Action1;

/**
 * 调用相机/相册
 * <p>
 * Created by litao on 2019/3/05.
 */
public class MediaPhotoUtils {

    public static String CAMERA_SAVE_PATH = FileUtils.getExternalCachePath(MainApplication.getInstance()) + File.separator + "temp.jpg"; // 拍照缓存的目录
    public static String CROPPED_SAVE_PATH = FileUtils.getExternalCachePath(MainApplication.getInstance()) + File.separator + "cropped.jpg"; // 裁剪缓存的目录

    /**
     * 拍照
     */
    public static void openCamera(final Activity activity) {
        // 申请权限
        new RxPermissions(activity)
                .request(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean granted) {
                        if (granted) {
                            // 请求成功
                            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                                    || !Environment.isExternalStorageRemovable()) {
                                // 调用系统相机
                                Intent intentCamera = new Intent();
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    intentCamera.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // 添加这一句表示对目标应用临时授权该Uri所代表的文件
                                }
                                intentCamera.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                                // 将拍照结果保存至photo_file的Uri中，不保留在相册中
                                intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, FileUtils.getUri(activity, CAMERA_SAVE_PATH));
                                activity.startActivityForResult(intentCamera, Config.REQUEST_CODE_CAMERA);
                            } else {
                                ToastUtils.makeText("设备没有SD卡");
                            }
                        } else {
                            // 用户拒绝权限
                            ToastUtils.makeText("获取相机权限失败");
                        }
                    }
                });
    }

    /**
     * 打开相册
     *
     * @param activity
     */
    public static void openAlbum(final Activity activity) {
        // 申请权限
        new RxPermissions(activity)
                .request(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean granted) {
                        if (granted) {
                            // 请求成功
                            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                                    || !Environment.isExternalStorageRemovable()) {
                                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                activity.startActivityForResult(intent, Config.REQUEST_CODE_ALBUM);
                            } else {
                                ToastUtils.makeText("设备没有SD卡");
                            }
                        } else {
                            // 用户拒绝权限
                            ToastUtils.makeText("获取相机权限失败");
                        }
                    }
                });
    }

    /**
     * 裁剪图片
     *
     * @param activity
     */
    public static void cropPic(final Activity activity, Uri uri) {
        // 不支持GIF格式
        File file = CropUtil.getFromMediaUri(activity, activity.getContentResolver(), uri);
        if (file != null && file.getAbsolutePath().endsWith("gif")) {
            ToastUtils.makeText("不支持的图片格式，请重新选择");
            return;
        }
        Crop.of(uri, FileUtils.getUri(activity, CROPPED_SAVE_PATH)).asSquare().start(activity);
    }

    // 示例
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        // 拍照、相册
//        if (resultCode != RESULT_OK) {
//            return;
//        }
//        switch (requestCode) {
//                case MediaPhotoUtils.CAMERA_REQUEST_CODE: // 调用照相机
//            MediaPhotoUtils.cropPic(this, FileUtils.getUri(MediaPhotoUtils.CAMERA_SAVE_PATH));
//                break;
//            case MediaPhotoUtils.ALBUM_REQUEST_CODE: // 调用媒体库
//            MediaPhotoUtils.cropPic(this,data.getData());
//                break;
//            case MediaPhotoUtils.CROP_IMAGE_REQUEST_CODE: // 调用裁剪
//            if (ImageUtils.saveImage(MediaPhotoUtils.CROPPED_SAVE_PATH, MediaPhotoUtils.CAMERA_SAVE_PATH, 540, 540)) {
//        // 使用这个地址，做你想做的事 ： MediaPhotoUtils.CAMERA_SAVE_PATH
//        getFragmentChild().setImgHead(MediaPhotoUtils.CAMERA_SAVE_PATH);
//    }
//                break;
//        }
//    }

}
