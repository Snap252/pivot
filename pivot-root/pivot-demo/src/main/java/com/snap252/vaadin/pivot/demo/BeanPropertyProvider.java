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
public class BeanPropertyProvider<INPUT_TYPE> extends PropertyProvider<INPUT_TYPE, BeanProperty<INPUT_TYPE, ?>> {
	private List<BeanProperty<INPUT_TYPE, ?>> beanPropertyDescriptor;

	static class BeanProperty<INPUT_TYPE, OUTPUT_TYPE> extends Property<INPUT_TYPE, OUTPUT_TYPE> {

		private Method readMethod;

		public BeanProperty(PropertyDescriptor p) {
			super((Class<@Nullable OUTPUT_TYPE>) p.getPropertyType(), p.getDisplayName());
			Method readMethod = p.getReadMethod();
			assert readMethod != null;
			assert readMethod.getParameterCount() == 0;
			this.readMethod = readMethod;
		}

		@Override
		public OUTPUT_TYPE getValue(INPUT_TYPE o) {
			try {
				return (OUTPUT_TYPE) readMethod.invoke(o);
			} catch (ReflectiveOperationException | IllegalArgumentException e) {
				throw new AssertionError(e);
			}
		}

	}

	public BeanPropertyProvider(Class<INPUT_TYPE> clazz) {
		try {
			beanPropertyDescriptor = BeanUtil.getBeanPropertyDescriptor(clazz).stream()
					.map(BeanProperty<INPUT_TYPE, Object>::new).collect(toList());
		} catch (IntrospectionException e) {
			throw new AssertionError(e);
		}
	}

	@Override
	public Collection<BeanProperty<INPUT_TYPE, ?>> getProperties() {
		return beanPropertyDescriptor;
	}

	@Override
	public Stream<INPUT_TYPE> getItems() {
		// TODO Auto-generated method stub
		return null;
	}
}
