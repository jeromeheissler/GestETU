package com.polytech.gestetu.services;

import java.util.Comparator;

public class ComparableAlphabet implements Comparator<String> {
	 
    @Override
    public int compare(String string1, String string2) {
        return (string1.toString().compareTo(string2.toString()));
    }
}