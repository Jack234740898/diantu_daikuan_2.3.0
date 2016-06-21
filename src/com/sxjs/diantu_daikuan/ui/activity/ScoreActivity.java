package com.sxjs.diantu_daikuan.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.constants.GlobalFlag;
import com.constants.ParamsKey;
import com.net.service.BusinessJsonService;
import com.net.service.UserJsonService;
import com.niceapp.lib.tagview.widget.TagListView;
import com.sxjs.diantu_daikuan.R;
import com.sxjs.diantu_daikuan.ui.adapter.ScoreMarkGridAdapter;
import com.ui.view.CircularImage;
import com.ui.view.MyAsyncTask;
import com.ui.view.MyGridView;
import com.utils.DatetimeUtil;
import com.utils.JSONUtil;
import com.utils.ScreenUtil;
import com.utils.StringUtil;
import com.utils.ToastUtil;
import com.utils.UserHeadShowUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/*
 * 去评分
 */
public class ScoreActivity extends BaseActivity implements OnClickListener,OnItemClickListener,TextWatcher{

	private UserJsonService mUserJsonService;
	private BusinessJsonService mBusinessService;
	private int screenW;
	@Override
	protected void onInit(Bundle bundle) {
		super.onInit(bundle);
		screenW = ScreenUtil.getWidth(mActivity);
		mUserJsonService = new UserJsonService(mActivity);
		mBusinessService = new BusinessJsonService(mActivity);
		initParams();
		initView();
		loadData();
	}
	
	private int investId,userId;
	private void initParams() {
		Bundle bundle = getIntent().getExtras();
		userId = bundle.getInt(ParamsKey.user_id);
		investId = bundle.getInt(ParamsKey.investId);
	}

	private CircularImage head_img;
	private TextView name_text,verify_time_text,company_name_text,commit_text,head_text;
	private LinearLayout rating_group;
	private MyGridView mark_gridview;
	private EditText content_edit;
	private ImageView verify_img;
	private View body;
	private TagListView mark_taglistview;
	private void initView() {
		mHeadView.setLeftBtnBg(R.drawable.back_gray,this);
		mHeadView.setCentreTextView("提交评价");
		mHeadView.hideRightBtn();
		body = findViewById(R.id.body);
		body.setVisibility(View.INVISIBLE);
		mark_taglistview = (TagListView) findViewById(R.id.mark_taglistview);
		verify_img = (ImageView) findViewById(R.id.verify_img);
		verify_img.setSelected(true);
		head_img = (CircularImage) findViewById(R.id.head_img);
		head_text = (TextView) findViewById(R.id.head_text);
		name_text = (TextView) findViewById(R.id.name_text);
		verify_time_text = (TextView) findViewById(R.id.verify_time_text);
		company_name_text = (TextView) findViewById(R.id.company_name_text);
		rating_group = (LinearLayout) findViewById(R.id.rating_group);
		mark_gridview = (MyGridView) findViewById(R.id.mark_gridview);
		content_edit = (EditText) findViewById(R.id.content_edit);
		content_edit.addTextChangedListener(this);
		commit_text = (TextView) findViewById(R.id.commit_text);
		commit_text.setSelected(false);
		commit_text.setOnClickListener(this);
		addScoreImg();
	}

	private ArrayList<ImageView> imgList;
	private void addScoreImg() {
		imgList = new ArrayList<ImageView>();
		rating_group.removeAllViews();
		int margin = ScreenUtil.dip2px(mActivity, 46.0f);
		int padding = ScreenUtil.dip2px(mActivity, 10.0f);
		int imgW = (screenW-2*margin-4*padding)/5;
		for(int i=0;i<5;i++){
			View view = mInflater.inflate(R.layout.score_img_item, null);
			ImageView rating_img = (ImageView) view.findViewById(R.id.rating_img);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(imgW,imgW);
			if(1==i||3==i){
				params.rightMargin = padding;
				params.leftMargin = padding;
			}
			rating_img.setLayoutParams(params);
			rating_img.setSelected(false);
			if(0==i){
				//rating_img.setSelected(true);
			}
			rating_img.setOnClickListener(new MyClickListener(i));
			imgList.add(rating_img);
			rating_group.addView(view);
		}
	}

