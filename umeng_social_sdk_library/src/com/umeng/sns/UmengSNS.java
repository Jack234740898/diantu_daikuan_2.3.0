package com.umeng.sns;

import android.app.Activity;
import android.graphics.Bitmap;

import com.sina.weibo.sdk.utils.LogUtil;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng_social_sdk_res_lib.R;

/*
 * umeng SNS初始化一些操作
 */
public class UmengSNS {

	private static final String TAG = "UmengSNS";
	/*
	 * 各个平台的配置，建议放在全局Application或者程序入口
	 */
	public static void init() {
		PlatformConfig.setWeixin(SNSConstants.weixin_appID,SNSConstants.weixin_appSerect);
		// 微信 appid appsecret
		PlatformConfig.setSinaWeibo(SNSConstants.weibo_appID,SNSConstants.weibo_appSerect);
		// 新浪微博 appkey appsecret
		PlatformConfig.setQQZone(SNSConstants.QQ_appID,SNSConstants.QQ_appSerect);
		//mShareAPI = 
	}
	
	/*private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
        	Toast.makeText(shareActivity, " 分享成功啦", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
        	Toast.makeText(shareActivity," 分享失败啦", Toast.LENGTH_SHORT).show();        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(shareActivity," 分享取消了", Toast.LENGTH_SHORT).show();
        }
        
    };*/

    /*
     * 分享初始化
     */
    private ShareAction mShareAction;
    private Activity shareActivity;
    public void initShare(Activity activity){
    	this.shareActivity = activity;
    	mShareAction = new ShareAction(shareActivity);
    	//UMShareAPI.get(shareActivity);
    }
	/*
	 * QQ分享
	 */
	public void shareToQQ(boolean isQQ,String title,String content,String targetUrl, String img,
			UMShareListener umShareListener){
		UMImage image = null;
		LogUtil.d(TAG, "img is "+img);
		if(null!=img&&img.startsWith("http")){
			image = new UMImage(shareActivity,img);
		}else{
			image = new UMImage(shareActivity,R.drawable.share_logo);
		}
        if(isQQ){
        	mShareAction.setPlatform(SHARE_MEDIA.QQ);
        }else{
        	mShareAction.setPlatform(SHARE_MEDIA.QZONE);
        }
		
		mShareAction.setCallback(umShareListener);
		if(null!=content)
			mShareAction.withText(content);
		if(null!=image)
			mShareAction.withMedia(image);
		if(null!=title)
			mShareAction.withTitle(title);
		if(null!=targetUrl)
			mShareAction.withTargetUrl(targetUrl);
        mShareAction.share();
	}
	
	/*
	 * 微信朋友圈分享
	 */
	public void shareToWeixin(boolean isCircle,String title,String content,String targetUrl,String img,Bitmap bitmap,
			UMShareListener umShareListener){
		UMImage image= null;//new UMImage(shareActivity,R.drawable.share_logo);
		if(null!=img&&img.startsWith("http")){
			image = new UMImage(shareActivity,img);
		}else{
			image = new UMImage(shareActivity,R.drawable.share_logo);
		}
		/*if(null!=bitmap){
			image = new UMImage(shareActivity,bitmap);
		}else{
			image = new UMImage(shareActivity,R.drawable.share_logo);
		}*/
		
        if(isCircle){
        	mShareAction.setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE);
        }else{
        	mShareAction.setPlatform(SHARE_MEDIA.WEIXIN);
        }
		//new UMWXHandler().
		mShareAction.setCallback(umShareListener);
		if(null!=content)
			mShareAction.withText(content);
		if(null!=image)
			mShareAction.withMedia(image);
		if(null!=title)
			mShareAction.withTitle(title);
		if(null!=targetUrl)
			mShareAction.withTargetUrl(targetUrl);
        mShareAction.share();
	}
	
	/*
	 * 微博分享
	 */
	public void shareToWeibo(String title,String content,String targetUrl,String img,Bitmap bitmap,UMShareListener umShareListener){
		UMImage image=null;//=new UMImage(shareActivity,R.drawable.share_logo);
		if(null!=img&&img.startsWith("http")){
			image = new UMImage(shareActivity,img);
			//image.resize(100, 100);
		}else{
			image = new UMImage(shareActivity,R.drawable.share_logo);
		}
		/*if(null!=bitmap){
			image = new UMImage(shareActivity,bitmap);
		}else{
			image = new UMImage(shareActivity,R.drawable.share_logo);
		}*/
		mShareAction.setPlatform(SHARE_MEDIA.SINA);
		
		mShareAction.setCallback(umShareListener);
		if(null!=content)
			mShareAction.withText(content);
		if(null!=image)
			mShareAction.withMedia(image);
		//if(null!=title)
			//mShareAction.withTitle(title);
		if(null!=targetUrl)
			mShareAction.withTargetUrl(targetUrl);
        mShareAction.share();
	}
	
	
}
