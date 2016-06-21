package com.utils;

import android.content.Context;
import android.media.MediaPlayer;
import android.widget.ImageView;

import com.model.PlayState;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AudioPlayUtil {

	private Context mContext;
	private MediaPlayer mMediaPlayer;
	public AudioPlayUtil(Context context){
		this.mContext = context;
	}
	
	private ImageView play_img;
	public void setImgView(ImageView play_img){
		this.play_img = play_img;
	}
	
	private HashMap<String, PlayState> recordPlayMaps;
	public void setMaps(HashMap<String, PlayState> recordPlayMaps){
		this.recordPlayMaps = recordPlayMaps;
	}
	private String playUrl;
	public void playAudio(String playUrl){
		if(!StringUtil.checkStr(playUrl)||!playUrl.startsWith("http")){
			ToastUtil.showToast(mContext, 0, "语音链接无效", true);
			return;
		}
		if(null==mMediaPlayer)
			mMediaPlayer = new MediaPlayer();
		this.playUrl = playUrl;
		resetAllPlayState();
		addPlayRecord();
		mMediaPlayer.reset();
		try {
			mMediaPlayer.setDataSource(playUrl);
			mMediaPlayer.prepare();//准备播放  
			mMediaPlayer.start();//开始播放 
			mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {   
	            public void onCompletion(MediaPlayer arg0) {   
	                //next();//如果当前歌曲播放完毕,自动播放下一首.  
	            	play_img.setSelected(false);
	            	mMediaPlayer.release();
	            	removePlayRecord();
	            }   
	        });   
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * 添加播放记录
	 */
	private void addPlayRecord(){
		PlayState playState = new PlayState();
		playState.mediaPlayer = mMediaPlayer;
		playState.play_img = play_img;
		recordPlayMaps.put(playUrl, playState);
	}
	
	/*
	 * 移除播放记录
	 */
	private void removePlayRecord(){
		if(recordPlayMaps.containsKey(playUrl))
			recordPlayMaps.remove(playUrl);
	}
	
	/*
	 * 重置播放状态
	 */
	public void resetAllPlayState(){
		for(Map.Entry<String, PlayState> map:recordPlayMaps.entrySet()){
			PlayState playState = map.getValue();
			MediaPlayer mediaplayer = playState.mediaPlayer;
			ImageView play_img = playState.play_img;
			if(null!=play_img){
				play_img.setSelected(false);
			}
			if(null!=mediaplayer){
				mediaplayer.release();
			}
		}
	}
	/*
	 * 当前是否正在播放
	 */
	public boolean isPlaying(){
		if(null==mMediaPlayer)
			return false;
		return mMediaPlayer.isPlaying();
	}
	/*
	 * 停止播放
	 */
	public void stopPlay(){
		if(null!=mMediaPlayer){
			mMediaPlayer.stop();
		}
		removePlayRecord();
	}
	/*
	 * 释放资源
	 */
	public void release(){ 
		if(null!=mMediaPlayer){
			mMediaPlayer.release(); 
		}
		//mMediaPlayer = null;
	}
}
