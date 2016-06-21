package com.constants;


public class InterfaceParams {
	
	/*
	 * 注册Device
	 */
	public static final String device_registerIncludeAppId = "device/registerIncludeAppId";
	
	/*
	 * 注册Device
	 */
	public static final String device_register = "device/register";
	/*
	 * 注册Device
	 */
	public static final String user2_login = "user2/login";
	
	/*
	 * 注册发送短信验证码
	 * /user2/sendRegCode
	 */
	public static final String user2_sendRegCode = "user2/sendRegCode";
	
	/*
	 * user2/register
	 */
	public static final String user2_register = "user2/register";
	
	/*
	 * 注册设置名称与密码
	 * /user2/updateNameAndPassword
	 */
	public static final String user2_updateNameAndPassword = "user2/updateNameAndPassword";
	
	/*
	 * 找回密码 发送短信验证码
	 * user2/forgetPassword
	 */
	public static final String user2_forgetPassword = "user2/forgetPassword";
	
	/*
	 * 修改密码 发送短信验证码
	 * /user2/sendVCode3
	 */
	public static final String user2_sendVCode3 = "user2/sendVCode3";
	
	/*
	 * 找回密码 验证短信验证码
	 * user2/verifyPasswordCode
	 */
	public static final String user2_verifyPasswordCode = "user2/verifyPasswordCode";
	
	/*
	 * 找回密码 验证短信验证码
	 * user2/verifyVcode3
	 */
	public static final String user2_verifyVCode3 = "user2/verifyVCode3";
	
	/*
	 * 设置新密码
	 * user2/updatePassword
	 */
	public static final String user2_updatePassword = "user2/updatePassword";
	
	/*
	 * 意见反馈
	 * feedback/add
	 */
	public static final String feedback_add = "feedback/add";
	
	/*
	 * 意见反馈
	 * message/system
	 */
	public static final String message_system = "message/system";
	
	/*
	 * upload/pictures
	 * 统一图片上传
	 */
	public static final String upload_pictures = "upload/pictures";
	
	/*
	 * 城市列表
	 * http://test.daigj.com/api/adr/option/city
	 */
	public static final String option_city = "option/city";
	
	/*
	 * 城市列表
	 * http://test.daigj.com/api/adr/option/city
	 */
	public static final String option_province = "option/province";
	
	/*
	 * university列表
	 * http://test2.daigj.com/api/adr/option/province/university
	 */
	public static final String option_province_university = "option/province/university";
	
	/*
	 * 贷款首页
	 * http://test.daigj.com/api/adr/dk/index
	 */
	public static final String dk_index = "dk/index";
	
	/*
	 * 热门贷款|现金分期 详情
	 * bank/service
	 */
	public static final String productHot_detail = "productHot/detail";
	
	/*
	 * 发送注册短信验证码
	 * userRegister/sendRegCode
	 */
	public static final String userRegister_sendRegCode = "userRegister/sendRegCode";
	
	/*
	 * 验证短信验证码注册
	 * userRegister/register
	 */
	public static final String userRegister_register = "userRegister/register";
	
	/*
	 * 热门贷款|现金分期
	 * productHot/list
	 */
	public static final String productHot_list = "productHot/list";
	
	/*
	 * 顾问列表
	 * http://test.daigj.com/api/adr/dk/expert
	 */
	public static final String dk_expert = "dk/expert";
	
	/*
	 * 获取顾问经理
	 * http://test.daigj.com/api/adr/dk/user/getInvestUser
	 */
	public static final String dk_user_getInvestUser = "dk/user/getInvestUser";
	
	/*
	 * 抢单的信贷经理
	 * http://test.daigj.com/api/adr/dk/loan/listInvestor
	 */
	public static final String dk_loan_listInvestor = "dk/loan/listInvestor";
	
	/*
	 * 我的贷款详情
	 * http://test.daigj.com/api/adr/dk/loan/detail
	 */
	public static final String dk_loan_detail = "dk/loan/detail";
	
