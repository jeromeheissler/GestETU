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
	
	/** 
	 * Crée un string formaté en json
	 * @return string formaté en json
	 */
	public String toJSON(String token)
	{
		String result;

		result = "t=" + token + "&student=";
		result += "{\"firstname\":\"" + this.firstname + "\"";
		result += ",\"lastname\":\"" + this.lastname + "\"";
		result += ",\"numStu\":\"" + this.numStu + "\"";
		result += ",\"email\":\"" + this.email + "\"";
		result += ",\"promotion\":{";
		result += "\"id\":\"" + this.promotion.getId() + "\"";
		result += ",\"annee\":" + this.promotion.getAnnee();
		result += ",\"label\":" + this.promotion.getLabel() + "\"}";
		if (this.lstTest.size() == 0)
		{
			result += ",\"lstTest\":[]}";
		}
		else
		{
			//,"firstname":"Jerome","lastname":"Heissler","delete":false}}]}}
			
			result += ",\"lstTest\":[";
			for (int indexTest = 0; indexTest < this.lstTest.size(); indexTest++)
			{
				result += "{\"id\":\"" + this.lstTest.get(indexTest).getId() + "\"";
				result += ",\"subject\":{";
				result += "\"id\":\"" + this.lstTest.get(indexTest).getSubject().getId() + "\"";
				result += ",\"name\":\"" + this.lstTest.get(indexTest).getSubject().getName() + "\"}";
				result += ",\"note\":" + this.lstTest.get(indexTest).getNote();
				result += ",\"date\":null";
				result += ",\"teacher\":{";
				result += "\"id\":\"" + this.lstTest.get(indexTest).getTeacher().getId() + "\"";
				result += ",\"mail\":\"" + this.lstTest.get(indexTest).getTeacher().getMail() + "\"";
				result += ",\"firstname\":\"" + this.lstTest.get(indexTest).getTeacher().getFirstname() + "\"";
				result += ",\"lastname\":\"" + this.lstTest.get(indexTest).getTeacher().getLastname() + "\"";
				result += ",\"delete\":false}}";
			}
			
			result += "]}";
		}

		return result.toString();
	}
	
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
