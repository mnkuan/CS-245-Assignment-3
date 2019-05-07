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
import java.util.Scanner;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class SixDegrees {

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

    try (Scanner scanner = new Scanner(System.in);) {
      String actor1;
      String actor2;

      SD.storeNames(path, graph);

      graph.printGraph();

      System.out.print("Actor 1 name: ");
      actor1 = scanner.nextLine();

      System.out.print("Actor 2 name: ");
      actor2 = scanner.nextLine();

      graph.BFS(actor1, actor2);

//      for (String msg : graph.neighbors("Abigail Breslin")) {
//        System.out.println(msg);
//      }
    } catch (IOException | ParseException e) {
      e.printStackTrace();
    }

    System.out.println();
  }
}
