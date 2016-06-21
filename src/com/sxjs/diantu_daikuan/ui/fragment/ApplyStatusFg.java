package com.sxjs.diantu_daikuan.ui.fragment;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.net.service.UserJsonService;
import com.sxjs.diantu_daikuan.R;
import com.ui.view.MyAsyncTask;
import com.utils.DatetimeUtil;
import com.utils.StringUtil;
import com.utils.ToastUtil;

import org.json.JSONArray;
import org.json.JSONObject;

public class ApplyStatusFg extends BaseFragment {

	private int loanId,pd_id;

	public ApplyStatusFg(int loanId,int pd_id) {
		this.loanId = loanId;
		this.pd_id = pd_id;
	}

	@Override
	protected int setContentView() {
		return R.layout.apply_status_fg;
	}

	private UserJsonService mUserJsonService;

	@Override
	protected void initView() {
		mUserJsonService = new UserJsonService(mActivity);
		initV();
		loadData();
	}

	private LinearLayout status_group;

	private void initV() {
		status_group = (LinearLayout) findViewById(R.id.status_group);
	}

	private int vipLoanStatus;
	private String productName,investName;
	private int remainTime;
	private JSONArray apply_status_array;
	private final int[] status_ico_ids = { R.drawable.apply_status1,
			R.drawable.apply_status2, R.drawable.apply_status3,
			R.drawable.apply_status4, R.drawable.apply_status5,
			R.drawable.apply_status6 };

