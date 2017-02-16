package com.oscar.clusterlob.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class IOUtil {
	public static InputStream getFileInputStream(String path){
		File file = new File(path);
		InputStream stream = null;
		try {
			stream = new BufferedInputStream(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return stream;
	}
	
	public static OutputStream getFileOutputStream(String path){
		File file = new File(path);
		OutputStream stream = null;
		try {
			stream = new BufferedOutputStream(new FileOutputStream(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return stream;
	}
}
