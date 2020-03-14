package com.yfkk.cardbag.utils.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;

import com.yfkk.cardbag.MainApplication;
import com.yfkk.cardbag.log.LogUtils;
import com.yfkk.cardbag.utils.FileUtils;
import com.yfkk.cardbag.utils.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 图片处理工具类
 * <p>
 * Created by litao on 2018/1/24.
 */
public final class ImageUtils {

    /**
     * 尝试打开图片次数
     **/
    private static final int MAX_TRY_OPEN_IMAGE = 5;

    /**
     * 通过路径获取图片
     */
    public static Bitmap getBitmap(String pathFile, int maxLength) {
        if (pathFile == null || !FileUtils.isFileExist(pathFile)) {
            return null;
        }

        BitmapFactory.Options option = new BitmapFactory.Options();
        option.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(pathFile, option);
        // 获取压缩值
        option.inSampleSize = computeSampleSize(option, -1, maxLength);
        option.inJustDecodeBounds = false;

        Bitmap bitmap = null;

        // 重试次数
        int tryCount = 1;
        do {
            if (tryCount > 1) {
                if (option.inSampleSize < 1) {
                    option.inSampleSize = 1;
                }
                option.inSampleSize *= tryCount;
            }
            bitmap = getBitmap(pathFile, option);
            tryCount++;
        } while (bitmap == null && tryCount < MAX_TRY_OPEN_IMAGE);
        return bitmap;
    }

    public static int[] getImgSize(String pathFile) {
        if (pathFile == null || !FileUtils.isFileExist(pathFile)) {
            return new int[]{0, 0};
        }
        try {
            BitmapFactory.Options option = new BitmapFactory.Options();
            option.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(pathFile, option);
            return new int[]{option.outWidth, option.outHeight};
        } catch (Exception e) {
        }
        return new int[]{0, 0};
    }

    /**
     * 通过路径获取图片，指定寬高
     */
    public static Bitmap getBitmap(String pathFile, int reqWidth, int reqHeight) {
        if (pathFile == null || !FileUtils.isFileExist(pathFile)) {
            return null;
        }

        BitmapFactory.Options option = new BitmapFactory.Options();
        option.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathFile, option);
        // 获取压缩值
        option.inSampleSize = calculateInSampleSize(option, reqWidth, reqHeight);
        option.inJustDecodeBounds = false;

        Bitmap bitmap = null;
        // 重试次数
        int tryCount = 1;
        do {
            if (tryCount > 1) {
                if (option.inSampleSize < 1) {
                    option.inSampleSize = 1;
                }
                option.inSampleSize *= tryCount;
            }
            bitmap = getBitmap(pathFile, option);
            tryCount++;
        } while (bitmap == null && tryCount < MAX_TRY_OPEN_IMAGE);

