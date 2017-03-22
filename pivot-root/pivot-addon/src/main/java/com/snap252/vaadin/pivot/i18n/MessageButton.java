package com.snap252.vaadin.pivot.i18n;

import com.vaadin.server.Resource;
import com.vaadin.ui.Button;

public class MessageButton extends Button {

	private final String caption;

	public MessageButton(final String caption, final ClickListener listener) {
		super(caption, listener);
		this.caption = caption;
	}

	public MessageButton(final String caption, final Resource icon) {
		super(caption, icon);
		this.caption = caption;
	}

	public MessageButton(final String caption) {
		super(caption);
		this.caption = caption;
	}

	@Override
	public void attach() {
		super.attach();
		super.setCaption(Labels.getString(caption, this));
	}
}
