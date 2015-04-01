package com.jsoft.cocit.constant;

public interface ViewNames {
	/**
	 * 模块操作按钮：普通按钮(BUTTON标签)，不支持小图标、不支持下拉。
	 */
	String VIEW_ACTIONS_BUTTON = "button";
	/**
	 * 模块操作按钮：圆弧形风格(A标签 class="jCocit-ui jCocit-button")，支持小图标、不支持下拉。
	 */
	String VIEW_ACTIONS_LINKBUTTON = "linkbutton";
	/**
	 * 模块操作按钮：工具栏风格(A 标签 class="jCocit-ui jCocit-toolbar")，支持小图标、支持下拉。
	 */
	String VIEW_ACTIONS_TOOLBAR = "toolbar";

	String VIEW_MAINS = "mains";
	String VIEW_MAIN = "main";
	String VIEW_FORM = "form";
	String VIEW_FORM_BUTTONS = "formbuttons";
	String VIEW_FORM_FIELDS = "formfields";
	String VIEW_SUBFORM = "subform";
	String VIEW_FORMDATA = "formdata";
	String VIEW_GRID = "grid";
	String VIEW_GRIDDATA = "griddata";
	String VIEW_TREEGRID = "treegrid";
	String VIEW_TREEGRIDDATA = "treegriddata";
	String VIEW_SEARCHBOX = "searchbox";
	String VIEW_SEARCHFORM = "searchform";
	String VIEW_TREE = "tree";
	String VIEW_TREEDATA = "treedata";
	String VIEW_JSP = "jsp";
	String VIEW_SMARTY = "smarty";
	String VIEW_JSON = "json";
	String VIEW_LIST = "list";
	String VIEW_LISTDATA = "listdata";
	String VIEW_XML = "xml";
	String VIEW_HTML = "html";

	/*
	 * 
	 */
	String FIELD_VIEW_COMBODATE = "combodate";
	String FIELD_VIEW_COMBODATETIME = "combodatetime";
	String FIELD_VIEW_TEXTAREA = "textarea";
	String FIELD_VIEW_TEXT = "text";
	String FIELD_VIEW_SELECT = "select";
	String FIELD_VIEW_RADIO = "radio";
	String FIELD_VIEW_COMBOBOX = "combobox";
	String FIELD_VIEW_NUMBERBOX = "numberbox";
	String FIELD_VIEW_COMBOGRID = "combogrid";
	String FIELD_VIEW_COMBOTREE = "combotree";
	String FIELD_VIEW_CKEDITOR = "richtext";
	String FIELD_VIEW_UPLOAD = "upload";
	String FIELD_VIEW_DIC = "select";
	String FIELD_VIEW_PASSWORD = "password";
	String FIELD_VIEW_ONE2MANY = "one2many";
	String FIELD_VIEW_DEFAULT = FIELD_VIEW_TEXT;

	/*
	 * 
	 */
	String CELL_VIEW_LINK_TO_FORM = "linkToForm";
	String CELL_VIEW_LINK = "link";
	String CELL_VIEW_BUTTON_FOR_ROWACTIONS = "rowactions-button";
	String CELL_VIEW_LINK_FOR_ROWACTIONS = "rowactions-link";
	String CELL_VIEW_STYLE = "style";
}
