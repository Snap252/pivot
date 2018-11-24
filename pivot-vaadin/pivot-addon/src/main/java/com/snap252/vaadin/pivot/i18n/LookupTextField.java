package com.snap252.vaadin.pivot.i18n;

import com.vaadin.ui.TextField;

public class LookupTextField extends TextField {

	private final String caption;
	
	public LookupTextField(String caption) {
		super(caption);
		this.caption = caption;
	}

	@Override
	public void attach() {
		super.attach();
		super.setCaption(Labels.getString(caption, this));
	}
}
