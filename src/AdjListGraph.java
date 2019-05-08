import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * The graph implementation that uses an adjacent list
 * 
 * @author Marco
 */
public class AdjListGraph {

  /** The list of the actors where Actor -> CoActors */
  private Map<String, Set<String>> adjList;

  /** Constructor */
  public AdjListGraph() {
    adjList = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
  }

  /**
   * Reads the names of the actors in a file
   * 
   * @param file the file that is read
   * 
   * @throws IOException
   * @throws ParseException
   */
  @SuppressWarnings("rawtypes")
  public void storeNames(Path file) throws IOException, ParseException {
    try (BufferedReader reader = Files.newBufferedReader(file);
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
              addActors(actors.get(i), actors.get(j));
            }
          }
        }
      }
    }
  }

  /**
   * Adds coActors who worked with the actor Note: Undirected graph add
   * 
   * @param actor   the main actor
   * @param coActor the coActor
   */
  public void addActors(String actor, String coActor) {
    adjList.putIfAbsent(actor, new TreeSet<>(String.CASE_INSENSITIVE_ORDER));
    adjList.putIfAbsent(coActor, new TreeSet<>(String.CASE_INSENSITIVE_ORDER));

    adjList.get(actor).add(coActor);
    adjList.get(coActor).add(actor);
  }

  /**
   * Gets the neighbors of the actor
   * 
   * @param actor the actor to be searched
   * @return a list of coActors
   */
  public Set<String> neighbors(String actor) {
    if (!adjList.containsKey(actor)) {
      return null;
    }

    return adjList.get(actor);
  }

  /**
   * Performs breadth first search to find the shortest path between two actors
   * 
   * @param actor1 the starting actor (point)
   * @param actor2 the end actor (point)
   * 
   * @return the node that points to the end actor or null if not found
   */
  public Node BFS(String actor1, String actor2) {
    // Nodes to check
    Queue<Node> queue = new LinkedList<>();
    // Prevents checking the same actor again
    HashSet<String> visited = new HashSet<>();

    // If the name is null/empty or the name does not exist, return null
    if (actor1.isEmpty() || actor2.isEmpty() || !adjList.containsKey(actor1) || !adjList.containsKey(actor2)) {
      return null;
    }
    
    // If the actor is the same name
    if (actor1.toLowerCase().equals(actor2.toLowerCase())) {
      for (String actor : adjList.keySet()) {
        if (actor.toLowerCase().equals(actor1.toLowerCase())) {
          return new Node(actor, null);
        }
      }
    }

    // Add the first actor
    for (String actor : adjList.keySet()) {
      if (actor.toLowerCase().equals(actor1.toLowerCase())) {
        queue.add(new Node(actor, null));
        visited.add(actor);
      }
    }

    // Check if the node is connected
    while (!queue.isEmpty()) {
      Node prevActor = queue.remove();

      // If the node is found
      if (neighbors(prevActor.getName()).contains(actor2)) {
        // Return the last actor
        for (String actor : adjList.keySet()) {
          if (actor.toLowerCase().equals(actor2.toLowerCase())) {
            return new Node(actor, prevActor);
          }
        }

      }

      // If node is not found, add more nodes to the queue
      for (String actor : neighbors(prevActor.getName())) {
        if (!visited.contains(actor)) {
          queue.add(new Node(actor, prevActor));
          visited.add(actor);
        }
      }
    }

    return new Node(null, null);
  }

  /**
   * Prints the path of the actor to its end point
   * 
   * @param actor the actor node
   */
  public void printPath(Node actor) {
    if (actor == null) {
      System.out.println("There is no path between the actors or you listed an invalid actor.");

      return;
    }

    String path = actor.getName();

    Node curr = actor.getPrev();

    while (curr != null) {
      path = curr.getName() + " -> " + path;
      curr = curr.getPrev();
    }

    System.out.println(path);
  }
}
