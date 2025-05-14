package Quiz;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class UserManager {

    public static int ladeBenutzeranzahl() {
        File file = new File("Quiz/users.txt");
        if (!file.exists()) return 0;

        int count = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            while (reader.readLine() != null) count++;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return count;
    }

    public static Map<String, String> ladeBenutzer() {
        Map<String, String> userMap = new HashMap<>();
        File file = new File("Quiz/users.txt");
        if (!file.exists()) return userMap;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String zeile;
            while ((zeile = reader.readLine()) != null) {
                String[] teile = zeile.split(":");
                if (teile.length == 2) {
                    userMap.put(teile[0], teile[1]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return userMap;
    }
}
