package com.sxjs.diantu_daikuan.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.model.PlayState;
import com.openim.ConversationSampleHelper;
import com.openim.IMConfig;
import com.sxjs.diantu_daikuan.MyApplication;
import com.sxjs.diantu_daikuan.R;
import com.ui.view.CircularImage;
import com.utils.AudioPlayUtil;
import com.utils.DatetimeUtil;
import com.utils.StringUtil;

import org.json.JSONObject;

import java.util.HashMap;

public class MyOrdersAdapter extends MyBaseAdapter {

	private HashMap<String, PlayState> recordPlayMaps;
	private MyApplication mApplication;

	public MyOrdersAdapter(Context con) {
		super(con);
		mApplication = (MyApplication) con.getApplicationContext();
		recordPlayMaps = new HashMap<String, PlayState>();
	}

	private ConversationSampleHelper mConversationHelper;

	public void setParams(ConversationSampleHelper c) {
		this.mConversationHelper = c;
	}

	private HashMap<String, Integer> unReadCountMaps;
	public void setUnreadMaps(HashMap<String, Integer> unReadCountMaps) {
		this.unReadCountMaps = unReadCountMaps;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh = null;
		if (null == convertView) {
			vh = new ViewHolder();
			convertView = mInflater.inflate(R.layout.my_orders_list_item, null);
			vh.time_text = (TextView) convertView.findViewById(R.id.time_text);
			vh.desc_text = (TextView) convertView.findViewById(R.id.desc_text);
			vh.name_text = (TextView) convertView.findViewById(R.id.name_text);
			vh.bottom_text = (ImageView) convertView
					.findViewById(R.id.bottom_text);
			vh.head_img = (CircularImage) convertView
					.findViewById(R.id.head_img);
			vh.head_text = (TextView) convertView.findViewById(R.id.head_text);
			vh.title_text1 = (TextView) convertView
					.findViewById(R.id.title_text1);
			vh.title_text2 = (TextView) convertView
					.findViewById(R.id.title_text2);
			vh.play_ll = convertView.findViewById(R.id.play_ll);
			vh.play_img = (ImageView) convertView.findViewById(R.id.play_img);
			vh.length_text = (TextView) convertView
					.findViewById(R.id.length_text);
			vh.tips_text = (TextView) convertView.findViewById(R.id.tips_text);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		bindViewData(vh, position);
		return convertView;
	}

	private void bindViewData(final ViewHolder vh, int position) {
		final JSONObject obj = dataList.get(position);
		String userId = obj.optString("userId");
		String userName = obj.optString("userName");
		String companyName = obj.optString("companyName");
		String userMobile = obj.optString("userMobile");
		String userAvatar = obj.optString("userAvatar");
		int type = obj.optInt("type");
		String house = obj.optString("house");
		String income = obj.optString("income");
		long investCreateTime = obj.optLong("investCreateTime");
		String car = obj.optString("car");
		String amount = obj.optString("amount");
		String city = obj.optString("city");
		int useType = obj.optInt("useType");
		String loanInfo = obj.optString("loanInfo");
		String desc_str = "";
		if (StringUtil.checkStr(house))
			desc_str = house;
		if (StringUtil.checkStr(car))
			desc_str = desc_str + " " + car;
		if (StringUtil.checkStr(income))
			desc_str = desc_str + " 月收入" + income;
		// String desc_str = house+","+car+",月收入"+income;
		int investStatus = obj.optInt("investStatus");// 0未放款
		long investFinishTime = obj.optLong("investFinishTime");
		// mImgLoad.loadImg(vh.head_img, userAvatar, R.drawable.default_head);
		vh.head_img.setVisibility(View.GONE);
		vh.head_text.setVisibility(View.GONE);
		mImgLoad.loadImg(vh.head_img, userAvatar, R.drawable.default_head);
		if (StringUtil.checkStr(userAvatar)) {
			vh.head_img.setVisibility(View.VISIBLE);
		} else {
			vh.head_text.setVisibility(View.VISIBLE);
			if (StringUtil.checkStr(userName)) {
				vh.head_text
						.setBackgroundResource(R.drawable.circle_white_shape);
				// String letter = PingYinUtil.getPingYin(userName.substring(0,
				// 1)).toUpperCase();
				vh.head_text.setText(userName.substring(0, 1));
			} else {
				vh.head_text.setBackgroundResource(R.drawable.default_head);
			}
		}
		vh.name_text.setVisibility(View.INVISIBLE);
		if (StringUtil.checkStr(userName)) {
			vh.name_text.setVisibility(View.VISIBLE);
			vh.name_text.setText(userName);
			if (StringUtil.checkStr(companyName)) {
				vh.name_text.setText(userName + "-" + companyName);
			}
		}
		vh.time_text.setText(DatetimeUtil.format(investCreateTime));

		// 抢单后： -1已放弃 0跟进中 1已放款
		vh.bottom_text.setVisibility(View.VISIBLE);
		if (0 == investStatus) {
			vh.bottom_text.setBackgroundResource(R.drawable.status_genjinzhong);
		} else if (-1 == investStatus) {
			vh.bottom_text.setBackgroundResource(R.drawable.status_yifangqi);
		} else if (1 == investStatus) {
			vh.bottom_text.setBackgroundResource(R.drawable.status_yifangkuan);
		} else {
			vh.bottom_text.setVisibility(View.GONE);
		}
		vh.desc_text.setText("");
		if (StringUtil.checkStr(loanInfo)) {
			vh.desc_text.setText(loanInfo);
		}

		vh.desc_text.setVisibility(View.VISIBLE);
		vh.play_ll.setVisibility(View.GONE);
		vh.title_text1.setText("个人消费贷");
		if (1 == useType) {
			vh.title_text1.setText("企业经营贷");
		}
		vh.title_text2.setText("￥" + 0);
		if (StringUtil.checkStr(amount)) {
			vh.title_text2.setText("￥" + amount);
		}
		vh.tips_text.setVisibility(View.INVISIBLE);
		if(null!=unReadCountMaps&&unReadCountMaps.size()>0){
			//int unreadCount = mConversationHelper.getUnreadCount(ParamsKey.openim_userid_pre + userId);
			int unreadCount = unReadCountMaps.get(IMConfig.getUseridPre() + userId);
			if (unreadCount > 0) {
				vh.tips_text.setVisibility(View.VISIBLE);
				vh.tips_text.setText(String.valueOf(unreadCount));
			}
			vh.head_img.setOnClickListener(new ClickListener(unreadCount, userId,
					userName));
			vh.head_text.setOnClickListener(new ClickListener(unreadCount, userId,
					userName)); 
		}
	}

	private class ClickListener implements OnClickListener {

		int unreadCount;
		String userId, userName;

		public ClickListener(int unreadCount, String userId, String userName) {
			this.unreadCount = unreadCount;
			this.userId = userId;
			this.userName = userName;
		}

		@Override
		public void onClick(View v) {
			if (unreadCount <= 0) {
				return;
			}
			//GlobalFlag.cur_chat_username = userName;
			mApplication.mOpenImUtil.openNewConversation((Activity) mContext, userId);
		}

	}

	// private boolean isPlaying;
	private AudioPlayUtil playUtil;

	protected void playAudio(String voiceUrl, ImageView play_img) {
		if (null == playUtil)
			playUtil = new AudioPlayUtil(mContext);
		playUtil.setImgView(play_img);
		playUtil.setMaps(recordPlayMaps);
		if (playUtil.isPlaying()) {
			play_img.setSelected(false);
			playUtil.stopPlay();
			playUtil = null;
		} else {
			play_img.setSelected(true);
			playUtil.playAudio(voiceUrl);
		}
	}

	static final class ViewHolder {
		ImageView bottom_text;
		TextView title_text1, title_text2, time_text, desc_text, name_text,
				length_text, head_text, tips_text;
		CircularImage head_img;
		View play_ll;
		ImageView play_img;
	}

}
