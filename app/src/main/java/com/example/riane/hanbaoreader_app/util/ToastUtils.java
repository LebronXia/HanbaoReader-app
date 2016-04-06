package com.example.riane.hanbaoreader_app.util;

import android.content.Context;
import android.widget.Toast;

public class ToastUtils {
	public static void toast(Context context, int resId) {
		Toast.makeText(context, context.getString(resId), Toast.LENGTH_LONG).show();
	}

	public static void toast(Context context, String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
	}
}
