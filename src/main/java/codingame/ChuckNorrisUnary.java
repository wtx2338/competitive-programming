package codingame;

import java.util.Scanner;

/**
 * Created by swu on 14/03/17.
 */
public class ChuckNorrisUnary {
  public static void main(String args[]) {
    Scanner in = new Scanner(System.in);
    String MESSAGE = in.nextLine();
    StringBuilder binary = new StringBuilder();
    StringBuilder result = new StringBuilder();

    for (char c : MESSAGE.toCharArray()) {
      String bit = Integer.toBinaryString(Character.valueOf(c));
      for (int i = 0; i < 7 - bit.length(); i++) binary.append('0');
      binary.append(bit);
    }
    char first = binary.charAt(0);
    if (first == '0') {
      result.append("00 0");
    } else {
      result.append("0 0");
    }
    for (int i = 1; i < binary.length(); i ++) {
      char b = binary.charAt(i);
      if (b == first) {
        result.append("0");
      } else {
        result.append(" ");
        first = binary.charAt(i);
        if (first == '0') {
          result.append("00 0");
        } else {
          result.append("0 0");
        }
      }
    }

    // Write an action using System.out.println()
    // To debug: System.err.println("Debug messages...");

    System.out.println(result.toString());
  }
}