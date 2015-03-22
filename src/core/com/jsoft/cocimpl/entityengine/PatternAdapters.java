package com.jsoft.cocimpl.entityengine;

import com.jsoft.cocit.entityengine.PatternAdapter;

public interface PatternAdapters {
	PatternAdapter getAdapter(String pattern);

	void addAdapter(PatternAdapter adapter);
}
