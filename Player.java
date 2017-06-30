import java.util.Scanner;
import java.util.Collections;
import java.util.Comparator;
import java.util.ArrayList;
class Player {
	private static class Vector {
	    private static String doubleToString(double d) {
	        return String.format("%.3f", d);
	    }
	    public final double x;
	    public final double y;
	    public static double COMPARISON_TOLERANCE = 0.0000001;
	    public Vector(Coord coord) {
	        this(coord.x, coord.y);
	    }
	    public Vector(double x, double y) {
	        this.x = x;
	        this.y = y;
	    }
	    public Vector(Vector other) {
	        this(other.x, other.y);
	    }
	    public Vector add(Vector other) {
	        return new Vector(x + other.x, y + other.y);
	    }
	    public Vector negate() {
	        return new Vector(-x, -y);
	    }
	    public Vector rotateInDegree(double degree){
	    	return rotateInRadian(Math.toRadians(degree));
	    }
	    public Vector rotateInRadian(double radians) {
	        final double length = length();
	        double angle = angleInRadian();
	        angle += radians;
	        final Vector result = new Vector(Math.cos(angle), Math.sin(angle));
	        return result.multiply(length);
	    }
	    public double angleInDegree() {
	        return Math.toDegrees(angleInRadian());
	    }
		private double angleInRadian() {
			return Math.atan2(y, x);
		}
	    public double dot(Vector other) {
	        return x * other.x + y * other.y;
	    }
	    @Override
	    public boolean equals(Object obj) {
	        if (this == obj) {
	            return true;
	        }
	        if (obj == null) {
	            return false;
	        }
	        if (getClass() != obj.getClass()) {
	            return false;
	        }
	        final Vector other = (Vector) obj;
	        if (Math.abs(x - other.x) > COMPARISON_TOLERANCE) {
	            return false;
	        }
	        if (Math.abs(y - other.y) > COMPARISON_TOLERANCE) {
	            return false;
	        }
	        return true;
	    }
	    @Override
	    public int hashCode() {
	        final int prime = 31;
	        int result = 1;
	        long temp;
	        temp = Double.doubleToLongBits(x);
	        result = prime * result + (int) (temp ^ (temp >>> 32));
	        temp = Double.doubleToLongBits(y);
	        result = prime * result + (int) (temp ^ (temp >>> 32));
	        return result;
	    }
	    public double length() {
	        return Math.sqrt(x * x + y * y);
	    }
	    public double length2() {
	        return x * x + y * y;
	    }
	    public Vector minus(Vector other) {
	        return new Vector(x - other.x, y - other.y);
	    }
	    public Vector multiply(double factor) {
	        return new Vector(x * factor, y * factor);
	    }
	    public Vector norm() {
	        final double length = length();
	        if (length>0)
	        	return new Vector(x / length, y / length);
	        return new Vector(0,0);
	    }
	    public Vector ortho() {
	        return new Vector(-y, x);
	    }
	    @Override
	    public String toString() {
	        return "[x=" + doubleToString(x) + ", y=" + doubleToString(y) + "]";
	    }
	}
	private static class Coord {
	    public final int x;
	    public final int y;
	    public Coord(int x, int y) {
	        this.x = x;
	        this.y = y;
	    }
	    public Coord(Vector v) {
	        this.x = (int) v.x;
	        this.y = (int) v.y;
	    }
	    public Coord add(Coord coord) {
	        return new Coord(x + coord.x, y + coord.y);
	    }
	    public double distance(Coord coord) {
	        return Math.sqrt(distanceSquare(coord));
	    }
	    public long distanceSquare(Coord coord) {
	        long dx = coord.x - x;
	        long dy = coord.y - y;
	        return dx * dx + dy * dy;
	    }
	    public Coord minus(Coord coord) {
	        return new Coord(x - coord.x, y - coord.y);
	    }
	    @Override
	    public boolean equals(Object obj) {
	        if (this == obj) {
	            return true;
	        }
	        if (obj == null) {
	            return false;
	        }
	        if (getClass() != obj.getClass()) {
	            return false;
	        }
	        final Coord other = (Coord) obj;
	        if (x != other.x) {
	            return false;
	        }
	        if (y != other.y) {
	            return false;
	        }
	        return true;
	    }
	    @Override
	    public int hashCode() {
	        final int prime = 31;
	        int result = 1;
	        result = prime * result + x;
	        result = prime * result + y;
	        return result;
	    }
	    @Override
	    public String toString() {
	        return "[x=" + x + ", y=" + y + "]";
	    }
	}
	private static class Game {
	  int MY_ID = 0;
	  int NB_PLAYER = 2;
	  char[][] map;
	  ArrayList<Coord[]> units = new ArrayList(NB_PLAYER);
	  int size;
	  int units_per_player;
	  ArrayList<Action> actions;
	  private int eIndex = -1;
	  private Coord ePos = null;
	  Game(int size, int units_per_player) {
	    this.size = size;
	    this.map = new char[size][size];
	    this.units_per_player = units_per_player;
	    units.add(new Coord[units_per_player]);
	    units.add(new Coord[units_per_player]);
	  }
	  void readLine(int i, String row) {
	    for(int j = 0; j < row.length(); j++) {
	      char c = row.charAt(j);
	      map[i][j] = c;
	    }
	  }
	  void readUnit(int idPlayer, int index,  int x, int y) {
	    Coord coord = new Coord(x, y);
	    units.get(idPlayer)[index] = coord;
	  }
	  void calculateScore() {
	    for (int i = 0; i < actions.size(); i++) {
	      Action a = actions.get(i);
	      this.performAction(a);
	      a.score= calculate(a);
	      this.reverseAction(a);
	    }
	  }
	  Action sortBestAction() {
	    Collections.sort(actions, (a1, a2) -> {
	      if (a1.score - a2.score > 0)
	        return -1;
	      else if (a1.score - a2.score < 0)
	        return 1;
	      else
	        return 0;
	    });
	    return actions.get(0);
	  }
	  double calculate(Action a) {
	    double score = 0;
	    score += myUnitScore();
	    score += eUnitScore();
	    score += mapScore();
	    if (a.isPush()) { score +=  5; }
	    return score;
	  }
	  double myUnitScore() {
	    Coord[] myUnit = units.get(0);
	    for (int i = 0; i < units_per_player  ; i++) {
	      Coord cori = myUnit[i];
	      char newPlace = map[cori.y][cori.x];
	      if (newPlace != '4') {
	        return Math.pow(newPlace - '0', 2);
	      }
	    }
	    return 0;
	  }
	  double eUnitScore() {
	    double score = 0;
	    Coord[] eUnit = units.get(1);
	    for (int i = 0; i < units_per_player  ; i++) {
	      Coord cori = eUnit[i];
	      if (cori.y != -1 && cori.x != -1) {
	        char newPlace = map[cori.y][cori.x];
	        int h = newPlace - '0';
	        score += Math.pow(3 - h, 2);
	      }
	    }
	    return 0;
	  }
	  double mapScore() {
	    double score = 0;
	    for(int i = 0; i < size; i++) {
	      for(int j = 0; j < size; j++) {
	        if (map[i][j] != '4' && map[i][j] != '.' ) {
	          score += Math.pow(map[i][j] - '0', 2);
	        }
	      }
	    }
	    return score;
	  }
	  void performAction(Action a) {
	    Coord[] myUnit = units.get(0);
	    if (a.isPush()) {
	      Coord[] eUnit = units.get(1);
	      Coord eCoord = newCoord(myUnit[a.index], a.dir1);
	      int i = 0;
	      boolean found = false;
	      while(i < eUnit.length && !found) {
	        if(eUnit[i].equals(eCoord)) {
	          eIndex = i;
	          ePos = eUnit[i];
	          eUnit[i] = newCoord(eCoord, a.dir2);
	          found = true;
	        }
	        i++;
	      }
	    } else {
	      myUnit[a.index] = newCoord(myUnit[a.index], a.dir1);
	      Coord newBlockCor = newCoord(myUnit[a.index], a.dir2);
	      build(newBlockCor);
	    }
	  }
	  void build(Coord cor) {
	    if(this.map[cor.y][cor.x] != '.') {
	      this.map[cor.y][cor.x] += 1;
	    }
	  }
	  void destroy(Coord cor) {
	    if(this.map[cor.y][cor.x] > '0') {
	      this.map[cor.y][cor.x] -= 1;
	    }
	  }
	  void reverseAction(Action a) {
	    Coord[] myUnit = units.get(0);
	    if (a.isPush()) {
	      Coord[] eUnit = units.get(1);
	      eUnit[eIndex] = ePos;
	    } else {
	      Coord newBlockCor = newCoord(myUnit[a.index], a.dir2);
	      destroy(newBlockCor);
	      myUnit[a.index] = oldCoord(myUnit[a.index], a.dir1);
	    }
	  }
	  static Coord newCoord(Coord cor, String dir) {
	    switch (dir) {
	      case "E":
	        return new Coord(cor.x + 1, cor.y);
	      case "W":
	        return new Coord(cor.x - 1, cor.y);
	      case "N":
	        return new Coord(cor.x, cor.y - 1);
	      case "NE":
	        return new Coord(cor.x + 1, cor.y - 1);
	      case "NW":
	        return new Coord(cor.x - 1, cor.y - 1);
	      case "S":
	        return new Coord(cor.x, cor.y + 1);
	      case "SE":
	        return new Coord(cor.x + 1, cor.y + 1);
	      case "SW":
	        return new Coord(cor.x - 1, cor.y + 1);
	    }
	    return null;
	  }
	  static Coord oldCoord(Coord cor, String dir) {
	    switch (dir) {
	      case "E":
	        return new Coord(cor.x - 1, cor.y);
	      case "W":
	        return new Coord(cor.x + 1, cor.y);
	      case "N":
	        return new Coord(cor.x, cor.y + 1);
	      case "NE":
	        return new Coord(cor.x - 1, cor.y + 1);
	      case "NW":
	        return new Coord(cor.x + 1, cor.y + 1);
	      case "S":
	        return new Coord(cor.x, cor.y - 1);
	      case "SE":
	        return new Coord(cor.x - 1, cor.y - 1);
	      case "SW":
	        return new Coord(cor.x + 1, cor.y - 1);
	    }
	    return null;
	  }
	  public String toString() {
	    String s = "";
	    s += "Map:\n";
	    for(int i = 0; i < size; i++) {
	      for(int j = 0; j < size; j++) {
	        s += map[i][j] + " ";
	      }
	      s += "\n";
	    }
	    for (int i = 0; i < 2 ; i++) {
	      s += "Player" + i +" \n";
	      for (int j = 0; j < units_per_player; j++) {
	        s += units.get(i)[j];
	      }
	      s+= "\n";
	    }
	    s += "\n";
	    s += actions.toString();
	    return s;
	  }
	}
	private static class Action {
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
  public static void main(String args[]) {
    boolean debug = true;
    Scanner in = new Scanner(System.in);
    int size = in.nextInt();
    int unitsPerPlayer = in.nextInt();
    if(debug) System.err.println(size + " " + unitsPerPlayer);
    Game game = new Game(size, unitsPerPlayer);
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
      System.out.println(game.actions.get(0).toAction());
    }
  }
}
