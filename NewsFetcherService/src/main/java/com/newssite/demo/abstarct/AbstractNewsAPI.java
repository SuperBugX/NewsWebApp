package com.newssite.demo.abstarct;

public abstract class AbstractNewsAPI {

	//Method converts an array of String into a single string of comma separated values
	public static String commaSeperateArray(String[] stringArray) {
		String newString = "";
		if (stringArray != null) {
			for (int i = 0; i < stringArray.length; i++) {

				newString += stringArray[i];

				if (i != stringArray.length - 1) {
					newString += ",";
				}
			}
		}
		return newString;
	}
}
