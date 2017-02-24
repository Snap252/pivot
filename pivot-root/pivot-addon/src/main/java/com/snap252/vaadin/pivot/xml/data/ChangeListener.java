package com.snap252.vaadin.pivot.xml.data;

@FunctionalInterface
public interface ChangeListener<T> {
    void changed(T whatChanged);
}