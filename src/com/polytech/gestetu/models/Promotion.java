package com.polytech.gestetu.models;

/**
 * Model for managing promotion
 */

public class Promotion {

	private String id;	
	private int annee;
	private String label;
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	public int getAnnee() {
		return annee;
	}

	public void setAnnee(int annee) {
		this.annee = annee;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
}
