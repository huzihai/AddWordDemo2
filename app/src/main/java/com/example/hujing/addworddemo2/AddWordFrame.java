package com.example.hujing.addworddemo2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;


public class AddWordFrame extends FrameLayout {
    private Paint paint;


    private Bitmap bitmap;

    private Context context;
    private AddWordOutsideLinearLayout layout;



    public PointF leftTop = new PointF();
    public PointF rightTop = new PointF();
    public PointF leftBottom = new PointF();
    public PointF rightBottom = new PointF();
    private boolean isSelect = false;
    public Bitmap bitDelete = null;
    public Bitmap bitMove = null;
    private AddWordTextView addWordTextView;
    Matrix matrix = new Matrix();


    private int xiao;

    public AddWordFrame(Context context) {
        super(context);
        this.context = context;
        initPaint();
        addWordView();
        setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        setWillNotDraw(false);
    }


@Override
protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    // TODO Auto-generated method stub
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    int width = measureDimension(339, widthMeasureSpec);
    int height = measureDimension(200, heightMeasureSpec);
    setMeasuredDimension(width, height);
}

    public int measureDimension(int defaultSize, int measureSpec){
        int result;

        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if(specMode == MeasureSpec.EXACTLY){

            result = specSize;
        }else{
            result = defaultSize;   //UNSPECIFIED
            if(specMode == MeasureSpec.AT_MOST){
                result = Math.min(result, specSize);
            }
        }
        return result;
    }




    public AddWordOutsideLinearLayout getLayout() {
        return layout;
    }
    public AddWordTextView getAddWordTextView() {
        return addWordTextView;
    }




    private void addWordView() {
        layout = new AddWordOutsideLinearLayout(context);
        layout.setTextColor(AppConst.getColor());
        layout.setTextSize(AppConst.getSize());

        layout.setText("请在下方输入文字");


        layout.setSelect(true);
        addView(layout);
      /* AddWordTextView addWordTextView =new AddWordTextView(context);
        addWordTextView.setText("XXXXXXXXX");
        this.addWordTextView =addWordTextView;
        addView(addWordTextView);*/

    }


    private void initPaint() {
        paint = new Paint();
        paint.setAntiAlias(true);

        paint.setColor(Color.WHITE);

        paint.setStrokeWidth(5f);

        if (bitDelete == null) {
            bitDelete = BitmapFactory.decodeResource(getResources(),
                    R.drawable.btn_sticker_cancel_n);
        }
        if (bitMove == null) {
            bitMove = BitmapFactory.decodeResource(getResources(),
                    R.drawable.btn_sticker_turn_n);
        }


        xiao = bitDelete.getHeight()/2;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setVisibility(INVISIBLE);

        if (isSelect) {
            canvas.drawLine(leftTop.x, leftTop.y, rightTop.x, rightTop.y, paint);
            canvas.drawLine(leftBottom.x, leftBottom.y, rightBottom.x,rightBottom.y, paint);
            canvas.drawLine(leftTop.x, leftTop.y, leftBottom.x, leftBottom.y,paint);
            canvas.drawLine(rightTop.x, rightTop.y, rightBottom.x,rightBottom.y, paint);
            canvas.drawBitmap(bitDelete, leftTop.x-xiao, leftTop.y-xiao, paint);
            canvas.drawBitmap(bitMove, rightBottom.x-xiao, rightBottom.y-xiao, paint);
        }

        this.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        canvas.concat(matrix);
        this.setLayerType(View.LAYER_TYPE_NONE, null);
        setVisibility(VISIBLE);
    }

    @Override
    public Matrix getMatrix() {
        return matrix;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public void setMatrix(Matrix matrix) {
        this.matrix = matrix;
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                postInvalidate();
            }
        });
    }

}
