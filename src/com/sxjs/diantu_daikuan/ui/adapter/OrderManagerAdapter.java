package com.sxjs.diantu_daikuan.ui.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.mobileim.conversation.IYWConversationService;
import com.alibaba.mobileim.conversation.YWConversation;
import com.constants.ParamsKey;
import com.openim.IMConfig;
import com.sxjs.diantu_daikuan.R;
import com.sxjs.diantu_daikuan.ui.activity.CreditManagerDetailActiviy;
import com.sxjs.diantu_daikuan.ui.activity.ScoreActivity;
import com.ui.view.CircularImage;
import com.utils.DatetimeUtil;
import com.utils.IntentUtil;
import com.utils.StringUtil;
import com.utils.UserHeadShowUtil;

import org.json.JSONObject;

import java.util.HashMap;

public class OrderManagerAdapter extends MyBaseAdapter {

	public OrderManagerAdapter(Context con) {
		super(con);
	}

	private IYWConversationService conversationService;

	public void setYWConversation(IYWConversationService conversationService) {
		this.conversationService = conversationService;
	}

	private HashMap<Integer, Integer> scoreFlagMaps;
    public void setscoreFlagMaps(HashMap<Integer, Integer> scoreFlagMaps) {
		this.scoreFlagMaps = scoreFlagMaps;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		ViewHolder vh = null;
		if (null == view) {
			vh = new ViewHolder();
			view = mInflater.inflate(R.layout.order_manager_item, null);
			vh.head_ll = view.findViewById(R.id.head_ll);
			vh.head_img = (CircularImage) view.findViewById(R.id.head_img);
			vh.head_text = (TextView) view.findViewById(R.id.head_text);
			vh.time_text = (TextView) view.findViewById(R.id.time_text);
			vh.name_text = (TextView) view.findViewById(R.id.name_text);
			vh.desc_text = (TextView) view.findViewById(R.id.desc_text);
			vh.line_img = view.findViewById(R.id.line_img);
			vh.go_to_score_img = (ImageView) view.findViewById(R.id.go_to_score_img);
			view.setTag(vh);
		} else {
			vh = (ViewHolder) view.getTag();
		}
		bindviewData(position, vh);
		return view;
	}

	private void bindviewData(int position, ViewHolder vh) {
		JSONObject obj = dataList.get(position);
		int scoreFlag = obj.optInt("scoreFlag");
		final int investId = obj.optInt("investId");
		final int userId = obj.optInt("userId");
		String userName = obj.optString("userName");
		String avatar = obj.optString("avatar");
		long createTime = obj.optLong("createTime");
		vh.time_text.setText(DatetimeUtil.getYMDTimeLocal1(createTime));
		vh.desc_text.setText("");
		if (null != conversationService) {
			YWConversation conversation = conversationService
					.getConversationByUserId(IMConfig.getUseridPre()
							+ userId);
			if (null != conversation) {
				String message = conversation.getLatestContent();
				long latest_time = conversation.getLatestTimeInMillisecond();
				if (latest_time > 0L) {
					vh.time_text.setText(DatetimeUtil
							.getYMDTimeLocal1(latest_time));
				}
				if (StringUtil.checkStr(message)) {
					vh.desc_text.setText(message + "");
				}
			}
		}
		UserHeadShowUtil.showHead(mImgLoad, vh.head_img, vh.head_text, avatar, userName);
		//mImgLoad.loadImg(vh.head_img, avatar, 0);
		vh.name_text.setText("");
		if (StringUtil.checkStr(userName)) {
			vh.name_text.setText(userName);
		}

		vh.line_img.setVisibility(View.VISIBLE);
		if (position == (dataList.size() - 1)) {
			vh.line_img.setVisibility(View.GONE);
		}

		vh.head_ll.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Bundle bundle = new Bundle();
				bundle.putInt(ParamsKey.id, userId);
				IntentUtil.activityForward(mContext,
						CreditManagerDetailActiviy.class, bundle, false);
			}
		});
		
		if(1==scoreFlag){
			vh.go_to_score_img.setEnabled(false);
			vh.go_to_score_img.setClickable(false);
			vh.go_to_score_img.setBackgroundResource(R.drawable.have_scored_img);
		}else{
			vh.go_to_score_img.setClickable(true);
			vh.go_to_score_img.setEnabled(true);
			vh.go_to_score_img.setBackgroundResource(R.drawable.un_score_img);
		}
		if(null!=scoreFlagMaps&&scoreFlagMaps.containsKey(userId)){
			int flag = scoreFlagMaps.get(userId);
			if(1==flag){
				vh.go_to_score_img.setClickable(false);
				vh.go_to_score_img.setEnabled(false);
				vh.go_to_score_img.setBackgroundResource(R.drawable.have_scored_img);
			}
		}
		vh.go_to_score_img.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Bundle bundle = new Bundle();
				bundle.putInt(ParamsKey.user_id, userId);
				bundle.putInt(ParamsKey.investId, investId);
				IntentUtil.activityForward(mContext, ScoreActivity.class,bundle, false);
			}
		});
	}

	static final class ViewHolder {
		CircularImage head_img;
		TextView time_text, name_text, desc_text,head_text;
		View head_ll,line_img;
		ImageView go_to_score_img;
	}

}
