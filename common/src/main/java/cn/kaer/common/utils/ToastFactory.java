package cn.kaer.common.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.Toast;

/**
 * Toast工厂 防止Toast重复出现
 * 
 * @author Wxcily
 * @date 2014-10-21
 */
public class ToastFactory {
	private ToastFactory() {
	};

	private static Context context = null;
	private static Toast toast = null;

	@SuppressLint("ShowToast")
	public static Toast getToast(Context context, String text) {
		if (ToastFactory.context == context) {
			// toast.cancel();
			toast.setText(text);
			toast.setDuration(Toast.LENGTH_SHORT);

		} else {
			ToastFactory.context = context;
			toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
		}
		return toast;
	}
	
	@SuppressLint("ShowToast")
	public static Toast getToast(Context context, int text) {
		if (ToastFactory.context == context) {
			// toast.cancel();
			toast.setText(text);
			toast.setDuration(Toast.LENGTH_SHORT);

		} else {

			ToastFactory.context = context;
			toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
		}
		return toast;
	}

	@SuppressLint("ShowToast")
	public static Toast getLongToast(Context context, String text) {
		if (ToastFactory.context == context) {
			// toast.cancel();
			toast.setText(text);
			toast.setDuration(Toast.LENGTH_LONG);

		} else {

			ToastFactory.context = context;
			toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
		}
		return toast;
	}
	
	@SuppressLint("ShowToast")
	public static Toast getLongToast(Context context, int text) {
		if (ToastFactory.context == context) {
			// toast.cancel();
			toast.setText(text);
			toast.setDuration(Toast.LENGTH_LONG);

		} else {

			ToastFactory.context = context;
			toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
		}
		return toast;
	}

	public static void cancelToast() {
		if (toast != null) {
			toast.cancel();
		}
	}

}
