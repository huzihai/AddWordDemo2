package com.example.hujing.addworddemo2;


import android.graphics.Color;

public class AppConst {
    public static int textHeight = 0;
    public static int color= Color.parseColor("#FFFFFF");
    public static int size =20;


    public static void setColor(int color) {
        AppConst.color = color;
    }

    public static int getColor() {
        return color;
    }

    public static void setSize(int size) {
        AppConst.size = size;
    }

    public static int getSize() {
        return size;
    }
}
