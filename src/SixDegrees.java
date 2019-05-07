import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class SixDegrees {

  /**
   * Counts actors
   * 
   * @param file the file to read
   * 
   * @return the amount of actors
   * @throws IOException
   */
  public int countActors(Path file) throws IOException {
    int amtActors = 0;

    try (BufferedReader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8);
        CSVParser csvParser = new CSVParser(reader,
            CSVFormat.DEFAULT.withHeader("movie_id", "title", "cast", "crew").withFirstRecordAsHeader().withTrim());) {

      for (CSVRecord csv : csvParser) {
        amtActors++;
      }
    }

    return amtActors;
  }

  /**
   * Reads the names of the actors in a file
   * 
   * @param file the file that is read
   * @throws FileNotFoundException
   * @throws IOException
   * @throws ParseException
   */
  public void storeNames(Path file, adjListGraph graph) throws IOException, ParseException {
    try (BufferedReader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8);
        CSVParser csvParser = new CSVParser(reader,
            CSVFormat.DEFAULT.withHeader("movie_id", "title", "cast", "crew").withFirstRecordAsHeader().withTrim());) {
      JSONParser parser = new JSONParser();

      // Read each line
      for (CSVRecord csv : csvParser) {
        List<String> actors = new ArrayList<>();
        JSONArray arr = (JSONArray) parser.parse(csv.get("cast"));
        Iterator iter = arr.iterator();

        // Read each content (name) in the array
        while (iter.hasNext()) {
          JSONObject jObj = new JSONObject();
          jObj = (JSONObject) iter.next();

          actors.add((String) jObj.get("name"));
        }

        // Add actors to list
        for (int i = 0; i < actors.size(); i++) {
          for (int j = 0; j < actors.size(); j++) {
            if (actors.get(i) != actors.get(j)) {
              graph.addActors(actors.get(i), actors.get(j));

              // System.out.println(graph.getCoActors(actors.get(j)));
            }
          }
        }
      }
    }
  }

  public static void main(String[] args) {
    Path path = Paths.get(args[0]);
    SixDegrees SD = new SixDegrees();
    adjListGraph graph = new adjListGraph();

    try {
      SD.storeNames(path, graph);
      // graph.printGraph();
      for (String msg : graph.neighbors("Abigail Breslin")) {
        System.out.println(msg);
      }
    } catch (IOException | ParseException e) {
      e.printStackTrace();
    }

    System.out.println();
  }
}
