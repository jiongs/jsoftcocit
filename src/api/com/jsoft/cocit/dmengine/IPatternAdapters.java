package com.jsoft.cocit.dmengine;


public interface IPatternAdapters {
	IPatternAdapter getAdapter(String pattern);

	void addAdapter(IPatternAdapter adapter);
}
