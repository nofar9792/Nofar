package com.example.nofarcohenzedek.dogo;

import android.content.Context;
import android.graphics.Typeface;

import java.lang.reflect.Field;

public class FontReplacer {

    public static void replaceDefaultFont(Context context, String replacedFontName, String assetFontName) {
        final Typeface assetFontTypeface = Typeface.createFromAsset(context.getAssets(), assetFontName);
        replaceFont(replacedFontName, assetFontTypeface);
    }

    private static void replaceFont(String replacedFontName, Typeface assetFontTypeface) {
        try {
            Field field = Typeface.class.getDeclaredField(replacedFontName);
            field.setAccessible(true);
            field.set(null, assetFontTypeface);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
