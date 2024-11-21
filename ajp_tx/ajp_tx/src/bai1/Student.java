package bai1;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Student implements Serializable {
	private String name;
	private String studentCode;
	private String major;
	private String language;
	private float gpa;

	public Student() {

	}

	public Student(String name, String studentCode, String major, String language, float gpa) {
		this.name = name;
		this.studentCode = studentCode;
		this.major = major;
		this.language = language;
		this.gpa = gpa;
	}

	public String getName() {
		return name;
	}

	public String getStudentCode() {
		return studentCode;
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

	public void setName(String name) {
		this.name = name;
	}

	public void setStudentCode(String studentCode) {
		this.studentCode = studentCode;
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
		return String.format("%-20s %-10s %-15s %-10s %-5s%n", name, studentCode, major, language, gpa);
	}

}
