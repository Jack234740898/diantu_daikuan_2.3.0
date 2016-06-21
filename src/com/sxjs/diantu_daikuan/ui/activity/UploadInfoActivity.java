package com.sxjs.diantu_daikuan.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.constants.DKUserType;
import com.constants.ParamsKey;
import com.net.service.UserJsonService;
import com.sxjs.diantu_daikuan.R;
import com.ui.view.MyAsyncTask;
import com.utils.IntentUtil;
import com.utils.LogUtil;
import com.utils.StringUtil;
import com.utils.TakePicUtil;
import com.utils.ToastUtil;

import org.json.JSONArray;
import org.json.JSONObject;

/*
 * 上传资料信息
 */
public class UploadInfoActivity extends BaseActivity implements OnClickListener{

	private static final String TAG = "UploadInfoActivity";
	private UserJsonService mUserService;
	@Override
	protected int setContentView() {
		return R.layout.upload_info;
	}

	@Override
	protected void onInit(Bundle bundle) {
		super.onInit(bundle);
		mUserService = new UserJsonService(mActivity);
		initparams();
		initView();
		showTextByIdentity();
	}

	private void showTextByIdentity() {
		text1.setText("身份证+工作证拍照");
		text2.setText("身份证+工牌、工作证、名片、邮箱、前台均可");
		text2.setVisibility(View.VISIBLE);
		exam_img.setBackgroundResource(R.drawable.id_gongpai);
		if(workingIdentity==DKUserType.student_type){
			text1.setText("身份证+学生证拍照");
			text2.setVisibility(View.GONE);
			exam_img.setBackgroundResource(R.drawable.id_student_card);
		}else if(workingIdentity==DKUserType.getihu_type||workingIdentity==DKUserType.qiyezhu_type){
			text1.setText("身份证+营业执照拍照");
			exam_img.setBackgroundResource(R.drawable.id_zhizhao);
			text2.setVisibility(View.GONE);
		}else if(workingIdentity==DKUserType.otherwork_type){
			//自由职业
			text1.setText("手持身份证拍照");
			exam_img.setBackgroundResource(R.drawable.id_exam);
			text2.setVisibility(View.GONE);
		}
	}

	private int workingIdentity,loanId;
	private void initparams() {
		Bundle bundle = getIntent().getExtras();
		if(null==bundle)
			return;
		workingIdentity = bundle.getInt(ParamsKey.workingIdentity);
		loanId = bundle.getInt(ParamsKey.loanId);
	}

	private TextView text1,text2;
	private ImageView cd_img,exam_img;
	private void initView() {
		mHeadView.setLeftBtnBg(R.drawable.back_gray, this);
		mHeadView.setCentreTextView("上传资料");
		mHeadView.hideRightBtn();
		cd_img = (ImageView) findViewById(R.id.cd_img);
		cd_img.setOnClickListener(this);
		findViewById(R.id.upload_text).setOnClickListener(this);
		findViewById(R.id.unupload_text).setOnClickListener(this);
		text1 = (TextView) findViewById(R.id.text1);
		text2 = (TextView) findViewById(R.id.text2);
		exam_img = (ImageView) findViewById(R.id.exam_img);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.leftImg:
			finish();
			break;
		case R.id.upload_text:
			choose_photos();
			break;
		case R.id.unupload_text:
			IntentUtil.activityForward(mActivity,ApplySucessActivity.class, null, true);
			break;
		case R.id.cd_img:
			break;
		default:
			break;
		}
	}
	
	private String identityImg;
	private void commit_info() {
		if(!StringUtil.isHttpUrl(identityImg)){
			ToastUtil.showToast(mActivity,0,"请选择图片", true);
			return;
		}
			
		new AsyCommit(mActivity, "").execute();
	}

	private class AsyCommit extends MyAsyncTask{

		protected AsyCommit(Context context, String title) {
			super(context, title);
		}

		@Override
		protected Object doInBackground(Object... params) {
			return mUserService.applyLoan_updateIdentityImg(loanId, identityImg);
		}
		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);
			String msg = "证件资料上传失败，请重新上传";
			if((Boolean)result){
				msg = "上传证件成功，去分享一下吧";
				IntentUtil.activityForward(mActivity,ApplySucessActivity.class, null, true);
			}
			ToastUtil.showToast(mActivity, 0, msg, true);
		}
	}
	private TakePicUtil mTakePic;

	private void choose_photos() {
		if (null == mTakePic)
			mTakePic = new TakePicUtil(mActivity);
		mTakePic.ShowPickDialog();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		// 如果是直接从相册获取
		case TakePicUtil.CHOOSE_PIC:
			String pathFromPic = mTakePic.getBitmapFromPic2(data);// getBitmapFromPic(mActivity,
																	// //
																	// data);//getBitmapPath(data);
			LogUtil.d(TAG, "从相册获取==pathFromPic is " + pathFromPic);
			showPicImg(pathFromPic);
			break;
		// 如果是调用相机拍照时
		case TakePicUtil.TAKE_PIC:
			String pathFromTakePic = mTakePic.getBitmapPath2(data);// getBitmapPath(data);
			LogUtil.d(TAG, "从拍照获取==pathFromTakePic is " + pathFromTakePic);
			showPicImg(pathFromTakePic);
			break;
		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	private void showPicImg(String imgPath) {
		// 上传图片
		if (!StringUtil.checkStr(imgPath))
			return;
		new AsyUploadPic(mActivity,"", imgPath).execute();
	}
	
	private class AsyUploadPic extends MyAsyncTask {
		String pic;
		protected AsyUploadPic(Context context,String title, String pic) {
			super(context,title);
			this.pic = pic;
		}

		@Override
		protected Object doInBackground(Object... params) {
			return mUserService.upload_pictures(pic);
		}

		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);
			LogUtil.d(TAG, "AsyUploadPic==result is "+result);
			JSONObject dataResult = (JSONObject) result;
			if (null == dataResult)
				return;
			int code = dataResult.optInt("code");
			JSONObject data = dataResult.optJSONObject("data");
			if (null != data) {
				String message = data.optString("message");
				JSONArray pictures = data.optJSONArray("pictures");
				String pictureURL = data.optString("pictureURL");
				if (StringUtil.checkStr(message)) {
					ToastUtil.showToast(mActivity, 0, message, true);
				}
				if (200 == code) {
					if (null != pictures) {
						String image = pictures.optString(0);
						identityImg = pictureURL+image;
						commit_info();
						mImgLoad.loadImg(cd_img, identityImg,R.drawable.cd_shili);
					}
				}
			}
		}
	}

}
