package ping_task_project_CORE;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Project Name:        Parallel-Ping
 * Date of Creation:    Day:16-Month:02-Year:2021
 *
 * @author Dimitrios Panagopoulos
 * Comments:
 *           - CLASS used for removing host's credentials into the existing json file
 *             based on the 'NAME' of the host
 *
 */

public class ClassRemoveHost {
    static void removeParameters(JSONArray JSON_ARRAY) {
        Scanner sc4 = new Scanner(System.in);    // third
        System.out.println("Enter the NAME you want to remove;");
        String REMOVE_NAME = sc4.next();
        int REMOVING_INDEX = 0;
        for (Object o : JSON_ARRAY) {
            JSONObject jsonObject = (JSONObject) o;
            if (REMOVE_NAME.equals(jsonObject.get("NAME"))) {
                REMOVING_INDEX = JSON_ARRAY.indexOf(o);
                System.out.println(REMOVING_INDEX);
            }
        }
        JSON_ARRAY.remove(REMOVING_INDEX);
    }

    public static void main(String[] args) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new FileReader("src/main/java/ping_task_project_CORE/ip_testing.json"));
        JSONArray JSON_ARRAY = (JSONArray) obj;
        System.out.println(JSON_ARRAY);
        removeParameters(JSON_ARRAY);
        System.out.println(JSON_ARRAY);
        FileWriter file = new FileWriter("src/main/java/ping_task_project_CORE/ip_testing.json");
        file.write(JSON_ARRAY.toJSONString());
        file.flush();
    }

}