        if (bitmap != null && bitmap.getWidth() > reqWidth) {
            bitmap = small(bitmap, reqWidth / (float) bitmap.getHeight());
        }
        return bitmap;
    }

    /**
     * 保存照片(默认最大长宽1280px)
     */
    public static boolean saveImage(String path, String imageSavePath) {
        try {
            File file = new File(path);
            if (file.exists() && file.length() < 100 * 1024) {
                if (FileUtils.copyFile(file.getAbsolutePath(), imageSavePath) > 0) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ImageUtils.saveBitmap(ImageUtils.getBitmap(path, 1280, 1280), imageSavePath);
    }

    /**
     * 保存照片（指定宽高）
     */
    public static boolean saveImage(String path, String imageSavePath, int width, int height) {
        Bitmap bimap = ImageUtils.getBitmap(path, width, height);
        return ImageUtils.saveBitmap(bimap, imageSavePath);
    }

    /**
     * 保存图片 (动态设置照片大小)
     */
    public static boolean saveBitmap(Bitmap srcBit, String imageSavePath) {
        if (srcBit != null) {
            return saveBitmap(srcBit, imageSavePath, (srcBit.getWidth() + srcBit.getHeight()) * 100);
        }
        return false;
    }

    /**
     * 保存图片(指定大小)
     */
    public static boolean saveBitmap(Bitmap srcBit, String imageSavePath, int maxLength) {
        try {
            File mFile = new File(imageSavePath);
            if (mFile.isDirectory()) {
                mFile.delete();
            }
            if (!mFile.isFile()) {
                // 创建目录
                mFile.getParentFile().mkdirs();
                try {
                    boolean r = mFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            FileOutputStream fos = new FileOutputStream(mFile);

            int quality = 80;
            for (int i = 0; i < 4; i++) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                srcBit.compress(Bitmap.CompressFormat.JPEG, quality, baos);

                if (Math.abs(1 - baos.toByteArray().length / (float) maxLength) < 0.1f || quality == 100
                        || quality == 20) {
                    break;
                } else {
                    quality = (int) (quality + 20 * (1 - baos.toByteArray().length / (float) maxLength));
                    quality = quality > 100 ? 100 : quality;
                    quality = quality < 20 ? 20 : quality;
                }
            }
            quality = quality > 99 ? 99 : quality;
            quality = quality < 30 ? 30 : quality;
            LogUtils.d("压缩率：" + quality);

            boolean result = srcBit.compress(Bitmap.CompressFormat.JPEG, quality, fos);
            fos.flush();
            fos.close();
            LogUtils.d("保存后照片大小：" + mFile.length());
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 保存PNG图片
     */
    public static boolean saveBitmapToPng(Bitmap srcBit, String imageSavePath) {
        try {
            File mFile = new File(imageSavePath);
            if (mFile.isDirectory()) {
                mFile.delete();
            }
            if (!mFile.isFile()) {
                // 创建目录
                mFile.getParentFile().mkdirs();
                try {
                    boolean r = mFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            FileOutputStream fos = new FileOutputStream(mFile);
            srcBit.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
            LogUtils.d("保存后照片大小：" + mFile.length());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 缩小图片(不会放大图片)
     */
    public static Bitmap small(Bitmap bitmap, int maxWidth) {
        if (bitmap == null) {
            return null;
        }

        float scale = maxWidth / (float) bitmap.getWidth();
        if (scale < 1) {
            return zoom(bitmap, scale);
        }
        return bitmap;
    }

    /**
     * 缩小图片(不会放大图片)
     */
    public static Bitmap small(Bitmap bitmap, float scale) {
        if (bitmap == null || scale <= 0) {
            return null;
        }
        if (scale < 1) {
            return zoom(bitmap, scale);
        }
        return bitmap;
    }

    /**
     * 缩放图片
     */
    public static Bitmap zoom(Bitmap bitmap, float scale) {
        if (bitmap == null || scale <= 0) {
            return null;
        }
        try {
            Matrix matrix = new Matrix();
            matrix.postScale(scale, scale); // 长和宽放大缩小的比例
            Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            return resizeBmp;
        } catch (OutOfMemoryError oom) {
        }
        return null;
    }

    /**
     * 圆形图片
     */
    public static Bitmap getRoundedBitmap(Bitmap src, int maxWidth, boolean isShowStroke) {
        if (src == null) {
            return null;
        }
        try {
            Bitmap bitmap;
            if (src.getWidth() > src.getHeight()) {
                bitmap = small(src, maxWidth / (float) src.getHeight());
            } else {
                bitmap = small(src, maxWidth / (float) src.getWidth());
            }
            if (bitmap == null) {
                return null;
            }
            // 计算画布大小
            int rect_width;
            if (bitmap.getWidth() > bitmap.getHeight()) {
                rect_width = bitmap.getHeight();
            } else {
                rect_width = bitmap.getWidth();
            }
            if (rect_width > maxWidth) {
                rect_width = maxWidth;
            }

            Bitmap output = Bitmap.createBitmap(rect_width, rect_width, Config.ARGB_8888);
            Canvas canvas = new Canvas(output);

            final Paint paint = new Paint();
            // 抗锯齿
            paint.setAntiAlias(true);
            paint.setFilterBitmap(true);

            final RectF rectF = new RectF(0, 0, rect_width, rect_width);
            final float roundPx = rect_width / 2;
            canvas.drawARGB(0, 0, 0, 0);
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

            paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
            canvas.drawBitmap(bitmap,
                    new Rect((bitmap.getWidth() - rect_width) / 2, (bitmap.getHeight() - rect_width) / 2,
                            rect_width + (bitmap.getWidth() - rect_width) / 2,
                            rect_width + (bitmap.getHeight() - rect_width) / 2),
                    new Rect(0, 0, rect_width, rect_width), paint);
            // 绘制边缘
            if (isShowStroke) {
                paint.setColor(0xffffffff);

                float stroke = rect_width / 30f;
                if (stroke > StringUtils.dpToPx(MainApplication.getInstance(), 2)) {
                    stroke = StringUtils.dpToPx(MainApplication.getInstance(), 2);
                }
                paint.setStrokeWidth(stroke);
                paint.setStyle(Paint.Style.STROKE);// 设置空心
                canvas.drawCircle(rect_width / 2, rect_width / 2, rect_width / 2, paint);
            }
            return output;
        } catch (OutOfMemoryError oom) {

        }
        return null;
    }

    /**
     * 正方形图片（可设置圆角）
     *
     * @param src
     * @param maxWidth
     * @return
     */
    public static Bitmap getSquareBitmap(Bitmap src, int maxWidth, boolean isShowStroke) {
        if (src == null) {
            return null;
        }
        // 如果是圆角，则先缩放（如果图片很小，圆角显示会失真）
        if (isShowStroke) {
            src = zoom(src, maxWidth / (float) src.getHeight());
        }

        try {
            // 计算画布大小
            int rect_width;
            if (src.getWidth() > src.getHeight()) {
                rect_width = src.getHeight();
            } else {
                rect_width = src.getWidth();
            }
            // if (rect_width > maxWidth) {
            // rect_width = maxWidth;
            // }

            Bitmap output = Bitmap.createBitmap(rect_width, rect_width, Config.ARGB_8888);
            Canvas canvas = new Canvas(output);

            final Paint paint = new Paint();
            paint.setAntiAlias(true);

            if (isShowStroke) {
                float stroke = rect_width / 10f + 1;
                if (stroke > StringUtils.dpToPx(MainApplication.getInstance(), 8f)) {
                    stroke = StringUtils.dpToPx(MainApplication.getInstance(), 8f);
                }
                canvas.drawRoundRect(new RectF(0, 0, rect_width, rect_width), stroke, stroke, paint);
                paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
            }
            canvas.drawBitmap(src, new Rect((src.getWidth() - rect_width) / 2, (src.getHeight() - rect_width) / 2,
                            rect_width + (src.getWidth() - rect_width) / 2, rect_width + (src.getHeight() - rect_width) / 2),
                    new Rect(0, 0, rect_width, rect_width), paint);

            // 圆角之前已经缩放过了
            if (output != null && !isShowStroke) {
                if (output.getWidth() > output.getHeight()) {
                    output = small(output, maxWidth / (float) output.getHeight());
                } else {
                    output = small(output, maxWidth / (float) output.getWidth());
                }
            }
            return output;
        } catch (OutOfMemoryError oom) {
            return src;
        } catch (Exception e) {
            return src;
        }
    }

    private static Bitmap getBitmap(String pathFile, BitmapFactory.Options option) {
        Bitmap bitmap = null;
        if (pathFile != null) {
            InputStream stream = null;
            try {
                stream = new FileInputStream(pathFile);

                bitmap = BitmapFactory.decodeStream(stream, null, option);
            } catch (FileNotFoundException e) {
                // LogUtil.e(TAG, "没有文件，pathFile=" + pathFile, e);
            } catch (OutOfMemoryError oom) {
                long length = -1;
                try {
                    length = stream != null ? stream.available() : -1;
                } catch (IOException e) {
                    // LogUtil.e(TAG, e.toString(), e);
                }
            } finally {
                if (stream != null) {
                    try {
                        stream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return bitmap;
    }


    /**
     * 圆角处理
     *
     * @param bitmap  原图
     * @param roundPx 角度
     * @return
     */
    public static Bitmap getCornerBitmap(Bitmap bitmap, float roundPx) {
        if (bitmap == null)
            return null;
        // 创建一个指定宽度和高度的空位图对象
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Config.ARGB_8888);
        // 用该位图创建画布
        Canvas canvas = new Canvas(output);
        // 画笔对象
        final Paint paint = new Paint();
        // 画笔的颜色
        final int color = 0xff424242;
        // 矩形区域对象
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        // 未知
        final RectF rectF = new RectF(rect);
        // 拐角的半径
        roundPx = roundPx == 0 ? (bitmap.getWidth() + bitmap.getHeight()) / 4
                : roundPx;
        // 消除锯齿
        paint.setAntiAlias(true);
        // 画布背景色
        canvas.drawARGB(0, 0, 0, 0);
        // 设置画笔颜色
        paint.setColor(color);
        // 绘制圆角矩形
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        // 把该图片绘制在该圆角矩形区域中
        canvas.drawBitmap(bitmap, rect, rect, paint);
        // 最终在画布上呈现的就是该圆角矩形图片，然后我们返回该Bitmap对象
        return output;
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? widthRatio : heightRatio;
        }
        return inSampleSize;
    }

    /**
     * android源码提供的计算inSampleSize方法
     *
     * @param options
     * @param minSideLength
     * @param maxNumOfPixels
     * @return int [返回类型说明]
     * @throws throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    private static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);

        int roundedSize;

        if (initialSize <= 8) {
            roundedSize = 1;

            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }

        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }

        return roundedSize;
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;

        double h = options.outHeight;

        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));

        int upperBound = (minSideLength == -1) ? 128
                : (int) Math.min(Math.floor(w / minSideLength), Math.floor(h / minSideLength));

        if (upperBound < lowerBound) {

            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }

        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }

    public static Drawable getRoundedDrawable(Context context, int resourceId, float width, int radius) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId);
        bitmap = zoom(bitmap, width / bitmap.getWidth());

        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), bitmap);
        roundedBitmapDrawable.setCornerRadius(radius);
        return roundedBitmapDrawable;
    }
}
