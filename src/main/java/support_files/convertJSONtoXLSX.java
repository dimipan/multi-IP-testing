package support_files;

import com.aspose.cells.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
// works
public class convertJSONtoXLSX {
    public static void main(String[] args) throws Exception {
        // Instantiating a Workbook object
        Workbook workbook = new Workbook();
        Worksheet worksheet = workbook.getWorksheets().get(0);
        // Read File
        File file = new File("src/main/java/support_files/ip_testing.json");
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        StringBuilder jsonInput = new StringBuilder();
        String tempString;
        while ((tempString = bufferedReader.readLine()) != null) {
            jsonInput.append(tempString);
        }
        bufferedReader.close();
        // Set Styles
        CellsFactory factory = new CellsFactory();
        Style style = factory.createStyle();
        style.setHorizontalAlignment(TextAlignmentType.CENTER);
        style.getFont().setColor(Color.getBlueViolet());
        style.getFont().setBold(true);
        // Set JsonLayoutOptions
        JsonLayoutOptions options = new JsonLayoutOptions();
        options.setTitleStyle(style);
        options.setArrayAsTable(true);
        // Import JSON Data
        JsonUtility.importData(jsonInput.toString(), worksheet.getCells(), 0, 0, options);
        workbook.save("src/main/java/support_files/convertedJSONtoXLSX.xlsx");
        System.out.println("executed successfully.");
    }
}
