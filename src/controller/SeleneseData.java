package controller;

import java.util.ArrayList;

public class SeleneseData {
	private String baseurl = null;
	private String navigateurl=null;	
	private String step = null;
	private String command = null;
	private String target = null;
	private String value = null;
	private String message = null;
	private boolean exstatus = false;
	private int x = 0;
	private int y = 0;
	private int height = 0;
	private int width = 0;
	private String color = "";
	private String fontFamily = "";
	private String fontSize = "";
	private String fontWeight = "";
	private String fontStyle = "";
	private String screenshotPath="";
	private ArrayList<String> targetList = null;
	ArrayList<String> parameter_Name_ValueList = null;

	public SeleneseData(String baseurl, String step, String command, String target,
			String value, boolean exstatus, int x, int y, int height, int width) {
		this.baseurl = baseurl;
		this.step = step;
		this.command = command;
		this.target = target;
		this.value = value;
		this.exstatus = exstatus;
		this.x = x;
		this.y = y;
		this.height = height;
		this.width = width;
		this.targetList = new ArrayList<String>();
		this.parameter_Name_ValueList = new ArrayList<String>();
	}

	public SeleneseData() {
		this.baseurl = "";
		this.step = "";
		this.command = "";
		this.target = "";
		this.value = "";
		this.exstatus = false;
		this.targetList = new ArrayList<String>();
		this.parameter_Name_ValueList = new ArrayList<String>();
		this.fontFamily = "";
		this.fontSize = "";
		this.fontStyle = "";
		this.fontWeight = "";
		this.color = "";
		this.screenshotPath="";
	}

	public String getNavigateurl() {
		return navigateurl;
	}

	public void setNavigateurl(String navigateurl) {
		this.navigateurl = navigateurl;
	}

	public ArrayList<String> getParameter_Name_ValueList() {
		return this.parameter_Name_ValueList;
	}

	public void setParameter_Name_ValueList(
			ArrayList<String> parameter_Name_ValueList) {
		this.parameter_Name_ValueList = parameter_Name_ValueList;
	}

	public ArrayList<String> getTargetList() {
		return this.targetList;
	}

	public void setTargetList(ArrayList<String> targetList) {
		this.targetList = targetList;
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getBaseurl() {
		return this.baseurl;
	}

	public void setBaseurl(String baseurl) {
		this.baseurl = baseurl;
	}

	public String getStep() {
		return this.step;
	}

	public void setStep(String step) {
		this.step = step;
	}

	public String getCommand() {
		return this.command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getTarget() {
		return this.target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getValue() {
		return this.value;
	}

	public boolean isExstatus() {
		return this.exstatus;
	}

	public void setExstatus(boolean exstatus) {
		this.exstatus = exstatus;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public int getX() {
		return this.x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return this.y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getHeight() {
		return this.height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return this.width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public String getColor() {
		return this.color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getFontFamily() {
		return this.fontFamily;
	}

	public void setFontFamily(String fontFamily) {
		this.fontFamily = fontFamily;
	}

	public String getFontSize() {
		return this.fontSize;
	}

	public void setFontSize(String fontSize) {
		this.fontSize = fontSize;
	}

	public String getFontWeight() {
		return this.fontWeight;
	}

	public void setFontWeight(String fontWeight) {
		this.fontWeight = fontWeight;
	}

	public String getFontStyle() {
		return this.fontStyle;
	}

	public void setFontStyle(String fontStyle) {
		this.fontStyle = fontStyle;
	}

	public String getScreenshotPath() {
		return screenshotPath;
	}

	public void setScreenshotPath(String screenshotPath) {
		this.screenshotPath = screenshotPath;
	}
}
