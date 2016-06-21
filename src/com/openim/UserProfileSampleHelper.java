package com.openim;

import android.content.Intent;
import android.os.AsyncTask;

import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.channel.util.YWLog;
import com.alibaba.mobileim.contact.IYWContact;
import com.alibaba.mobileim.contact.IYWContactHeadClickCallback;
import com.alibaba.mobileim.contact.IYWContactService;
import com.alibaba.mobileim.contact.IYWCrossContactProfileCallback;
import com.alibaba.mobileim.lib.model.contact.Contact;
import com.net.service.UserJsonService;
import com.sxjs.diantu_daikuan.MyApplication;
import com.utils.LogUtil;
import com.utils.StringUtil;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 用户自定义昵称和头像
 * @author zhaoxu
 */
public class UserProfileSampleHelper {

	private static final String TAG = "UserProfileSampleHelper";
   
	//private HashMap<String,UserInfo> userInfoMaps;
	private UserJsonService mUserJsonService;
	private MyApplication mContext;
	public UserProfileSampleHelper(MyApplication context){
		this.mContext = context;
		mUserJsonService = new UserJsonService(mContext);
		//userInfoMaps = new HashMap<String,UserInfo>();
	}
    //初始化，建议放在登录之前
    public void initProfileCallback() {
        YWIMKit imKit = mContext.mOpenImUtil.getIMKit();
        if(null==imKit)
        	return;
        final IYWContactService contactManager = imKit.getContactService();
        //设置用户信息回调，如果开发者已经把用户信息导入了IM服务器，则不需要再调用该方法，IMSDK会自动到IM服务器获取用户信息
        contactManager.setCrossContactProfileCallback(new IYWCrossContactProfileCallback() {

            /**
             * 设置头像点击事件, 该方法已废弃，后续请使用{@link IYWContactService#setContactHeadClickCallback(IYWContactHeadClickCallback)}
             * @param userId 需要打开页面的用户
             * @param appKey 需要返回个人信息的用户所属站点
             * @return
             * @deprecated
             */
            @Override
            public Intent onShowProfileActivity(String userId, String appKey) {
                return null;
            }

            @Override
            public void updateContactInfo(Contact contact) {

            }
            //此方法会在SDK需要显示头像和昵称的时候，调用。同一个用户会被多次调用的情况。
            //比如显示会话列表，显示聊天窗口时同一个用户都会被调用到。
            @Override
            public IYWContact onFetchContactInfo(final String userId, final String appKey) {
                //[需特殊处理的账号(如客服账号)] 或者 [昵称等Profile信息未导入云旺服务器]时。此为示例代码
                if(isNeedSpecialHandleAccount(userId, appKey)){
                    //首先从内存中获取用户信息
                    // todo 由于每次更新UI都会调用该方法，所以必现创建一个内存缓存，先从内存中拿用户信息，内存中没有才访问数据库或者网络，如果不创建内存缓存，而是每次都访问数据库或者网络，会导致死循环！！
                    final IYWContact userInfo = IMUserdata.userInfoMaps.get(userId);
                    //若内存中有用户信息，则直接返回该用户信息
                    if (userInfo != null){
                        YWLog.d(TAG+"@profile","onFetchContactInfo Hit! "+userInfo.toString());
                        return userInfo;
                    } else {
                        new AsyGetUserInfo(userId).execute();
                        return modifyUserInfo(userId);
                    }
                }
                //todo 当返回null时，SDK内部才会从云旺服务器获取对应的profile,因此这里必须返回null
                return null;
            }
        });
        /*YWContactManager contactManager = imKit.getIMCore().getContactManager();
        contactManager.setContactProfileCallback(new IYWContactProfileCallback() {

            @Override
            public Intent onShowProfileActivity(String arg0) {
                return null;
            }

            //此方法会在SDK需要显示头像和昵称的时候，调用。同一个用户会被多次调用的情况。
            //比如显示会话列表，显示聊天窗口时同一个用户都会被调用到。
            @Override
            public IYWContact onFetchContactInfo(String arg0) {
                // 开发者需要根据不同的用户ID显示不同的昵称和头像。
                try {
                    String userid = arg0;//ParamsKey.openim_userid_pre+
                    new AsyGetUserInfo(userid).execute();
                    return modifyUserInfo(userid);
                   // LogUtil.d(TAG, "onFetchContactInfo==arg0 is "+arg0);
                    //return new UserInfo("马云", "hah");
                } catch (Exception e) {

                }
                return null;
            }
        });*/
    }
    private static boolean isNeedSpecialHandleAccount(String userid, String appkey){
//      if(!TextUtils.isEmpty(userid)&&userid.startsWith("云")){
//          return true;
//      }
//      return  false;

      return true;
  }

