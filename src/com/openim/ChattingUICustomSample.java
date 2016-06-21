package com.openim;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.NinePatch;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.aop.Pointcut;
import com.alibaba.mobileim.aop.custom.IMChattingPageUI;
import com.alibaba.mobileim.channel.util.AccountUtils;
import com.alibaba.mobileim.contact.IYWContact;
import com.alibaba.mobileim.conversation.YWConversation;
import com.alibaba.mobileim.conversation.YWConversationType;
import com.alibaba.mobileim.conversation.YWMessage;
import com.alibaba.mobileim.conversation.YWP2PConversationBody;
import com.alibaba.mobileim.conversation.YWTribeConversationBody;
import com.alibaba.mobileim.utility.YWIMImageUtils;
import com.db.service.UserInfoService;
import com.sxjs.diantu_daikuan.MyApplication;
import com.sxjs.diantu_daikuan.R;
import com.utils.LogUtil;
import com.utils.StringUtil;

/**
 * Created by mayongge on 15-9-23.
 */
public class ChattingUICustomSample extends IMChattingPageUI {

	private static final String TAG = "ChattingUICustomSample";
	
    public ChattingUICustomSample(Pointcut pointcut) {
        super(pointcut);
    }
    /**
     * 设置左边图片消息汽泡背景图，需要.9图
     * @param resId 资源id
     * @return 0: 默认背景 －1:透明背景（无背景） resId：使用resId对应图片做背景
     */
    @Override
    public int getLeftImageMsgBackgroundResId() {
        return R.drawable.left_chat_bg;
//		return 0;
//		return -1;
    }


    @Override
    public int getLeftTextMsgBackgroundResId() {
        return R.drawable.left_chat_bg;
//		return 0;
    }

    @Override
    public int getLeftGeoMsgBackgroundResId(YWConversation conversation) {
        return R.drawable.aliwx_comment_l_bg;
    }

    @Override
    public int getLeftCustomMsgBackgroundResId(YWConversation conversation) {
        return R.drawable.aliwx_comment_l_bg;
    }

    /**
     * 设置右边图片消息汽泡背景图，需要.9图
     * @param resId 资源id
     * @return 0: 默认背景 －1:透明背景（无背景） resId：使用resId对应图片做背景
     */
    @Override
    public int getRightImageMsgBackgroundResId() {
        return R.drawable.right_chat_bg;
//		return 0;
//		return -1;
    }
    @Override
    public int getRightTextMsgBackgroundResId() {
        return R.drawable.right_chat_bg;
//		return 0;
    }

    @Override
    public int getRightGeoMsgBackgroundResId(YWConversation conversation) {
        return R.drawable.aliwx_comment_r_bg;
    }

    @Override
    public int getRightCustomMsgBackgroundResId(YWConversation conversation) {
        return R.drawable.aliwx_comment_r_bg;
    }


    /**
     * 建议使用{@link processBitmapOfLeftImageMsg｝和{@link processBitmapOfRightImageMsg｝灵活修改Bitmap，达到对图像进行［圆角处理］,［裁减］等目的,这里建议return false
     * 设置是否需要将聊天界面的图片设置为圆角
     * @return false: 不做圆角处理 true：做圆角处理（重要：返回true时不会做{@link processBitmapOfLeftImageMsg｝和{@link processBitmapOfRightImageMsg｝二次图片处理，两者互斥！）
     */

    @Override
    public boolean needRoundChattingImage() {
        return true;
    }

    /**
     * 设置聊天界面图片圆角的边角半径的长度(单位：dp)
     * @return
     */
    @Override
    public float getRoundRadiusDps() {
        // TODO Auto-generated method stub
        return 12.6f;
    }

    @Override
    public int getChattingBackgroundResId() {
        //聊天窗口背景，默认不显示
        return R.drawable.openim_chatting_bg;
    }

