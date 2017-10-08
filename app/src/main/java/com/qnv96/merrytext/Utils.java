package com.qnv96.merrytext;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by qnv96 on 16-Dec-15.
 */
public class Utils {

    public static int[] COLORS = {Color.RED, Color.GREEN, Color.BLUE, Color.CYAN, Color.parseColor("#3366CC"),
            Color.YELLOW, Color.GRAY, Color.DKGRAY, Color.MAGENTA, Color.LTGRAY,
            Color.BLACK, Color.parseColor("#00DD00"), Color.parseColor("#FFCC33"), Color.parseColor("#aabbcc"), Color.parseColor("#aa2266"), Color.parseColor("#996699")
    };

    public static List<Typeface> loadFonts(Context activity) {

        List<Typeface> mTypefaces = new ArrayList<>();
        mTypefaces.add(Typeface.createFromAsset(activity.getAssets(), "fonts/UTM EdwardianB.ttf"));
        mTypefaces.add(Typeface.createFromAsset(activity.getAssets(), "fonts/f1.ttf"));
        mTypefaces.add(Typeface.createFromAsset(activity.getAssets(), "fonts/Candle3D.ttf"));
        mTypefaces.add(Typeface.createFromAsset(activity.getAssets(), "fonts/CH______.TTF"));
        mTypefaces.add(Typeface.createFromAsset(activity.getAssets(), "fonts/Christbaumkugeln.ttf"));
        mTypefaces.add(Typeface.createFromAsset(activity.getAssets(), "fonts/CHRISTMAS SOUNDS.ttf"));
        mTypefaces.add(Typeface.createFromAsset(activity.getAssets(), "fonts/JFSnobiz.ttf"));
        mTypefaces.add(Typeface.createFromAsset(activity.getAssets(), "fonts/JournalDingbats4.ttf"));
        mTypefaces.add(Typeface.createFromAsset(activity.getAssets(), "fonts/Kingthings Christmas 2.2.ttf"));

        //
        mTypefaces.add(Typeface.createFromAsset(activity.getAssets(), "fonts/Kingthings Eggypeg.ttf"));
        mTypefaces.add(Typeface.createFromAsset(activity.getAssets(), "fonts/KINKIE__.TTF"));
        mTypefaces.add(Typeface.createFromAsset(activity.getAssets(), "fonts/AL Snowmen.ttf"));
        mTypefaces.add(Typeface.createFromAsset(activity.getAssets(), "fonts/PWHappyChristmas.ttf"));
        mTypefaces.add(Typeface.createFromAsset(activity.getAssets(), "fonts/Summer's BearHearts.ttf"));
        mTypefaces.add(Typeface.createFromAsset(activity.getAssets(), "fonts/SWEEHB__.TTF"));
        mTypefaces.add(Typeface.createFromAsset(activity.getAssets(), "fonts/XmasDings.ttf"));
        mTypefaces.add(Typeface.createFromAsset(activity.getAssets(), "fonts/xmaslght.ttf"));
        mTypefaces.add(Typeface.createFromAsset(activity.getAssets(), "fonts/XTREE.TTF"));

        //
        mTypefaces.add(Typeface.createFromAsset(activity.getAssets(), "fonts/asxmasballs.ttf"));
        mTypefaces.add(Typeface.createFromAsset(activity.getAssets(), "fonts/UTM Fleur.ttf"));
        mTypefaces.add(Typeface.createFromAsset(activity.getAssets(), "fonts/crystal.ttf"));
        mTypefaces.add(Typeface.createFromAsset(activity.getAssets(), "fonts/UVNConThuy.TTF"));

        return mTypefaces;
    }


    public static int[] initValues(Context context) {
        int[] vals = new int[3];

        vals[0] = getInt(context,Define.SIZE);
        vals[1] = getInt(context, Define.FONT);
        vals[2] = getInt(context, Define.COLOR);

        return vals;
    }

    public static int[] getScreenSize(Context context) {
        int[] rs = new int[2];

        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        manager.getDefaultDisplay().getMetrics(metrics);

        rs[0] = metrics.widthPixels;
        rs[1] = metrics.heightPixels;

        return rs;
    }

    public static int pxToDp(int px) {
        int rs = (int) (px / Resources.getSystem().getDisplayMetrics().density);
        return rs;
    }

    public static void setInt(Context context, String key, int value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static int getInt(Context context, String key) {
        int rs = -1;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        if (preferences.contains(key)) {
            rs = preferences.getInt(key, -1);
        }

        return rs;
    }
}
