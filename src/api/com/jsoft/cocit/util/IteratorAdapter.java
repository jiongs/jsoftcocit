package com.jsoft.cocit.util;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class IteratorAdapter implements Iterator {
	private Enumeration e;

	public IteratorAdapter(Enumeration e) {
		this.e = e;
	}

	public boolean hasNext() {
		return e.hasMoreElements();
	}

	public Object next() {
		if (!e.hasMoreElements()) {
			throw new NoSuchElementException("IteratorAdaptor.next() has no more elements");
		}

		return e.nextElement();
	}

	public void remove() {
		throw new UnsupportedOperationException("Method IteratorAdaptor.remove() not implemented");
	}
}