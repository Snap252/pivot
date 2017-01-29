package com.snap252.vaadin.pivot.client;

import java.math.BigDecimal;

import com.vaadin.client.ApplicationConnection;
import com.vaadin.client.communication.JSONSerializer;
import com.vaadin.client.metadata.Type;

import elemental.json.Json;
import elemental.json.JsonValue;

public class BigDecimal_Serializer implements JSONSerializer<BigDecimal> {

	@Override
	public BigDecimal deserialize(final Type type, final JsonValue jsonValue, final ApplicationConnection connection) {
		return new BigDecimal(jsonValue.asString());
	}

	@Override
	public JsonValue serialize(final BigDecimal value, final ApplicationConnection connection) {
		return Json.create(value.toString());
	}
}