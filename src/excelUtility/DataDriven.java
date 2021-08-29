package excelUtility;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class DataDriven {
	
	public static ArrayList<String> getData( String testcase,String sheetName)throws IOException {
		ArrayList<String> dataList = new ArrayList<String>();
		
		FileInputStream fis = new FileInputStream("D://eclipse-workspace//RestAssured-Basics//TestData.xlsx");
		
		//XSSF accepts file as an input stream
		XSSFWorkbook workbook = new XSSFWorkbook(fis);
		int sheetCount =workbook.getNumberOfSheets();
		for (int i =0;i<sheetCount;i++) {
			if (workbook.getSheetName(i).equalsIgnoreCase(sheetName)) {
				XSSFSheet sheet = workbook.getSheetAt(i);
				
				//identify testcase column by scanning the 1st row
				Iterator<Row> rows= sheet.iterator();
				Row firstRow = rows.next();
				Iterator <Cell> cell = firstRow.cellIterator();
				int k=0,column = 0;
				
				while (cell.hasNext()) {
					Cell value = cell.next();
					if (value.getStringCellValue().equalsIgnoreCase("testcase")) {
						column =k;
						
					}
					k++;
				}
				System.out.println(column);
				while(rows.hasNext()) {
					Row r = rows.next();
					if (r.getCell(column).getStringCellValue().equalsIgnoreCase(testcase)) {
						Iterator<Cell> cv =r.cellIterator();
						while (cv.hasNext()) {
							Cell c =cv.next();
							if(c.getCellType()==CellType.STRING)
								dataList.add(c.getStringCellValue());
							else
								dataList.add(NumberToTextConverter.toText(c.getNumericCellValue()));
								
								
						}
					}
				}
			}
			
			
		}
		return dataList;
		
	}
	
	

}
