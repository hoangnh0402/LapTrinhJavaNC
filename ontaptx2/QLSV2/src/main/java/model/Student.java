package model;

public class Student {
    private String id;
    private String name;
    private String major;
    private String language;
    private float gpa;

    public Student(String id, String name, String major, String language, float gpa) {
        this.id = id;
        this.name = name;
        this.major = major;
        this.language = language;
        this.gpa = gpa;
    }

    // Getter và setter cho các thuộc tính
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

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public float getGpa() {
        return gpa;
    }

    public void setGpa(float gpa) {
        this.gpa = gpa;
    }

    @Override
    public String toString() {
        return "ID: " + id + ", Name: " + name + ", Major: " + major + ", Language: " + language + ", GPA: " + gpa;
    }
}
