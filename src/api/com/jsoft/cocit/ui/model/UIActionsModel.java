package com.jsoft.cocit.ui.model;

import java.io.Writer;
import java.util.List;

import com.jsoft.cocit.util.Tree;

public interface UIActionsModel {
	public Tree getData();

	public void render(Writer out) throws Exception;

	public void addResultUI(String resultUI);

	/**
	 * 操作执行完毕后，将在哪些UI上刷新结果？
	 * 
	 * @return
	 */
	public List<String> getResultUI();

	public <T> T get(String propName, T defaultReturn);

	public <T> T get(String propName);

}
