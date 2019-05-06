import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

public class GraphImplementation implements Graph {

  public int[][] adjMatrix;
  public int vertices;
  public int edges;

  public GraphImplementation(int vertices) {
    adjMatrix = new int[vertices][vertices];
    this.vertices = vertices;
    this.edges = 0;
  }

  public void addEdge(int v1, int v2) {
    adjMatrix[v1][v2] = 1;
    edges++;
  }

  public List<Integer> topologicalSort() {
    int[][] temp = new int[vertices][vertices];

    for (int i = 0; i < vertices; i++) {
      for (int j = 0; j < vertices; j++) {
        temp[i][j] = adjMatrix[i][j];
      }
    }

    Set<Integer> visitedValue = new HashSet<Integer>();
    List<Integer> values = new ArrayList<>();

    for (int f = 0; f < vertices; f++) {
      for (int i = 0; i < vertices; i++) {
        boolean isRemovable = true;

        for (int j = 0; j < vertices; j++) {
          if (temp[j][i] > 0) {
            isRemovable = false;
            break;
          }
        }

        if (isRemovable && !visitedValue.contains(i)) {
          visitedValue.add(i);
          values.add(i);

          for (int k = 0; k < vertices; k++) {
            temp[i][k] = 0;
          }

          break;
        }
      }
    }

    return values;
  }

  public int[] neighbors(int vertex) {
    int count = 0;
    int neighborCount = 0;

    for (int i = 0; i < vertices; i++) {
      if (adjMatrix[vertex][i] > 0) {
        count++;
      }
    }

    int[] neighbors = new int[count];

    for (int i = 0; i < vertices; i++) {
      if (adjMatrix[vertex][i] > 0) {
        neighbors[neighborCount++] = i;
      }
    }

    return neighbors;
  }
}
