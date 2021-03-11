package com.houlik.libhoulik.android.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
//import android.support.v7.widget.Toolbar;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AlertDialog;

import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Spinner;

import java.io.InputStream;
import java.util.List;

/**
 * 安卓工具
 * Created by Houlik on 24/11/2016.
 */
public class AUtils {

	private static AUtils hl_utils = new AUtils();

	private AUtils(){}

	/**
	 * 单一模式
	 * @return
     */
	public static AUtils getInstance(){
		if(hl_utils == null){
			hl_utils = new AUtils();
		}
		return hl_utils;
	}

	/**
	 * 这是在输入框输入第一个字母的时候就开始自动搜索全部资料有关第一个输入的字母
	 * @param arrays_xml_id 这是 R.array.元素名称
	 * @param autoCompleteTextView_id 这是自动填充输入框的id R.id.元件名称
	 * @param activity 这是显示在哪个界面
	 */
	public void autoCompletion(int arrays_xml_id,int autoCompleteTextView_id,Activity activity){
		String[] strings = activity.getResources().getStringArray(arrays_xml_id);
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(activity, android.R.layout.simple_dropdown_item_1line, strings);
		AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) activity.findViewById(autoCompleteTextView_id);
		autoCompleteTextView.setAdapter(arrayAdapter);
	}

	/**
	 * 这是类似下拉表，点击之后将出现已经备好的内容列表。
	 * 如果要触发所选的内容，需要使用 setOnItemSelectedListener 来监听所选
	 * 不能绑定在 TexView 上，绑定后 TextView 还是为空
	 * @param activity 当前界面
	 * @param spinner_id 绑定 spinner id
	 * @param layoutResource android.R.layout.simple_spinner_dropdown_item 展示状态
	 * @param list 选项集合
	 * @return 返回一个spinner 以便可以得到其所有的其它事项
	 */
	public Spinner spinner(Activity activity, int spinner_id, int layoutResource, List list){
		Spinner spinner = (Spinner)activity.findViewById(spinner_id);
		ArrayAdapter adapter = new ArrayAdapter(activity, layoutResource, list);
		spinner.setAdapter(adapter);
		return spinner;
	}

	/**
	 * 用于自定义layout中使用
	 * @param activity
	 * @param spinner
	 * @param layoutResource android.R.layout.simple_spinner_dropdown_item 展示状态
	 * @param list
	 * @return
	 */
	public ArrayAdapter spinner(Activity activity, Spinner spinner, int layoutResource, List list){
		ArrayAdapter adapter = new ArrayAdapter(activity, layoutResource, list);
		spinner.setAdapter(adapter);
		return adapter;
	}

	/**
	 * 如果只是普通字符串数组就返回普通的对象,否则返回种类数组对象
	 * @param context
	 * @param isSimpleArrayResources
	 * @param arrayResource
	 * @return
	 */
	public Object getResource(Context context, boolean isSimpleArrayResources, int arrayResource){
		Resources resources = context.getResources();
		resources.getResourceName(arrayResource);
		TypedArray typedArray = context.getResources().obtainTypedArray(arrayResource);
		return isSimpleArrayResources ? resources : typedArray;
	}

	/**
	 * ToolBar 设置
	 * @param activity
	 * @param idToolBar
	 * @param logo
	 * @param title
	 * @param secondTitle
	 * @param navigationIcon
	 * @param onMenuItemClickListener
	 * return toolbar 用于activity界面中 setSupportActionBar(toolbar)
	 */
	public Toolbar setToolBar(Activity activity, int idToolBar, int logo, String title, String secondTitle,
							  int navigationIcon, Toolbar.OnMenuItemClickListener onMenuItemClickListener){
		Toolbar toolbar= (Toolbar) activity.findViewById(idToolBar);
		//设置APP图标
		toolbar.setLogo(logo);
		// 设置title
		toolbar.setTitle(title);
		// 设置副标题 zxcv
		toolbar.setSubtitle(secondTitle);
		// 设置toolabar
		// 设置导航图标一定要设置在setsupportactionbar后面才有用,不然会显示小箭头
		toolbar.setNavigationIcon(navigationIcon);
		toolbar.setOnMenuItemClickListener(onMenuItemClickListener);
		return toolbar;
	}

	//COMPLEX_UNIT_DIP value * metrics.density
	//COMPLEX_UNIT_SP value * metrics.scaledDensity
	//COMPLEX_UNIT_PT value * metrics.xdpi * (1.0f/72)
	//COMPLEX_UNIT_IN value * metrics.xdpi
	//COMPLEX_UNIT_MM value * metrics.xdpi * (1.0f/25.4f)

	//根据手机的分辨率从 dp 的单位 转成为 px(像素)
	public static int dp2px(Context context, float dpValue) {
		float scale = context.getResources().getDisplayMetrics().density;
		return (int)(dpValue * scale + 0.5f);
	}

	//根据手机的分辨率从 px(像素) 的单位 转成为 dp
	public static float px2dp(Resources resources, float pxValue) {
		final float scale = resources.getDisplayMetrics().density;
		return (pxValue / scale + 0.5f);
	}

	//获取屏幕dpi
	public static int getDpi(Context context) {
		return context.getResources().getDisplayMetrics().densityDpi;
	}

	/**
	 * 文字
	 * sp 转 px
	 * @param context
	 * @param sp
	 * @return
	 */
	public static int sp2px(Context context, int sp){
		return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
	}

	public int getScreenHeight(Activity activity){
		DisplayMetrics displayMetrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		int height = displayMetrics.heightPixels;
		return height;
	}

	/**
	 * 通过Activity获取屏幕宽高
	 * @param activity getActivity()
	 * @return
	 */
	public int getScreenWidth(Activity activity){
		DisplayMetrics displayMetrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		int width = displayMetrics.widthPixels;
		return width;
	}

	public int getScreenHeight(Context context){
		DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
		int height = displayMetrics.heightPixels;
		return height;
	}

	public int getScreenWidth(Context context){
		DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
		int width = displayMetrics.widthPixels;
		return width;
	}

	/**
	 * 动态设置ViewGroup宽高
	 * @param width
	 * @param viewGroup
	 */
	public void setLayoutWidthParam(int width, ViewGroup viewGroup){
		ViewGroup.LayoutParams layoutParam = viewGroup.getLayoutParams();
		layoutParam.width = width;
		viewGroup.setLayoutParams(layoutParam);
	}

	/**
	 * 读取raw文件
	 * @param activity
	 * @param rawID
	 * @return
	 */
	public String getFromRaw(Activity activity, int rawID){
		String result = "";
		try {
			InputStream in = activity.getResources().openRawResource(rawID);
			//获取文件的字节数
			int lenght = in.available();
			//创建byte数组
			byte[]  buffer = new byte[lenght];
			//将文件中的数据读到byte数组中
			in.read(buffer);
//			result = EncodingUtils.getString(buffer, "UTF-8");
			result = new String("UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	//浏览网页
	public void toWeb(String http, Activity fromActivity, Class toActivity, String bundleKey){
		Uri uri = Uri.parse(http);
		Intent intent = new Intent();
		intent.setClass(fromActivity, toActivity);
		Bundle bundle = new Bundle();
		bundle.putString(bundleKey,uri.toString());
		intent.putExtras(bundle);
		fromActivity.startActivity(intent);
	}

	/**
	 * 这是设置全屏方法
	 * @param activity
	 */
	public void setFullScreen(Activity activity){
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}

	/**
	 * 设置ImageView长宽
	 * @param activity
	 * @param imageView
	 * @param width
	 * @param height
	 */
	public void setImageViewSize(Activity activity, ImageView imageView, int width, int height){
		ViewGroup.LayoutParams params = imageView.getLayoutParams();
		params.height=width;
		params.width =height;
		imageView.setLayoutParams(params);
	}

	public int getSdkVersion(){
		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		return currentapiVersion;
	}

	/**
	 * 自启动软件
	 * @param context
	 */
	public void startApp(Context context, Class<?> cls) {
		Intent start = new Intent(context, cls);
		start.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(start);
	}

	/**
	 * 返回桌面
	 * @param context
	 */
	public void backToHome(Context context){
		Intent home=new Intent(Intent.ACTION_MAIN);
		home.addCategory(Intent.CATEGORY_HOME);
		context.startActivity(home);
	}

	/**
	 * 不透明 1f 半透明 0.5f
	 *
	 * 使用后返回原背景方法
	 * class CustomPWDismiss implements PopupWindow.OnDismissListener{
	 *
	 *         @Override
	 *         public void onDismiss() {
	 *             AUtils.getInstance().setAlphaBackground(activity, 1f);
	 *         }
	 *     }
	 *
	 * 调用
	 * pw[0].setOnDismissListener(new CustomPWDismiss());
	 *
	 * @param activity
	 * @param alpha
	 */
	public void setAlphaBackground(Activity activity, float alpha){
		WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
		lp.alpha = alpha;
		//设置背景模糊 - 测试后未见效果
		activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		activity.getWindow().setAttributes(lp);
	}

	/**
	 * 设置AlertDialog透明背景
	 * @param builder
	 */
	public void setAlphaDialogBackground(AlertDialog.Builder builder, float alpha){
		Dialog dialog = builder.create();
		Window window = dialog.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.alpha = alpha;
		window.setAttributes(lp);
		dialog.show();
	}

	/**
	 * 这是点击两次才能退出的方法
	 * 这是要写在Activity类里才能见效，并且重写父类的方法
	 * 点击退出按钮的时间
	 * private long hlExitTime = 0;
	 *
	 * 退出程序
	 * public boolean onKeyDown(int keyCode, KeyEvent event){
	 * 		if(keyCode==KeyEvent.KEYCODE_BACK){
	 * 			if(System.currentTimeMillis()-hlExitTime < 2000){
	 * 				System.exit(0); //注意: 最好使用finish() 否则退出前不会触发onDestroy()
	 * 			}else{
	 * 				Toast.makeText(this,"再按一次退出", Toast.LENGTH_SHORT).show();
	 * 				hlExitTime = System.currentTimeMillis();
	 * 			}
	 * 			return true;
	 * 		}
	 * 		return onKeyDown(keyCode, event);
	 * }
	 *
	 *
	 * 清除当前页面
	 * intent.setClass(Qlmy_Main_Activity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	 * 停止当前页面
	 * Activity.finish()
	 *
	 *
	 * Timer
	 * TimerTask
	 *
	 * timer.schedule(TimerTask, Date timeDelay, long delay)
	 *
	 */

}
