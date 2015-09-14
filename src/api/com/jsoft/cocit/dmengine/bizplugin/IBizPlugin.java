package com.jsoft.cocit.dmengine.bizplugin;

/**
 * 操作插件：在执行业务表相关操作时自动回调插件中的方法处理业务逻辑。
 * 
 * @author jiongsoft
 * @preserve public
 */
public interface IBizPlugin<T> {

	/**
	 * 执行业务操作“加载数据前”调用该方法
	 * 
	 * @param event
	 */
	public void beforeLoad(BizEvent<T> event);

	/**
	 * 执行业务操作“加载数据后”调用该方法
	 * 
	 * @param event
	 */
	public void afterLoad(BizEvent<T> event);

	/**
	 * 执行业务操作“提交数据前”调用该方法
	 * 
	 * @param event
	 */
	public void beforeSubmit(BizEvent<T> event);

	/**
	 * 执行业务操作“提交数据后”调用该方法
	 * 
	 * @param event
	 */
	public void afterSubmit(BizEvent<T> event);
}