	/*
	 * 不通过产品发单状态
	 */
	private void show_apply_status() {
		if (null == apply_status_array || apply_status_array.length() <= 0)
			return;
		status_group.removeAllViews();
		for (int i = 0; i < apply_status_array.length(); i++) {
			View view = mInflater.inflate(R.layout.apply_status_item, null);
			final View shu_line1_img = view.findViewById(R.id.shu_line1_img);
			final View shu_line2_img = view.findViewById(R.id.shu_line2_img);
			View last_line_img = view.findViewById(R.id.last_line_img);
			ImageView status_img = (ImageView) view
					.findViewById(R.id.status_img);
			TextView title_text = (TextView) view.findViewById(R.id.title_text);
			TextView time_text = (TextView) view.findViewById(R.id.time_text);
			TextView desc_text = (TextView) view.findViewById(R.id.desc_text);
			TextView status_text = (TextView) view
					.findViewById(R.id.status_text);
			final LinearLayout apply_status_right_ll = (LinearLayout) view
					.findViewById(R.id.apply_status_right_ll);
			final LinearLayout left_ll = (LinearLayout) view
					.findViewById(R.id.left_ll);
			JSONObject obj = apply_status_array.optJSONObject(i);
			long updateTime = obj.optLong("time");
			time_text.setText(DatetimeUtil.getTimeLocal(updateTime) + "");
			shu_line1_img.setVisibility(View.VISIBLE);
			if (0 == i) {
				shu_line1_img.setVisibility(View.INVISIBLE);
			}
			shu_line2_img.setVisibility(View.VISIBLE);
			last_line_img.setVisibility(View.VISIBLE);
			if (i == apply_status_array.length() - 1) {
				shu_line2_img.setVisibility(View.INVISIBLE);
				last_line_img.setVisibility(View.INVISIBLE);
			}
			status_img.setBackgroundResource(status_ico_ids[i]);
			status_text.setVisibility(View.GONE);
			int flag = obj.optInt("flag");
			String investUserName = obj.optString("investUserName");
			int count = obj.optInt("count");
			String s1 = obj.optString("s1");
			String s2 = obj.optString("s2");
			title_text.setText("");
			desc_text.setText("");
			if (0 == i) {
				title_text.setText("提交申请成功");
				desc_text.setText("请等待系统通知信贷经理");
			} else if (1 == i) {
				String html1 = StringUtil.getHtmlStr1("已通知", count, "位信贷经理");
				title_text.setText(Html.fromHtml(html1));
				String htm2 = StringUtil.getHtmlStr2(remainTime,
						"秒后结束,请等待信贷经理抢单");
				desc_text.setText(Html.fromHtml(htm2));
			} else if (2 == i) {
				String html = StringUtil.getHtmlStr2(count, "位信贷经理已抢单");
				title_text.setText(Html.fromHtml(html));
				if (count <= 0) {
					status_text.setVisibility(View.VISIBLE);
					desc_text.setText("可能您提交的申请条件不符合");
					status_text.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							mContext.mOpenImUtil.openCustomerChat(mActivity,
									0 + "");
						}
					});
				} else {
					desc_text.setText("您可以主动给他们发起聊天");
				}

			} else if (3 == i) {
				String html = StringUtil.getHtmlStr2(count, "位信贷经理决定跟进");
				title_text.setText(Html.fromHtml(html));
				// title_text.setText("收集材料,提交审核");
				// desc_text.setText("信贷经理给您发起聊天,您也可以主动给他发起聊天");
				if (StringUtil.checkStr(s1)) {
					desc_text.setText(s1);
					desc_text.setTextColor(mActivity.getResources().getColor(
							R.color.orange_red_text));
				}
			} else if (4 == i) {
				// title_text.setText(investUserName+"经理已放款");
				// desc_text.setText("恭喜您成功贷款");
				if (StringUtil.checkStr(s1)) {
					title_text.setText(s1);
				}
				if (StringUtil.checkStr(s2)) {
					desc_text.setText(s2);
				}
				if (!StringUtil.checkStr(s1)) {
					if (StringUtil.checkStr(s2)) {
						title_text.setText(s2);
						desc_text.setText("");
					}
				}
			} else if (5 == i) {
				if (StringUtil.checkStr(s1)) {
					title_text.setText(s1);
					desc_text.setText("");// 恭喜您成功贷款
				}
			}
			ViewTreeObserver vto2 = apply_status_right_ll.getViewTreeObserver();
			vto2.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
				@Override
				public void onGlobalLayout() {
					apply_status_right_ll.getViewTreeObserver()
							.removeGlobalOnLayoutListener(this);
					// textView.append("\n\n"+imageView.getHeight()+","+imageView.getWidth());
					// int w = apply_status_right_ll.getWidth();
					int h = apply_status_right_ll.getHeight();
					RelativeLayout.LayoutParams para = new RelativeLayout.LayoutParams(
							LayoutParams.WRAP_CONTENT, h);
					left_ll.setLayoutParams(para);
					// LogUtil.d(TAG, ""+);
				}
			});
			status_group.addView(view);
		}
	}

	/*
	 * 通过产品发单状态申请
	 */
	private void add_dk_status_bypd() {
		if (null == apply_status_array || apply_status_array.length() <= 0)
			return;
		status_group.removeAllViews();
		for (int i = 0; i < apply_status_array.length(); i++) {
			JSONObject obj = apply_status_array.optJSONObject(i);
			int status = obj.optInt("status");
			long time = obj.optLong("time");
			String s3 = obj.optString("s3");
			View view = mInflater.inflate(R.layout.apply_status_by_pd_item,null);
			TextView title_text = (TextView) view.findViewById(R.id.title_text);
			TextView time_text = (TextView) view.findViewById(R.id.time_text);
			TextView desc_text = (TextView) view.findViewById(R.id.desc_text);
			View divider_line = view.findViewById(R.id.divider_line);
			View title_rl = view.findViewById(R.id.title_rl);
			ImageView status_img = (ImageView) view.findViewById(R.id.status_img);
			View shu_line1_img = view.findViewById(R.id.shu_line1_img);
			View shu_line2_img = view.findViewById(R.id.shu_line2_img);
			View last_line_img = view.findViewById(R.id.last_line_img);
			if (i == apply_status_array.length() - 1) {
				shu_line2_img.setVisibility(View.INVISIBLE);
				last_line_img.setVisibility(View.INVISIBLE);
			}
			status_img.setBackgroundResource(status_ico_ids[i]);
			divider_line.setVisibility(View.GONE);
			desc_text.setVisibility(View.GONE);
			if((status<0&&StringUtil.checkStr(s3))|| i==1){
				divider_line.setVisibility(View.VISIBLE);
				desc_text.setVisibility(View.VISIBLE);
			}else{
				RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
				params.addRule(RelativeLayout.CENTER_VERTICAL);
				title_rl.setPadding(0, 0, 0, 0);
				title_rl.setLayoutParams(params);
			}
			
			time_text.setText(DatetimeUtil.getTimeLocal(time) + "");
			//productName,investName;
			if (0 == i) {
				shu_line1_img.setVisibility(View.INVISIBLE);
				String ss = "提交申请贷款产品成功";
				if(StringUtil.checkStr(productName)){
					ss = "提交 "+productName+" 申请成功";
				}
				title_text.setText(ss);
			} else if (1 == i) {
				//divider_line.setVisibility(View.GONE);
				//desc_text.setVisibility(View.GONE);
				String title = "经理已接单";
				//desc_text.setText("您的贷款资质正在审核中");
				desc_text.setText("");
				if(1==vipLoanStatus){
					//desc_text.setText("您的资质通过审核");
				}else if(-1==vipLoanStatus){
					title = "经理放弃接单";
					desc_text.setText("您的资质不合要求");
					if(StringUtil.checkStr(s3)){
						divider_line.setVisibility(View.VISIBLE);
						desc_text.setVisibility(View.VISIBLE);
						desc_text.setText(""+s3);
					}
				}
				RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
				//title_rl.setPadding(0, 0, 0, ScreenUtil.dip2px(mActivity, 8.0f));
				//params.bottomMargin = ScreenUtil.dip2px(mActivity, 8.0f);
				//title_rl.setLayoutParams(params);
				title_text.setText(Html.fromHtml(StringUtil.getHtmlStr3(investName, title)));
			} else if (2 == i) {
				String title = "经理沟通成功";
				if(-1==status){
					title = "经理沟通失败";
				}
				title_text.setText(Html.fromHtml(StringUtil.getHtmlStr3(investName, title)));
				desc_text.setText(""+s3);
			} else if (3 == i) {
				String title = "经理审核成功";
				if(-1==status){
					title = "经理审核失败";
				}
				title_text.setText(Html.fromHtml(StringUtil.getHtmlStr3(investName,title)));
				desc_text.setText(""+s3);
			} else if (4 == i) {
				String title = "经理放款成功";
				if(-1==status){
					title = "经理放款失败";
				}
				title_text.setText(Html.fromHtml(StringUtil.getHtmlStr3(investName, title)));
				desc_text.setText(""+s3);
			}
			status_group.addView(view);
		}
	}

	private class AsyFinishOrder extends MyAsyncTask {

		protected AsyFinishOrder(Context context, String title) {
			super(context, title);
		}

		@Override
		protected Object doInBackground(Object... params) {
			return mUserJsonService.loan_closeSeckill(loanId);
		}

		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);
			String msg = "结束抢单失败";
			if ((Boolean) result) {
				msg = "结束抢单成功";
			}
			ToastUtil.showToast(mActivity, 0, msg, true);
		}
	}

	@Override
	protected void loadData() {
		super.loadData();
		new AsyLoadData(mActivity, "").execute();
	}

	private class AsyLoadData extends MyAsyncTask {

		protected AsyLoadData(Context context, String title) {
			super(context, title);
		}

		@Override
		protected Object doInBackground(Object... params) {
			mUserJsonService.setNeedCach(false);
			return mUserJsonService.dk_loan_processv2(loanId,pd_id);
		}

		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);
			if (null == result)
				return;
			JSONObject data = (JSONObject) result;
			remainTime = data.optInt("remainTime");
			apply_status_array = data.optJSONArray("list");
			productName = data.optString("productName");
			investName = data.optString("investName");
			vipLoanStatus = data.optInt("vipLoanStatus");
			if(pd_id>0){
				add_dk_status_bypd();
			}else{
				show_apply_status();
			}
		}
	}
}
