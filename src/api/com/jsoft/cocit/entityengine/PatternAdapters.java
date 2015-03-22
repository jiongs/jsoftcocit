package com.jsoft.cocit.entityengine;


public interface PatternAdapters {
	PatternAdapter getAdapter(String pattern);

	void addAdapter(PatternAdapter adapter);
}
