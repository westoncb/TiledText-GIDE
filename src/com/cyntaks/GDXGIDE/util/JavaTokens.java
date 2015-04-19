package com.cyntaks.GDXGIDE.util;

import java.util.HashMap;

import com.cyntaks.GDXGIDE.code.java.JavaLexer;

public class JavaTokens {
	private static HashMap<Integer, String> codes = new HashMap<Integer, String>();
	
	static {
		codes.put(JavaLexer.IDENTIFIER, "IDENTIFIER");
	}
	
	
	public static String getTokenNameFromCode(int code) {
		return codes.get(code);
	}
}
