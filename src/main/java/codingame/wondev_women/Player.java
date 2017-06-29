package codingame.wondev_women;


import java.util.ArrayList;
import java.util.Scanner;

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
class Player {

  public static void main(String args[]) {
    boolean debug = true;
    Scanner in = new Scanner(System.in);
    int size = in.nextInt();
    int unitsPerPlayer = in.nextInt();
    if(debug) System.err.println(size + " " + unitsPerPlayer);

    Game game = new Game(size, unitsPerPlayer);
    // game loop
    while (true) {
      for (int i = 0; i < size; i++) {
        String row = in.next();
        if(debug) System.err.print(row + " ");
        game.readLine(i, row);
      }
      for (int i = 0; i < unitsPerPlayer; i++) {
        int unitX = in.nextInt();
        int unitY = in.nextInt();
        game.readUnit(0, i, unitX, unitY);
        if(debug) System.err.print(unitX + " ");
        if(debug) System.err.print(unitY + " ");
      }
      for (int i = 0; i < unitsPerPlayer; i++) {
        int otherX = in.nextInt();
        int otherY = in.nextInt();
        game.readUnit(1, i, otherX, otherY);
        if(debug) System.err.print(otherX + " ");
        if(debug) System.err.print(otherY + " ");

      }
      int legalActions = in.nextInt();
      game.actions = new ArrayList(legalActions);
      if(debug) System.err.print(legalActions + " ");

      for (int i = 0; i < legalActions; i++) {
        String atype = in.next();
        int index = in.nextInt();
        String dir1 = in.next();
        String dir2 = in.next();
        game.actions.add(new Action(atype, index, dir1, dir2));
        if(debug) System.err.print(atype + " ");
        if(debug) System.err.print(index + " ");
        if(debug) System.err.print(dir1 + " ");
        if(debug) System.err.print(dir2 + " ");
      }

      game.calculateScore();
      game.sortBestAction();
      if(debug) System.err.println(game.toString());
      // Write an action using System.out.print()
      // To debug: System.err.print("Debug messages...");
      // System.err.print(game);
      System.out.println(game.actions.get(0).toAction());
    }
  }
}