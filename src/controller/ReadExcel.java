/*****************************************
Purpose: Reads Data from input Excel file and writes data onto output excel after script execution.
Version: 1.0
Author: X-Browser Automation Team, TCS
Main Function: readExcel(),writeExcel(),mainControll()
Pre-requisites: Input and output excel file path from MainController class. 
 *****************************************/

package controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import jxl.*;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.format.VerticalAlignment;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableHyperlink;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class ReadExcel {

	private String inputFile, outputFile,iOSProxy, screenFlag;
	private String[] Browsers;
	
	public String getiOSProxy() {
		return iOSProxy;
	}

	public void setiOSProxy(String iOSProxy) {
		this.iOSProxy = iOSProxy;
	}
	
	public String getScreenFlag() {
		return screenFlag;
	}

	public void setScreenFlag(String screenFlag) {
		this.screenFlag = screenFlag;
	}

	/*****************************************************************************************************
	 * Purpose: Setter method to set input file path. Return Value: Nil.
	 * Callers:readExcel, mainControll
	 *****************************************************************************************************/
	public void setInputFile(String inputFile) {
		this.inputFile = inputFile;
	}

	/*****************************************************************************************************
	 * Purpose: Getter method to get input file path. Return Value: returns
	 * input file path. Callers:readExcel, mainControll
	 *****************************************************************************************************/
	public String getInputFile() {
		return inputFile;
	}

	/*****************************************************************************************************
	 * Purpose: Setter method to set output file path. Return Value: Nil.
	 * Callers:readExcel, mainControll
	 *****************************************************************************************************/
	public void setoutputFile(String outputFile) {
		this.outputFile = outputFile;
	}

	/*****************************************************************************************************
	 * Purpose: Getter method to get output file path. Return Value: returns
	 * output file path. Callers:readExcel, mainControll
	 *****************************************************************************************************/
	public String getoutputFile() {
		return outputFile;
	}

	/*****************************************************************************************************
	 * Purpose: List of xpath's to be loaded as list of arrays. Parameters:
	 * content-Target values in input Excel Return Value:List of xpath array.
	 * Callers:readExcel Pre-conditions:Input and output file path must be
	 * obtained from user/layman.
	 *****************************************************************************************************/
	public ArrayList<String> getTargetList(String content) {
		ArrayList<String> list = new ArrayList<String>();
		String[] a = content.split("\n");
		for (int i = 0; i < a.length; i++) {
			list.add(a[i]);
		}
		return list;
	}

	/*****************************************************************************************************
	 * Purpose: Reads data from input excel file and returns data to
	 * mainControll function as list of arrays. Parameters: Nil Return Value:
	 * List of Arrays of data from driver sheet to be executed
	 * Callers:mainControll() Pre-conditions:Input and output file path must be
	 * obtained from user/layman.
	 *****************************************************************************************************/
	public ArrayList<ExcelData> readExcel() throws FileNotFoundException {
		Workbook workbook = null;
		ArrayList<ExcelData> listObjExcelData = new ArrayList<ExcelData>();
		int count = 0;
		try {
			File file = new File(inputFile);
			workbook = Workbook.getWorkbook(file);
			MyLogger.logger.info("***Reading Excel File***");
			Sheet Driver = workbook.getSheet(0);
			Sheet OR = workbook.getSheet("OR");
			Driver.findCell("Yes");
			for (int i = 1; i <= Driver.getRows(); i++) {
				String execstatus = Driver.getCell(3, i).getContents()
						.toString();
				try {
					if (execstatus.equalsIgnoreCase("Yes")) {
						ExcelData edata = new ExcelData();
						edata.setTracksheetname(Driver.getCell(1, i)
								.getContents().toString());
						edata.setTestcasename(Driver.getCell(2, i)
								.getContents().toString());
						String testBrowsers = Driver.getCell(4, i)
								.getContents().toString();
						edata.setBaseUrl(Driver.getCell(5, i).getContents()
								.toString());
						edata.setTimeout(Driver.getCell(6, i).getContents()
								.toString());
						Browsers = testBrowsers.split(",");
						edata.setBrowserStart(Browsers);
						Sheet Track = workbook.getSheet(edata
								.getTracksheetname());
						String temptestcase;
						try {
							int j = 1;
							int hcount = 0;
							ArrayList<SeleneseData> hdlist = new ArrayList<SeleneseData>();
							while (j < Track.getRows()) {

								temptestcase = Track.getCell(0, j)
										.getContents();
								if (temptestcase.equalsIgnoreCase(edata
										.getTestcasename())) {

									SeleneseData seleneseData = new SeleneseData();
									seleneseData.setStep(Track.getCell(1, j)
											.getContents().toString());
									if (Track.getCell(2, j).getContents()
											.toString().isEmpty()) {
										seleneseData
												.setTargetList(getTargetList(Track
														.getCell(2, j)
														.getContents()
														.toString()));
										seleneseData
												.setTarget((String) seleneseData
														.getTargetList().get(0));

									} else {
										Cell objCell = OR.findCell(Track
												.getCell(2, j).getContents()
												.toString());
										if (objCell == null) {
											seleneseData.setTarget("");
										} else {
											seleneseData
													.setTargetList(getTargetList(OR
															.getCell(
																	2,
																	objCell.getRow())
															.getContents()
															.toString()));
											seleneseData
													.setTarget((String) seleneseData
															.getTargetList()
															.get(0));
										}
									}
									seleneseData.setCommand(Track.getCell(3, j)
											.getContents().toString());
									String parameterName = Track.getCell(4, j)
											.getContents().toString();
									seleneseData.setValue(parameterName);
									seleneseData.setBaseurl(edata.getBaseUrl());
									hdlist.add(hcount, seleneseData);
									hcount++;
									edata.setSeleneseData(hdlist);
								}
								j++;
							}
							listObjExcelData.add(count, edata);
							count++;
						} catch (Exception e) {
							System.out.println(e.getMessage());
						}
					}
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return listObjExcelData;
	}

	/****************************************************************************************************
	 * Purpose: Copy input excel file into output file path Parameters:
	 * InputFile-Input File path outputFile-Output File path Return Value: Nil
	 * Callers:mainControll() Pre-conditions:Input and output file path must be
	 * obtained from user/layman.
	 *****************************************************************************************************/
	public void copyExcel(String inputFile, String outputFile)
			throws IOException {
		Workbook workbook = null;
		WritableWorkbook copy;
		WorkbookSettings wbSettings = new WorkbookSettings();
		wbSettings.setLocale(new Locale("en", "EN"));
		try {
			workbook = Workbook.getWorkbook(new File(inputFile), wbSettings);
			copy = Workbook.createWorkbook(new File(outputFile), workbook);
			WritableSheet Driver = copy.getSheet(0);
			Label passfailStatus = new Label(7, 0, "Pass/Fail Status",
					createFormattedCell(10, null, false, false, null, null,
							null, null, Colour.WHITE, Colour.GREY_25_PERCENT));
			Label exeTime = new Label(8, 0, "Execution Time",
					createFormattedCell(10, null, false, false, null, null,
							null, null, Colour.WHITE, Colour.GREY_25_PERCENT));
			Driver.addCell(passfailStatus);
			Driver.addCell(exeTime);
			Driver.setColumnView(7, 20);
			Driver.setColumnView(8, 20);
			copy.write();
			copy.close();
			workbook.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	/****************************************************************************************************
	 * Purpose: Writes name of browser in output excel file. Parameters:
	 * colCount-Column count in excel file to be written, output-output excel
	 * file, ed- excel data Return Value: Nil Callers:mainControll()
	 * Pre-conditions:Atleast Per test case per browser execution is over
	 *****************************************************************************************************/
	public void writeBrowserLabel(int colCount, String output, ExcelData ed,
			String browserVersion) {
		Workbook workbook = null;
		WritableWorkbook copy = null;
		WorkbookSettings wbSettings = new WorkbookSettings();
		wbSettings.setLocale(new Locale("en", "ER"));
		try {
			workbook = Workbook.getWorkbook(new File(output), wbSettings);
			copy = Workbook.createWorkbook(new File(output), workbook);
			WritableSheet sheet = copy.getSheet(ed.getTracksheetname());
			Cell cell = sheet.getCell(5, 0);
			if (cell.getContents() == ""
					&& sheet.findCell(browserVersion) == null) {
				Label browsers = new Label(5, 0, browserVersion,
						createFormattedCell(10, null, false, false, null, null,
								null, null, Colour.WHITE,
								Colour.GREY_25_PERCENT));
				sheet.addCell(browsers);
			} else if (sheet.getCell(colCount + 5, 0).getContents() == ""
					&& sheet.findCell(browserVersion) == null) {
				Label browsers = new Label(colCount + 5, 0, browserVersion,
						createFormattedCell(10, null, false, false, null, null,
								null, null, Colour.WHITE,
								Colour.GREY_25_PERCENT));
				sheet.addCell(browsers);
			} else if (sheet.getCell(colCount + 15, 0).getContents() == ""
					&& sheet.findCell(browserVersion) == null) {
				Label browsers = new Label(colCount + 15, 0, browserVersion,
						createFormattedCell(10, null, false, false, null, null,
								null, null, Colour.WHITE,
								Colour.GREY_25_PERCENT));
				sheet.addCell(browsers);
			}
			copy.write();
			copy.close();
			workbook.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void deleteRows(String output) {
		Workbook workbook = null;
		WritableWorkbook copy = null;
		WorkbookSettings wbSettings = new WorkbookSettings();
		wbSettings.setLocale(new Locale("en", "ER"));
		try {
			workbook = Workbook.getWorkbook(new File(output), wbSettings);
			copy = Workbook.createWorkbook(new File(output), workbook);
			WritableSheet driverSheet = copy.getSheet(0);
			// Deleting "No" Rows in Output sheet.
			for (int i = 1; i <= driverSheet.getRows(); i++) {
				String execstatus = driverSheet.getCell(3, i).getContents()
						.toString();
				try {
					if (execstatus.equalsIgnoreCase("No")) {
						WritableSheet Track = copy.getSheet(driverSheet
								.getCell(1, i).getContents().toString());
						Cell testcaserow;
						do {
							testcaserow = Track.findCell(driverSheet
									.getCell(2, i).getContents().toString());
							if (testcaserow != null) {
								Track.removeRow(testcaserow.getRow());
							}
						} while (testcaserow != null);
						driverSheet.removeRow(i);
						i--;
					}
				} catch (Exception e) {
					System.out.println(e);
				}
			}
			copy.write();
			copy.close();
			workbook.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	/****************************************************************************************************
	 * Purpose: Writes output data i.e font family, description,etc onto output
	 * excel file Parameters: ouput-Output excel file, hd- List of data after
	 * excuted in atleast one browser, browserVersion-version of browser,
	 * executionTime- Execution time. Return Value: Nil Callers:mainControll()
	 * Pre-conditions:Atleast Per test case per browser execution is over
	 *****************************************************************************************************/
	public void writeExcel(String output, ArrayList<SeleneseData> hd,
			ExcelData ed, String browserVersion, long executionTime) {
		boolean failcheck = false;
		Workbook workbook = null;
		WritableWorkbook copy = null;
		WorkbookSettings wbSettings = new WorkbookSettings();
		wbSettings.setLocale(new Locale("en", "ER"));
		try {
			workbook = Workbook.getWorkbook(new File(output), wbSettings);
			copy = Workbook.createWorkbook(new File(output), workbook);
			WritableSheet driverSheet = copy.getSheet(0);
			// Execution Time Update in Driver Sheet
			Cell dCell = driverSheet.findCell(ed.getTestcasename());
			Label timer = new Label(8, dCell.getRow(),
					Long.toString(executionTime) + "sec", createFormattedCell(
							10, null, true, false, null, null, null, null,
							Colour.GREEN, Colour.WHITE));
			driverSheet.addCell(timer);

			// Track Sheet Updates
			WritableSheet trackSheet = copy.getSheet(ed.getTracksheetname());
			Cell rowcell, colcell;
			int row, col;
			rowcell = trackSheet.findCell(ed.getTestcasename());
			row = rowcell.getRow();
			colcell = trackSheet.findCell(browserVersion);
			col = colcell.getColumn();
			for (int i = 0; i < hd.size(); i++) {
				Label passfailLabel, LeftTop, RightBottom, Description;
				WritableHyperlink Screenshots;

				// Pass/Fail,LeftTop,RightBottom,Description & Screenshot Update
				// in TrackSheet
				if (((SeleneseData) hd.get(i)).isExstatus()) {
					passfailLabel = new Label(col, row, "Pass",
							createFormattedCell(10, null, true, false, null,
									null, null, null, Colour.GREEN,
									Colour.WHITE));
					LeftTop = new Label(col + 6, row, "("
							+ ((SeleneseData) hd.get(i)).getX() + ","
							+ ((SeleneseData) hd.get(i)).getY() + ")",
							createFormattedCell(10, null, false, false, null,
									null, null, null, Colour.BLACK,
									Colour.WHITE));
					int rx = ((SeleneseData) hd.get(i)).getX()
							+ ((SeleneseData) hd.get(i)).getWidth();
					// Ry (y-Height is updated to y+Height
					int ry = ((SeleneseData) hd.get(i)).getY()
							+ ((SeleneseData) hd.get(i)).getHeight();
					RightBottom = new Label(col + 7, row, "(" + rx + "," + ry
							+ ")", createFormattedCell(10, null, false, false,
							null, null, null, null, Colour.BLACK, Colour.WHITE));
					Description = new Label(col + 8, row, "",
							createFormattedCell(10, null, true, false, null,
									null, null, null, Colour.GREEN,
									Colour.WHITE));
					Screenshots = new WritableHyperlink(col + 9, row, new File(
							"File://"
									+ ((SeleneseData) hd.get(i))
											.getScreenshotPath()));
					row++;
				} else {
					failcheck = true;
					passfailLabel = new Label(col, row, "Fail",
							createFormattedCell(10, null, true, false, null,
									null, null, null, Colour.RED, Colour.WHITE));
					LeftTop = new Label(col + 6, row, "(0,0)",
							createFormattedCell(10, null, false, false, null,
									null, null, null, Colour.BLACK,
									Colour.WHITE));
					RightBottom = new Label(col + 7, row, "(0,0)",
							createFormattedCell(10, null, false, false, null,
									null, null, null, Colour.BLACK,
									Colour.WHITE));
					// modified by srini.
					Description = new Label(col + 8, row,
							((SeleneseData) hd.get(i)).getMessage(),
							createFormattedCell(10, null, true, false, null,
									null, null, null, Colour.GREEN,
									Colour.WHITE));
					Screenshots = new WritableHyperlink(col + 9, row, new File(
							"File://"
									+ ((SeleneseData) hd.get(i))
											.getScreenshotPath()));
					row++;
				}
				trackSheet.addCell(passfailLabel);
				trackSheet.addCell(LeftTop);
				trackSheet.addCell(RightBottom);
				trackSheet.addCell(Description);
				trackSheet.addHyperlink(Screenshots);
			}
			rowcell = trackSheet.findCell(ed.getTestcasename());
			row = rowcell.getRow();
			colcell = trackSheet.findCell(browserVersion);
			col = colcell.getColumn();
			for (int i = 0; i < hd.size(); i++) {
				SeleneseData h = (SeleneseData) hd.get(i);
				if ((!h.getCommand().equals("waitForPageToLoad"))
						&& (!h.getCommand().equals("ForAlert"))
						&& (!h.getCommand().equals("highlight"))
						&& (!h.getCommand().equals("waitForElementPresent"))
						&& (!h.getCommand().equals("PressTab"))
						&& (!h.equals("Escape"))
						&& (!h.getCommand().equals("PressEnter"))
						&& (!h.getCommand().equals("Escape"))
						&& (!h.getCommand().equals("close"))
						&& (!h.getCommand().equals("windowFocus"))
						&& (!h.getCommand().equals("windowMaximize"))
						&& (!h.getCommand().equals("waitForTextPresent"))
						&& (!h.getCommand().equals("waitForFrameToLoad"))
						&& (!h.getCommand().equals("waitForPopup"))
						&& (!h.getCommand().equals("waitForPageToLoad"))
						&& (!h.getCommand().equals("goBack"))) {

					Label color = new Label(col + 1, row, h.getColor(),
							createFormattedCell(10, null, false, false, null,
									null, null, null, Colour.BLACK,
									Colour.WHITE));
					Label fontFamily = new Label(col + 2, row,
							h.getFontFamily(), createFormattedCell(10, null,
									false, false, null, null, null, null,
									Colour.BLACK, Colour.WHITE));
					Label fontStyle = new Label(col + 3, row, h.getFontStyle(),
							createFormattedCell(10, null, false, false, null,
									null, null, null, Colour.BLACK,
									Colour.WHITE));
					Label fontWeight = new Label(col + 4, row,
							h.getFontWeight(), createFormattedCell(10, null,
									false, false, null, null, null, null,
									Colour.BLACK, Colour.WHITE));
					Label fontSize = new Label(col + 5, row, h.getFontSize(),
							createFormattedCell(10, null, false, false, null,
									null, null, null, Colour.BLACK,
									Colour.WHITE));
					trackSheet.addCell(color);
					trackSheet.addCell(fontFamily);
					trackSheet.addCell(fontStyle);
					trackSheet.addCell(fontWeight);
					trackSheet.addCell(fontSize);
				}
				row++;
			}

			// Testcasewise pass/fail update in DriverSheet
			Cell testcase = driverSheet.findCell(ed.getTestcasename());
			Label passfailStatus;
			if (failcheck == true) {
				passfailStatus = new Label(7, testcase.getRow(), "Fail",
						createFormattedCell(10, null, true, false, null, null,
								null, null, Colour.RED, Colour.WHITE));
			} else {
				passfailStatus = new Label(7, testcase.getRow(), "Pass",
						createFormattedCell(10, null, true, false, null, null,
								null, null, Colour.GREEN, Colour.WHITE));
			}
			driverSheet.addCell(passfailStatus);

			copy.write();
			copy.close();
			workbook.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	/****************************************************************************************************
	 * Purpose: Formatting cell in output excel Parameters:
	 * fontName,fontColor,fontStyle-bold,italic,underline. Return Value:
	 * Formatted writable cell value. Callers:mainControll() Pre-conditions:
	 * Atleast Per test case per browser execution is over
	 *****************************************************************************************************/
	public WritableCellFormat createFormattedCell(int pointSize,
			WritableFont.FontName fontName, boolean isBold, boolean italic,
			UnderlineStyle underLineStyle, Border border,
			BorderLineStyle lineStyle, Alignment alignment, Colour fontcolor,
			Colour background) throws WriteException {
		WritableFont font = new WritableFont(fontName != null ? fontName
				: WritableFont.ARIAL, pointSize, isBold ? WritableFont.BOLD
				: WritableFont.NO_BOLD, italic,
				underLineStyle != null ? underLineStyle
						: UnderlineStyle.NO_UNDERLINE);
		font.setColour(fontcolor);

		WritableCellFormat writableCellFormat = new WritableCellFormat(font);
		if (lineStyle == null) {
			lineStyle = BorderLineStyle.THIN;
		}
		if (border == null) {
			border = Border.NONE;
		}
		if (alignment == null) {
			alignment = Alignment.CENTRE;
		}

		writableCellFormat.setBorder(border, lineStyle, Colour.BLACK);
		writableCellFormat.setAlignment(alignment);
		writableCellFormat.setBackground(background);
		writableCellFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
		return writableCellFormat;
	}

	/****************************************************************************************************
	 * Purpose: Merge track sheet labels in output excel file. Parameters:
	 * trackSheet-Name of track sheet, outputFile-OutputFile name,
	 * prevSheet-Previous track sheet name. Return Value: Nill
	 * Callers:mainControll() Pre-conditions: Atleast Per test case to be in
	 * executable state of a track sheet.
	 *****************************************************************************************************/
	public void mergeTrackLabel(String trackSheet, String outputFile,
			String prevSheet) {
		Workbook workbook = null;
		WritableWorkbook copy = null;
		WorkbookSettings wbSettings = new WorkbookSettings();
		wbSettings.setLocale(new Locale("en", "ER"));
		try {
			workbook = Workbook.getWorkbook(new File(outputFile), wbSettings);
			copy = Workbook.createWorkbook(new File(outputFile), workbook);
			WritableSheet sheet = copy.getSheet(trackSheet);
			if (!trackSheet.equalsIgnoreCase(prevSheet)) {
				sheet.mergeCells(0, 0, 0, 1);
				sheet.mergeCells(1, 0, 1, 1);
				sheet.mergeCells(2, 0, 2, 1);
				sheet.mergeCells(3, 0, 3, 1);
				sheet.mergeCells(4, 0, 4, 1);
			}
			copy.write();
			copy.close();
			workbook.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	/****************************************************************************************************
	 * Purpose: Merge track sheet report labels like status, font family,
	 * description,font style,font size, font color in output excel file.
	 * Parameters: trackSheet-Name of track sheet, outputFile-OutputFile name,
	 * maxBrowserCount-Maximun no of browsers Return Value: Nill
	 * Callers:mainControll() Pre-conditions: Atleast Per test case to be in
	 * executable state of a track sheet.
	 *****************************************************************************************************/
	public void mergeReportLabel(int maxBrowserCount, String outputFile,
			String trackSheet) {
		Workbook workbook = null;
		WritableWorkbook copy = null;
		WorkbookSettings wbSettings = new WorkbookSettings();
		wbSettings.setLocale(new Locale("en", "ER"));
		try {
			workbook = Workbook.getWorkbook(new File(outputFile), wbSettings);
			copy = Workbook.createWorkbook(new File(outputFile), workbook);
			WritableSheet sheet = copy.getSheet(trackSheet);
			Label result, colorHeading, fontFamilyHeading, fontStyleHeading, fontWeightHeading, fontSizeHeading, Description, Screenshots;
			sheet.insertRow(1);
			for (int i = 0; i < maxBrowserCount; i++) {
				int colCell = sheet.getColumns();
				result = new Label(colCell, 1, "RESULT", createFormattedCell(
						10, null, true, false, null, null, null, null,
						Colour.BLACK, Colour.GREY_25_PERCENT));
				colorHeading = new Label(colCell + 1, 1, "COLOR",
						createFormattedCell(10, null, true, false, null, null,
								null, null, Colour.BLACK,
								Colour.GREY_25_PERCENT));
				fontFamilyHeading = new Label(colCell + 2, 1, "Font-Family",
						createFormattedCell(10, null, true, false, null, null,
								null, null, Colour.BLACK,
								Colour.GREY_25_PERCENT));
				fontStyleHeading = new Label(colCell + 3, 1, "Font-Style",
						createFormattedCell(10, null, true, false, null, null,
								null, null, Colour.BLACK,
								Colour.GREY_25_PERCENT));
				fontWeightHeading = new Label(colCell + 4, 1, "Font-Weight",
						createFormattedCell(10, null, true, false, null, null,
								null, null, Colour.BLACK,
								Colour.GREY_25_PERCENT));
				fontSizeHeading = new Label(colCell + 5, 1, "Font-Size",
						createFormattedCell(10, null, true, false, null, null,
								null, null, Colour.BLACK,
								Colour.GREY_25_PERCENT));
				Label lth = new Label(colCell + 6, 1, "(LEFT,TOP)",
						createFormattedCell(10, null, true, false, null, null,
								null, null, Colour.BLACK,
								Colour.GREY_25_PERCENT));
				Label rbh = new Label(colCell + 7, 1, "(RIGHT,BOTTOM)",
						createFormattedCell(10, null, true, false, null, null,
								null, null, Colour.BLACK,
								Colour.GREY_25_PERCENT));
				Description = new Label(colCell + 8, 1, "Description",
						createFormattedCell(10, null, true, false, null, null,
								null, null, Colour.BLACK,
								Colour.GREY_25_PERCENT));
				Screenshots = new Label(colCell + 9, 1, "Screenshots",
						createFormattedCell(10, null, true, false, null, null,
								null, null, Colour.BLACK,
								Colour.GREY_25_PERCENT));
				sheet.addCell(result);
				sheet.addCell(colorHeading);
				sheet.addCell(fontFamilyHeading);
				sheet.addCell(fontStyleHeading);
				sheet.addCell(fontWeightHeading);
				sheet.addCell(fontSizeHeading);
				sheet.addCell(Description);
				sheet.addCell(lth);
				sheet.addCell(rbh);
				sheet.addCell(Screenshots);
				sheet.mergeCells(colCell, 0, colCell + 9, 0);
				sheet.setColumnView(colCell + 1, 25);
				sheet.setColumnView(colCell + 2, 30);
				sheet.setColumnView(colCell + 3, 10);
				sheet.setColumnView(colCell + 4, 15);
				sheet.setColumnView(colCell + 5, 15);
				sheet.setColumnView(colCell + 6, 20);
				sheet.setColumnView(colCell + 7, 20);
				sheet.setColumnView(colCell + 8, 20);
				sheet.setColumnView(colCell + 9, 20);

			}

			copy.write();
			copy.close();
			workbook.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	/****************************************************************************************************
	 * Purpose: Calls all functionalities like readExcel,
	 * writeExcel,mergeReportlabel,etc color in output excel file. Parameters:
	 * test Return Value: Nill Callers:mainControll() Pre-conditions: GUI to be
	 * loaded and execution button should be pressed.
	 *****************************************************************************************************/
	public void mainControll(ReadExcel rdExcel) throws IOException {
		String screenshotPath = null;
		long prevTimer;
		ArrayList<ExcelData> ElementArray = new ArrayList<ExcelData>();
		DriverExecution exeExcel = new DriverExecution();
		String OS = System.getProperty("os.name").toLowerCase();
		if (OS.indexOf("win") >= 0) {
			File screenshotDirectory = new File(rdExcel.getoutputFile()
					+ "\\Screenshots");
			if (!screenshotDirectory.exists())
				screenshotDirectory.mkdir();
			screenshotPath = screenshotDirectory.getCanonicalPath() + "\\";
			String[] fileName = inputFile.split(".xls");
			String[] out = fileName[0].split("\\\\");
			rdExcel.setoutputFile(rdExcel.getoutputFile() + "\\"
					+ out[out.length - 1] + "_Output.xls");
		}else if (OS.indexOf("mac") >= 0) {
			File screenshotDirectory = new File(rdExcel.getoutputFile()
					+ "/Screenshots");
			System.out.println(rdExcel.getoutputFile());
			if (!screenshotDirectory.exists())
				screenshotDirectory.mkdir();
			screenshotPath = screenshotDirectory.getCanonicalPath() + "/";
			String[] fileName = inputFile.split(".xls");
			String[] out = fileName[0].split("/");
			rdExcel.setoutputFile(rdExcel.getoutputFile() + "/"+ out[out.length - 1] + "_Output.xls");			
		}
		ElementArray = rdExcel.readExcel();
		rdExcel.copyExcel(rdExcel.getInputFile(), rdExcel.getoutputFile());
		int colCount = 0;
		// int maxCount[] = new int[100];
		String prevSheet = "", trackSheet = "";
		// int counter = 1;
		// String[] temp = null;
		try {

			trackSheet = "";
			for (ExcelData edata : ElementArray) { // Merge cells loop
				if (trackSheet != edata.getTracksheetname()
						|| trackSheet.isEmpty()) {
					rdExcel.mergeReportLabel(/* maxCount[i - 1] */3,
							rdExcel.getoutputFile(), edata.getTracksheetname());
				}
				trackSheet = edata.getTracksheetname();
			}
			for (ExcelData edata : ElementArray) {// Test case loop
				ArrayList<SeleneseData> hdlist1 = edata.getSeleneseData();
				String browserVersion;
				rdExcel.mergeTrackLabel(edata.getTracksheetname(),
						rdExcel.getoutputFile(), prevSheet);
				prevSheet = edata.getTracksheetname();
				prevTimer = 0;
				for (int j = 0; j < edata.getBrowserStart().length; j++)// Browser
																		// loop
				{
					Timer timer = new Timer();
					timer.start();
					String[] browser = edata.getBrowserStart();
					browserVersion = exeExcel.initDriver(browser[j],rdExcel.getiOSProxy());
					hdlist1 = exeExcel.runserver(edata.getTimeout(), hdlist1,
							screenshotPath + edata.getTestcasename(),
							browserVersion,rdExcel.getScreenFlag());					
					timer.end();
					/*if(browserVersion.startsWith("Internet")){
						Runtime.getRuntime().exec("taskkill /F /IM IEDriverServer.exe");
					}else if(browserVersion.startsWith("Internet")){
						Runtime.getRuntime().exec("taskkill /F /IM chromedriver.exe");
					}*/
					rdExcel.writeBrowserLabel(colCount,
							rdExcel.getoutputFile(), edata, browserVersion);
					colCount = 10;
					rdExcel.writeExcel(rdExcel.getoutputFile(), hdlist1, edata,
							browserVersion, timer.getTotalTime() + prevTimer);
					prevTimer = timer.getTotalTime();
				}

			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		// deleteRows(rdExcel.getoutputFile());
		// compareExcelData(rdExcel.getInputFile(),10,"Assisted Shopping");
	}

	public void compareExcelData(String output, int deviation, String sheet) {
		// deviation=10;
		// sheet="Assisted Shopping";
		Workbook workbook = null;
		WritableWorkbook copy = null;
		try {
			workbook = Workbook.getWorkbook(new File(output));
			copy = Workbook.createWorkbook(new File(output), workbook);

			for (int j = 0; j < 5; j += 4) {
				int i = 2;
				if ((copy.getSheet(sheet).getCell(11, i) != null)
						&& (copy.getSheet(sheet).getCell(21 + j, i) != null)) {
					while ((!copy.getSheet(sheet).getCell(11, i).getContents()
							.equals(""))
							&& (!copy.getSheet(sheet).getCell(21 + j, i)
									.getContents().equals(""))) {
						StringBuffer lt1 = new StringBuffer(copy
								.getSheet(sheet).getCell(11, i).getContents());
						StringBuffer lt2 = new StringBuffer(copy
								.getSheet(sheet).getCell(21 + j, i)
								.getContents());
						lt1.deleteCharAt(0);
						lt1.deleteCharAt(lt1.length() - 1);
						lt2.deleteCharAt(0);
						lt2.deleteCharAt(lt2.length() - 1);

						String[] leftt1 = lt1.toString().split(",");
						String[] leftt2 = lt2.toString().split(",");

						StringBuffer rb1 = new StringBuffer(copy
								.getSheet(sheet).getCell(12, i).getContents());
						StringBuffer rb2 = new StringBuffer(copy
								.getSheet(sheet).getCell(22 + j, i)
								.getContents());
						rb1.deleteCharAt(0);
						rb1.deleteCharAt(rb1.length() - 1);
						rb2.deleteCharAt(0);
						rb2.deleteCharAt(rb2.length() - 1);

						String[] rightb1 = rb1.toString().split(",");
						String[] rightb2 = rb2.toString().split(",");

						int lx1 = Integer.parseInt(leftt1[0]);
						int ly1 = Integer.parseInt(leftt1[1]);
						int rx1 = Integer.parseInt(rightb1[0]);
						int ry1 = Integer.parseInt(rightb1[1]);
						int lx2 = Integer.parseInt(leftt2[0]);
						int ly2 = Integer.parseInt(leftt2[1]);
						int rx2 = Integer.parseInt(rightb2[0]);
						int ry2 = Integer.parseInt(rightb2[1]);

						if (((lx1 != 0) || (ly1 != 0) || (rx1 != 0) || (ry1 != 0))
								&& ((lx2 != 0) || (ly2 != 0) || (rx2 != 0) || (ry2 != 0))) {
							if ((Math.abs(lx1 - lx2) <= deviation)
									&& (Math.abs(ly1 - ly2) <= deviation)) {
								Label compareStatus = new Label(36 + j, i,
										"TRUE", createFormattedCell(10, null,
												true, false, null, null, null,
												null, Colour.GREEN,
												Colour.WHITE));
								copy.getSheet(sheet).addCell(compareStatus);
							} else {
								Label compareStatus = new Label(36 + j, i,
										"FALSE", createFormattedCell(10, null,
												true, false, null, null, null,
												null, Colour.RED, Colour.WHITE));
								copy.getSheet(sheet).addCell(compareStatus);
							}
							if ((Math.abs(rx1 - rx2) <= deviation)
									&& (Math.abs(ry1 - ry2) <= deviation)) {
								Label compareStatus = new Label(37 + j, i,
										"TRUE", createFormattedCell(10, null,
												true, false, null, null, null,
												null, Colour.GREEN,
												Colour.WHITE));
								copy.getSheet(sheet).addCell(compareStatus);
							} else {
								Label compareStatus = new Label(37 + j, i,
										"FALSE", createFormattedCell(10, null,
												true, false, null, null, null,
												null, Colour.RED, Colour.WHITE));
								copy.getSheet(sheet).addCell(compareStatus);
							}
						} else {
							Label compareStatus = new Label(36 + j, i,
									"NOT APPLICABLE", createFormattedCell(10,
											null, true, false, null, null,
											null, null, Colour.BLUE,
											Colour.WHITE));
							copy.getSheet(sheet).addCell(compareStatus);
							copy.getSheet(sheet).mergeCells(36 + j, i, 37 + j,
									i);
						}

						i++;
					}
				}
			}
			copy.write();
		} catch (BiffException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		} catch (RowsExceededException e) {
			System.out.println(e.getMessage());
		} catch (WriteException e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				copy.close();
			} catch (WriteException e) {
				System.out.println(e.getMessage());
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}

			workbook.close();
		}
	}

	

}