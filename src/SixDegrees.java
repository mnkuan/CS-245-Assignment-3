import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import org.json.simple.parser.ParseException;

/**
 * The SixDegrees class that calls the graph
 * 
 * @author Marco
 */
public class SixDegrees {

  /**
   * The main method
   * 
   * @param args the arguments
   */
  public static void main(String[] args) {
    Path path = Paths.get(args[0]);

    AdjListGraph graph = new AdjListGraph();

    try (Scanner scanner = new Scanner(System.in);) {
      String actor1;
      String actor2;

      graph.storeNames(path);

      System.out.print("Actor 1 name: ");
      actor1 = scanner.nextLine();

      System.out.print("Actor 2 name: ");
      actor2 = scanner.nextLine();

      graph.printPath(graph.BFS(actor1, actor2));
    } catch (IOException | ParseException e) {
      e.printStackTrace();
    }

    System.out.println();
  }
}
