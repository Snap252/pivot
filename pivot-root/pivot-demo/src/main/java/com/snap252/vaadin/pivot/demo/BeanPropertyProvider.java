package com.snap252.vaadin.pivot.demo;

import static java.util.stream.Collectors.toList;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.snap252.vaadin.pivot.Property;
import com.snap252.vaadin.pivot.PropertyProvider;
import com.snap252.vaadin.pivot.demo.BeanPropertyProvider.BeanProperty;
import com.vaadin.data.util.BeanUtil;

@NonNullByDefault
public class BeanPropertyProvider<T> extends PropertyProvider<T, BeanProperty<T>> {
	private List<BeanProperty<T>> beanPropertyDescriptor;

	static class BeanProperty<T> extends Property<T> {

		private Method readMethod;

		public BeanProperty(PropertyDescriptor p) {
			super(p.getPropertyType(), p.getDisplayName());
			Method readMethod = p.getReadMethod();
			assert readMethod != null;
			assert readMethod.getParameterCount() == 0;
			this.readMethod = readMethod;
		}

		@Override
		public Object getValue(T o) {
			try {
				return readMethod.invoke(o);
			} catch (ReflectiveOperationException | IllegalArgumentException e) {
				throw new AssertionError(e);
			}
		}

	}

	public BeanPropertyProvider(Class<T> clazz) {
		try {
			beanPropertyDescriptor = BeanUtil.getBeanPropertyDescriptor(clazz).stream().map(BeanProperty<T>::new)
					.collect(toList());
		} catch (IntrospectionException e) {
			throw new AssertionError(e);
		}
	}

	@Override
	public Collection<BeanProperty<T>> getProperties() {
		return beanPropertyDescriptor;
	}

	@Override
	public Stream<T> getItems() {
		// TODO Auto-generated method stub
		return null;
	}
}