    /**
     *
     * 用于更灵活地加工［左边图片消息］的Bitmap用于显示，SDK内部会缓存之，后续直接使用缓存的Bitmap显示。例如：对图像进行［裁减］，［圆角处理］等等
     * 重要：使用该方法时：
     * 1.请将 {@link needRoundChattingImage}设为return false(不裁剪圆角)，两者是互斥关系
     * 2.建议将{@link getLeftImageMsgBackgroundResId}设为return－1（背景透明）
     * @param input 网络获取的聊天图片
     * @return  供显示的Bitmap
     */
    public  Bitmap processBitmapOfLeftImageMsg(Bitmap input) {
        Bitmap output = Bitmap.createBitmap(input.getWidth(),
                input.getHeight(), Bitmap.Config.ARGB_8888);
        //为提高性能，对取得的resource图片做缓存
        Bitmap distBitmap = YWIMImageUtils.getFromCacheOrDecodeResource(R.drawable.left_chat_bg);//left_bubble
        NinePatch np = new NinePatch(distBitmap, distBitmap.getNinePatchChunk(), null);
        Canvas canvas = new Canvas(output);
        final Paint paint = new Paint();
        final Rect rectSrc = new Rect(0, 0, input.getWidth(), input.getHeight());
        final RectF rectDist = new RectF(0, 0, input.getWidth(), input.getHeight());
        np.draw(canvas, rectDist);
        canvas.drawARGB(0, 0, 0, 0);
        //设置Xfermode
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
        canvas.drawBitmap(input, rectSrc, rectSrc, paint);
        return output;
    }

    /**
     *  用于更灵活地加工［右边图片消息］的Bitmap用于显示，SDK内部会缓存之，后续直接使用缓存的Bitmap显示。例如：对图像进行［裁减］，［圆角处理］等等
     * 重要：使用该方法时：
     * 1.请将 {@link needRoundChattingImage}设为return false(不裁剪圆角)，两者是互斥关系
     * 2.建议将{@link getRightImageMsgBackgroundResId}设为return－1（背景透明）
     * @param input 网络获取的聊天图片
     * @return  供显示的Bitmap
     */
    public  Bitmap processBitmapOfRightImageMsg(Bitmap input) {
        Bitmap output = Bitmap.createBitmap(input.getWidth(),
                input.getHeight(), Bitmap.Config.ARGB_8888);
        //为提高性能，对取得的resource图片做缓存
        Bitmap distBitmap = YWIMImageUtils.getFromCacheOrDecodeResource(R.drawable.right_chat_bg);//right_bubble
        NinePatch np = new NinePatch(distBitmap, distBitmap.getNinePatchChunk(), null);
        Canvas canvas = new Canvas(output);
        final Paint paint = new Paint();
        final Rect rectSrc = new Rect(0, 0, input.getWidth(), input.getHeight());
        final RectF rectDist = new RectF(0, 0, input.getWidth(), input.getHeight());
        np.draw(canvas, rectDist);
        canvas.drawARGB(0, 0, 0, 0);
        //设置Xfermode
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
        canvas.drawBitmap(input, rectSrc, rectSrc, paint);
        return output;
    }

    /**
     * 是否隐藏标题栏
     *
     * @param fragment
     * @param conversation
     * @return
     */
    @Override
    public boolean needHideTitleView(Fragment fragment, YWConversation conversation) {
//        if (conversation.getConversationType() == YWConversationType.Tribe){
//            return true;
//        }
        //@消息功能需要展示标题栏，暂不隐藏
        return false;
    }

