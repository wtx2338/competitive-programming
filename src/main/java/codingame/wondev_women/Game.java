package codingame.wondev_women;

import competitive.programming.geometry.Coord;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by swu on 27/06/2017.
 */
public class Game {
  int NB_PLAYER = 2;
  char[][] map;
  ArrayList<Coord[]> units = new ArrayList(NB_PLAYER);
  int size;
  int units_per_player;
  ArrayList<Action> actions;

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
      a.score= calculate();
      this.reverseAction(a);
    }
  }

  Action getBestAction() {
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

  double calculate() {
    double score = 0;
    Coord[] myUnit = units.get(0);
    for (int i = 0; i < units_per_player  ; i++) {
      Coord cori = myUnit[i];
      if (map[cori.x][cori.y] != '4') {
        score += Math.sqrt(map[cori.x][cori.y] - '0');
      }
    }
    for(int i = 0; i < size; i++) {
      for(int j = 0; j < size; j++) {
        if (map[i][j] != '4') {
          score += Math.sqrt(map[i][j] - '0');
        }
      }
    }
    return score;
  }

  void performAction(Action a) {
    Coord[] myUnit = units.get(0);
    myUnit[a.index] = newCoord(myUnit[a.index], a.dir1);
    Coord newBlockCor = newCoord(myUnit[a.index], a.dir2);
    build(newBlockCor);
  }

  void build(Coord cor) {
    if(this.map[cor.x][cor.y] != '.') {
      this.map[cor.x][cor.y] += 1;
    }
  }

  void destroy(Coord cor) {
    if(this.map[cor.x][cor.y] > '0') {
      this.map[cor.x][cor.y] -= 1;
    }
  }

  void reverseAction(Action a) {
    Coord[] myUnit = units.get(0);
    Coord newBlockCor = newCoord(myUnit[a.index], a.dir2);
    destroy(newBlockCor);
    myUnit[a.index] = oldCoord(myUnit[a.index], a.dir1);
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
