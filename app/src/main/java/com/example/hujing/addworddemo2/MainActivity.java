package com.example.hujing.addworddemo2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity  {
    private EditText edit_text;
    private LinearLayout edit_layout;
    private FrameLayout frame;
    private int width;
    private int height;

    private AddWordOutsideLinearLayout layout;
    private AddWordFrame addWordFrame;
    private int AddWordMode;
    private Bitmap addWordBitmap;
    private int addWordWidth;
    private int addWordHeight;
    private int addWordx1;
    private int addWordy1;
    private Matrix addWordMatrix = new Matrix();
    private Matrix addWordSavedMatrix = new Matrix();
    private List<AddFrameHolder> addFrameHolders;
    private int AddWordSelectImageCount = -1;
    private Context context;

    private TextView size;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_main);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        height = dm.heightPixels;
        width = dm.widthPixels;
        initView();
        initData();
    }
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
    private void initData() {
        addFrameHolders = new ArrayList<>();
        addMyFrame();
    }

    private void initView() {
        frame = (FrameLayout) findViewById(R.id.frame);
        FrameLayout.LayoutParams layoutParams =(FrameLayout.LayoutParams) frame.getLayoutParams();
        layoutParams.height = 1800;
        frame.setLayoutParams(layoutParams);
//          edit_layout =(LinearLayout)findViewById(R.id.edit_layout);
//        edit_text = (EditText) findViewById(R.id.edit_text);
//        edit_text.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                addFrameHolders.get(AddWordSelectImageCount).getAddWordFrame().getLayout().setText(s.toString());
//                ajustAddWord();
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            /        });
//



    }


    /**
     * 调整加字框的大小 以及删除和变换坐标
     */
    private void ajustAddWord(){
        if(AddWordSelectImageCount != -1){
            addFrameHolders.get(AddWordSelectImageCount).getAddWordFrame().getLayout().layoutWidthAndHeight(addFrameHolders.get(AddWordSelectImageCount).getAddWordFrame().getLayout(), new AddWordOutsideLinearLayout.OnLayoutWidth() {
                @Override
                public void layout(int width, int height) {
                    addWordMatrix = addFrameHolders.get(AddWordSelectImageCount).getAddWordFrame().getMatrix();
                    addWordSavedMatrix.set(addWordMatrix);
                    adjustLocation(addWordMatrix, addFrameHolders.get(AddWordSelectImageCount).getAddWordFrame());
                }
            });
        }
    }



    /**
     * 平移1个单位 只为调整位置
     * @param matrix
     * @param addWordFrame
     */
    private void adjustLocation(Matrix matrix, AddWordFrame addWordFrame){
        //将有缩放平移和旋转相关值的矩阵赋值到f中
        float[] f = new float[9];
        matrix.getValues(f);
        int bWidth = 0;
        int bHeight = 0;

        //取到view的宽高
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            Rect rect = new Rect();
            addWordFrame.getLayout().getGlobalVisibleRect(rect);
            bWidth = rect.width();
            bHeight = rect.height();
        } else {
            bWidth = addWordWidth;
            bHeight = addWordHeight;
        }

        //如果想知道这里这样设置值的具体算法那必须要了解9*9的矩阵每个坐标的含义了 有兴趣的可以查阅一下 资料很多
        // 原图左上角
        float x1 = f[2];
        float y1 = f[5];
        addWordFrame.leftTop.set(x1, y1);
        // 原图右上角
        float x2 = f[0] * bWidth + f[2];
        float y2 = f[3] * bWidth + f[5];
        addWordFrame.rightTop.set(x2, y2);
        // 原图左下角
        float x3 = f[1] * bHeight + f[2];
        float y3 = f[4] * bHeight + f[5];
        addWordFrame.leftBottom.set(x3, y3);
        // 原图右下角
        float x4 = f[0] * bWidth + f[1] * bHeight + f[2];
        float y4 = f[3] * bWidth + f[4] * bHeight + f[5];
        addWordFrame.rightBottom.set(x4, y4);


        //这里一定要这是图片的最左 最右 最上 和 最下的位置 用来判断是不是点击到了当前的view
        // 最左边x
        float minX = 0;
        // 最右边x
        float maxX = 0;
        // 最上边y
        float minY = 0;
        // 最下边y
        float maxY = 0;

        minX = Math.min(x4, Math.min(x3, Math.min(x1, x2))) - 30;
        maxX = Math.max(x4, Math.max(x3, Math.max(x1, x2))) + 30;
        minY = Math.min(y4, Math.min(y3, Math.min(y1, y2))) - 30;
        maxY = Math.max(y4, Math.max(y3, Math.max(y1, y2))) + 30;

        addFrameHolders.get(AddWordSelectImageCount).getState().setLeft(minX);
        addFrameHolders.get(AddWordSelectImageCount).getState().setTop(minY);
        addFrameHolders.get(AddWordSelectImageCount).getState().setRight(maxX);
        addFrameHolders.get(AddWordSelectImageCount).getState().setBottom(maxY);

        //将当前的view设置上矩阵对象
        addFrameHolders.get(AddWordSelectImageCount).getAddWordFrame().setMatrix(matrix);
    }

    private void addMyFrame() {
        //Each additional view Now  set all view is not selected
        for (int i = (addFrameHolders.size() - 1); i >= 0; i--) {
            AddWordFrame addWordFrame = addFrameHolders.get(i).getAddWordFrame();
            if (addWordFrame.isSelect()) {
                addWordFrame.setSelect(false);
                break;
            }
        }

        //new a view
        addWordFrame = new AddWordFrame(this);
        addWordFrame.setSelect(true);
        //add to your frame
        frame.addView(addWordFrame);

        layout = addWordFrame.getLayout();

        addWordBitmap = BitmapUtils.convertViewToBitmap(layout);

        addWordWidth = addWordBitmap.getWidth();
        addWordHeight = addWordBitmap.getHeight();

        //Set to the center of the screen and set the vertical and horizontal coordinates of four points
        addWordx1 = width/2 - addWordWidth /2;
        addWordy1 = height/3;
        addWordFrame.leftTop.set(addWordx1, addWordy1);
        addWordFrame.rightTop.set(addWordx1 + addWordWidth, addWordy1);
        addWordFrame.leftBottom.set(addWordx1, addWordy1 + addWordHeight);
        addWordFrame.rightBottom.set(addWordx1 + addWordWidth, addWordy1 + addWordHeight);


        //here use matrix to scaling gesture
        addWordMatrix = new Matrix();
        addWordMatrix.postTranslate(addWordx1, addWordy1);
        addWordFrame.setMatrix(addWordMatrix);

        //Here for each view with a rectangular package , click the rectangle on selected current view
        AddWordFrameState addWordFrameState = new AddWordFrameState();
        addWordFrameState.setLeft(addWordx1);
        addWordFrameState.setTop(addWordy1);
        addWordFrameState.setRight(addWordx1 + addWordWidth);
        addWordFrameState.setBottom(addWordy1 + addWordHeight);

        AddFrameHolder addFrameHolder = new AddFrameHolder();
        addFrameHolder.setAddWordFrame(addWordFrame);
        addFrameHolder.setState(addWordFrameState);
        addFrameHolders.add(addFrameHolder);

        addWordFrame.setOnTouchListener(new AddWordMyOntouch());
        AddWordSelectImageCount = addFrameHolders.size() - 1;

    }

    private void selectMyFrame(float x, float y) {
        //Select the option to cancel all back to only one click is selected
        for (int i = (addFrameHolders.size() - 1); i >= 0; i--) {
            AddFrameHolder addFrameHolder = addFrameHolders.get(i);
            if (addFrameHolder.getAddWordFrame().isSelect()) {
                addFrameHolder.getAddWordFrame().setSelect(false);
                break;
            }
        }

        for (int i = (addFrameHolders.size() - 1); i >= 0; i--) {
            AddFrameHolder addFrameHolder = addFrameHolders.get(i);
            //Create a rectangular area here getLeft getTop etc. mean the current view of the leftmost
            // uppermost and lowermost rightmost only to click inside the region is selected
            Rect rect = new Rect((int)addFrameHolder.getState().getLeft(),
                    (int)addFrameHolder.getState().getTop(),
                    (int)addFrameHolder.getState().getRight(),
                    (int)addFrameHolder.getState().getBottom());
            //Toast.makeText(context,rect.toString()+"select contain"+x+":"+y,Toast.LENGTH_LONG).show();
            if (rect.contains((int) x, (int) y)) {
                //If you select the current view mentioned uppermost layer
                addFrameHolder.getAddWordFrame().bringToFront();
                addFrameHolder.getAddWordFrame().setSelect(true);
                //Which record is selected
                AddWordSelectImageCount = i;
                LogUtils.e("selected");
                break;
            }
            AddWordSelectImageCount = -1;
            LogUtils.e("no select");
        }
    }

    class AddWordMyOntouch implements View.OnTouchListener {
        private float baseValue = 0;
        //The original angle
        private float oldRotation;
        //旋转和缩放的中点
        private PointF midP;
        //点中的要进行缩放的点与图片中点的距离
        private float imgLengHalf;
        //保存刚开始按下的点
        private PointF startPoinF = new PointF();

        long touchDownTime =0;
        private int NONE = 0; // 无
        private int DRAG = 1; // 移动
        private int ZOOM = 2; // 变换
        private int DOUBLE_ZOOM = 3;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int eventaction = event.getAction();
            float event_x = (int) event.getX();
            float event_y = (int) event.getY();
            //Toast.makeText(context,event_x+":"+event_y,Toast.LENGTH_LONG).show();

            //这里算是一个点击区域值 点中删除或者点中变换的100 * 100 的矩形区域 用这个区域来判断是否点中
            int tempInt = 100;
            int addint = 100;

            switch (eventaction & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN: // touch down so check if the

                    baseValue = 0;

                    startPoinF.set(event_x, event_y);// 保存刚开始按下的坐标
                    touchDownTime = System.currentTimeMillis();

                    //因为可能要添加多个这样的view 所以要按选中了哪个
                    selectMyFrame(event_x, event_y);


                    //如果有选中状态的view
                    if (AddWordSelectImageCount != -1) {
                        addWordFrame = addFrameHolders.get(AddWordSelectImageCount).getAddWordFrame();
                        addWordMatrix = addFrameHolders.get(AddWordSelectImageCount).getAddWordFrame().getMatrix();
                        addWordSavedMatrix.set(addWordMatrix);
                        AddWordMode = DRAG;
                        //构造一个旋转按钮的矩形区域
                        Rect moveRect = new Rect((int) addWordFrame.rightBottom.x - tempInt,
                                (int) addWordFrame.rightBottom.y - tempInt, (int) addWordFrame.rightBottom.x + addint,
                                (int) addWordFrame.rightBottom.y + addint);

                        //删除按钮的矩形区域
                       Rect deleteRect = new Rect((int) addWordFrame.leftTop.x - tempInt,
                                (int) addWordFrame.leftTop.y - tempInt, (int) addWordFrame.leftTop.x + addint,
                                (int) addWordFrame.leftTop.y + addint);


                        //如果点中了变换
                        if(moveRect.contains((int)event_x, (int)event_y)){
                            LogUtils.e("点中了变换");
                            // 点中了变换
                            midP = midPoint(addWordFrame.leftTop, addWordFrame.rightBottom);
                            imgLengHalf = spacing(midP, addWordFrame.rightBottom);
                            oldRotation = rotation(midP, startPoinF);
                            AddWordMode = ZOOM;
                        }else if (deleteRect.contains((int)event_x, (int)event_y)) {
                            // 点中了删除
                            LogUtils.e("点中了删除");

                           // deleteMyFrame();
                        }
                    }
                    break;

                case MotionEvent.ACTION_POINTER_DOWN:
                    AddWordMode = NONE;
                    if(AddWordSelectImageCount != -1){
                        midP = midPoint(addWordFrame.leftTop, addWordFrame.rightBottom);
                        imgLengHalf = spacing(midP, addWordFrame.rightBottom);
                        oldRotation = rotationforTwo(event);
                    }
                    break;

                case MotionEvent.ACTION_MOVE: // touch drag with the ball
                    //如果是双指点中
                    if (event.getPointerCount() == 2) {
                        if (AddWordSelectImageCount != -1) {
                            AddWordMode = DOUBLE_ZOOM;
                            float x = event.getX(0) - event.getX(1);
                            float y = event.getY(0) - event.getY(1);
                            float value = (float) Math.sqrt(x * x + y * y);// 计算两点的距离

                            //旋转的角度
                            float newRotation = rotationforTwo(event) - oldRotation;
                            if (baseValue == 0) {
                                baseValue = value;
                            } else {
                                //旋转到一定角度再执行 不能刚点击就执行旋转或者缩放
                                if (value - baseValue >= 15 || value - baseValue <= -15) {
                                    float scale = value / baseValue;// 当前两点间的距离除以手指落下时两点间的距离就是需要缩放的比例。
                                    addWordMatrix.set(addWordSavedMatrix);
                                    if (scale>3)scale=3.0f;
                                    if (scale<0.5)scale=0.5f;

                                    Toast.makeText(context,"scale="+scale,Toast.LENGTH_LONG).show();
                                    addWordMatrix.postScale(scale, scale, midP.x, midP.y);
                                    addWordMatrix.postRotate(newRotation, midP.x, midP.y);
                                }
                            }
                        }
                    } else if (event.getPointerCount() == 1) {
                        //单指点击
                        if (AddWordSelectImageCount != -1) {
                            if (AddWordMode == DRAG) {
                                if (event_x < width - 50 && event_x > 50
                                        && event_y > 100
                                        && event_y < height - 100) {
                                    addWordMatrix.set(addWordSavedMatrix);
                                    // 图片移动的距离
                                    float translateX = event_x - startPoinF.x;
                                    float translateY = event_y - startPoinF.y;
                                    addWordMatrix.postTranslate(translateX, translateY);
                                }
                            } else if (AddWordMode == ZOOM) {
                                //点击到了缩放旋转按钮
                                PointF movePoin = new PointF(event_x, event_y);

                                float moveLeng = spacing(startPoinF, movePoin);
                                float newRotation = rotation(midP, movePoin) - oldRotation;

                                if (moveLeng > 10f) {
                                    float moveToMidLeng = spacing(midP, movePoin);
                                    float scale = moveToMidLeng / imgLengHalf;
                                    if (scale>3)scale=3.0f;
                                    if (scale<0.5)scale=0.5f;

                                    Toast.makeText(context,"scale="+scale,Toast.LENGTH_LONG).show();

                                    addWordMatrix.set(addWordSavedMatrix);
                                    addWordMatrix.postScale(scale, scale, midP.x, midP.y);
                                    addWordMatrix.postRotate(newRotation, midP.x, midP.y);
                                }
                            }
                        }
                    }

                    if(AddWordMode != NONE){
                        if (AddWordSelectImageCount != -1) {
                            //最后在action_move 执行完前设置好矩阵 设置view的位置
                            addWordFrame = addFrameHolders.get(AddWordSelectImageCount).getAddWordFrame();
                            adjustLocation(addWordMatrix, addWordFrame);
                            addWordFrame.getLayout().setBackgroundColor(Color.TRANSPARENT);
                            Bitmap bitmap=BitmapUtils.convertViewToBitmap(addWordFrame.getLayout().getAddTextView());

                            //Toast.makeText(context,""+bitmap.getConfig(),Toast.LENGTH_LONG).show();
                           // Bitmap.createBitmap(bitmap.getWidth(),bitmap.getHeight(), Bitmap.Config.ARGB_8888);
                            Bitmap bm = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                                    bitmap.getHeight(), addWordMatrix, true);
                            ImageView img=(ImageView)findViewById(R.id.img);
                            img.setImageBitmap(bm);

                           // Bitmap b = setTransparentBg(Color.,bm);
                            try {
                                String name ="addwoed"+".png";
                                saveBitmap(bm, name);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }
                    break;

                case MotionEvent.ACTION_POINTER_UP: //一只手指离开屏幕，但还有一只手指在上面会触此事件
                    //什么都没做
                    AddWordMode = NONE;
                    break;

                case MotionEvent.ACTION_UP:

                    if((System.currentTimeMillis() - touchDownTime < 200)&&(event.getRawX() - event_x < 5)&&addWordFrame.isSelect()==true) {
                        //showDialog(context);
                        View contentView= LayoutInflater.from(context).inflate(context.getResources().getLayout(R.layout.addwordlayout), null, false);

                        TextEditorPopupWindow textEditorPopupWindow =new TextEditorPopupWindow(context,addWordFrame);
                        textEditorPopupWindow.show(contentView);
                        textEditorPopupWindow.confirm.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });
                    }

                    AddWordMode = NONE;
                    break;
            }
            return true;
        }
    }

    private void deleteMyFrame() {
        if (AddWordSelectImageCount != -1) {
            frame.removeView(addFrameHolders.get(AddWordSelectImageCount).getAddWordFrame());
            addFrameHolders.remove(AddWordSelectImageCount);
            AddWordSelectImageCount = -1;
        }
    }

    private float rotationforTwo(MotionEvent event) {
        double delta_x = (event.getX(0) - event.getX(1));
        double delta_y = (event.getY(0) - event.getY(1));
        double radians = Math.atan2(delta_y, delta_x);
        return (float) Math.toDegrees(radians);
    }

    // 得到两个点的距离
    private float spacing(PointF p1, PointF p2) {
        float x = p1.x - p2.x;
        float y = p1.y - p2.y;
        return (float) Math.sqrt(x * x + y * y);
    }

    // 得到两个点的中点
    private PointF midPoint(PointF p1, PointF p2) {
        PointF point = new PointF();
        float x = p1.x + p2.x;
        float y = p1.y + p2.y;
        point.set(x / 2, y / 2);
        return point;
    }

    // 旋转
    private float rotation(PointF p1, PointF p2) {
        double delta_x = (p1.x - p2.x);
        double delta_y = (p1.y - p2.y);
        double radians = Math.atan2(delta_y, delta_x);
        return (float) Math.toDegrees(radians);
    }

    public void showDialog(final Context context){
        final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle("输入编辑内容");
        final EditText et = new EditText(context);
        et.setText(addWordFrame.getLayout().getText());
        et.requestFocus();
        dialog.setView(et);//给对话框添加一个EditText输入文本框

        //给对话框添加一个确定按钮，同样的方法可以添加一个取消按钮
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                //addFrameHolders.get(AddWordSelectImageCount).getAddWordFrame().getLayout().setText(et.getText().toString());
                if((et.getText().toString()=="") ||(null==et.getText().toString())) {
                    deleteMyFrame();
                } else {
                    addWordFrame.getLayout().setText(et.getText().toString());
                    ajustAddWord();
                }
            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                arg0.dismiss();
            }
        });

        //下面是弹出键盘的关键处
        AlertDialog tempDialog = dialog.create();
        tempDialog.setView(et, 0, 0, 0, 0);

        tempDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            public void onShow(DialogInterface dialog) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(et, 0);
            }
        });
        tempDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            public void onDismiss(DialogInterface dialog) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(et.getWindowToken(),0);
            }
        });


        tempDialog.show();
    }
    private void saveBitmap(Bitmap bitmap, String bitName) throws IOException
    {
        File file = new File("/sdcard/DCIM/Camera/"+bitName);
        if(file.exists()){
            file.delete();
        }
        FileOutputStream out;
        try{
            out = new FileOutputStream(file);
            if(bitmap.compress(Bitmap.CompressFormat.PNG, 50, out))
            {
                out.flush();
                out.close();
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    public static Bitmap setTransparentBg( int color,Bitmap orginBitmap) {

        Paint paint = new Paint();
        paint.setColor(color);
        Bitmap bitmap = Bitmap.createBitmap(orginBitmap.getWidth(),
                orginBitmap.getHeight(), orginBitmap.getConfig());
        Canvas canvas = new Canvas(bitmap);
        canvas.drawRect(0, 0, orginBitmap.getWidth(), orginBitmap.getHeight(), paint);
        canvas.drawBitmap(orginBitmap, 0, 0, paint);
        return bitmap;

    }



}
