package com.example.hujing.addworddemo2;



import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.Toast;


public class TextColorPickerView extends View {
    private final String TAG = "TextColorPickerView";
    private final int POPUP_WIDTH = (int)(0.5f +18.0f*3.0f);
    private final int POPUP_HEIGHT =(int)(0.5f +21.0f*3.0f);
    private final int COLOR_CHECKED_BOX_PADDING = (int)(0.5f +5.0f*3.0f);
    private final static float COLOR_CHECKED_LINE_WIDTH =(int)(0.5f +1.0f*3.0f);
    private Context mContext;
    private int mGridWidth = 0;
    private int mGridHeight = 0;
    private int mColorIndex = -1;
    private int mCurrentIndex = 0;
    private Rect mRect = new Rect(0, 0, 0, 0);
    private Paint mPaint = new Paint();
    private View mView;
    private TypedArray mTypedArray = null;
    private PopupWindow mPopupWindow = null;
    //private NinePatchDrawable mColorCheckedBox = null;
    private OnTextColorChangedListener mOnTextColorChangedListener = null;

    public TextColorPickerView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        mContext = context;
        mPaint.setAntiAlias(true);

        if (mTypedArray == null) {
            mTypedArray = getContext().getResources().obtainTypedArray(
                R.array.img_text_colors);
        }

       // mColorCheckedBox = ((NinePatchDrawable)getContext().getResources()
       //         .getDrawable(R.drawable.color_checked_box));
        return;
    }

    public void setCurrentIndex(int index) {
        mCurrentIndex = index;
    }
    public int getCurrentIndex() {
       return mCurrentIndex;
    }


    public void onDraw(Canvas canvas) {
        int i;
        int length = mTypedArray.length();

        super.onDraw(canvas);
        mRect.left = COLOR_CHECKED_BOX_PADDING;
        mRect.top = COLOR_CHECKED_BOX_PADDING;
        mRect.right = mRect.left + mGridWidth;
        mRect.bottom = mRect.top + mGridHeight;
        
        int cx = mRect.left + mGridWidth / 2;
        int cy = mRect.top + mGridHeight / 2;
        int radius = Math.min(mGridWidth, mGridHeight) / 2 - COLOR_CHECKED_BOX_PADDING;

        mPaint.setStyle(Paint.Style.FILL);

        for (i = 0; i < length; ++i) {
            mPaint.setColor(mTypedArray.getColor(i, 0));
            canvas.drawCircle(cx, cy, radius, mPaint);
            //draw black circle frame
            if (i == length - 1) {
                mPaint.setColor(mContext.getResources().getColor(R.color.default_text_color));
                mPaint.setStyle(Paint.Style.STROKE);
                mPaint.setStrokeWidth(1);

                canvas.drawCircle(cx, cy, radius, mPaint);
            }
            cx = cx + mGridWidth;
        }
        drawColorCheckedBox(mCurrentIndex, canvas);
        
        if (null != mOnTextColorChangedListener) {
           mOnTextColorChangedListener.OnTextColorChanged(mCurrentIndex, mTypedArray.getColor(mCurrentIndex, 0));

        }

        return;
    }

    private void drawColorCheckedBox(int index, Canvas canvas) {
        mRect.left = COLOR_CHECKED_BOX_PADDING + index * mGridWidth;

        int cx = mRect.left + mGridWidth / 2;
        int cy = mRect.top + mGridHeight / 2;
        int radius = Math.min(mGridWidth, mGridHeight) / 2 - COLOR_CHECKED_BOX_PADDING + 6;

        //mPaint.setColor(mTypedArray.getColor(index, 0));
        mPaint.setColor(mContext.getResources().getColor(R.color.text_selected_color));
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(COLOR_CHECKED_LINE_WIDTH);

        canvas.drawCircle(cx, cy, radius, mPaint);
    }



    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        if (null != mTypedArray) {
            int length = mTypedArray.length();
            mGridWidth = (w - COLOR_CHECKED_BOX_PADDING * 2 ) / length;
            mGridHeight = h - COLOR_CHECKED_BOX_PADDING * 2;
        }

        return;
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        int length = mTypedArray.length();

        mCurrentIndex = (int)motionEvent.getX() / mGridWidth;

        if (mCurrentIndex < 0) {
            mCurrentIndex = 0;
        } else if (mCurrentIndex > (length - 1)) {
            mCurrentIndex = length - 1;
        }

        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                mColorIndex = -1;
                //showPopupWindow(mCurrentIndex);
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                //showPopupWindow(mCurrentIndex);
                break;
            }

            case MotionEvent.ACTION_UP: {
                this.invalidate();
                mColorIndex = -1;
                //closePopupWindow();
                break;
            }
        }

        return true;
    }

    public void setOnTextColorChangedListener(OnTextColorChangedListener onTextColorChangedListener) {
        mOnTextColorChangedListener = onTextColorChangedListener;
        return;
    }

    public interface OnTextColorChangedListener {
        void OnTextColorChanged(int colorIndex, int color);
    }
}
