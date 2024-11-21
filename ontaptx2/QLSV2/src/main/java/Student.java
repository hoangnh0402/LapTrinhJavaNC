

import java.io.Serializable;

@SuppressWarnings("serial")
public class Student implements Serializable {
	private String id;
	private String name;
	private String major;
	private String language;
	private float gpa;

	public Student() {

	}

	public Student(String id, String name, String major, String language, float gpa) {
		this.id = id;
		this.name = name;
		this.major = major;
		this.language = language;
		this.gpa = gpa;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getMajor() {
		return major;
	}

	public String getLanguage() {
		return language;
	}

	public float getGpa() {
		return gpa;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setMajor(String major) {
		this.major = major;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public void setGpa(float gpa) {
		this.gpa = gpa;
	}

	@Override
	public String toString() {
		return String.format("%-10s %-20s %-15s %-10s %-5s%n", id, name, major, language, gpa);
	}

}
