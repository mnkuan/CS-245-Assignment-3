
/**
 * The node class that holds the name and the previous actor
 * 
 * @author Marco
 */
public class Node {

  /** The name of the actor/node */
  private String name;
  /** The previous actor/node */
  private Node prev;

  /**
   * Constructor
   * 
   * @param name the name of the node
   * @param prev
   */
  public Node(String name, Node prev) {
    this.name = name;
    this.prev = prev;
  }

  /**
   * Gets the name of the actor/node
   * 
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * Gets the previous node that it points to
   * 
   * @return the previous node
   */
  public Node getPrev() {
    return prev;
  }
}