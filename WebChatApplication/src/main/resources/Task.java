import java.util.Date;

class Task {
	private String id;
	private String description;
        private String name;
	private boolean done;
        private Date date;

	public Task(String id, String description, String name, boolean done) {
		this.id = id;
		this.description = description;
                this.name = name;
		this.done = done;
	}        
        public String getName() {
		return this.name;
	}
	public void setName(String value) {
		this.name = value;
	}      
        public Date getDate() {
		return this.date;
	}
	public void setDate(Date value) {
		this.date = value;
	} 
	public String getId() {
		return this.id;
	}
	public void setId(String value) {
		this.id = value;
	}

	public String getDescription() {
		return this.description;
	}
	public void setDescription(String value) {
		this.description = value;
	}

	public boolean getDone() {
		return this.done;
	}
	public void setDone(boolean value) {
		this.done = value;
	}

	public String toString() {
		return "{\"id\":\"" + this.id
                        + "\",\"name\":\"" + this.name
                        + "\",\"description\":\"" + this.description
                        + "\",\"done\":" + this.done + "}";
	}
}