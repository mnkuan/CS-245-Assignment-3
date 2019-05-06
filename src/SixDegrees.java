import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SixDegrees {
  /** Stores the actor -> worked with actors */
  public HashMap<String, Set<String>> actors;

  public SixDegrees() {
    actors = new HashMap<>();
  }

  /**
   * Reads the names of the actors in a file
   * 
   * @param file the file that is read
   * @throws FileNotFoundException
   * @throws IOException
   */
  public void storeNames(String file) throws FileNotFoundException, IOException {
    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
      String line;

      // Read each line
      while ((line = br.readLine()) != null) {
        Set<String> coActors = new HashSet<>();

        for (String val : line.split(",")) {
          Pattern p = Pattern.compile("\"\"name\"\": \"\"(.+?)\"\"");

          if (val.contains("name")) {
            Matcher m = p.matcher(val);

            // Regex add names
            while (m.find()) {
              coActors.add(m.group(1));
            }
          } else if (val.contains("}]")) {
            Set<String> temp = new HashSet<>(coActors);

            // Add coActors to actors
            for (String actor : coActors) {
              if (actors.containsKey(actor)) {
                // Add existing coActors
                for (String existingCoActor : actors.get(actor)) {
                  temp.add(existingCoActor);
                }
              }

              // Add coActors associated (+existing) with the actor
              for (String newCoActors : coActors) {
                actors.put(newCoActors, temp);
              }
            }

            // Avoid crews
            break;
          }
        }
      }
    }
  }

  public static void main(String[] args) {
    String path = args[0];
    SixDegrees SD = new SixDegrees();

    try {
      SD.storeNames(path);
    } catch (IOException e) {
      e.printStackTrace();
    }

    for (String actor : SD.actors.keySet()) {
      System.out.println(actor + " = " + SD.actors.get(actor));
    }
    System.out.println();
  }
}
