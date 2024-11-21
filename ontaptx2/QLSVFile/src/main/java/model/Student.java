/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.io.Serializable;

public class Student implements Serializable {
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

    public String getId() { return id; }
    public String getName() { return name; }
    public String getMajor() { return major; }
    public String getLanguage() { return language; }
    public float getGpa() { return gpa; }

    @Override
    public String toString() {
        return String.format("%-15s%-20s%-15s%-15s%-10.2f", id, name, major, language, gpa);
    }
}

