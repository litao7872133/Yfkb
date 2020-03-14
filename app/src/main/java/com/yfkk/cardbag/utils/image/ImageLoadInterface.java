package com.yfkk.cardbag.utils.image;

import android.content.Context;
import android.widget.ImageView;

public interface ImageLoadInterface {

    ImageLoadInterface init(Context context, ImageView view, String url);

    ImageLoadInterface resize(int width, int height);

    void loadCircle();

    void loadCircleStroke();

    void loadSquare();

    void loadSquareFillet();

    void loadSquareFillet(int fillet);

    void loadImage();
}
