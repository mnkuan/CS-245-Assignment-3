import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

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
   * @throws ParseException
   * @throws InterruptedException
   */
  public void storeNames(Path file) throws FileNotFoundException, IOException, ParseException, InterruptedException {
    try (BufferedReader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8);
        CSVParser csvParser = new CSVParser(reader,
            CSVFormat.DEFAULT.withHeader("movie_id", "title", "cast", "crew").withFirstRecordAsHeader().withTrim());) {
      JSONParser parser = new JSONParser();

      // Read each line
      for (CSVRecord csv : csvParser) {
        JSONArray arr = (JSONArray) parser.parse(csv.get("cast"));
        Iterator iter = arr.iterator();

        // Read each content (name) in the array
        while (iter.hasNext()) {
          JSONObject jObj = new JSONObject();
          jObj = (JSONObject) iter.next();
          //jObj.get("name");
          
          System.out.println(jObj.get("name"));
        }
      }
    }
  }

  public static void main(String[] args) {
    Path path = Paths.get(args[0]);
    SixDegrees SD = new SixDegrees();

    try {
      SD.storeNames(path);
    } catch (IOException | ParseException | InterruptedException e) {
      e.printStackTrace();
    }

    for (String actor : SD.actors.keySet()) {
      System.out.println(actor + " = " + SD.actors.get(actor));
    }
    System.out.println();
  }
}