    /**
     * 设定希望显示的用户昵称和头像
     * 头像支持本地路径和URL路径。重要：头像图片最好小于10K，否则第一次加载可能会有图像压缩的延时,URL路径还有下载的延时
     *
     * @param userid   用户ID
     * @return
     */
    private IYWContact modifyUserInfo(String userid) {
    	IYWContact userInfo = null;
    	if(IMUserdata.userInfoMaps.containsKey(userid)){
        	userInfo = IMUserdata.userInfoMaps.get(userid);
    	}
    	if(null==userInfo){
    		mUserJsonService.setNeedCach(true);
        	JSONArray array = mUserJsonService.user2_getUserInfo(userid);
        	if(null==array||array.length()<=0){
    			return userInfo;
    		}
    		JSONObject obj = array.optJSONObject(0);
    		String userName = obj.optString("userName");
    		String avatar = obj.optString("avatar");
    		userInfo = new UserInfo(userName, avatar);
    	}
    	if(null!=userInfo){
    		String name = userInfo.getShowName();
    		String head_pic = userInfo.getAvatarPath();
    		LogUtil.d(TAG, "userid is "+userid+",name is "+name+",head_pic is "+head_pic);
    	}
    	
        return userInfo;
    }
    
    private class MyThread extends Thread{
    	String ids,appKey;
    	MyThread(String ids,String appKey){
    		this.ids = ids;
    		this.appKey = appKey;
    	}
    	@Override
    	public void run() {
    		super.run();
    		mUserJsonService.setNeedCach(true);
			JSONArray array = mUserJsonService.user2_getUserInfo(ids);
			if(null==array||array.length()<=0){
				mUserJsonService.setNeedCach(false);
				array = mUserJsonService.user2_getUserInfo(ids);
			}
			if(null!=array||array.length()>0){
				JSONObject obj = array.optJSONObject(0);
				String userName = obj.optString("userName");
				String avatar = obj.optString("avatar");
				if(!StringUtil.checkStr(userName))
					userName = "";
				UserInfo userInfo = new UserInfo(ids,appKey,userName, avatar);
				IMUserdata.userInfoMaps.put(ids, userInfo);
			}
    	}
    }
    private class AsyGetUserInfo extends AsyncTask<Void, Void, JSONArray>{
    	String ids;
    	AsyGetUserInfo(String ids){
    		this.ids = ids;
    	}
		@Override
		protected JSONArray doInBackground(Void... params) {
			mUserJsonService.setNeedCach(true);
			JSONArray array = mUserJsonService.user2_getUserInfo(ids);
			if(null==array||array.length()<=0){
				mUserJsonService.setNeedCach(false);
				array = mUserJsonService.user2_getUserInfo(ids);
			}
			return array;
		}
    	
		@Override
		protected void onPostExecute(JSONArray array) {
			super.onPostExecute(array);
			if(null==array||array.length()<=0){
				return;
			}
			JSONObject obj = array.optJSONObject(0);
			String userName = obj.optString("userName");
			String avatar = obj.optString("avatar");
			if(!StringUtil.checkStr(userName))
				userName = "";
			UserInfo userInfo = new UserInfo(userName, avatar);
			IMUserdata.userInfoMaps.put(ids, userInfo);
		}
    }
}
