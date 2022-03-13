package ping_task_project_CORE;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import static java.lang.Thread.sleep;
/**
 * Project Name:        Parallel-Ping
 * Date of Creation:    Day:16-Month:02-Year:2021
 *
 * @author Dimitris Panagopoulos
 * Comments:
 *           - CLASS that executes parallel ping for checking networks' connectivity
 *           - EXTENDED form of ping execution task
 *           - HERE the evaluation takes place after each execution loop
 */

public class ClassParallelPingMAIN {

    // function that reads the details of json file
    public static JSONArray[] readFile() throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new FileReader("src/main/java/ping_task_project_CORE/ip_testing.json"));
        JSONArray JSON_ARRAY = (JSONArray) obj;
        int TOTAL_IP = JSON_ARRAY.size();
        JSONArray NAME_LIST = new JSONArray();
        JSONArray IP_LIST = new JSONArray();
        for (Object o : JSON_ARRAY) {
            JSONObject jsonObject = (JSONObject) o;
            NAME_LIST.add(jsonObject.get("NAME"));
            IP_LIST.add(jsonObject.get("IP"));
        }
        System.out.println("Available names: " + NAME_LIST);
        System.out.println("Available IP addresses: " + IP_LIST);
        System.out.println("the list contains " + TOTAL_IP + " IP addresses");
        JSONArray[] RESULTS = new JSONArray[2];
        RESULTS[0] = NAME_LIST;
        RESULTS[1] = IP_LIST;
        return RESULTS;
    }

    // function that returns the indices of a given object (either 'active' or 'inactive')
    public static ArrayList<Integer> getIndices(ArrayList<Integer> ARR, Object object){
        ArrayList<Integer> ARRAY_INDEX = new ArrayList<>(); // init list to store the relevant indices/positions
        for (int i = 0; i < ARR.size(); i++) {
            if (ARR.get(i).equals(object)) {
                ARRAY_INDEX.add(i);
            }
        }
        return ARRAY_INDEX;
    }

    // function that evaluates details after ping execution
    static void checkDetails(ArrayList RESULT_CODE_LIST, int ACTIVE, int INACTIVE, JSONArray NAME_LIST) {
        final String ANSI_RED = "\u001B[31m", ANSI_RESET = "\u001B[0m", ANSI_GREEN = "\u001B[32m";
        ArrayList<String> NEW_INACTIVE = new ArrayList<>();
        ArrayList<String> NEW_ACTIVE = new ArrayList<>();
        // ------ INACTIVE details
        int OCCUR_INACTIVE = Collections.frequency(RESULT_CODE_LIST, INACTIVE);  // give me the inactive addresses based on result code
        System.out.println("Inactive addresses: " + OCCUR_INACTIVE);
        ArrayList INDICES_INACTIVE = getIndices(RESULT_CODE_LIST, INACTIVE);
        for(Object i : INDICES_INACTIVE){
            NEW_INACTIVE.add((String) NAME_LIST.get((Integer) i));
        }
        System.out.println(ANSI_RED + "INACTIVE clients: " + NEW_INACTIVE + ANSI_RESET);
        // ------ ACTIVE details
        int OCCUR_ACTIVE = Collections.frequency(RESULT_CODE_LIST, ACTIVE);  // give me the active addresses based on result code
        System.out.println("Active addresses: " + OCCUR_ACTIVE);
        ArrayList INDICES_ACTIVE = getIndices(RESULT_CODE_LIST, ACTIVE);
        for(Object i : INDICES_ACTIVE){
            NEW_ACTIVE.add((String) NAME_LIST.get((Integer) i));
        }
        System.out.println(ANSI_GREEN + "ACTIVE clients: " + NEW_ACTIVE + ANSI_RESET);
    }

    // function that checks the evaluation step and defines the network as (REACHABLE, UNSTABLE, UNREACHABLE)
    public static String[] checkWarnings(int TOTAL_IP, ArrayList<Integer> RTT_LIST, int THRESHOLD, ArrayList<Integer> RESULT_CODE_LIST) {
        String[] INDICATOR_SIGNS = new String[TOTAL_IP];
        int CNT = 0;
        for (int resp : RTT_LIST) {
            if (resp >= THRESHOLD) { // then inactive
                RESULT_CODE_LIST.set(CNT, 0);  // replace value
                INDICATOR_SIGNS[CNT] = "Network-UNREACHABLE";
            } else if (resp > 100) {
                INDICATOR_SIGNS[CNT] = "Network-UNSTABLE";
            } else {
                INDICATOR_SIGNS[CNT] = "Network-REACHABLE";
            }
            CNT++;
        }
        return INDICATOR_SIGNS;
    }

    // function that writes the results in json file
    static void writeFile(JSONArray NAME_LIST, JSONArray IP_LIST, int TOTAL_IP, String[] INDICATOR_SIGNS) throws IOException {
        FileWriter file = new FileWriter("src/main/java/ping_task_project_CORE/output_file.json");
        JSONArray SHIP_LIST = new JSONArray();
        for (int j=0; j < TOTAL_IP; j++) {
            JSONObject SHIP_DETAILS = new JSONObject();
            SHIP_DETAILS.put("name", NAME_LIST.get(j));
            SHIP_DETAILS.put("IP", IP_LIST.get(j));
            SHIP_DETAILS.put("id", j+1);
            SHIP_DETAILS.put("alert", Arrays.toString(new String[]{INDICATOR_SIGNS[j]}));
            SHIP_LIST.add(SHIP_DETAILS);
        }
        file.write(SHIP_LIST.toJSONString());
        file.flush();
    }

    // --------------------------------------- M A I N ---------------------------------------
    public static void main(String[] args) throws InterruptedException, IOException, ParseException {
        boolean RUNNING = true;
        int ITERATION_NUMBER = 1, MAX_ITERATIONS = 20, ACTIVE = 1, INACTIVE = 0, THRESHOLD = 450, SLEEP = 5000;
        while (RUNNING) {
            if (ITERATION_NUMBER >= MAX_ITERATIONS) {
                RUNNING = false;
            }
            System.out.println("--------------------------------------- " + "ITERATION " + ITERATION_NUMBER + " ---------------------------------------");
            // ----------------------------- READ FILE -----------------------------
            JSONArray[] RESULTS = readFile();
            JSONArray NAME_LIST = RESULTS[0];
            JSONArray IP_LIST = RESULTS[1];
            int TOTAL_IP = IP_LIST.size();

            // ----------------------------- PING EXECUTION -----------------------------
            ExecutorService EXECUTOR = Executors.newFixedThreadPool(TOTAL_IP);
            List<Future<PingResult>> PING_RESULT_LIST = new ArrayList<>();
            Callable<PingResult> CALLABLE;
            ArrayList<Integer> RESULT_CODE_LIST = new ArrayList<>(); // list to store the extracted 'resultCode' values
            ArrayList<Integer> RTT_LIST = new ArrayList<>(); // list to store the extracted 'RTT' values
            for (Object ip : IP_LIST) {
                CALLABLE = new PingTask((String) ip); // Get the ipAddress buttons[i].getText());
                Future<PingResult> future = EXECUTOR.submit(CALLABLE);
                PING_RESULT_LIST.add(future);
            }
            for (Future<PingResult> fut : PING_RESULT_LIST) {
                try {
                    System.out.println(fut.get());
                    RESULT_CODE_LIST.add(fut.get().getResultCode());
                    RTT_LIST.add((int) fut.get().getTimeToRespond());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Result Code: " + RESULT_CODE_LIST);
            System.out.println("Round-Trip Time (RTT): " + RTT_LIST);
//            System.out.println("------ FIRST CHECK ------");
//            checkDetails(RESULT_CODE_LIST, ACTIVE, INACTIVE, NAME_LIST); // call the function for FIRST checking/evaluation
//            // (will be erased --- for demonstration purposes now)
//            System.out.println("------   ------");

            // ----------------------------- EVALUATE & MAKE CORRECTIONS -----------------------------
            String[] INDICATOR_SIGNS = checkWarnings(TOTAL_IP, RTT_LIST, THRESHOLD, RESULT_CODE_LIST);
            System.out.println("Refreshed Result Code: " + RESULT_CODE_LIST);
            System.out.println("------ FINAL CHECK ------");
            checkDetails(RESULT_CODE_LIST, ACTIVE, INACTIVE, NAME_LIST); // call again the function for FINAL checking/evaluation
            System.out.println("Warning signs: " + Arrays.toString(INDICATOR_SIGNS));
            System.out.println("-------------------------------------------------------------------------------------------");

            // ----------------------------- WRITE FILE -----------------------------
            writeFile(NAME_LIST, IP_LIST, TOTAL_IP, INDICATOR_SIGNS);

            EXECUTOR.shutdown();
            ITERATION_NUMBER++;
            sleep(SLEEP); // sleep for 5 seconds and repeat
        }
        System.out.println("TASK COMPLETED");
        System.exit(0);
    }
}


