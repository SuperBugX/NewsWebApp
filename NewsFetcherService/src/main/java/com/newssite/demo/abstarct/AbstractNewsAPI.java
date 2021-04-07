package com.newssite.demo.abstarct;

public abstract class AbstractNewsAPI {

	public static String commaSeperateArray(Object[] arrayObj) {
		String newString = "";
		if (arrayObj != null) {
			for (int i = 0; i < arrayObj.length; i++) {

				newString += arrayObj[i];

				if (i != arrayObj.length - 1) {
					newString += ",";
				}
			}
		}
		return newString;
	}
}
