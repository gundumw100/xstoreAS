package com.base.util;

import java.util.ArrayList;
import java.util.Collection;

/**
 * 
 * 不包含重复元素的ArrayList
 * 
 * @author pythoner
 *
 * @param <E>
 */
public class NoDuplicatesArrayList<E> extends ArrayList<E> {

    public NoDuplicatesArrayList() {
    }

    public NoDuplicatesArrayList(final Collection<? extends E> collection) {
        addAll(collection);
    }

    public NoDuplicatesArrayList(final int capacity) {
        super(capacity);
    }

    @Override
    public boolean add(final E e) {
        return !contains(e) && super.add(e);
    }

    @Override
    public void add(final int index, final E element) {
        if (!contains(element)) {
            super.add(index, element);
        }
    }

    @Override
    public boolean addAll(final Collection<? extends E> collection) {
        final Collection<E> copy = new ArrayList<E>(collection);
        copy.removeAll(this);
        return super.addAll(copy);
    }

    @Override
    public boolean addAll(final int index, final Collection<? extends E> collection) {
        final Collection<E> copy = new ArrayList<E>(collection);
        copy.removeAll(this);
        return super.addAll(index, copy);
    }
}
