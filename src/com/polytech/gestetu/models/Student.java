package com.polytech.gestetu.models;

import java.util.List;

/**
 * Model for managing student
 */

public class Student {

	private String id;
	private String lastname; //nom
	private List<Test> lstTest;
	private String firstname; //prenom
	private String email;
	private String numStu;
	private Promotion promotion;
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getLastname() {
		return lastname;
	}
	
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	
	public List<Test> getLstTest() {
		return lstTest;
	}
	
	public void setLstTest(List<Test> lstTest) {
		this.lstTest = lstTest;
	}
	
	public String getFirstname() {
		return firstname;
	}
	
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getNumStu() {
		return numStu;
	}
	
	public void setNumStu(String numStu) {
		this.numStu = numStu;
	}
	
	public Promotion getPromotion() {
		return promotion;
	}
	
	public void setPromotion(Promotion promotion) {
		this.promotion = promotion;
	}
}
