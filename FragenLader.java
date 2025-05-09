import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;

public class FragenLader {
    public static List<Frage> ladeFragen(String dateiname) throws IOException {
        String content = Files.readString(Path.of(dateiname));
        List<Frage> fragenListe = new ArrayList<>();

        Pattern pattern = Pattern.compile(
                "Frage: (.*?)\\s*A: (.*?)\\s*B: (.*?)\\s*C: (.*?)\\s*D: (.*?)\\s*Richtig: (\\w)",
                Pattern.DOTALL);

        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            String frage = matcher.group(1).trim();
            String[] antworten = {
                    matcher.group(2).trim(),
                    matcher.group(3).trim(),
                    matcher.group(4).trim(),
                    matcher.group(5).trim()
            };
            String richtig = matcher.group(6).trim();
            fragenListe.add(new Frage(frage, antworten, richtig));
        }

        return fragenListe;
    }
}
