package com.snap252.vaadin.pivot.xml.renderers;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.jsoup.nodes.Element;

import com.vaadin.data.Property;
import com.vaadin.data.Validator;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.server.ClientMethodInvocation;
import com.vaadin.server.ErrorHandler;
import com.vaadin.server.Extension;
import com.vaadin.server.Resource;
import com.vaadin.server.ServerRpcManager;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinResponse;
import com.vaadin.shared.communication.SharedState;
import com.vaadin.ui.Field;
import com.vaadin.ui.HasComponents;
import com.vaadin.ui.UI;
import com.vaadin.ui.declarative.DesignContext;

import elemental.json.JsonObject;

@NonNullByDefault({})
public interface DefaultField<T> extends Field<T> {

	@Override
	default String getStyleName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	default void setStyleName(final String style) {
		// TODO Auto-generated method stub

	}

	@Override
	default void addStyleName(final String style) {
		// TODO Auto-generated method stub
	}

	@Override
	default void removeStyleName(final String style) {
		// TODO Auto-generated method stub

	}

	@Override
	default String getPrimaryStyleName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	default void setPrimaryStyleName(final String style) {
		// TODO Auto-generated method stub

	}

	@Override
	default boolean isEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	default void setEnabled(final boolean enabled) {
		// TODO Auto-generated method stub

	}

	@Override
	default boolean isVisible() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	default void setVisible(final boolean visible) {
		// TODO Auto-generated method stub

	}

	@Override
	default void setParent(final HasComponents parent) {
		// TODO Auto-generated method stub

	}

	@Override
	default HasComponents getParent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	default boolean isReadOnly() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	default void setReadOnly(final boolean readOnly) {
		// TODO Auto-generated method stub

	}

	@Override
	default String getCaption() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	default void setCaption(final String caption) {
		// TODO Auto-generated method stub

	}

	@Override
	default Resource getIcon() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	default void setIcon(final Resource icon) {
		// TODO Auto-generated method stub

	}

	@Override
	default UI getUI() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	default void attach() {
		// TODO Auto-generated method stub

	}

	@Override
	default Locale getLocale() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	default void setId(final String id) {
		// TODO Auto-generated method stub

	}

	@Override
	default String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	default String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	default void readDesign(final Element design, final DesignContext designContext) {
		// TODO Auto-generated method stub

	}

	@Override
	default void writeDesign(final Element design, final DesignContext designContext) {
		// TODO Auto-generated method stub

	}

	@Override
	default void addListener(final Listener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	default void removeListener(final Listener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	default void addAttachListener(final AttachListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	default void removeAttachListener(final AttachListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	default void addDetachListener(final DetachListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	default void removeDetachListener(final DetachListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	default List<ClientMethodInvocation> retrievePendingRpcCalls() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	default boolean isConnectorEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	default Class<? extends SharedState> getStateType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	default void requestRepaint() {
		// TODO Auto-generated method stub

	}

	@Override
	default void markAsDirty() {
		// TODO Auto-generated method stub

	}

	@Override
	default void requestRepaintAll() {
		// TODO Auto-generated method stub

	}

	@Override
	default void markAsDirtyRecursive() {
		// TODO Auto-generated method stub

	}

	@Override
	default boolean isAttached() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	default void detach() {
		// TODO Auto-generated method stub

	}

	@Override
	default Collection<Extension> getExtensions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	default void removeExtension(final Extension extension) {
		// TODO Auto-generated method stub

	}

	@Override
	default void beforeClientResponse(final boolean initial) {
		// TODO Auto-generated method stub

	}

	@Override
	default JsonObject encodeState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	default boolean handleConnectorRequest(final VaadinRequest request, final VaadinResponse response,
			final String path) throws IOException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	default ServerRpcManager<?> getRpcManager(final String rpcInterfaceName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	default ErrorHandler getErrorHandler() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	default void setErrorHandler(final ErrorHandler errorHandler) {
		// TODO Auto-generated method stub

	}

	@Override
	default String getConnectorId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	default float getWidth() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	default float getHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	default Unit getWidthUnits() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	default Unit getHeightUnits() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	default void setHeight(final String height) {
		// TODO Auto-generated method stub

	}

	@Override
	default void setWidth(final float width, final Unit unit) {
		// TODO Auto-generated method stub

	}

	@Override
	default void setHeight(final float height, final Unit unit) {
		// TODO Auto-generated method stub

	}

	@Override
	default void setWidth(final String width) {
		// TODO Auto-generated method stub

	}

	@Override
	default void setSizeFull() {
		// TODO Auto-generated method stub

	}

	@Override
	default void setSizeUndefined() {
		// TODO Auto-generated method stub

	}

	@Override
	default void setWidthUndefined() {
		// TODO Auto-generated method stub

	}

	@Override
	default void setHeightUndefined() {
		// TODO Auto-generated method stub

	}

	@Override
	default boolean isInvalidCommitted() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	default void setInvalidCommitted(final boolean isCommitted) {
		// TODO Auto-generated method stub

	}

	@Override
	default void commit() throws SourceException, InvalidValueException {
		// TODO Auto-generated method stub

	}

	@Override
	default void discard() throws SourceException {
		// TODO Auto-generated method stub

	}

	@Override
	default void setBuffered(final boolean buffered) {
		// TODO Auto-generated method stub

	}

	@Override
	default boolean isBuffered() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	default boolean isModified() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	default void addValidator(final Validator validator) {
		// TODO Auto-generated method stub

	}

	@Override
	default void removeValidator(final Validator validator) {
		// TODO Auto-generated method stub

	}

	@Override
	default void removeAllValidators() {
		// TODO Auto-generated method stub

	}

	@Override
	default Collection<Validator> getValidators() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	default boolean isValid() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	default void validate() throws InvalidValueException {
		// TODO Auto-generated method stub

	}

	@Override
	default boolean isInvalidAllowed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	default void setInvalidAllowed(final boolean invalidValueAllowed) throws UnsupportedOperationException {
		// TODO Auto-generated method stub

	}

	@Override
	default void addListener(final com.vaadin.data.Property.@NonNull ValueChangeListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	default void removeValueChangeListener(final com.vaadin.data.Property.@NonNull ValueChangeListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	default void removeListener(final com.vaadin.data.Property.@NonNull ValueChangeListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	default void valueChange(final com.vaadin.data.Property.ValueChangeEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	default void setPropertyDataSource(final Property newDataSource) {
		// TODO Auto-generated method stub

	}

	@Override
	default Property getPropertyDataSource() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	default int getTabIndex() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	default void setTabIndex(final int tabIndex) {
		// TODO Auto-generated method stub

	}

	@Override
	default boolean isRequired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	default void setRequired(final boolean required) {
		// TODO Auto-generated method stub

	}

	@Override
	default void setRequiredError(final String requiredMessage) {
	}

	@Override
	default String getRequiredError() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	default boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	default void clear() {
		// TODO Auto-generated method stub

	}

}
