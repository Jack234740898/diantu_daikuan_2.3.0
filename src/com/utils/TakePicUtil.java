package com.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;

import com.img.download.SDCardUtil;
import com.sxjs.diantu_daikuan.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/*
 * 拍照 ，相册
 */
public class TakePicUtil implements OnClickListener {

	private static final String TAG = "TakePicUtil";
	public static final int CHOOSE_PIC = 1;
	public static final int TAKE_PIC = 2;
	private String localimgPath;
	private Activity mActivity;
	private static final int need_ori_w = 650;
	private static final int need_ori_h = 800;
	public TakePicUtil(Activity activity) {
		this.mActivity = activity;
	}
	/**
	 * 选择提示对话框
	 */
	private AlertDialog dialog;
	public void ShowPickDialog() {
		dialog = new AlertDialog.Builder(mActivity).create();
		dialog.show();
		Window window = dialog.getWindow();
		window.setGravity(Gravity.CENTER);
		int width = ScreenUtil.getWidth(mActivity) * 5 / 6;
		window.setLayout(width,
				android.view.WindowManager.LayoutParams.WRAP_CONTENT);
		View view = mActivity.getLayoutInflater().inflate(
				R.layout.choose_pics, null);
		window.setContentView(view);//
		view.findViewById(R.id.take_pic_text).setOnClickListener(this);
		view.findViewById(R.id.choose_pic_text).setOnClickListener(this);
		view.findViewById(R.id.cancle).setOnClickListener(this);
		/*AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
		builder.setTitle("图片来源");
		builder.setNegativeButton("取消", null);		
		builder.setItems(new String[] { "拍照", "相册" },
				new DialogInterface.OnClickListener() {
					// 类型码
					int REQUEST_CODE;
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						switch (which) {
						case 0:
							takePic();
							break;
						case 1:
							choosePic();							
							break;
						default:
							break;
						}
					}
				});
		builder.create().show();*/
	}

	/*
	 * 直接拍照
	 */
	public void takePic() {//MediaStore.ACTION_IMAGE_CAPTURE
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		File file = new File(getLocalImgPath(mActivity));//new File(Environment.getExternalStorageDirectory(),fileName);
		Uri imageUri = Uri.fromFile(file);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		
		localimgPath = file.getPath();
		LogUtil.d(TAG, "拍照==localimgPath is "+localimgPath);
		mActivity.startActivityForResult(intent, TAKE_PIC);
		//mActivity.startActivityForResult(intent, mActivity.RESULT_FIRST_USER);
	}

	/*
	 * 从相册获取
	 */
	//static String path;
	public void choosePic() {
		Intent intent = new Intent(Intent.ACTION_PICK, null);
		intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
		mActivity.startActivityForResult(intent, CHOOSE_PIC);
		//mActivity.startActivityForResult(intent, mActivity.RESULT_FIRST_USER);
	}
	
