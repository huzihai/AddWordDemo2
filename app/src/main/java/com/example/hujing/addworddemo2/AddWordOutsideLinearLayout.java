package com.example.hujing.addworddemo2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class AddWordOutsideLinearLayout extends LinearLayout {
    private Paint paint;

    private String text;
    private Context context;
    private int color ;
    private int size;

    private boolean isSelect = false;
    public Bitmap bitDelete = null;
    public Bitmap bitMove = null;
    private AddWordTextView addWordTextView =null;

    private int bdeleteWidth;


    public AddWordOutsideLinearLayout(Context context) {
        super(context);
        this.context = context;
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.LEFT;
        this.setLayoutParams(params);
        setWillNotDraw(false);

        paint = new Paint();
        paint.setAntiAlias(true);
       // paint.setColor(Color.parseColor("#F77DA3"));
        paint.setStrokeWidth(5f);

        if (bitDelete == null) {
            bitDelete = BitmapFactory.decodeResource(getResources(),
                    R.drawable.btn_sticker_cancel_n);
        }
        if (bitMove == null) {
            bitMove = BitmapFactory.decodeResource(getResources(),
                    R.drawable.btn_sticker_word_turn_n);
        }

        bdeleteWidth = bitDelete.getHeight() / 2;
    }





    /**
     * @param text
     */
    public void setText(String text) {
        this.text = text;
        addText();
    }

    public String getText() {
        return text;
    }
    public AddWordTextView getAddTextView() {
        return addWordTextView;
    }

    private void addText() {
        removeAllViews();
//        map.clear();
        AddWordTextView addWordTextView = new AddWordTextView(context);
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        addWordTextView.setLayoutParams(params);
        addWordTextView.setText(this.text);
        addWordTextView.setTextSize(this.size);
        addWordTextView.setTextColor(this.color);
        this.addWordTextView =addWordTextView;
        addView(addWordTextView);

        invalidate();
    }

    public void setTextColor(int color) {
        this.color = color;

    }

    public void setTextSize(int size) {
        this.size = size;
    }



    public void setSelect(boolean isSelect) {
        this.isSelect = isSelect;
        invalidate();
    }

    public void setColor(int color) {
        setTextColor(color);
    }


    public interface OnLayoutWidth {
        void layout(int width, int height);
    }

    public void layoutWidthAndHeight(final AddWordOutsideLinearLayout addWordOutsideLinearLayout, final OnLayoutWidth onLayoutWidth) {
        addWordOutsideLinearLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    addWordOutsideLinearLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                onLayoutWidth.layout(addWordOutsideLinearLayout.getMeasuredWidth(), addWordOutsideLinearLayout.getMeasuredHeight());
            }
        });


    }
}