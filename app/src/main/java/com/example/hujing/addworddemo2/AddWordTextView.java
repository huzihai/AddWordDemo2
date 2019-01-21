package com.example.hujing.addworddemo2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.TextView;

import java.util.Locale;


public class AddWordTextView extends TextView {


    public AddWordTextView(Context context) {
        super(context);
    }

    public AddWordTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AddWordTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }





    @NonNull
    @Override
    public Locale getTextLocale() {
        return super.getTextLocale();
    }

    @Override
    protected void onDraw(Canvas canvas) {


            this.setTextColor(getTextColors());


            TextPaint tp = this.getPaint();
            tp.setFakeBoldText(false);
            tp.setStrokeWidth(20);
            super.onDraw(canvas);
        }


}
