package com.polytech.gestetu.models;

/**
 * Model for managing test
 */

public class Test {

	private String id;
	private Subject subject;
	private float note;
	private Teacher teacher;
	private String date;
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	public Subject getSubject() {
		return subject;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}

	public float getNote() {
		return note;
	}

	public void setNote(float note) {
		this.note = note;
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}
	
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
}
