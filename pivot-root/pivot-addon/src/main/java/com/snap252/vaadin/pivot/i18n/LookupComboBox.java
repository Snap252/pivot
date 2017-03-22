package com.snap252.vaadin.pivot.i18n;

import java.util.Arrays;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.vaadin.ui.ComboBox;

public class LookupComboBox extends ComboBox {

	private final String caption;

	@SafeVarargs
	public <T extends Enum<T>> LookupComboBox(final String caption, final @NonNull T ... options) {
		super(caption, Arrays.asList(options));
		this.caption = caption;
	}

	@Override
	public @Nullable String getItemCaption(final Object itemId) {
		return Enums.getString((Enum<?>) itemId, this);
	}

	@Override
	public void attach() {
		super.attach();
		setCaption(Labels.getString(caption, this));
	}
}
