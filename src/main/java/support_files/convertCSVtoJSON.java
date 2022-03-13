package support_files;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * Project Name:        Parallel-Ping
 * Date of Creation:    Day:16-Month:02-Year:2021
 *
 * @author Dimitris Panagopoulos
 * Comments:
 *           - script that converts .csv file to json
 *           - HERE staightforward conversion (only name of input and output files required)
 */
public class convertCSVtoJSON {
    public static void main(String[] args) throws IOException {
        File inputFile = new File("src/main/java/support_files/data_csv.csv");
        File outputFile = new File("src/main/java/support_files/convertedCSVtoJSON.json");
        CsvSchema csvSchema = CsvSchema.builder().setUseHeader(true).build();
        CsvMapper csvMapper = new CsvMapper();

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        mapper.writerWithDefaultPrettyPrinter().writeValue(outputFile,
                csvMapper.readerFor(Map.class).with(csvSchema).readValues(inputFile).readAll());
    }
}
