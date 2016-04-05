package com.example.riane.hanbaoreader_app.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.File;
import java.text.DecimalFormat;

public class ToolUtils {
	public static boolean isWifiConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mWiFiNetworkInfo = mConnectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if (mWiFiNetworkInfo != null) {
				return mWiFiNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	public static boolean isMobileConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mMobileNetworkInfo = mConnectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			if (mMobileNetworkInfo != null) {
				return mMobileNetworkInfo.isAvailable();
			}
		}
		return false;
	}
	public static int getStatusHeight(Activity activity){
		//获取状态栏高度
		Rect frame = new Rect();  
		activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);  
		int statusBarHeight = frame.top;  
		return statusBarHeight;
	}
	// exchange file size
		public static String FormetFileSize(long fileS) {
			DecimalFormat df = new DecimalFormat("#.00");
			String fileSizeString = "";
			if (fileS < 1024) {
				fileSizeString = df.format((double) fileS) + "B";
			} else if (fileS < 1048576) {
				fileSizeString = df.format((double) fileS / 1024) + "K";
			} else if (fileS < 1073741824) {
				fileSizeString = df.format((double) fileS / 1048576) + "M";
			} else {
				fileSizeString = df.format((double) fileS / 1073741824) + "G";
			}
			return fileSizeString;
		}
		public static void shareWithOther(Context context,String msg){
			  Intent intent=new Intent(Intent.ACTION_SEND);
		      intent.setType("text/plain");
		      intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
		      intent.putExtra(Intent.EXTRA_TEXT, msg);
		      context.startActivity(Intent.createChooser(intent, "分享软件"));
		}
		public static boolean fileIsExists(String filePath){
            File f=new File(filePath);
            if(!f.exists()){
                    return false;
            }
            return true;
    }

		public static boolean checkEnd(String fileName, String[] endType) {
			if (fileName != null && endType != null) {
				for (String end : endType) {
					if (fileName.endsWith(end))
						return true;
				}
			}
			return false;
		}
//		public static Drawable getIcon(Context context,String filename){
//			if (checkEnd(filename, context.getResources().getStringArray(R.array.epubFile))) {
//				return context.getResources().getDrawable(R.drawable.file_epub);
//			} else if (checkEnd(filename,
//					context.getResources().getStringArray(R.array.webFile))) {
//				return context.getResources().getDrawable(R.drawable.file_html);
//			} else if (checkEnd(filename,
//					context.getResources().getStringArray(R.array.txtFile))) {
//				return  context.getResources().getDrawable(R.drawable.file_txt);
//			} else if (checkEnd(filename,
//					context.getResources().getStringArray(R.array.mobiFile))) {
//				return  context.getResources().getDrawable(R.drawable.file_mobi);
//			}
//			else if (checkEnd(filename,
//					context.getResources().getStringArray(R.array.oebFile))) {
//				return  context.getResources().getDrawable(R.drawable.file_oeb);
//			}
//			else {
//				return context.getResources().getDrawable(R.drawable.file_icon_default);
//			}
//		}
		public static String myPercent(int y,int z)  
		{  
		    String baifenbi="";//接受百分比的值  
		     double baiy=y*1.0;  
		    double baiz=z*1.0;  
		    double fen=baiy/baiz;  
		    //NumberFormat nf   =   NumberFormat.getPercentInstance();     注释掉的也是一种方法  
		    //nf.setMinimumFractionDigits( 2 );        保留到小数点后几位  
		    DecimalFormat df1 = new DecimalFormat("##.00%");    //##.00%   百分比格式，后面不足2位的用0补齐  
		    //baifenbi=nf.format(fen);    
		    baifenbi= df1.format(fen);   
		    return baifenbi;  
		}  


		public static String returnSuffix(String fileName) {

			if (fileName.lastIndexOf(".") > 0) {
				String fileSuffix = fileName.substring(fileName.lastIndexOf("."));
				return fileSuffix;
			}
			return null;
		}

		public static String returnName(String fileName) {

			if (fileName.indexOf(".") > 0) {
				String name = fileName.substring(fileName.indexOf("."));
				return name;
			}
			return null;
		}

}
