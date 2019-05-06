import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SixDegrees {
  public static void main(String[] args) {
    String path = args[0];

    try {
      storeNames(path);
    } catch (IOException e) {
      e.printStackTrace();
    }

    System.out.println("end");
  }

  public static void storeNames(String file) throws FileNotFoundException, IOException {
    List<String> records = new ArrayList<>();

    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
      String line;

      // Read each line
      while ((line = br.readLine()) != null) {
        String[] values = line.split(",");

        for (String val : values) {
          if (val.contains("name")) {
            Pattern p = Pattern.compile("\"\"name\"\": \"\"(.+?)\"\"");
            Matcher m = p.matcher(val);

            // Regex add names
            while (m.find()) {
              records.add(m.group(1));
            }
          } else if (val.contains("}]")) {
            break; // Avoid crews
          }
        }
      }
    }

    for (String names : records) {
      System.out.print(names + " ");
    }
  }
}
