import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.LinkedList;

public class Solution {

  // Input data: initially all island area is designated with '1' and all water area with '0'.
  public int[][] matrix;

  // Keys: consequtive integers for each island, startign from '2'. Values: islands areas.
  public Map<Integer, Integer> islandKey_toArea;
  public int[][] moves = {
    {-1, 0}, // up
    {1, 0}, // down
    {0, -1}, // left
    {0, 1} // right
  };

  /*
    By the problem design on binarysearch.com, we have to work
    around the given method 'public int solve(int[][] matrix)' in order for the code
    to be able to run on the website. Even though the name 'solve' does not make
    a lot of sense, it is left as it is, so that the code can be run directly on the website,
    without any modifications.
  */
  public int solve(int[][] matrix) {
    this.matrix = matrix;
    findIslandsArea();

    // There is no land area. Therefore, just return one cell that can be turned from water to land;
    if (islandKey_toArea.size() == 0) {
      return 1;
    }

    // There is no water area. Therefore, just return the land area without any additions.
    if (islandKey_toArea.get(2) == matrix.length * matrix[0].length) {
      return islandKey_toArea.get(2);
    }

    return findLargestIslandArea();
  }

  
  //@return The largest island area (a single island or a groupd of connected islands).
  public int findLargestIslandArea() {

    int largestIslandArea = 0;

    for (int r = 0; r < matrix.length; r++) {
      for (int c = 0; c < matrix[0].length; c++) {
        if (matrix[r][c] == 0) {
          largestIslandArea = Math.max(largestIslandArea, checkForLandAreaNearTheWaterCell(r, c));
        }
      }
    }
    return largestIslandArea;
  }

  /* A helper method to method 'bfs_water(int r, int c)'.
     Checks for island area next to the water cell, in all four directions.

                            (up)island or water
                                    |
       (left)island or water-- one water cell -- (right)island or water
                                    |
                            (down)island or water

     @return 1. If connected islands are found (two, three or four islands), it returns the connected area, i.e.:
                islands' areas plus '1' (for the water cell that is truned into a land cell).
             2. If only a single island is found next to the water cell, it returns the island area plus + '1'.
             3. If the water cell is completely surrounded by other water cells, it returns '0'.
  */
  public int checkForLandAreaNearTheWaterCell(int r, int c) {

    Set<Integer> islandsAroundTheWaterCell = new HashSet<Integer>();

    for (int i = 0; i < moves.length; i++) {
      int new_r = r + moves[i][0];
      int new_c = c + moves[i][1];

      if (isInMatrix(new_r, new_c) && matrix[new_r][new_c] != 0) {
        islandsAroundTheWaterCell.add(matrix[new_r][new_c]);
      }
    }

    int area = 0;
    for (int n : islandsAroundTheWaterCell) {
      area = area + islandKey_toArea.get(n);
    }

    return area > 0 ? area + 1 : 0;
  }

  // The method finds each island's area and designates a unique integer(islandKey) to each island.
  public void findIslandsArea() {

    islandKey_toArea = new HashMap<Integer, Integer>();
    int islandKey = 2;

    for (int r = 0; r < matrix.length; r++) {
      for (int c = 0; c < matrix[0].length; c++) {
        if (matrix[r][c] == 1) {
          bfs_islands(r, c, islandKey);
          islandKey++;
        }
      }
    }
  }

  /*
    Breadth First Search: a helper method to method 'mapIslands_findIslandsArea()'.
    Maps an island, starting from the given input, calculates its area and designates a unique key to it.
  */
  public void bfs_islands(int r, int c, int islandKey) {

    LinkedList<int[][]> coordinates = new LinkedList<int[][]>();
    coordinates.add(new int[][] {{r, c}});

    matrix[r][c] = islandKey;
    int area = 1;

    while (!coordinates.isEmpty()) {
      int[][] current = coordinates.removeFirst();
      r = current[0][0];
      c = current[0][1];

      for (int i = 0; i < moves.length; i++) {
        int new_r = r + moves[i][0];
        int new_c = c + moves[i][1];

        if (isInMatrix(new_r, new_c) && matrix[new_r][new_c] == 1) {
          matrix[new_r][new_c] = islandKey;
          coordinates.add(new int[][] {{new_r, new_c}});
          area++;
        }
      }
    }
    islandKey_toArea.put(islandKey, area);
  }

  public boolean isInMatrix(int r, int c) {
    if (r < 0 || c < 0 || r > matrix.length - 1 || c > matrix[0].length - 1) {
      return false;
    }
    return true;
  }
}
