package com.jsoft.cocit.constant;

/**
 * 工作流相关URL地址常量定义
 * <p>
 * WF: Work Flow Instance “流程实例”前缀
 * <p>
 * WFD: Work Flow Definition “流程定义”前缀
 * 
 * @author Ji Yongshan
 * 
 */
public interface WfUrl {

	/**
	 * “过程日志”URL地址：用来获取“流程实例”执行过程日志
	 */
	public static final String WF_LOG_URL = "/cocworkflow/hiLogFlowChart/*";
	public static final String WF_CHART_URL = "/cocworkflow/piFlowChart/*";

	/**
	 * “流程定义图”URL地址：用来获取“流程定义图”
	 */
	public static final String WFD_CHART_URL = "/cocworkflow/toProcdefFlowChart/*";

	/**
	 * “流程开始表单”URL地址：用来获取“新建流程”的表单界面。
	 * <p>
	 * “getFormToStart”含义：1. 获取表单(getForm)；2. 表单提交“到哪里”?(答：toStart)
	 */
	public static final String WF_FORM_TO_START_URL = "/cocworkflow/getFormToStart/*";

	public static final String WF_START_URL = "/cocworkflow/start/*";

	public static final String WF_FORM_TO_PROCESS_URL = "/cocworkflow/getFormToProcess/*";

	public static final String WF_PROCESS_URL = "/cocworkflow/process/*";
	public static final String WF_DELETE_URL = "/cocworkflow/delete/*";
}
