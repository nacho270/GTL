package ar.com.textillevel.mobile.util.json;

import java.lang.reflect.Type;
import java.sql.Date;

import ar.com.textillevel.mobile.util.json.converter.SqlDateConverter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JSONParser {

	private static final String DATE_FORMAT = "dd/MM/yyyy hh:mm:ss";
	private static Gson gson;

	public static Gson getInstance() {
		if (gson == null) {
			GsonBuilder builder = new GsonBuilder();
			builder.setDateFormat(DATE_FORMAT);
			builder.registerTypeAdapter(Date.class, new SqlDateConverter(DATE_FORMAT));
			gson = builder.create();
		}
		return gson;
	}

	public static <T> T fromJson(String json, Class<T> classOfT) {
		return getInstance().fromJson(json, classOfT);
	}

	public static String toJson(Object obj) {
		return getInstance().toJson(obj);
	}

	/**
	 * Método que transforma un json a un objeto utilizando un TypeToken en
	 * lugar de un ClassType
	 * 
	 * @param <T>
	 * @param json
	 * @param type
	 * @return
	 */
	public static Object fromJson(String json, Type type) {
		return getInstance().fromJson(json, type);
	}

}
