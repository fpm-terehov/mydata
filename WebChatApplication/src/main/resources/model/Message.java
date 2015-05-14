package model;

public class Message {
	private String id;
	private String name;
	private String description;
	private boolean done;

	public Message(String id, String name, String description, boolean done) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.done = done;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

        public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
        
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isDone() {
		return done;
	}

	public void setDone(boolean done) {
		this.done = done;
	}

	public String toString() {
		return "{\"id\":\"" + this.id +
                        "\",\"name\":\"" + this.name + 
                        "\",\"description\":\"" + this.description + 
                        "\",\"done\":" + this.done + "}";
	}
}
