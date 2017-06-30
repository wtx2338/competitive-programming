package codingame.wondev_women;

/**
 * Created by swu on 27/06/2017.
 */
public class Action {
  String type;
  int index;
  String dir1;
  String dir2;
  double score;

  Action(String t, int i, String d1, String d2) {
    type = t;
    index = i;
    dir1 = d1;
    dir2 = d2;
  }

  boolean isPush() {
    return type.equals("PUSH&BUILD");
  }

  public String toString() {
    return type + " " + index + " " + dir1 + " " + dir2 + " " + score + "\n";
  }

  public String toAction() {
    return type + " " + index + " " + dir1 + " " + dir2;
  }
}
