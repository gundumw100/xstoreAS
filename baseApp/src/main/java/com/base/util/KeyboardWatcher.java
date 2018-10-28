package com.base.util;

import android.app.Activity;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;

/**
 * 监听软键盘的打开或者关闭
 * 
 * 用法：
 * 确保你在AndroidManifest.xml中为Acitivty配置了adjustResize windowSoftInputMode：
 * <activity
    android:name=".MainActivity"
    android:windowSoftInputMode="adjustResize" />
    
 * 
 * 在Activity.onCreate() 方法中绑定KeyboardWatcher，为了防止内存泄漏，请确保在onDestroy()方法中解绑。
 * 
 * public class MainActivity extends Activity implements KeyboardWatcher.OnKeyboardToggleListener {
    private KeyboardWatcher keyboardWatcher;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //...
        keyboardWatcher = KeyboardWatcher.initWith(this).bindKeyboardWatcher(this);
    }
 
    @Override
    protected void onDestroy() {
        keyboardWatcher.unbindKeyboardWatcher();
        super.onDestroy();
    }
 
    @Override
    public void onKeyboardShown(int keyboardSize) {
 
    }
 
    @Override
    public void onKeyboardClosed() {
 
    }
}
 * 
 * 
 * @author Ni Guijun
 *	@see http://www.jcodecraeer.com/a/opensource/2016/0328/4092.html
 */
public class KeyboardWatcher {

	private Activity activity;
	private ViewGroup rootView;
	private ViewTreeObserver.OnGlobalLayoutListener viewTreeObserverListener;
	private OnKeyboardToggleListener onKeyboardToggleListener;

	public static KeyboardWatcher initWith(Activity activity) {
		KeyboardWatcher keyboardWatcher = new KeyboardWatcher();
		keyboardWatcher.activity = activity;
		return keyboardWatcher;
	}

	public KeyboardWatcher bindKeyboardWatcher(
			OnKeyboardToggleListener onKeyboardToggleListener) {
		this.onKeyboardToggleListener = onKeyboardToggleListener;
		final View root = activity.findViewById(android.R.id.content);
		if (hasAdjustResizeInputMode()) {
			if (root instanceof ViewGroup) {
				rootView = (ViewGroup) root;
				viewTreeObserverListener = new GlobalLayoutListener();
				rootView.getViewTreeObserver().addOnGlobalLayoutListener(
						viewTreeObserverListener);
			}
		} else {
			Log.w("KeyboardWatcher",
					"Activity "
							+ activity.getClass().getSimpleName()
							+ " should have windowSoftInputMode=\"adjustResize\""
							+ "to make KeyboardWatcher working. You can set it in AndroidManifest.xml");
		}

		return this;
	}

	private boolean hasAdjustResizeInputMode() {
		return (activity.getWindow().getAttributes().softInputMode & WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE) != 0;
	}

	public void unbindKeyboardWatcher() {
		if (rootView != null && viewTreeObserverListener != null) {
			if (Build.VERSION.SDK_INT >= 16) {
				rootView.getViewTreeObserver().removeOnGlobalLayoutListener(
						viewTreeObserverListener);
			} else {
				rootView.getViewTreeObserver().removeGlobalOnLayoutListener(
						viewTreeObserverListener);
			}

			this.onKeyboardToggleListener = null;
			this.activity = null;
		}
	}

	private class GlobalLayoutListener implements
			ViewTreeObserver.OnGlobalLayoutListener {
		int initialValue;
		private boolean hasSentInitialAction;
		private boolean isKeyboardShown;

		@Override
		public void onGlobalLayout() {
			if (initialValue == 0) {
				initialValue = rootView.getHeight();
			} else {
				if (initialValue > rootView.getHeight()) {
					if (onKeyboardToggleListener != null) {
						if (!hasSentInitialAction || !isKeyboardShown) {
							isKeyboardShown = true;
							onKeyboardToggleListener
									.onKeyboardShown(initialValue
											- rootView.getHeight());
						}
					}
				} else {
					if (!hasSentInitialAction || isKeyboardShown) {
						isKeyboardShown = false;
						rootView.post(new Runnable() {
							@Override
							public void run() {
								if (onKeyboardToggleListener != null) {
									onKeyboardToggleListener.onKeyboardClosed();
								}
							}
						});
					}
				}

				hasSentInitialAction = true;
			}
		}
	}

	public interface OnKeyboardToggleListener {
		void onKeyboardShown(int keyboardSize);

		void onKeyboardClosed();
	}

}
