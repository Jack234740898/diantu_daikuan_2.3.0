package com.img.download;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ProgressBar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.Response;



public class ImageLoad implements Callback {

	private static String TAG = "ImageLoad";
	private static final int read_time = 15*1000;
	private static final int write_time = 15*1000;
	private static final int connect_time = 15*1000;
	private static String cachImgDir = SDCardUtil
			.getStoragePath(File.separator + "." + "dtdk");

	private static final int default_thread_count = 10;
	private LruCache<String, Bitmap> inercachBitmap;

	private static final int msg_what = 156788;
	private boolean needLoad = true;
	public void setNeedLoad(boolean needLoad) {
		this.needLoad = needLoad;
	}

	private OkHttpClient mOkHttpClient;
	private Handler mHandler;

	public interface CompleteListener {
		public void OnCompletionListener(String localpath);
	}

	private CompleteListener mCompleteListener;
	public void setCompleteListener(CompleteListener l) {
		this.mCompleteListener = l;
	}

	/*
	 * 设置disk cachDir
	 * @param dirName,如  dtdk;
	 */
	public void setDiskCachDir(String dirName){
		if(StringUtil.checkStr(dirName)){
			cachImgDir = SDCardUtil.getStoragePath(File.separator + "." + dirName);
		}
	}
	private ExecutorService mExecutorService;
	private Semaphore mSemaphore = new Semaphore(default_thread_count);
	private Context mContext;
	//@SuppressLint("NewApi")
	public ImageLoad() {
		mOkHttpClient = new OkHttpClient();
		mOkHttpClient.newBuilder().readTimeout(read_time, TimeUnit.MILLISECONDS);
		mOkHttpClient.newBuilder().writeTimeout(write_time, TimeUnit.MILLISECONDS);
		mOkHttpClient.newBuilder().connectTimeout(connect_time, TimeUnit.MILLISECONDS);
		mHandler = new Handler(this);
		final int totalSize = (int) Runtime.getRuntime().totalMemory();
		// 获取到可用内存的最大值，使用内存超出这个值会引起OutOfMemory异常。
		// LruCache通过构造函数传入缓存值，以KB为单位。
		final int maxMemory = (int) (Runtime.getRuntime() .maxMemory() / 1024);
		// 使用最大可用内存值的1/8作为缓存的大小。
		int cacheSize = maxMemory / 8;
		Log.d("cacheSize", "分配的内存缓存大小==maxMemory is " + maxMemory
				+ ",cacheSize is " + cacheSize+",totalSize is "+totalSize);
		// D/cacheSize(18766): 分配的内存缓存大小==maxMemory is 131072,cacheSize is 10922
		inercachBitmap = new LruCache<String, Bitmap>(cacheSize) {

			
			@Override
			protected void entryRemoved(boolean evicted, String key,
					Bitmap oldValue, Bitmap newValue) {
				//super.entryRemoved(evicted, key, oldValue, newValue);
				if (evicted && oldValue != null){
                    //oldValue.recycle();
					/*if(null!=mContext){
						try {
							mContext.setWallpaper(oldValue);
						} catch (IOException e) {
							e.printStackTrace();
							oldValue.recycle();
						}
					}*/
                }
			}

			@Override
			protected int sizeOf(String key, Bitmap bitmap) {
				return bitmap.getRowBytes() * bitmap.getHeight()/ 1024;
				//return value.getByteCount();
			}
			
		};
		//mExecutorService = Executors.newFixedThreadPool(default_thread_count);
		mExecutorService = Executors.newCachedThreadPool();
	}
	
	private ProgressBar mProgressBar;
	public void setProgressBar(ProgressBar progressBar) {
		this.mProgressBar = progressBar;
	}

	private int threadCount;
	public void setThreadCount(int threadCount){
		this.threadCount = threadCount;
		mSemaphore = getSemaphore();
	}
	
	private Semaphore getSemaphore(){
		mSemaphore.release(mSemaphore.availablePermits());
		if(threadCount>0){
			mSemaphore = new Semaphore(threadCount);
		}else{
			mSemaphore = new Semaphore(default_thread_count);
		}
		return mSemaphore;
	}
	/*
	 * 加载图片并显示,本地或网络图片均可显示
	 */
	public void loadImg(ImageView img, String url, int defaultImgId) {
		int availablePermits = mSemaphore.availablePermits();
		Log.d(TAG, "loadImg==availablePermits is "+availablePermits+",needLoad is "+needLoad);
		if(availablePermits<=0){
			//mSemaphore = getSemaphore();
			
		}
		if(null==mContext)
			mContext = img.getContext();
		img.setTag(url);
		Bitmap bitmap = getBitmapFromInnercach(url);
		img.setScaleType(ScaleType.CENTER_CROP);
		if (null != bitmap&&!bitmap.isRecycled()) {
			displayImg(img, url, bitmap,false);
			Log.d(TAG, "缓存显示图片==url is "+url);
		} else {
			if (defaultImgId > 0) {
				img.setImageDrawable(new BitmapDrawable(BitmapUtil.getShowBitmap(img, defaultImgId)));
			}
			//if (!needLoad)
				//return;
			Log.d(TAG, "线程加载显示图片==url is "+url);
			mExecutorService.execute(new MyThread(url, img));
		}
	}

