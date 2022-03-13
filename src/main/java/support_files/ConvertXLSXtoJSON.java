package support_files;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Project Name:        Parallel-Ping
 * Date of Creation:    Day:16-Month:02-Year:2021
 *
 * @author Dimitris Panagopoulos
 * Comments:
 *           - script that converts .xlsx file to json
 *           - HERE FILE DETAILS have to be declared
 */
public class ConvertXLSXtoJSON {
    public static void main(String[] args) {
        // Step 1: Read Excel File into Java List Objects
        List detail = readExcelFile();
        // Step 2: Write Java List Objects to JSON File
        writeObjects2JsonFile(detail);
        System.out.println("DONE");
    }
    // Read Excel File into Java List Objects
    private static List readExcelFile(){
        try {
            FileInputStream excelFile = new FileInputStream("src/main/java/support_files/data.xlsx");
            Workbook workbook = new XSSFWorkbook(excelFile);
            Sheet sheet = workbook.getSheet("PAGE");  // name of sheet in Excel
            Iterator<Row> rows = sheet.iterator();
            List lstFILE_DETAILS = new ArrayList();
            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();
                // skip header
                if(rowNumber == 0) {
                    rowNumber++;
                    continue;
                }
                Iterator<Cell> cellsInRow = currentRow.iterator();
                ClassFileDetails det = new ClassFileDetails();
                int cellIndex = 0;
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();
                    if (cellIndex == 0) { // NAME
                        det.setName(currentCell.getStringCellValue());

                    } else if (cellIndex == 1) { // id
                        det.setId((int) currentCell.getNumericCellValue());
                    } else if (cellIndex == 2) { // ip
                        det.setIp(currentCell.getStringCellValue());
                    } else if (cellIndex == 3) { // office
                        det.setOffice(currentCell.getStringCellValue());}
                    cellIndex++;
                }
                lstFILE_DETAILS.add(det);
            }
            workbook.close();
            return lstFILE_DETAILS;
        } catch (IOException e) {
            throw new RuntimeException("FAIL! -> message = " + e.getMessage());
        }
    }
    // Convert Java Objects to JSON File
    private static void writeObjects2JsonFile(List details) {
        ObjectMapper mapper = new ObjectMapper();
        File file = new File("src/main/java/support_files/convertedXLSXtoJSON.json");
        try {
            // Serialize Java object info JSON file.
            mapper.writeValue(file, details);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
