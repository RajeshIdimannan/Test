package controller;

import java.util.ArrayList;

public class ExcelData {
	private String tracksheetname = null;
	private String testcasename = null;
	private String baseUrl = null;
	private String browserStart[] = null;
    
	private String timeout = null;
	private ArrayList<SeleneseData> seleneseData = null;

	public ExcelData(String tracksheetname, String testcasesheetname,
			String baseUrl, String browserStart[], String timeout,
			ArrayList<SeleneseData> seleneseData) {
		this.tracksheetname = tracksheetname;
		this.testcasename = testcasesheetname;
		this.baseUrl = baseUrl;
		this.browserStart = browserStart;
		this.timeout = timeout;
		this.setSeleneseData(seleneseData);
	}

	public ExcelData() {
	}

	public String getTestcasename() {
		return this.testcasename;
	}

	public void setTestcasename(String testcasename) {
		this.testcasename = testcasename;
	}

	public String getBaseUrl() {
		return this.baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public String[] getBrowserStart() {
		return this.browserStart;
	}

	public void setBrowserStart(String[] browserStart) {
		this.browserStart = browserStart;
	}

	public String getTimeout() {
		return this.timeout;
	}

	public void setTimeout(String timeout) {
		this.timeout = timeout;
	}

	public String getTracksheetname() {
		return tracksheetname;
	}

	public void setTracksheetname(String tracksheetname) {
		this.tracksheetname = tracksheetname;
	}

	public ArrayList<SeleneseData> getSeleneseData() {
		return seleneseData;
	}

	public void setSeleneseData(ArrayList<SeleneseData> seleneseData) {
		this.seleneseData = seleneseData;
	}
}
