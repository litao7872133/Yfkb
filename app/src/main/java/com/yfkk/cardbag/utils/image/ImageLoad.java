package com.yfkk.cardbag.utils.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import com.yfkk.cardbag.R;
import com.yfkk.cardbag.utils.StringUtils;

import java.io.File;

/**
 * 图片异步加载
 * <p>
 * Created by litao on 2018/1/24.
 */
public class ImageLoad implements ImageLoadInterface {

    private Context context;
    private ImageView view;
    private String url;
    private int width = 240;
    private int height = 240;

    public static ImageLoadInterface getInstance() {
        return new ImageLoad();
    }

    public ImageLoadInterface init(Context context, final ImageView view, final String url) {
        this.context = context;
        this.view = view;
        this.url = url;
        return this;
    }

    public ImageLoadInterface resize(final int width, final int height) {
        this.width = width;
        this.height = height;
        return this;
    }

    /**
     * 加载圆形图
     */
    public void loadCircle() {
        loadRounded(context, view, url, width, height, false);
    }

    /**
     * 加载描边的圆形图
     */
    public void loadCircleStroke() {
        loadRounded(context, view, url, width, height, true);
    }

    /**
     * 加载正方形图
     */
    public void loadSquare() {
        loadSquare(context, view, url, width, 0);
    }

    /**
     * 加载圆角正方形图
     */
    public void loadSquareFillet() {
        loadSquare(context, view, url, width, width / 12);
    }

    /**
     * 加载圆角正方形图
     */
    public void loadSquareFillet(int fillet) {
        loadSquare(context, view, url, width, fillet);
    }

    /**
     * 加载普通图
     */
    public void loadImage() {
        loadImage(context, view, url, width, height);
    }


    private void loadRounded(Context context, final ImageView view, final String url, final int width, final int height, final boolean isShowStroke) {
        if (StringUtils.isEmpty(url)) {
            view.setImageResource(R.drawable.ic_circle_default);
            return;
        }
        Transformation transformation = new Transformation() {
            @Override
            public Bitmap transform(Bitmap source) {
                Bitmap output = ImageUtils.getRoundedBitmap(source, width, isShowStroke);
                source.recycle();
                return output;
            }

            @Override
            public String key() {
                return "circle";
            }
        };
        // 本地文件
        if (url.startsWith("/storage") || url.startsWith("/data")) {
            Picasso.get().load(new File(url)).resize(width, height).placeholder(R.drawable.ic_circle_default).centerCrop()
                    .transform(transformation).into(view);
        } else {
            Picasso.get().load(StringUtils.mosaicUrl(url)).resize(width, height).placeholder(R.drawable.ic_circle_default).centerCrop()
                    .transform(transformation).into(view);
        }
    }

    /**
     * 加载正方形图片
     */
    private void loadSquare(Context context, final ImageView view, final String url, final int width, final int fillet) {
        if (StringUtils.isEmpty(url)) {
            view.setImageResource(R.drawable.ic_rec_default);
            return;
        }
        Transformation transformation = new Transformation() {
            @Override
            public Bitmap transform(Bitmap source) {
                Bitmap output = ImageUtils.getCornerBitmap(source, fillet);
                source.recycle();
                return output;
            }

            @Override
            public String key() {
                return "fillet" + fillet;
            }
        };
        // 本地文件
        if (url.startsWith("/storage") || url.startsWith("/data")) {
            if (fillet == 0) {
                Picasso.get().load(new File(url)).resize(width, width).placeholder(R.drawable.ic_rec_default).centerCrop().into(view);
            } else {
                Picasso.get().load(new File(url)).resize(width, width).placeholder(ImageUtils.getRoundedDrawable(context, R.mipmap.ic_imgbg_default, width, fillet)).centerCrop()
                        .transform(transformation).into(view);
            }
        } else {
            if (fillet == 0) {
                Picasso.get().load(StringUtils.mosaicUrl(url)).resize(width, width).placeholder(R.drawable.ic_rec_default).centerCrop().into(view);
            } else {
                Picasso.get().load(StringUtils.mosaicUrl(url)).resize(width, width).placeholder(ImageUtils.getRoundedDrawable(context, R.mipmap.ic_imgbg_default, width, fillet)).centerCrop()
                        .transform(transformation).into(view);
            }
        }
    }

    /**
     * 加载图片
     * <p>
     * 这里的width/height 是最大宽高，为保证图片分辨率清晰度，应该以最小缩放比例为准
     */
    private void loadImage(Context context, final ImageView view, final String url, final int width, int height) {
        if (StringUtils.isEmpty(url)) {
            view.setImageResource(R.drawable.ic_rec_default);
            return;
        }
        if (width > height) {
            height = width;
        }
        // 本地文件
        if (url.startsWith("/storage") || url.startsWith("/data")) {
            Picasso.get().load(new File(url)).resize(width, height).placeholder(R.drawable.transparent).centerInside().into(view);
        } else {
            Picasso.get().load(StringUtils.mosaicUrl(url)).resize(width, height).placeholder(R.drawable.transparent).centerInside().into(view);
        }
    }


}
