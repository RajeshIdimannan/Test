package controller;

import java.io.IOException;

public class VisualBasic {
	public static void main(String Args[]) {
		try {
			Runtime.getRuntime().exec("wscript C:\\macro1.vbs");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
