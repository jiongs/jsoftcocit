package com.jsoft.cocit.entity;

/**
 * 流程实例接口：实现该接口的实体类（业务模块）即为流程模块，往该模块中添加数据的时候，将会自动启动工作流。
 * 
 * @author Ji Yongshan
 * 
 */
public interface IWorkflowInstance {

	/**
	 * 获取流程实例编号
	 * <p>
	 * 如果“实体类”实现了该接口，则该实体类必需包含一个属性名为“workflowKey”的字段。
	 * 
	 * @return
	 */
	public String getWorkflowKey();

	/**
	 * 设置流程实例编号
	 * <p>
	 * 当新建数据的时候，启动流程成功后，会从流程引擎获取到流程实例编号，然后调用该方法将流程示例编号写到该字段里面。
	 * 
	 * @param workflowKey
	 */
	public void setWorkflowKey(String workflowKey);

}
