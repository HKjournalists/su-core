package com.gettec.fsnip.fsn.util;

public class FileUtils {
	public static String getExtension(String filename) {
	    String ext = null;
	    String s = filename;
	    int i = s.lastIndexOf('.');

	    if (i > 0 &&  i < s.length() - 1) {
	        ext = s.substring(i+1).toLowerCase();
	    }
	    return ext;
	}
}
