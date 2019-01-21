package com.example.hujing.addworddemo2;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * Created by hujing on 2019/1/7.
 */

public class TextEditorPopupWindow  implements TextColorPickerView.OnTextColorChangedListener{
    private Context mCtx;
    private View mContentView;
    private int viewHeght;
    private Dialog mDialog;
    private  EditText input;
    private  AddWordFrame addWordFrame;
    private TextColorPickerView textColorPickerView;
    public TextView cancel,confirm;
    public static  int index =0;
    public TextEditorPopupWindow(Context context,AddWordFrame addWordFrame){
        this.mCtx = context;
        this.addWordFrame = addWordFrame;
        mDialog = new Dialog(mCtx, R.style.Dialog);
    }
    public void dismiss() {
        // TODO Auto-generated method stub
        if (mDialog != null)
            mDialog.dismiss();
    }


    public void show(View mContentView) {
        mDialog.setContentView(mContentView);
        Window dialogWindow = mDialog.getWindow();


        dialogWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE|WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        dialogWindow
                .addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (mCtx.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            dialogWindow.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            dialogWindow.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

//        if (styleAnim <= 0) {
//            dialogWindow.setWindowAnimations(R.style.dialog_anim);
//        } else {
//            dialogWindow.setWindowAnimations(styleAnim);
//        }
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        input= (EditText)mContentView.findViewById(R.id.text_input);
        /*if(addWordFrame.getLayout().getText().toString() == "请在下方输入文字") {
            input.setText("");
        }else {
            input.setText(addWordFrame.getLayout().getText());
        }*/
        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                addWordFrame.getLayout().setText(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        textColorPickerView =(TextColorPickerView)mContentView.findViewById(R.id.color_panel);
        textColorPickerView.setCurrentIndex(index);
        textColorPickerView.setOnTextColorChangedListener(this);

        cancel = (TextView)mContentView.findViewById(R.id.cancel_button);
        confirm = (TextView)mContentView.findViewById(R.id.confirm_button);


       /* cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index = textColorPickerView.getCurrentIndex();
                dismiss();
            }
        });*/

            mDialog.show();
    }

    public boolean isShowing() {
        // TODO Auto-generated method stub
        if (mDialog == null)
            return false;
        return mDialog.isShowing();
    }

    @Override
    public void OnTextColorChanged(int colorIndex, int color) {
        input.setTextColor(color);
        addWordFrame.getLayout().setTextColor(color);
        addWordFrame.getLayout().setText(input.getText().toString());



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
            if(bitmap.compress(Bitmap.CompressFormat.PNG, 90, out))
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
}