	private ArrayList<TextView> list;
	private void show_mark_tag(){
		if(null==commentArray)
			return;
		list = new ArrayList<TextView>();
		for(int i=0;i<commentArray.length();i++){
			View view = mInflater.inflate(R.layout.score_mark_grid_item, null);
			TextView textview = (TextView) view.findViewById(R.id.textview);
			final JSONObject obj = commentArray.optJSONObject(i);
			String label = obj.optString("label");
			textview.setText("");
			if(StringUtil.checkStr(label)){
				textview.setText(label);
			}
			textview.setTag(i);
			list.add(textview);
			textview.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {	
					String content = content_edit.getText().toString();
					comment = obj.optString("value");
					if(StringUtil.checkStr(content)){
						comment = comment+","+content;
					}
					int cur_index = (Integer) v.getTag();
					for(int j=0;j<list.size();j++){
						TextView textview = list.get(j);
						textview.setSelected(false);
						if(cur_index==j){
							textview.setSelected(true);
						}
					}
					show_commit_state();
				}
			});
			mark_taglistview.addView(view);
		}
	}
	private class MyClickListener implements OnClickListener{

		int index;
		MyClickListener(int index){
			this.index = index;
		}
		@Override
		public void onClick(View v) {
			for(int i=0;i<imgList.size();i++){
				ImageView rating_img = imgList.get(i);
				rating_img.setSelected(false);
				if(i<=index){
					rating_img.setSelected(true);
				}
			}
			score = index+1;
			show_commit_state();
		}
	}
	@Override
	protected void loadData() {
		super.loadData();
		new AsyLoadData(mActivity,"").execute();
	}

	private JSONArray commentArray;
	private class AsyLoadData extends MyAsyncTask{

		protected AsyLoadData(Context context,String title) {
			super(context,title);
		}
		@Override
		protected Object doInBackground(Object... params) {
			mUserJsonService.setNeedCach(true);
			JSONObject obj = mUserJsonService.option_list(1);
			JSONObject commentObj=null;
			if(null!=obj){
				commentObj = obj.optJSONObject("comment");
			}
			if(null==commentObj){
				mUserJsonService.setNeedCach(false);
				obj = mUserJsonService.option_list(1);
				if(null!=obj){
					commentObj = obj.optJSONObject("comment");
				}
			}
			if(null!=commentObj){
				commentArray = commentObj.optJSONArray("list");
				if(null!=commentArray&&commentArray.length()>0){
					comment = commentArray.optJSONObject(0).optString("value");
				}
			}
			return mBusinessService.dk_user_getInvestUserV2(userId);
		}
		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);
			if(null==result)
				return;
			
			JSONObject data = (JSONObject) result;
			String message = data.optString("message");
			if(StringUtil.checkStr(message)){
				ToastUtil.showToast(mActivity, 0, message, true);
			}
			JSONObject user = data.optJSONObject("user");
			if(null==user)
				return;
			body.setVisibility(View.VISIBLE);
			bindviewData(user);
			//show_mark_grid();
			show_mark_tag();
		}
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.leftImg:
			finish();
			break;
		case R.id.commit_text:
			commit();
			break;
		default:
			break;
		}
	}

	private ScoreMarkGridAdapter mAdapter;
	public void show_mark_grid() {
		mark_gridview.setAdapter(mAdapter = new ScoreMarkGridAdapter(mActivity));
		mAdapter.setData(JSONUtil.arrayToList(commentArray));
		mAdapter.notifyDataSetChanged();
		mark_gridview.setOnItemClickListener(this);
	}

	public void bindviewData(JSONObject user) {
		if(null==user)
			return;
		String userName = user.optString("userName");
		String avatar = user.optString("avatar");
		String companyName = user.optString("companyName");
		long authTime = user.optLong("authTime");
		//mImgLoad.loadImg(head_img, avatar, 0);
		UserHeadShowUtil.showHead(mImgLoad, head_img, head_text, avatar, userName);
		name_text.setText("");
		if(StringUtil.checkStr(userName)){
			name_text.setText(userName);
		}
		company_name_text.setText("");
		if(StringUtil.checkStr(companyName)){
			company_name_text.setText(companyName);
		}
		verify_time_text.setText("2012年8月5日实名认证通过");
		if(authTime>100){
			String auth_time = DatetimeUtil.getYMDTimeLocal2(authTime);
			verify_time_text.setText(auth_time+"实名认证通过");
		}
	}

	private String comment;
	private float score=0.0f;
	private void commit() {
		if(score<=0.0f){
			ToastUtil.showToast(mActivity, 0, "您还未给评分", true);
			return;
		}
		if(!StringUtil.checkStr(comment)){
			ToastUtil.showToast(mActivity, 0, "您还未选择或输入您的评论", true);
			return;
		}
		commit_text.setSelected(true);
		new AsyCommit(mActivity,"").execute();
	}
	
	private class AsyCommit extends MyAsyncTask{

		protected AsyCommit(Context context,String title) {
			super(mActivity,title);
		}

		@Override
		protected Object doInBackground(Object... params) {
			return mUserJsonService.score_add(investId, userId, comment, score);
		}
		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);
			String msg = "评价失败";
			if((Boolean)result){
				msg = "评价成功";
				GlobalFlag.scoreFlagMaps.put(userId, 1);
				finish();
			}
			ToastUtil.showToast(mActivity, 0, msg, true);
		}
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if(null==commentArray||commentArray.length()<=0)
			return;
		comment = commentArray.optJSONObject(position).optString("value");
		mAdapter.setSelect(position);
		mAdapter.notifyDataSetChanged();
	}
	
	private void show_commit_state(){
		commit_text.setSelected(false);
		if(StringUtil.checkStr(comment)&&score>0.0){
			commit_text.setSelected(true);
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {		
	}

	@Override
	public void afterTextChanged(Editable s) {
		String edit_content = s.toString();
		if(StringUtil.checkStr(comment)){
			if(StringUtil.checkStr(edit_content)){
				comment = comment+","+edit_content;
			}
		}else{
			comment = edit_content;
		}
		show_commit_state();
	}

	@Override
	protected int setContentView() {
		// TODO Auto-generated method stub
		return R.layout.score_page;
	}
}
