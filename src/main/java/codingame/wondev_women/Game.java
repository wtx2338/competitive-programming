package codingame.wondev_women;

import competitive.programming.geometry.Coord;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by swu on 27/06/2017.
 */
public class Game {
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

  // My Unit want to get a higher place
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

  // Enemy's Unit want to get a lower place
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
      // Calculate new enemy position
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
