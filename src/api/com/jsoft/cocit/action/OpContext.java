package com.jsoft.cocit.action;

import com.jsoft.cocit.dmengine.command.WebCommandContext;
import com.jsoft.cocit.mvc.CocEntityParam;

/**
 * @deprecated 用HttpCommand代替
 * 
 */
public class OpContext extends WebCommandContext {

	/**
	 * 创建一个“操作助手”对象
	 * 
	 * @param funcExpr
	 *            功能参数：“moduleID:tableID:opMode”
	 * @return
	 */
	public static OpContext make(String funcExpr) {
		OpContext ret = new OpContext(funcExpr, null, null, 0);

		if (ret.httpContext != null) {
			ret.httpContext.getRequest().setAttribute(REQUEST_KEY_HTTPCOMMAND, ret);
		}

		return ret;
	}

	/**
	 * 创建一个“操作环境”对象
	 * 
	 * @param funcExpr
	 *            功能表达式：该参数由“菜单编号:实体编号:操作编号”组成，其中菜单编号是必需的，后两个根据需要是可选的。<br/>
	 *            平台会自动根据该参数加载“菜单服务对象，实体服务对象，操作服务对象”。<br/>
	 *            菜单服务对象：可以调用opContext.getSystemMenu()方法获得；<br/>
	 *            实体服务对象：可以调用opContext.getCocEntity()方法获得；<br/>
	 *            操作服务对象：可以调用opContext.getCocAction()方法获得；
	 * @param dataArgs
	 *            数据参数：由数据ID(物理主键)组成，可以是单值也可以是多值，多值用逗号分隔。<br/>
	 *            平台会自动根据该参数加载“数据对象或数据列表”。<br>
	 *            如果该参数是单值，则调用opContext.getDataObject()方法可以获得“实体对象”；<br/>
	 *            如果该参数是多值，则调用opContext.getDataObject()方法可以获得“实体列表(List对象)”。<br/>
	 * @param httpParams
	 *            HTTP参数：浏览器上传入的参数如果以“entity.”开头的话，这些参数将被注入到httpParams中。<br/>
	 *            httpParams传入到OpContext.make方法后，这些参数将被注入到已经加载的“数据对象或对象列表”中。
	 * @return
	 */
	public static OpContext make(String funcExpr, String dataArgs, CocEntityParam httpParams) {
		OpContext ret = new OpContext(funcExpr, dataArgs, httpParams, 0);

		if (ret.httpContext != null) {
			ret.httpContext.getRequest().setAttribute(REQUEST_KEY_HTTPCOMMAND, ret);
		}

		return ret;
	}

	/**
	 * 创建一个“操作环境”对象
	 * 
	 * @param funcExpr
	 *            功能表达式：该参数由“菜单编号:实体编号:操作编号”组成，其中菜单编号是必需的，后两个根据需要是可选的。<br/>
	 *            平台会自动根据该参数加载“菜单服务对象，实体服务对象，操作服务对象”。<br/>
	 *            菜单服务对象：可以调用opContext.getSystemMenu()方法获得；<br/>
	 *            实体服务对象：可以调用opContext.getCocEntity()方法获得；<br/>
	 *            操作服务对象：可以调用opContext.getCocAction()方法获得；
	 * @param dataArgs
	 *            数据参数：由数据ID(物理主键)组成，可以是单值也可以是多值，多值用逗号分隔。<br/>
	 *            平台会自动根据该参数加载“数据对象或数据列表”。<br>
	 *            如果该参数是单值，则调用opContext.getDataObject()方法可以获得“实体对象”；<br/>
	 *            如果该参数是多值，则调用opContext.getDataObject()方法可以获得“实体列表(List对象)”。<br/>
	 * @param httpParams
	 *            HTTP参数，浏览器上传入的参数如果以“entity.”开头的话，这些参数将被注入到httpParams中。<br/>
	 *            httpParams传入到OpContext.make方法后，这些参数将被注入到已经加载的“数据对象或对象列表”中。
	 * @param fireLoadEvent
	 * @return
	 */
	public static OpContext make(String funcExpr, String dataArgs, CocEntityParam httpParams, int cmdType) {
		OpContext ret = new OpContext(funcExpr, dataArgs, httpParams, cmdType);

		if (ret.httpContext != null) {
			ret.httpContext.getRequest().setAttribute(REQUEST_KEY_HTTPCOMMAND, ret);
		}

		return ret;
	}

	private OpContext(String funcExpr, String dataArgs, CocEntityParam httpParams, int cmdType) {
		super(funcExpr, dataArgs, httpParams, cmdType);
	}

}
