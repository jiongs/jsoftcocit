package com.jsoft.cocit.constant;

import com.jsoft.cocit.dmengine.bizplugin.IBizPlugin;

/**
 * 操作码分类：
 * <UL>
 * <LI>表单操作：操作步骤分为两步，即点击操作按钮后，第一步弹出表单窗口，第二步提交表单；业务插件中的四个方法都将生效。操作码（101-200）
 * <UL>
 * <LI>“单条数据”操作：操作码（101-150）可以作为GRID行操作（单101“添加”操作不能作为行操作）
 * <LI>“多条数据”操作：操作码（151-190）可以作为GRID行操作
 * <LI>“GRID数据”操作：“只能”用业务插件实现业务逻辑，操作码（191-200）不能作为行操作
 * </UL>
 * </LI>
 * <LI>非表单操作：即点击操作按钮后操作即可完成；对于这类操作，业务插件中的（{@link IBizPlugin#beforeLoad(com.jsoft.cocit.dmengine.bizplugin.BizEvent)}）和（{@link IBizPlugin#afterLoad(com.jsoft.cocit.dmengine.bizplugin.BizEvent)}）方法将失效。操作码（201-300）
 * <UL>
 * <LI>“单条数据”操作：操作码（201-250）可以作为GRID行操作
 * <LI>“多条数据”操作：操作码（251-290）可以作为GRID行操作
 * <LI>“GRID数据”操作：“只能”用业务插件实现业务逻辑，操作码（291-300）不能作为行操作
 * </UL>
 * </LI>
 * </UL>
 */
public interface OpCodes {

	// =================================================================================
	// 表单操作 101-200
	// =================================================================================

	/*
	 * 基于“单条数据”的操作（打开表单）：101-150
	 */
	/** 添加表单数据：在插件方法({@link IBizPlugin#beforeSubmit(com.jsoft.cocit.dmengine.bizplugin.BizEvent)})中修改过的数据会被自动保存。 */
	static final int OP_INSERT_FORM_DATA = 101;
	/** 修改表单数据：在插件方法({@link IBizPlugin#beforeSubmit(com.jsoft.cocit.dmengine.bizplugin.BizEvent)})中修改过的数据会被自动保存。 */
	static final int OP_UPDATE_FORM_DATA = 102;
	/** 移除表单数据：即逻辑删除 */
	static final int OP_REMOVE_FORM_DATA = 103;
	/** 删除表单数据： 即物理删除、永久删除 */
	static final int OP_DELETE_FORM_DATA = 109;
	/** 处理表单数据：即基于表单数据执行业务插件，在业务插件中修改过的数据不会自动保存到数据库中。 */
	static final int OP_RUN_FORM_DATA = 121;

	/*
	 * 基于“多条数据”的操作（打开表单）：151-190
	 */
	/** 批量修改表单数据：在插件方法({@link IBizPlugin#beforeSubmit(com.jsoft.cocit.dmengine.bizplugin.BizEvent)})中修改过的数据会被自动保存。 */
	static final int OP_UPDATE_FORM_DATAS = 162;
	/** 批量移除表单数据：即逻辑删除 */
	static final int OP_REMOVE_FORM_DATAS = 163;
	/** 批量删除表单数据： 即物理删除、永久删除 */
	static final int OP_DELETE_FORM_DATAS = 169;
	/** 批量处理表单数据：即基于表单数据执行业务插件，在业务插件中修改过的数据不会自动保存到数据库中。 */
	static final int OP_RUN_FORM_DATAS = 181;

	/*
	 * 基于“查询表达式”的操作（打开表单）：191-200 不能作为行操作
	 */
	/***/
	/** 处理表单业务：“只能”通过业务插件处理业务逻辑 */
	static final int OP_RUN_FORM = 191;
	/** 导出数据到EXCEL文件： */
	static final int OP_EXPORT_XLS = 192;
	/** 从EXCEL文件导入数据： */
	static final int OP_IMPORT_XLS = 193;

	// =================================================================================
	// 非表单操作 201-300
	// =================================================================================

	/*
	 * 基于“单条数据”的操作（不打开表单）：201-250
	 */
	/** 修改单条数据：在GRID上选中一条记录后直接发送到后台，通过注解完成字段的修改。 */
	static final int OP_UPDATE_ROW = 202;
	/** 移除单条数据：即逻辑删除 */
	static final int OP_REMOVE_ROW = 203;
	/** 删除单条数据： 即物理删除、永久删除 */
	static final int OP_DELETE_ROW = 209;
	/** 处理单条数据：通过业务插件处理单条数据 */
	static final int OP_RUN_ROW = 221;
	/** 插入单条数据：点击操作按钮后，在GRID前插入一条数据。 */
	static final int OP_INSERT_GRID_ROW = 241;
	/** 修改单条数据：在GRID上选中一条记录后直接发送到后台，通过注解完成字段的修改。 */
	static final int OP_UPDATE_GRID_ROW = 242;
	/** 保存多条数据：在GRID上新增、修改数据后，一次性保存。 */
	static final int OP_SAVE_GRID_ROWS = 243;

	/*
	 * 基于“多条数据”的操作（不打开表单）：251-290
	 */
	/** 排序：移到顶 */
	static final int OP_SORT_TOP = 251;
	/** 排序：上移 */
	static final int OP_SORT_UP = 252;
	/** 排序：下移 */
	static final int OP_SORT_DOWN = 253;
	/** 排序：移到底 */
	static final int OP_SORT_BOTTOM = 254;
	/** 排序：反转 */
	static final int OP_SORT_REVERSE = 255;
	/** 排序：取消排序 */
	static final int OP_SORT_CANCEL = 256;
	/** 修改多条数据： */
	static final int OP_UPDATE_ROWS = 262;
	/** 移除多条数据：即逻辑删除 */
	static final int OP_REMOVE_ROWS = 263;
	/** 删除多条数据： 即物理删除、永久删除 */
	static final int OP_DELETE_ROWS = 269;
	/** 处理多条数据：通过业务插件处理数据 */
	static final int OP_RUN_ROWS = 281;

	/*
	 * 基于“查询表达式”的操作（不打开表单）：291-300 不能作为行操作
	 */
	/** 处理GRID数据：通过业务插件处理数据 */
	static final int OP_RUN = 291;
	/** 清空数据表 */
	static final int OP_CLEAR = 300;

}
