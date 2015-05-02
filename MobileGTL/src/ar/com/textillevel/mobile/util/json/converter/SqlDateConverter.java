package ar.com.textillevel.mobile.util.json.converter;

import java.lang.reflect.Type;
import java.sql.Date;
import java.text.SimpleDateFormat;

import android.annotation.SuppressLint;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

@SuppressLint("SimpleDateFormat")
public class SqlDateConverter implements JsonSerializer<Date>, JsonDeserializer<Date> {

	private String format;

	public SqlDateConverter(String dateFormat) {
		this.format = dateFormat;
	}

	public JsonElement serialize(Date date, Type srcType, JsonSerializationContext context) {
		return new JsonPrimitive(new SimpleDateFormat(format).format(date));
	}

	public Date deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
		try {
			return new Date(new SimpleDateFormat(format).parse(json.getAsString()).getTime());
		} catch (Exception e) {
			return null;
		}
	}

}
