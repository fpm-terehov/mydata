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

	public static void addTask(Message task) {
		INSTANSE.add(task);
	}

	public static void addAll(Message[] tasks) {
		INSTANSE.addAll(Arrays.asList(tasks));
	}
	
	public static void addAll(List<Message> tasks) {
		INSTANSE.addAll(tasks);
	}

	public static int getSize() {
		return INSTANSE.size();
	}

	public static List<Message> getSubTasksByIndex(int index) {
		return INSTANSE.subList(index, INSTANSE.size());
	}

	public static Message getTaskById(String id) {
		for (Message task : INSTANSE) {
			if (task.getId().equals(id)) {
				return task;
			}
		}
		return null;
	}

}
