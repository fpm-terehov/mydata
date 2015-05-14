package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class MessageStorage {
	private static final List<Message> INSTANSE = Collections.synchronizedList(new ArrayList<Message>());

	private MessageStorage() {
	}

	public static List<Message> getStorage() {
		return INSTANSE;
	}

	public static void addMess(Message mess) {
		INSTANSE.add(mess);
	}

	public static void addAll(Message[] messages) {
		INSTANSE.addAll(Arrays.asList(messages));
	}
	
	public static void addAll(List<Message> messages) {
		INSTANSE.addAll(messages);
	}

	public static int getSize() {
		return INSTANSE.size();
	}

	public static List<Message> getSubMessagesByIndex(int index) {
		return INSTANSE.subList(index, INSTANSE.size());
	}

	public static Message getMessById(String id) {
		for (Message mess : INSTANSE) {
			if (mess.getId().equals(id)) {
				return mess;
			}
		}
		return null;
	}

}
