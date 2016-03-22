package com.example.riane.hanbaoreader_app.util;

import android.text.TextUtils;
import android.util.Log;

public class LogUtils {
	public static String customTagPrefix = "";

	private LogUtils() {
		
    }

    public static boolean allowD = true;
    public static boolean allowE = true;
    public static boolean allowI = true;
    public static boolean allowV = true;
    public static boolean allowW = true;
    public static boolean allowWTF = true;
    
	private static String generateTag() {
		StackTraceElement caller = Thread.currentThread().getStackTrace()[4];
        String tag = "%s.%s(L:%d)";
        String callerClazzName = caller.getClassName();
        callerClazzName = callerClazzName.substring(callerClazzName.lastIndexOf(".") + 1);
        tag = String.format(tag, callerClazzName, caller.getMethodName(), caller.getLineNumber());
        tag = TextUtils.isEmpty(customTagPrefix) ? tag : customTagPrefix + ":" + tag;
        return tag;
    }
	
	public static void e(Object content) {
		if(!allowE){
			return;
		}
        Log.e(generateTag(), content + "");
    }
	
	public static void e(Object content, Exception e){
		if(!allowE){
			return;
		}
		Log.e(generateTag(), content + "", e);
	}
	
	public static void d(Object content){
		if(!allowD){
			return;
		}
		Log.d(generateTag(), content + "");
	}
	
	public static void i(Object content){
		if(!allowI){
			return;
		}
		Log.i(generateTag(), content + "");
	}
	
	public static void v(Object content){
		if(!allowV){
			return;
		}
		Log.v(generateTag(), content + "");
	}
	
	public static void w(Object content){
		if(!allowW){
			return;
		}
		Log.w(generateTag(), content + "");
	}
	
	public static void wtf(Object content){
		if(!allowWTF){
			return;
		}
		Log.wtf(generateTag(), content + "");
	}
}
