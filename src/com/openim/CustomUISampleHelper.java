package com.openim;

import com.alibaba.mobileim.aop.AdviceBinder;
import com.alibaba.mobileim.aop.PointCutEnum;

/**
 * UI定制化初始化统一入口，这里后续会增加更多的UI定制化功能
 * 
 * @author zhaoxu
 *
 */
public class CustomUISampleHelper {
	public static void initCustomUI() {
		// 如果需要对聊天界面的ui进行定制，需要绑定自己定义的advice ,具体写法参见
		AdviceBinder.bindAdvice(PointCutEnum.CHATTING_FRAGMENT_UI_POINTCUT,
				ChattingUICustomSample.class);
		// 会话列表UI相关
		AdviceBinder.bindAdvice(PointCutEnum.CONVERSATION_FRAGMENT_UI_POINTCUT,
				ConversationListUICustomSample.class);
		// 会话列表业务相关
		AdviceBinder.bindAdvice(
				PointCutEnum.CONVERSATION_FRAGMENT_OPERATION_POINTCUT,
				ConversationListOperationCustomSample.class);
	}
}