	// int a =0;
	private class MyThread extends Thread {
		String url;
		ImageView imgview;
		MyThread(String url, ImageView imgview) {
			this.url = url;
			this.imgview = imgview;
		}

		@Override
		public void run() {
			try {
				// 获取许可 
				mSemaphore.acquire();
				Bitmap bitmap = getBitmapByUrl(url);
				bitmap = BitmapUtil.getShowBitmap(imgview,bitmap);
				addBitmapTocach(url, bitmap);
				BitmapObj bitmapObj = new BitmapObj();
				bitmapObj.bitmap = bitmap;
				bitmapObj.url = url;
				bitmapObj.img = imgview;
				Message msg = new Message();
				msg.what = msg_what;
				msg.obj = bitmapObj;
				mHandler.sendMessage(msg);
				// 访问完后，释放 ，如果屏蔽下面的语句，则在控制台只能打印5条记录，之后线程一直阻塞
				mSemaphore.release();  
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private BitmapDrawable getBitmapDrawable(Bitmap bitmap){
		BitmapDrawable drawable = null;
		if (null != bitmap) {
			if (Utils.hasHoneycomb()) {
				drawable = new BitmapDrawable(mResources, bitmap);
			} else {
				drawable = new RecyclingBitmapDrawable(mResources,bitmap);
			}
		}
		return drawable;
	}
	
	/*
	 * 根据url从本地或网络获得一个bitmap
	 */
	public Bitmap getBitmapByUrl(String url) {
		if (!StringUtil.checkStr(url))
			return null;
		// 先从本地找
		if (url.startsWith("http")) {
			String imgPath = cachImgDir + File.separator + MD5.Md5(url);
			File imgFile = new File(imgPath);
			if (imgFile.exists()) {
				return BitmapUtil.getLocalOriBitmap(imgPath);
			} else {
				return getInStream(url);
			}
		} else {
			return BitmapUtil.getLocalOriBitmap(url);
		}
	}

	private Bitmap getInStream(String imgurl) {
		Builder builder = new Request.Builder().url(imgurl);
		if(null==builder)
			return null;
		Request request =builder.build();
		Call call = mOkHttpClient.newCall(request);
		try {
			Response response = call.execute();
			int res_code = response.code();
			if(200==res_code){
				Bitmap bitmap = BitmapFactory.decodeStream(response.body().byteStream());//getBitmapByStream(httpConn.getInputStream(), contentLength, imgurl);
				Log.d(TAG, "网络请求==bitmap is "+bitmap);
				saveBitmap(imgurl, bitmap);
				return bitmap;
			}
		}catch (IOException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		/*HttpURLConnection httpConn = null;
		try {
			httpConn = (HttpURLConnection) new URL(imgurl).openConnection();
			httpConn.setRequestMethod("GET");
			int code = httpConn.getResponseCode();
			int contentLength = httpConn.getContentLength();
			if (200 == code) {
				try {
					Log.d(TAG, "网络请求==");
					Bitmap bitmap = BitmapFactory.decodeStream(httpConn.getInputStream());//getBitmapByStream(httpConn.getInputStream(), contentLength, imgurl);
					Log.d(TAG, "网络请求==bitmap is "+bitmap);
					saveBitmap(imgurl, bitmap);
					return bitmap;// BitmapFactory.decodeStream(httpConn.getInputStream());
				} catch (OutOfMemoryError error) {
					return null;
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			if (null != httpConn) {
				httpConn.disconnect();
			}
			httpConn = null;
		}
		return null;*/
	}

	private static void saveBitmap(String url, Bitmap bitmap) {
		if (StringUtil.checkStr(url)) {
			String imgName = url.substring(url.lastIndexOf("/") + 1);
			String format = imgName.substring(imgName.lastIndexOf(".") + 1);
			if (null == bitmap || bitmap.isRecycled())
				return;
			try {
				File imgFile = new File(cachImgDir, MD5.Md5(url));// imgName
				if (imgFile.exists())
					return;
				FileOutputStream fos = new FileOutputStream(imgFile);
				if (format != null && format.trim().equalsIgnoreCase("png")) {
					bitmap.compress(CompressFormat.PNG, 100, fos);
				} else {
					bitmap.compress(CompressFormat.JPEG, 100, fos);
				}
				fos.flush();
				fos.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	
	private void displayImg(ImageView img, String url, Bitmap bitmap,boolean fadeInBitmap) {
		if (null != mCompleteListener) {
			mCompleteListener.OnCompletionListener(getLocalImgPath(url));
		}
		if (StringUtil.checkStr(url) && url.equals(img.getTag())) {
			if (null != mProgressBar) {
				mProgressBar.setVisibility(View.GONE);
			}
			setImageBitmap(img, getBitmapDrawable(bitmap),fadeInBitmap);
		}
	}

	/*
	 * 添加至内存缓存
	 */
	private void addBitmapTocach(String url, Bitmap bitmap) {
		if (!StringUtil.checkStr(url) || null == bitmap||bitmap.isRecycled())
			return;
		inercachBitmap.put(url, bitmap);
	}

	/*
	 * 从内存缓存qu图片
	 */
	private Bitmap getBitmapFromInnercach(String url) {
		if (!StringUtil.checkStr(url))
			return null;
		return inercachBitmap.get(url);
	}

	/*
	 * 判斷圖片本地是否存在
	 */
	public boolean isExist(String netUrl) {
		String localpath = getLocalImgPath(netUrl);
		if (!StringUtil.checkStr(localpath))
			return false;
		return new File(localpath).exists();
	}

	/*
	 * 得到本地图片的储存路径
	 */
	public String getLocalImgPath(String netUrl) {
		if (!StringUtil.checkStr(netUrl))
			return null;
		return cachImgDir + File.separator + MD5.Md5(netUrl);
	}

	/*
	 * 清除内存缓存中的图片
	 */
	public void clearCachBitmap() {
		if (inercachBitmap.size() == 0)
			return;
		inercachBitmap.evictAll();
	}

	/*
	 * 清除物理缓存中的图片
	 */
	public void clearDiskCachBitmap() {
		FileUtil.deleteFiles(cachImgDir);
	}

	/**
	 * Get the size in bytes of a bitmap in a BitmapDrawable.
	 * 
	 * @param value
	 * @return size in bytes
	 */
	@TargetApi(12)
	public static int getBitmapSize(BitmapDrawable value) {
		Bitmap bitmap = value.getBitmap();
		if (Utils.hasHoneycombMR1()) {
			return bitmap.getByteCount();
		}
		// Pre HC-MR1
		return bitmap.getRowBytes() * bitmap.getHeight();
	}

	

	/**
	 * 显示一张图片
	 */
	protected Resources mResources;
	private static final int FADE_IN_TIME = 300;

	private void setImageBitmap(ImageView imageView, BitmapDrawable bitmap,boolean fadeInBitmap) {
		Log.d(TAG, "显示图片==handleMessage==bitmap is "+bitmap+",fadeInBitmap is "+fadeInBitmap);
		if (null == bitmap)
			return;
		if (fadeInBitmap) {
			final TransitionDrawable td = new TransitionDrawable(
					new Drawable[] {
							new ColorDrawable(android.R.color.transparent),
							new BitmapDrawable(mResources, bitmap.getBitmap()) });
			imageView.setImageDrawable(td);
			td.startTransition(FADE_IN_TIME);
			imageView.setImageDrawable(td);
		} else {
			imageView.setImageDrawable(bitmap);
		}
	}

	/*
	 * 得到指定目录下缓存大小
	 * @param firDir,若为空则计算所有缓存图片的大小
	 */
	public long getCachSize(String firDir) {
		if(!StringUtil.checkStr(firDir)){
			firDir = cachImgDir;
		}
		GetFileSize fileSize = new GetFileSize();
		try {
			return fileSize.getFileSize(new File(firDir))
					/ (1024l * 1024l);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public void resetParamsValue() {
		mProgressBar = null;
	}

	@Override
	public boolean handleMessage(Message msg) {
		if (156788 == msg.what) {
			BitmapObj obj = (BitmapObj) msg.obj;
			String url = obj.url;
			Bitmap b = obj.bitmap;
			ImageView img = obj.img;
			Log.d(TAG, "显示图片==handleMessage==");
			if (null != img) {
				displayImg(img, url, b,true);
			}
		}
		return false;
	}
}