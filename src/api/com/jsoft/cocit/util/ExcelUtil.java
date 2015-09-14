package com.jsoft.cocit.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;

public abstract class ExcelUtil {
	private static int cellValueMaxLength = 32767;

	public static void makeExcel(OutputStream out, List<String[]> excelResult) throws FileNotFoundException, IOException {
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet();
		int len = excelResult.size();
		for (int i = 0; i < len; i++) {
			HSSFRow row = sheet.createRow(i);
			String[] rowData = excelResult.get(i);
			for (int j = 0; j < rowData.length; j++) {
				HSSFCell cell = row.createCell(j);

				if (rowData[j].length() > cellValueMaxLength) {
					// RichTextString str = new HSSFRichTextString(rowData[j]);
					// cell.setCellValue(str);
					String str = rowData[j].substring(0, cellValueMaxLength);
					String str2 = rowData[j].substring(cellValueMaxLength);
					cell.setCellValue(str);
					int k = j;
					while (str2.length() > cellValueMaxLength) {
						str = str2.substring(0, cellValueMaxLength);
						str2 = str2.substring(cellValueMaxLength);

						k++;
						cell = row.createCell(k);
						cell.setCellValue(str);
					}

					if (str2.length() > 0) {
						k++;
						cell = row.createCell(k);
						cell.setCellValue(str2);
					}
				} else {
					cell.setCellValue(rowData[j]);
				}
			}
		}
		workbook.write(out);
	}

	/**
	 * 解析excel文件中的全部sheet，并返回结果集。
	 * 
	 * @param excelFile
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static List<String[]> parseExcel(File excelFile) throws FileNotFoundException, IOException {
		List<String[]> excelResult = new ArrayList();
		HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(excelFile));
		int sheetNumber = workbook.getNumberOfSheets();
		for (int sheetIndex = 0; sheetIndex < sheetNumber; sheetIndex++) {
			HSSFSheet sheet = workbook.getSheetAt(sheetIndex);
			if (sheet != null) {
				excelResult.addAll(parseSheet(sheet));
			}
		}
		return excelResult;
	}

	/**
	 * 解析 sheet 行
	 * 
	 * @param excelFile
	 * @return <sheetName, rows>
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static Map<String, List<String[]>> parseSheets(File excelFile) throws FileNotFoundException, IOException {
		Map<String, List<String[]>> excelResult = new HashMap();
		HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(excelFile));
		int sheetNumber = workbook.getNumberOfSheets();
		for (int sheetIndex = 0; sheetIndex < sheetNumber; sheetIndex++) {
			HSSFSheet sheet = workbook.getSheetAt(sheetIndex);
			if (sheet != null) {
				excelResult.put(sheet.getSheetName(), parseSheet(sheet));
			}
		}
		return excelResult;
	}

	/**
	 * 解析excel文件中指定索引的sheet，并返回结果集。
	 * 
	 * @param excelFile
	 * @param sheetIndex
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static List<String[]> parseSheet(File excelFile, int sheetIndex) throws FileNotFoundException, IOException {
		List<String[]> excelResult = new ArrayList();
		HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(excelFile));
		HSSFSheet sheet = workbook.getSheetAt(sheetIndex);
		if (sheet != null) {
			excelResult.addAll(parseSheet(sheet));
		}
		return excelResult;
	}

	private static List<String[]> parseSheet(HSSFSheet sheet) {
		List<String[]> sheetResult = new ArrayList();
		int rowNumber = sheet.getPhysicalNumberOfRows();
		for (int rowIdx = 0; rowIdx < rowNumber; rowIdx++) {
			HSSFRow row = sheet.getRow(rowIdx);
			if (row != null && row.getLastCellNum() > 0) {
				boolean emptyRow = true;
				for (String celVal : parseRow(row)) {
					if (!StringUtil.isBlank(celVal)) {
						emptyRow = false;
						break;
					}
				}
				if (!emptyRow) {
					sheetResult.add(parseRow(row));
				}
			}
		}
		return sheetResult;
	}

	private static String getMergedRegionValue(HSSFSheet sheet, int row, int column) {
		int sheetMergeCount = sheet.getNumMergedRegions();

		for (int i = 0; i < sheetMergeCount; i++) {
			CellRangeAddress ca = sheet.getMergedRegion(i);
			int firstColumn = ca.getFirstColumn();
			int lastColumn = ca.getLastColumn();
			int firstRow = ca.getFirstRow();
			int lastRow = ca.getLastRow();
			if (row >= firstRow && row <= lastRow) {
				if (column >= firstColumn && column <= lastColumn) {
					HSSFRow fRow = sheet.getRow(firstRow);
					HSSFCell fCell = fRow.getCell(firstColumn);

					return getCellValue(fCell);
				}
			}
		}

		return null;
	}

	private static String[] parseRow(HSSFRow row) {
		int size = row.getLastCellNum();
		String[] rowResult = new String[size];
		for (int col = 0; col < size; col++) {
			HSSFCell cell = row.getCell(col);
			if (cell != null) {
				rowResult[col] = getCellValue(cell);
			}
		}
		return rowResult;
	}

	private static String getCellValue(HSSFCell cell) {
		int col = cell.getColumnIndex();
		int row = cell.getRowIndex();

		// try {
		// cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		// } catch (Throwable e) {
		// // e.printStackTrace();
		// }

		// String str = cell.getStringCellValue();
		int type = cell.getCellType();
		switch (type) {
			case HSSFCell.CELL_TYPE_FORMULA:
				// FormulaEvaluator formula=new FormulaEvaluator();
				// return formula.evaluate(cell).getNumberValue();
				return cell.getCellFormula();
			case HSSFCell.CELL_TYPE_NUMERIC:
				// if (HSSFDateUtil.isCellDateFormatted(cell) || HSSFDateUtil.isCellInternalDateFormatted(cell)) {
				// return DateUtil.format(cell.getDateCellValue());
				// } else {
				// Integer num = new Integer((int) cell.getNumericCellValue());
				// return String.valueOf(num);
				// }
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			case HSSFCell.CELL_TYPE_BOOLEAN:
				// return String.valueOf(cell.getBooleanCellValue());
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			case HSSFCell.CELL_TYPE_STRING:
				return cell.getRichStringCellValue().toString();
			case HSSFCell.CELL_TYPE_BLANK:
				return getMergedRegionValue(cell.getSheet(), row, col);
			case HSSFCell.CELL_TYPE_ERROR:
				break;
		}

		return "";
	}
}
