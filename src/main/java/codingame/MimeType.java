package codingame;

import java.util.HashMap;
import java.util.Scanner;

/**
 * Created by swu on 23/02/2017.
 *
 * Input:
 * 2 // number of elements make up the association table
   4 // nb of file name to be analyzed

   html text/html
   png image/png
   test.html
   noextension
   portrait.png
   doc.TXT

 * Output:
 * text/html
   UNKNOWN
   image/png
   UNKNOWN
 *
 */
public class MimeType {
  public static void main(String args[]) {
    Scanner in = new Scanner(System.in);
    int N = in.nextInt(); // Number of elements which make up the association table.
    int Q = in.nextInt(); // Number Q of file names to be analyzed.

    HashMap<String, String> mappingTable = new HashMap<String, String> ();

    for (int i = 0; i < N; i++) {
      String EXT = in.next(); // file extension
      String MT = in.next(); // MIME type.

      mappingTable.put(EXT.toLowerCase(), MT);
    }
    in.nextLine();
    for (int i = 0; i < Q; i++) {
      String FNAME = in.nextLine().toLowerCase(); // One file name per line.
      String mapping = "UNKNOWN";
      String[] split = FNAME.split("\\.");
      if (split.length > 1) {
        String type = mappingTable.get(split[split.length - 1]);
        if (type != null && FNAME.endsWith(type)) mapping = type;
      }
      System.out.println(mapping);
    }

    // Write an action using System.out.println()
    // To debug: System.err.println("Debug messages...");


    // For each of the Q filenames, display on a line the corresponding MIME type. If there is no corresponding type, then display UNKNOWN.

  }
}
