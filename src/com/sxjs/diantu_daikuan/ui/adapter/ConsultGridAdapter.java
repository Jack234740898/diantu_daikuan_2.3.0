package com.sxjs.diantu_daikuan.ui.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.constants.ParamsKey;
import com.db.service.UserInfoService;
import com.sxjs.diantu_daikuan.MyApplication;
import com.sxjs.diantu_daikuan.R;
import com.sxjs.diantu_daikuan.ui.activity.ConsultantDetailActiviy;
import com.ui.view.RoundImageView;
import com.utils.IntentUtil;
import com.utils.StringUtil;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ConsultGridAdapter extends MyBaseAdapter {

	public ConsultGridAdapter(Context con) {
		super(con);
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		ViewHolder vh=null;
		if(null==view){
			vh = new ViewHolder();
			view = mInflater.inflate(R.layout.consult_grid_item, null);
			vh.head_img = (RoundImageView) view.findViewById(R.id.head_img);
			vh.status_text = (TextView) view.findViewById(R.id.status_text);
			vh.name_text = (TextView) view.findViewById(R.id.name_text);
			vh.desc_text = (TextView) view.findViewById(R.id.desc_text);
			view.setTag(vh);
		}else{
			vh = (ViewHolder) view.getTag();
		}
		bindviewData(position,vh);
		return view;
	}
	
	private void bindviewData(int position, ViewHolder vh) {
		JSONObject obj = dataList.get(position);
		final int id = obj.optInt("id");
		String userName = obj.optString("userName");
		String advantage = obj.optString("advantage");
		String description = obj.optString("description");
		String avatar = obj.optString("avatar");
		int authFlag = obj.optInt("onlineFlag");
		//IMUserdata.userInfoMaps.put(ParamsKey.openim_userid_pre+id,new UserInfo(userName, avatar));
		UserInfoService userInfoService = new UserInfoService((MyApplication)mContext.getApplicationContext(), id+"");
		Map<String, String> map = new HashMap<String, String>();
		map.put(ParamsKey.user_head, avatar);
		map.put(ParamsKey.user_realname, "电兔顾问-"+userName);
		userInfoService.saveData(map);
		
		if(authFlag>0){
			vh.status_text.setBackgroundResource(R.drawable.busy_small_ico);
		}else{
			vh.status_text.setBackgroundResource(R.drawable.online_small_ico);
		}
		mImgLoad.loadImg(vh.head_img, avatar, R.drawable.default_head);
		if(StringUtil.checkStr(userName)){
			vh.name_text.setText(userName);
		}
		if(StringUtil.checkStr(advantage)){
			vh.desc_text.setText(advantage);
		}
		vh.head_img.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Bundle bundle = new Bundle();
				bundle.putInt(ParamsKey.id, id); 
				IntentUtil.activityForward(mContext, ConsultantDetailActiviy.class, bundle, false);
			}
		});
	}

	static final class ViewHolder{
		RoundImageView head_img;
		TextView status_text,name_text,desc_text;
	}
}