    /**
     * isv需要返回自定义的view. openIMSDK会回调这个方法，获取用户设置的view. Fragment 聊天界面的fragment
     */
    @Override
    public View getCustomTitleView(final Fragment fragment,
                                   final Context context, LayoutInflater inflater,
                                   final YWConversation conversation) {
        // 单聊和群聊都会使用这个方法，所以这里需要做一下区分
        // 本demo示例是处理单聊，如果群聊界面也支持自定义，请去掉此判断

        //重要：必须以该形式初始化view---［inflate(R.layout.**, new RelativeLayout(context),false)］------，以让inflater知道父布局的类型，否则布局**中的高度和宽度无效，均变为wrap_content
    	View view = inflater.inflate(R.layout.headview, null);
		view.setBackgroundColor(Color.parseColor("#FFFFFF"));
		ImageView left_img = (ImageView) view.findViewById(R.id.leftImg);
		TextView title_text = (TextView) view.findViewById(R.id.textview);
		view.findViewById(R.id.rightImg).setVisibility(View.GONE);
		left_img.setImageResource(R.drawable.gray_back_down);
		title_text.setTextColor(Color.parseColor("#333333"));
		String title = null;
        if (conversation.getConversationType() == YWConversationType.P2P) {
            YWP2PConversationBody conversationBody = (YWP2PConversationBody) conversation
                    .getConversationBody();
            String userID = conversationBody.getContact().getUserId();
            String appKey = conversationBody.getContact().getAppKey();
            title = conversationBody.getContact().getShowName();
            if(!StringUtil.checkStr(title)||StringUtil.isMobileNO(title)){
            	YWIMKit imKit = MyApplication.mContext.mOpenImUtil.getIMKit();
                IYWContact contact = imKit.getContactService().getContactProfileInfo(userID,appKey);
                //生成showName，According to id。
                if (contact != null) {
                    title = contact.getShowName();
                }
            }
            if (!StringUtil.checkStr(title)||StringUtil.isMobileNO(title)) {
				MyApplication myApplication = (MyApplication) context.getApplicationContext();
				if(userID.contains(IMConfig.getUseridPre())){
					String uid = userID.replace(IMConfig.getUseridPre(), "");
					UserInfoService userService = new UserInfoService(myApplication, uid);
					title = userService.getUserRealName();//&&!StringUtil.checkStr(title)
				}
				
				if(IMUserdata.userInfoMaps.containsKey(userID)&&!StringUtil.checkStr(title)) {
					IYWContact userInfo = IMUserdata.userInfoMaps.get(userID);
					if(null!=userInfo){
						title = userInfo.getShowName();
						LogUtil.d(TAG, "显示聊天名称==userInfoMaps==title is "+title);
					}
				}
			}
            //如果标题为空，那么直接使用Id
            if (!StringUtil.checkStr(title)||StringUtil.isMobileNO(title)) {
                //title = conversationBody.getContact().getUserId();
            }
        } else {
            if (conversation.getConversationBody() instanceof YWTribeConversationBody) {
                title = ((YWTribeConversationBody) conversation.getConversationBody()).getTribe().getTribeName();
                if (TextUtils.isEmpty(title)) {
                    title = "自定义的群标题";
                }
            } else {
                if (conversation.getConversationType() == YWConversationType.SHOP) { //为OpenIM的官方客服特殊定义了下、
                    title = AccountUtils.getShortUserID(conversation.getConversationId());
                }
            }
        }
        LogUtil.d(TAG, "最后显示聊天名称==userInfoMaps==title is "+title);
        if(StringUtil.checkStr(title)){
        	if(!StringUtil.isMobileNO(title)){
        		title_text.setText(title);
        	}
        }     
        left_img.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				fragment.getActivity().finish();
			}
		});        
        return view;
    }


    /**
     * 定制图片预览页面titlebar右侧按钮点击事件
     *
     * @param fragment
     * @param message  当前显示的图片对应的ywmessage对象
     * @return true：使用用户定制的点击事件， false：使用默认的点击事件(默认点击事件为保持该图片到本地)
     */
    @Override
    public boolean onImagePreviewTitleButtonClick(Fragment fragment, YWMessage message) {
        Context context = fragment.getActivity();
        LogUtil.d(TAG, "onImagePreviewTitleButtonClick==context is "+context);
        return true;
    }
}
