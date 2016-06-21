package com.openim;

import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.contact.IYWContact;
import com.alibaba.mobileim.conversation.IYWConversationService;
import com.alibaba.mobileim.conversation.YWConversation;
import com.alibaba.mobileim.conversation.YWConversationType;
import com.alibaba.mobileim.conversation.YWMessage;
import com.alibaba.mobileim.conversation.YWP2PConversationBody;
import com.alibaba.mobileim.conversation.YWTribeConversationBody;
import com.alibaba.mobileim.gingko.model.tribe.YWTribe;
import com.alibaba.mobileim.login.YWLoginState;
import com.sxjs.diantu_daikuan.MyApplication;
import com.utils.StringUtil;

import java.util.List;

/**
 * 会话相关的数据操作 相关接口和对象说明： IYWConversationService是会话的操作入口，所有会话相关都通过此接口来进行访问
 * YWConversation，代表一个会话的实例
 * 
 * @author zhaoxu
 *
 */
public class ConversationSampleHelper {

	private MyApplication mContext;
	public ConversationSampleHelper(MyApplication application) {
		this.mContext = application;
	}

	/**
	 * 会话相关的操作统一通过IYWConversationService接口来进行操作
	 * 
	 * @return
	 */
	public IYWConversationService getConversationService() {
		final YWIMKit imKit = mContext.mOpenImUtil.getIMKit();//LoginSampleHelper.getInstance().getIMKit();
		if(YWLoginState.success !=imKit.getIMCore().getLoginState()){
			return null;
		}
		return imKit.getConversationService();
	}

	/**
	 * 获取当前所有的会话，无序
	 * 
	 * @return
	 */
	public List<YWConversation> getAllConversations() {
		IYWConversationService conversationService = getConversationService();
		if(null==conversationService)
			return null;
		return conversationService.getConversationList();
	}

	/**
	 * 根据用户ID，获取最新的一条消息
	 * 
	 * @param userId
	 * @return
	 */
	public YWConversation getConversation(String userId) {
		if(!StringUtil.checkStr(userId))
			return null;
		IYWConversationService conversationService = getConversationService();
		if(null==conversationService)
			return null;
		// 根据用户ID，获取指定的会话
		return conversationService.getConversationByUserId(userId);
	}

	/**
	 * 获取某个联系人的最新一条消息
	 * 
	 * @param userId
	 * @return
	 */
	public YWMessage getLatestMessage(String userId) {
		YWConversation conversation = getConversation(userId);
		if (conversation != null) {
			return conversation.getLastestMessage();
		}
		return null;
	}

	/**
	 * 总的未读消息数
	 * 
	 * @return
	 */
	public int getTotalUnreadCount() {
		IYWConversationService conversationService = getConversationService();
		if(null==conversationService)
			return 0;
		return conversationService.getAllUnreadCount();
	}

	/**
	 * 指定会话的未读数
	 * 
	 * @param userId
	 * @return
	 */
	public int getUnreadCount(String userId) {
		YWConversation conversation = getConversation(IMConfig.getUseridPre() + userId);
		if (conversation != null) {
			return conversation.getUnreadCount();
		}
		return 0;
	}

	/**
	 * 指定会话的最新一条消息
	 * 
	 * @param userId
	 * @return
	 */
	public long getLatestMessageTime(String userId) {
		YWMessage msg = getLatestMessage(userId);
		if (msg != null) {
			// 单位为毫秒
			return msg.getTimeInMillisecond();
		}
		return 0;
	}

	/**
	 * 从会话中获取当前聊天的用户ID
	 * 
	 * @param conversation
	 * @return
	 */
	public String getUserIdFromConversation(YWConversation conversation) {
		if (conversation == null) {
			return "";
		}

		if (conversation.getConversationType() == YWConversationType.P2P) {
			YWP2PConversationBody conversationBody = (YWP2PConversationBody) conversation
					.getConversationBody();
			IYWContact contact = conversationBody.getContact();
			return contact.getUserId();
		}
		return "";
	}

	/**
	 * 从会话获取群聊ID
	 * 
	 * @param conversation
	 * @return
	 */
	public long getTribeIdFromConversation(YWConversation conversation) {
		if (conversation == null) {
			return 0;
		}

		// 群聊会话才支持
		if (conversation.getConversationType() == YWConversationType.Tribe) {
			YWTribeConversationBody conversationBody = (YWTribeConversationBody) conversation
					.getConversationBody();
			YWTribe tribe = conversationBody.getTribe();
			return tribe.getTribeId();
		}
		return 0;
	}
}
