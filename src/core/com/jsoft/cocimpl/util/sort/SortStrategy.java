package com.jsoft.cocimpl.util.sort;

import java.util.List;

public interface SortStrategy {
	public static final SortStrategy defaultStrategy = new InsertSort();

	public void sort(Object[] list, String field, boolean nullGT);

	public void sort(List list, String field, boolean nullGT);
}
