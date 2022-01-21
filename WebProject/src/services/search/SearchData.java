package services.search;

public class SearchData {
	private String name;
	private String lastName;
	private String start;
	private String end;

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "SearchData [name=" + name + ", lastName=" + lastName + ", start=" + start + ", end=" + end + "]";
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}
}
