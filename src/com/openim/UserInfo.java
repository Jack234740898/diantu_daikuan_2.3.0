package com.openim;

import com.alibaba.mobileim.contact.IYWContact;

public class UserInfo implements IYWContact {

	private String mUserNick;    // 用户昵称
    private String mAvatarPath;    // 用户头像URL
    private int mLocalResId;//主要用于本地资源
    private String mUserId;    // 用户id
    private String mAppKey;    // 用户appKey

    public UserInfo(String nickName, String avatarPath) {
        this.mUserNick = nickName;
        this.mAvatarPath = avatarPath;
    }
    public UserInfo(String userId,String appKey,String nickName, String avatarPath) {
        this.mUserNick = nickName;
        this.mAvatarPath = avatarPath;
        this.mUserId = userId;
        this.mAppKey = appKey;
    }

    public UserInfo(String nickName, int resId) {
        this.mUserNick = nickName;
        this.mLocalResId = resId;
    }

    @Override
    public String getAppKey() {
        return mAppKey;
    }

    @Override
    public String getAvatarPath() {
        if (mLocalResId != 0) {
            return mLocalResId + "";
        } else {
            return mAvatarPath;
        }
    }

    @Override
    public String getShowName() {
        return mUserNick;
    }

    @Override
    public String getUserId() {
        return mUserId;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "mUserNick='" + mUserNick + '\'' +
                ", mAvatarPath='" + mAvatarPath + '\'' +
                ", mUserId='" + mUserId + '\'' +
                ", mAppKey='" + mAppKey + '\'' +
                ", mLocalResId=" + mLocalResId +
                '}';
    }
	
}