	public String getBitmapFromPic2(Intent data){
		if(null==data)
			return null;
		Uri originalUri = data.getData(); 
		if(null==originalUri)
			return null;
		ContentResolver resolver = mActivity.getContentResolver();
		//照片的原始资源地址
		try {
			Bitmap bitmap = MediaStore.Images.Media.getBitmap(resolver, originalUri);
			String path = saveBitmap(bitmap);
			bitmap.recycle();
			bitmap = null;
			bitmap = getSmallBitmap(path);
			path = saveBitmap(bitmap);
			LogUtil.d(TAG, "相册照片选取保存完后==path is "+path);
	       return path;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * 路径格式
	 * /storage/emulated/0/Android/data/com.yishengjia.patients/cache/1411833903074.jpg
	 */
	public String getBitmapPath2(Intent data) {
		LogUtil.d(TAG, "拍照后处理==localimgPath is "+localimgPath);
		if(!new File(localimgPath).exists())
			return null;
		Bitmap bitmap = BitmapFactory.decodeFile(localimgPath);//getSmallBitmap(localimgPath);
		int degree = readPictureDegree(localimgPath);
		bitmap = rotaingImageView(degree, bitmap);
		String path = saveBitmap(bitmap);
		bitmap = getSmallBitmap(path);
		return saveBitmap(bitmap);
	}
	
	private Bitmap getBitmap(String path){
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);
		int height = options.outHeight;
		int width = options.outWidth;
		
		//设置图片缩小比例   
		float scale = 0.8f;  
		LogUtil.d(TAG, "图片缩小比例 ==scale is "+scale);
		 //计算出这次要缩小的比例   
		//scaleW = (float) (scaleWidth * scale);  
		//scaleH = (float) (scaleHeight * scale);  
		Matrix matrix = new Matrix();
		matrix.postScale(scale, scale);// 利用矩阵进行缩放不会造成内存溢出
		options.inJustDecodeBounds = false;
		Bitmap bitmap = BitmapFactory.decodeFile(path, options);
		Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
		if(null!=bitmap)
			bitmap.recycle();
		return newbmp;
	}
	
	private String saveBitmap(Bitmap b){
		LogUtil.d(TAG, "saveBitmap()==b is "+b);
		if(null==b)
			return null;	
		try {
			String path = getLocalImgPath(mActivity);
			FileOutputStream fos = new FileOutputStream(new File(path));
			b.compress(Bitmap.CompressFormat.JPEG, 100, fos);
			b.recycle();
			b = null;
			return path;	
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;	
	}


	/**
	 * 裁剪图片方法实现
	 * 
	 * @param uri
	 */
	public void startPhotoZoom(final Activity activity, Uri uri) {
		/*
		 * 至于下面这个Intent的ACTION是怎么知道的，大家可以看下自己路径下的如下网页
		 * yourself_sdk_path/docs/reference/android/content/Intent.html
		 * 直接在里面Ctrl+F搜：CROP ，之前小马没仔细看过，其实安卓系统早已经有自带图片裁剪功能, 是直接调本地库的，小马不懂C C++
		 * 这个不做详细了解去了，有轮子就用轮子，不再研究轮子是怎么 制做的了...吼吼
		 */
		int width = ScreenUtil.getWidth(activity);
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 3);
		intent.putExtra("aspectY", 2);
		// outputX outputY 是裁剪图片宽高
		int outputX = width / 2;
		int outputY = (width / 2);
		intent.putExtra("outputX", outputY);
		intent.putExtra("outputY", width);
		// intent.putExtra("scale", true);
		// intent.putExtra("return-data", false);
		// intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		//intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
		intent.putExtra("outputFormat", "JPEG");// 返回格式
		intent.putExtra("noFaceDetection", true);
		activity.startActivityForResult(intent, 3);
	}

	/*
	 * 取得本地图片存储路径
	 * Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
	 * || !Environment.isExternalStorageRemovable()
	 */
	public static String getLocalImgPath(Activity activity) {
		String cachePath = null;
		if (SDCardUtil.hasSdcard()) {
			cachePath = activity.getExternalCacheDir().getPath();
		} else {
			cachePath = activity.getCacheDir().getPath();
		}
		return cachePath + File.separator + System.currentTimeMillis() + ".jpg";
	}
	
	/*private int reqWidth,reqHeight;
	public void setWH(int reqWidth,int reqHeight){
		this.reqWidth = reqWidth;
		this.reqHeight = reqHeight;
	}*/

	public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
		// 旋转图片 动作
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		System.out.println("angle2=" + angle);
		// 创建新的图片
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
				bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		return resizedBitmap;
	}

	/**
	 * 读取图片属性：旋转的角度
	 * 
	 * @param path
	 *            图片绝对路径
	 * @return degree旋转的角度
	 */
	public static int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}
	
	private Bitmap getSmallBitmapByUri(Uri uri){
		if(null==uri)
			return null;
		ContentResolver cr = mActivity.getContentResolver();	
		try {
			InputStream in = cr.openInputStream(uri);
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(in, null, options);
			options.inSampleSize = calculateInSampleSize(options);
			options.inJustDecodeBounds = false;
			options.inPreferredConfig = Bitmap.Config.RGB_565;   
			//options.inPurgeable = true;  
			//options.inInputShareable = true; 
			in = cr.openInputStream(uri);
			return BitmapFactory.decodeStream(in, null, options);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	private static Bitmap getSmallBitmap(String path){
		if(!StringUtil.checkStr(path))
			return null;
		if(!new File(path).exists())
			return null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);
		options.inSampleSize = calculateInSampleSize(options);
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(path, options);
	}
	
	/*
	 * 计算图片的缩放比
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		if(width>height){
			if (width > need_ori_w) {
				inSampleSize = Math.round((float) width / (float) need_ori_w);
			}
		}else{
			if (height > need_ori_h) {
				inSampleSize = Math.round((float) height / (float) need_ori_h);
			}
		}
		LogUtil.d(TAG, "最终缩放比==inSampleSize is "+inSampleSize);
		return inSampleSize;
	}
	
	@Override
	public void onClick(View v) {
		if(null!=dialog)
			dialog.dismiss();
		switch (v.getId()) {
		case R.id.take_pic_text:
			takePic();
			break;
		case R.id.choose_pic_text:
			choosePic();
			break;
		case R.id.cancle:
			break;
		default:
			break;
		}
	}
}
