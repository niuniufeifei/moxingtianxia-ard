package com.dgzd.mxtx.tools;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * @version V1.0 <功能>
 * @FileName: TipoDisp.java
 * @author: Jessica
 * @date: 2016-01-05 16:51
 */

public class TipoDisp {

    public static int alt_tabs(Context cont)
    {
        int alt;
        int dx, dy;
        DisplayMetrics metrics = cont.getResources().getDisplayMetrics();

        dx = metrics.widthPixels;
        dy = metrics.heightPixels;
        if (dx < dy)
            alt = dy/13;
        else
            alt = dy/10;

        return alt;
    }
}

