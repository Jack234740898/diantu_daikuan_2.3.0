package com.img.download;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.util.Base64;
import android.widget.ImageView;

import com.img.download.ImageSizeUtil.ImageSize;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class BitmapUtil {

	private static final String TAG = "BitmapUtil";
	private static float roundPx = 25;

	/*
	 * 如果图片是正方形的，将ratio设置�?2，如果图片不是正方形，自己再做个截图吧！将图片截成需要的正方形�?�显示圆角边�?1/4,则传�?8，以此类推！
	 */
	public static Bitmap toRoundCorner(Bitmap bitmap, float ratio) {
		System.out.println("图片是否变成圆形模式�?+++++++++++++");
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		canvas.drawRoundRect(rectF, bitmap.getWidth() / ratio,
				bitmap.getHeight() / ratio, paint);

		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}

	// 圆角�?
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {

		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}

	/*
	 * 得到不变形的图片
	 */
	public static Bitmap getBitmap(Bitmap bitmap, int width) {
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		Matrix matrix = new Matrix();
		float scale = (float) width / w;
		// 保证图片不变�?.
		matrix.postScale(scale, scale);
		// w,h是原图的属�??.
		return Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
	}

	/*
	 * 判断照片角度
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

	/*
	 * 旋转照片
	 */
	public static Bitmap rotateBitmap(Bitmap bitmap, int degress) {
		if (bitmap != null && !bitmap.isRecycled()) {
			Matrix m = new Matrix();
			m.postRotate(degress);
			bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
					bitmap.getHeight(), m, true);
			return bitmap;
		}
		return bitmap;
	}

	/*
	 * 计算图片的缩放�??
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		int width = options.outWidth;
		int height = options.outHeight;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			int widthRatio = Math.round((float) width / (float) reqWidth);
			int heightRatio = Math.round((float) height / (float) reqHeight);
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return inSampleSize;
	}

	/*
	 * 根据路径获得图片并压缩，返回bitmap用于显示
	 */
	public static Bitmap getSmallBitmap(String filePath, int reqWidth,
			int reqHeight) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;

		return BitmapFactory.decodeFile(filePath, options);
	}

	/*
	 * 把bitmap转换成String
	 */
	@SuppressLint("NewApi")
	public static String bitmapToString(String filePath) {

		Bitmap bm = getSmallBitmap(filePath, 0, 0);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.JPEG, 40, baos);
		byte[] b = baos.toByteArray();
		return Base64.encodeToString(b, Base64.DEFAULT);
	}

	/*
	 * 压缩图片，处理某些手机拍照角度旋转的问题
	 */
	public static String compressImage(Context context, String filePath,
			String fileName, int q) throws FileNotFoundException {

		Bitmap bm = getSmallBitmap(filePath, 0, 0);

		int degree = readPictureDegree(filePath);

		if (degree != 0) {// 旋转照片角度
			bm = rotateBitmap(bm, degree);
		}

		File imageDir = null;// SDCardUtils.getImageDir(context);

		File outputFile = new File(imageDir, fileName);

		FileOutputStream out = new FileOutputStream(outputFile);

		bm.compress(Bitmap.CompressFormat.JPEG, q, out);

		return outputFile.getPath();
	}

	/*
	 * 根据Bitmap得到byte[]
	 */
	public static byte[] getBytesByBitmap(Bitmap bitmap) {
		if (null == bitmap || bitmap.isRecycled())
			return null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		if(bitmap.hasAlpha()){
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
		}else{
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		}
		return baos.toByteArray();
	}
	
	public static Bitmap getShowBitmap(ImageView imgview, byte[] data) {
		if(null==data || data.length<=0)
			return null;
		ImageSize imgsise = ImageSizeUtil.getImageViewSize(imgview);
		int imgW = imgsise.width;
		int imgH = imgsise.height;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeByteArray(data,0, data.length, options);
		options.inSampleSize = ImageSizeUtil.caculateInSampleSize(options, imgW, imgH);
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeByteArray(data,0, data.length, options);
	}
	
	public static Bitmap getShowBitmap(ImageView imgview, Bitmap bitmap) {
		byte[] data = BitmapUtil.getBytesByBitmap(bitmap);
		if(null==data || data.length<=0)
			return null;
		ImageSize imgsise = ImageSizeUtil.getImageViewSize(imgview);
		int imgW = imgsise.width;
		int imgH = imgsise.height;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeByteArray(data,0, data.length, options);
		options.inSampleSize = ImageSizeUtil.caculateInSampleSize(options, imgW, imgH);
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeByteArray(data,0, data.length, options);
	}
	
	public static Bitmap getShowBitmap(ImageView imgview, int resId){
		ImageSize imgsise = ImageSizeUtil.getImageViewSize(imgview);
		int imgW = imgsise.width;
		int imgH = imgsise.height;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(imgview.getContext().getResources(), resId,options);
		options.inSampleSize = ImageSizeUtil.caculateInSampleSize(options, imgW, imgH);
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeResource(imgview.getContext().getResources(), resId,options);
	}
	
	/*
	 * 得到本地的一张原图
	 */
	public static Bitmap getLocalOriBitmap(String localpath){
		if(FileUtil.isExist(localpath)){
			try {
				FileInputStream fis = new FileInputStream(localpath);
				return BitmapFactory.decodeStream(fis);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		return null;
	} 
	
	/*
	 * 得到bitmap的二进制
	 */
	public static byte[] getBitmapByte(String url, Bitmap bitmap) {
		if (StringUtil.checkStr(url) && url.contains(".")) {
			String imgUrl = url;
			imgUrl = imgUrl.substring(imgUrl.lastIndexOf(".") + 1);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			if ("PNG".equalsIgnoreCase(imgUrl)) {
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
			} else {
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			}
			return baos.toByteArray();
		}
		return null;
	}
}
