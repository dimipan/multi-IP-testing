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
 *           - CLASS used for adding host's credentials into the existing json file
 *
 */

public class ClassAddingHost {
    // function that updates json file
    static void updateFile(String name, String ip_add, int id_add, JSONArray JSON_ARRAY) throws IOException {
        FileWriter file = new FileWriter("src/main/java/ping_task_project_CORE/ip_testing.json");
        JSONObject SHIP_DETAILS = new JSONObject();
        SHIP_DETAILS.put("NAME", name);
        SHIP_DETAILS.put("IP", ip_add);
        SHIP_DETAILS.put("ID", id_add);
        JSON_ARRAY.add(SHIP_DETAILS);
        System.out.println(JSON_ARRAY);
        file.write(JSON_ARRAY.toJSONString());
        file.flush();
    }


    // function that adds new host credentials and call the update function
    static void addParameters(JSONArray JSON_ARRAY) throws IOException {
        Scanner sc1 = new Scanner(System.in);
        System.out.println("Enter your Name;");  // first
        String NAME = sc1.next();
        Scanner sc2 = new Scanner(System.in);    // second
        System.out.println("Enter your IP;");
        String IP_ADD = sc2.next();
        Scanner sc3 = new Scanner(System.in);    // third
        System.out.println("Enter your id;");
        int ID_ADD = sc3.nextInt();
        updateFile(NAME, IP_ADD, ID_ADD, JSON_ARRAY);
    }

        public static void main(String[] args) throws IOException, ParseException {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(new FileReader("src/main/java/ping_task_project_CORE/ip_testing.json"));
            JSONArray JSON_ARRAY = (JSONArray) obj;
            System.out.println(JSON_ARRAY);
            addParameters(JSON_ARRAY);

    }

}
