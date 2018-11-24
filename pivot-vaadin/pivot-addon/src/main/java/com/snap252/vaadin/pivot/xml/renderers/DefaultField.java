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
		return null;
	}

	@Override
	default void setStyleName(final String style) {

	}

	@Override
	default void addStyleName(final String style) {
	}

	@Override
	default void removeStyleName(final String style) {

	}

	@Override
	default String getPrimaryStyleName() {
		return null;
	}

	@Override
	default void setPrimaryStyleName(final String style) {

	}

	@Override
	default boolean isEnabled() {
		return false;
	}

	@Override
	default void setEnabled(final boolean enabled) {

	}

	@Override
	default boolean isVisible() {
		return false;
	}

	@Override
	default void setVisible(final boolean visible) {
	}

	@Override
	default void setParent(final HasComponents parent) {

	}

	@Override
	default HasComponents getParent() {
		return null;
	}

	@Override
	default boolean isReadOnly() {
		return false;
	}

	@Override
	default void setReadOnly(final boolean readOnly) {

	}

	@Override
	default String getCaption() {
		return null;
	}

	@Override
	default void setCaption(final String caption) {
	}

	@Override
	default Resource getIcon() {
		return null;
	}

	@Override
	default void setIcon(final Resource icon) {
	}

	@Override
	default UI getUI() {
		return null;
	}

	@Override
	default void attach() {
	}

	@Override
	default Locale getLocale() {
		return null;
	}

	@Override
	default void setId(final String id) {
	}

	@Override
	default String getId() {
		return null;
	}

	@Override
	default String getDescription() {
		return null;
	}

	@Override
	default void readDesign(final Element design, final DesignContext designContext) {
	}

	@Override
	default void writeDesign(final Element design, final DesignContext designContext) {
	}

	@Override
	default void addListener(final Listener listener) {
	}

	@Override
	default void removeListener(final Listener listener) {
	}

	@Override
	default void addAttachListener(final AttachListener listener) {
	}

	@Override
	default void removeAttachListener(final AttachListener listener) {

	}

	@Override
	default void addDetachListener(final DetachListener listener) {

	}

	@Override
	default void removeDetachListener(final DetachListener listener) {

	}

	@Override
	default List<ClientMethodInvocation> retrievePendingRpcCalls() {

		return null;
	}

	@Override
	default boolean isConnectorEnabled() {

		return false;
	}

	@Override
	default Class<? extends SharedState> getStateType() {

		return null;
	}

	@Override
	default void requestRepaint() {

	}

	@Override
	default void markAsDirty() {

	}

	@Override
	default void requestRepaintAll() {

	}

	@Override
	default void markAsDirtyRecursive() {

	}

	@Override
	default boolean isAttached() {

		return false;
	}

	@Override
	default void detach() {

	}

	@Override
	default Collection<Extension> getExtensions() {

		return null;
	}

	@Override
	default void removeExtension(final Extension extension) {

	}

	@Override
	default void beforeClientResponse(final boolean initial) {

	}

	@Override
	default JsonObject encodeState() {

		return null;
	}

	@Override
	default boolean handleConnectorRequest(final VaadinRequest request, final VaadinResponse response,
			final String path) throws IOException {

		return false;
	}

	@Override
	default ServerRpcManager<?> getRpcManager(final String rpcInterfaceName) {

		return null;
	}

	@Override
	default ErrorHandler getErrorHandler() {

		return null;
	}

	@Override
	default void setErrorHandler(final ErrorHandler errorHandler) {

	}

	@Override
	default String getConnectorId() {

		return null;
	}

	@Override
	default float getWidth() {

		return 0;
	}

	@Override
	default float getHeight() {

		return 0;
	}

	@Override
	default Unit getWidthUnits() {

		return null;
	}

	@Override
	default Unit getHeightUnits() {

		return null;
	}

	@Override
	default void setHeight(final String height) {

	}

	@Override
	default void setWidth(final float width, final Unit unit) {

	}

	@Override
	default void setHeight(final float height, final Unit unit) {

	}

	@Override
	default void setWidth(final String width) {

	}

	@Override
	default void setSizeFull() {

	}

	@Override
	default void setSizeUndefined() {

	}

	@Override
	default void setWidthUndefined() {

	}

	@Override
	default void setHeightUndefined() {

	}

	@Override
	default boolean isInvalidCommitted() {

		return false;
	}

	@Override
	default void setInvalidCommitted(final boolean isCommitted) {

	}

	@Override
	default void commit() throws SourceException, InvalidValueException {

	}

	@Override
	default void discard() throws SourceException {

	}

	@Override
	default void setBuffered(final boolean buffered) {

	}

	@Override
	default boolean isBuffered() {

		return false;
	}

	@Override
	default boolean isModified() {

		return false;
	}

	@Override
	default void addValidator(final Validator validator) {

	}

	@Override
	default void removeValidator(final Validator validator) {

	}

	@Override
	default void removeAllValidators() {

	}

	@Override
	default Collection<Validator> getValidators() {

		return null;
	}

	@Override
	default boolean isValid() {

		return false;
	}

	@Override
	default void validate() throws InvalidValueException {

	}

	@Override
	default boolean isInvalidAllowed() {

		return false;
	}

	@Override
	default void setInvalidAllowed(final boolean invalidValueAllowed) throws UnsupportedOperationException {

	}

	@Override
	default void addListener(final com.vaadin.data.Property.@NonNull ValueChangeListener listener) {

	}

	@Override
	default void removeValueChangeListener(final com.vaadin.data.Property.@NonNull ValueChangeListener listener) {

	}

	@Override
	default void removeListener(final com.vaadin.data.Property.@NonNull ValueChangeListener listener) {

	}

	@Override
	default void valueChange(final com.vaadin.data.Property.ValueChangeEvent event) {

	}

	@Override
	default void setPropertyDataSource(@SuppressWarnings("rawtypes") final Property newDataSource) {
	}

	@SuppressWarnings("rawtypes")
	@Override
	default Property getPropertyDataSource() {
		return null;
	}

	@Override
	default int getTabIndex() {

		return 0;
	}

	@Override
	default void setTabIndex(final int tabIndex) {

	}

	@Override
	default boolean isRequired() {
		return false;
	}

	@Override
	default void setRequired(final boolean required) {

	}

	@Override
	default void setRequiredError(final String requiredMessage) {
	}

	@Override
	default String getRequiredError() {

		return null;
	}

	@Override
	default boolean isEmpty() {

		return false;
	}

	@Override
	default void clear() {

	}

}
