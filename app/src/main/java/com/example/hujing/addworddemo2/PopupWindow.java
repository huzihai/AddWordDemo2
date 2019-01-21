package com.example.hujing.addworddemo2;

/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.animation.AnimatorSet;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;


import java.lang.ref.WeakReference;
import java.util.Currency;



public class PopupWindow {
	private Context mCtx;
	private AnimatorSet mEntryAnimatorSet = new AnimatorSet();
	private AnimatorSet mExitAnimatorSet = new AnimatorSet();
	private View animView, parentViewBg;
	private TextView tvTitel;
	private View mContentView;
	private final int ENTER_TOTAL_DURATION_TIME = 300;
	private int viewHeght;
	private Dialog mDialog;

	public PopupWindow(Context context) {
		this.mCtx = context;
		mDialog = new Dialog(mCtx, R.style.Dialog);
	}

	public void dismiss() {
		// TODO Auto-generated method stub
		if (mDialog != null)
			mDialog.dismiss();
	}

	public void show(View mContentView, int styleAnim) {
		mDialog.setContentView(mContentView);
		Window dialogWindow = mDialog.getWindow();
		dialogWindow
				.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		if (mCtx.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			dialogWindow.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		} else {
			dialogWindow.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		}

		if (styleAnim <= 0) {
			dialogWindow.setWindowAnimations(R.style.dialog_anim);
		} else {
			dialogWindow.setWindowAnimations(styleAnim);
		}
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		 lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		 lp.height = WindowManager.LayoutParams.MATCH_PARENT;

//		float w = 1920;
//		float h = 1080;
//		Log.d("s", "->HBbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb: w = "+w+"   h:"+h);
//		// float ww = (w < h) ? w : h;
//		lp.width = (int) w;
//		lp.height = (int) h;
		mDialog.show();
	}

	public boolean isShowing() {
		// TODO Auto-generated method stub
		if (mDialog == null)
			return false;
		return mDialog.isShowing();
	}
}
