import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class adjListGraph {

  private Map<String, Set<String>> adjList;

  /** Constructor */
  public adjListGraph() {
    adjList = new HashMap<>();
  }

  /**
   * Adds coActors who worked with the actor Note: Undirected graph add
   * 
   * @param actor   the main actor
   * @param coActor the coActor
   */
  public void addActors(String actor, String coActor) {
    adjList.putIfAbsent(actor, new HashSet<>());
    adjList.putIfAbsent(coActor, new HashSet<>());

    adjList.get(actor).add(coActor);
    adjList.get(coActor).add(actor);
  }

  public Set<String> neighbors(String actor) {
    if (!adjList.containsKey(actor)) {
      return null;
    }

    return adjList.get(actor);
  }

  public void printGraph() {
    for (String key : adjList.keySet()) {
      System.out.println(key + " = " + adjList.get(key));
    }
  }
}
