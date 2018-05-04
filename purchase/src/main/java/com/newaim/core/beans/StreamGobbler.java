package com.newaim.core.beans;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class StreamGobbler extends Thread {
	InputStream is;
	String type;

	public StreamGobbler(InputStream is, String type) {
		this.is = is;
		this.type = type;
	}

	public void run() {
		BufferedReader br = null;
		try {
			InputStreamReader isr = new InputStreamReader(is);
			br = new BufferedReader(isr);
			String line = null;
			//这里并没有对流的内容进行处理，只是读了一遍
			while ((line = br.readLine()) != null){
				//TODO line 可以做一些写入文件的内容
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