	/*
	 * 我的贷款列表
	 * http://test.daigj.com/api/adr/dk/loan/list
	 */
	public static final String dk_loan_list = "dk/loan/list";
	
	/*
	 * 获取用户信息
	 * http://test.daigj.com/api/adr/applyLoan/getUserLoanInfo
	 */
	public static final String applyLoan_getUserLoanInfo = "applyLoan/getUserLoanInfo";
	
	/*
	 * http://test.daigj.com/api/ios/option/list
	 * 借款表单选项
	 */
	public static final String option_list = "option/list";
	
	/*
	 * 保存|更新我的资料
	 * http://test.daigj.com/api/adr/applyLoan/updateUserLoanInfo
	 */
	public static final String applyLoan_updateUserLoanInfo = "applyLoan/updateUserLoanInfo";
	
	/*
	 * 单子进度
	 * http://test.daigj.com/api/adr/dk/loan/process
	 */
	public static final String dk_loan_process = "dk/loan/process";
	
	/*
	 * 单子进度新接口
	 * http://test.daigj.com/api/adr/dk/loan/processv2
	 */
	public static final String dk_loan_processv2 = "dk/loan/processv2";
	/*
	 * 关闭贷款申请单
	 * http://test.daigj.com/api/adr/loan/close
	 */
	public static final String loan_close = "loan/close";
	
	/*
	 * 校验并且贷款发单
	 * http://test.daigj.com/api/adr/applyLoan/verifyAndAdd
	 */
	public static final String applyLoan_verifyAndAdd = "applyLoan/verifyAndAdd";
	
	
	/*
	 * 利息计算
	 * http://test.daigj.com/api/adr/dk/calc
	 */
	public static final String dk_calc = "dk/calc";
	
	/*
	 * 贷款通知进度
	 * http://test.daigj.com/api/adr/applyLoan/progress
	 */
	public static final String applyLoan_progress = "applyLoan/progress";
	
	/*
	 * 结束抢单
	 * http://test.daigj.com/api/adr/loan/closeSeckill
	 */
	public static final String loan_closeSeckill = "loan/closeSeckill";
	
	/*
	 * 获取用户信息
	 * http://test.daigj.com/api/adr/userRegister/getUser
	 */
	public static final String userRegister_getUser = "userRegister/getUser";
	
	/*
	 * 批量用户信息获取
	 * http://test.daigj.com/api/adr/user2/getUserInfo
	 */
	public static final String user2_getUserInfo = "user2/getUserInfo";
	
	/*
	 * 用户查询
	 * http://test.daigj.com/api/adr/user2/get
	 */
	public static final String user2_get = "user2/get";
	
	/*
	 * 更新用户头像
	 * http://test.daigj.com/api/adr/user2/updateAvatar
	 */
	public static final String user2_updateAvatar = "user2/updateAvatar";
	
	/*
	 * 登陆态下贷款发单
	 * http://test.daigj.com/api/adr/applyLoan/add
	 */
	public static final String applyLoan_add = "applyLoan/add";
	
	/*
	 * 打分评论
	 * http://test.daigj.com/api/adr/score/add
	 */
	public static final String score_add = "score/add";
	
	/*
	 * 信贷经理详细信息
	 * http://test.daigj.com/api/adr/dk/user/getInvestUserV2
	 */
	public static final String dk_user_getInvestUserV2 = "dk/user/getInvestUserV2";
	
	/*
	 * http://test.daigj.com/api/adr/qd/pd/detail
	 */
	public static final String qd_pd_detail = "qd/pd/detail";
	
	/*分享信息
	 * http://test.daigj.com/api/adr/dk/user/share
	 */
	public static final String dk_user_share = "dk/user/share";
	
	/*
	 * 上传身份照片：
	 * http://test.daigj.com/api/ios/applyLoan/updateIdentityImg
	 */
	public static final String applyLoan_updateIdentityImg = "applyLoan/updateIdentityImg";
}
