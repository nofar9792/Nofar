package com.example.nofarcohenzedek.dogo;

public final class Application extends android.app.Application{
    @Override
    public void onCreate() {
        super.onCreate();
        FontReplacer.replaceDefaultFont(this, "DEFAULT", "fonts/Guttman Yad-Brush.ttf");
        FontReplacer.replaceDefaultFont(this, "MONOSPACE", "fonts/Guttman Yad-Brush.ttf");
        FontReplacer.replaceDefaultFont(this, "SERIF", "fonts/Guttman Yad-Brush.ttf");
        FontReplacer.replaceDefaultFont(this, "SANS_SERIF", "fonts/Guttman Yad-Brush.ttf");
    }
}
