package util;

import model.Message;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public final class MessUtil {
	public static final String TOKEN = "token";
	public static final String MESSAGES = "messages";
	private static final String TN = "TN";
	private static final String EN = "EN";
	private static final String ID = "id";
	private static final String NAME = "name";
	private static final String DESCRIPTION = "description";
	private static final String DONE = "done";

	private MessUtil() {
	}

	public static String getToken(int index) {
		Integer number = index * 8 + 11;
		return TN + number + EN;
	}

	public static int getIndex(String token) {
		return (Integer.valueOf(token.substring(2, token.length() - 2)) - 11) / 8;
	}

	public static JSONObject stringToJson(String data) throws ParseException {
		JSONParser parser = new JSONParser();
		return (JSONObject) parser.parse(data.trim());
	}

	public static Message jsonToMessage(JSONObject json) {
		Object id = json.get(ID);
		Object name = json.get(NAME);
		Object description = json.get(DESCRIPTION);
		Object done = json.get(DONE);

		if (id != null && description != null && done != null && name != null) {
			return new Message((String) id, (String) name, (String) description, (Boolean) done);
		}
		return null;
	}
}
