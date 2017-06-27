package codingame.wondev_women;


import java.util.ArrayList;
import java.util.Scanner;

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
class Player {

  public static void main(String args[]) {
    Scanner in = new Scanner(System.in);
    int size = in.nextInt();
    int unitsPerPlayer = in.nextInt();

    Game game = new Game(size, unitsPerPlayer);

    // game loop
    while (true) {
      for (int i = 0; i < size; i++) {
        String row = in.next();
        game.readLine(i, row);
      }
      for (int i = 0; i < unitsPerPlayer; i++) {
        int unitX = in.nextInt();
        int unitY = in.nextInt();
        game.readUnit(0, i, unitX, unitY);
      }
      for (int i = 0; i < unitsPerPlayer; i++) {
        int otherX = in.nextInt();
        int otherY = in.nextInt();
        game.readUnit(1, i, otherX, otherY);
      }
      int legalActions = in.nextInt();
      game.actions = new ArrayList(legalActions);
      for (int i = 0; i < legalActions; i++) {
        String atype = in.next();
        int index = in.nextInt();
        String dir1 = in.next();
        String dir2 = in.next();
        game.actions.add(new Action(atype, index, dir1, dir2));
      }

      game.calculateScore();

      // Write an action using System.out.println()
      // To debug: System.err.println("Debug messages...");
      // System.err.println(game);
      System.out.println(game.getBestAction().toAction());
    }
  }
}