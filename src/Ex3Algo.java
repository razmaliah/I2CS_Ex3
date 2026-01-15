
import exe.ex3.game.Game;
import exe.ex3.game.GhostCL;
import exe.ex3.game.PacManAlgo;
import exe.ex3.game.PacmanGame;

import java.awt.*;
import java.util.ArrayList;

/**
 * This is the major algorithmic class for Ex3 - the PacMan game:
 *
 * This code is a very simple example (random-walk algorithm).
 * Your task is to implement (here) your PacMan algorithm.
 */
public class Ex3Algo implements PacManAlgo {
    private int _count;
    private static final int OBS_VALUE = 1;
    private static final int FOOD_VALUE = 3;
    private static final int POWER_VALUE = 5;
    private static Index2D _pacPos = null;
    private static Pixel2D[] _ghostsPos = null;
    private static boolean _isEatable = false;

    public Ex3Algo() {
        _count = 0;
    }

    @Override
    /**
     *  Add a short description for the algorithm as a String.
     */
    public String getInfo() {
        return null;
    }

    @Override
    /**
     * This ia the main method - that you should design, implement and test.
     */
    public int move(PacmanGame game) {
        if (_count == 0 || _count == 300) {
            int code = 0;
            int[][] board = game.getGame(0);
            printBoard(board);
            int blue = Game.getIntColor(Color.BLUE, code);
            int pink = Game.getIntColor(Color.PINK, code);
            int black = Game.getIntColor(Color.BLACK, code);
            int green = Game.getIntColor(Color.GREEN, code);
            System.out.println("Blue=" + blue + ", Pink=" + pink + ", Black=" + black + ", Green=" + green);
            String pos = game.getPos(code).toString();
            System.out.println("Pacman coordinate: " + pos);
            GhostCL[] ghosts = game.getGhosts(code);
            printGhosts(ghosts);
            int up = Game.UP, left = Game.LEFT, down = Game.DOWN, right = Game.RIGHT;
        }
        _count++;
        Pixel2D[] cPP = closestPP(game);
        updatePac(game);
        updateGhosts(game);
        Pixel2D start = _pacPos;
        Pixel2D next = cPP[1];
        int dir = getDir(start,next);
        updateGhosts(game);
        return dir;
    }

    private static void printBoard(int[][] b) {
        for (int y = 0; y < b[0].length; y++) {
            for (int x = 0; x < b.length; x++) {
                int v = b[x][y];
                System.out.print(v + "\t");
            }
            System.out.println();
        }
    }

    private static void printGhosts(GhostCL[] gs) {
        for (int i = 0; i < gs.length; i++) {
            GhostCL g = gs[i];
            System.out.println(i + ") status: " + g.getStatus() + ",  type: " + g.getType() + ",  pos: " + g.getPos(0) + ",  time: " + g.remainTimeAsEatable(0));
        }
    }

    private static int randomDir() {
        int[] dirs = {Game.UP, Game.LEFT, Game.DOWN, Game.RIGHT};
        int ind = (int) (Math.random() * dirs.length);
        return dirs[ind];
    }

    ///  ///////////////////////////////////// My private methods  /////////////////////////////////////

    private static Pixel2D[] closestPP(PacmanGame game) {
        Pixel2D[] ans = null;
        int[][] arr2D = game.getGame(0);
        updatePac(game);
        Index2D pacPos = _pacPos;
        Map board = new Map(arr2D);
        Map2D allD = board.allDistance(pacPos, OBS_VALUE);
        Pixel2D target = closestFood(board, allD);
        ans = board.shortestPath(pacPos, target, OBS_VALUE);
        return ans;
    }


    private static void updatePac(PacmanGame game) {
        String pos = game.getPos(1);
        int x = Integer.parseInt(pos.split(",")[0]);
        int y = Integer.parseInt(pos.split(",")[1]);
        Index2D p1 = new Index2D(x, y);
        _pacPos = p1;
    }

    private static Index2D closestFood(Map2D board, Map2D allD) {
        Index2D ans = new Index2D(0,0);
        allD.setPixel(ans,1000); // set an obstacle to large value
        for (int i = 0; i < board.getWidth(); i++) {
            for (int j = 0; j < board.getHeight(); j++) {
                Pixel2D pc = new Index2D(i, j);
                if (board.getPixel(pc) == FOOD_VALUE && allD.getPixel(pc) < allD.getPixel(ans)) {
                    ans = new Index2D(pc);
                }
            }
        }
        return ans;
    }

    private static int getDir(Pixel2D start, Pixel2D next) {
        if(next.getX() == start.getX() + 1 || start.getX() - next.getX() > 1){return Game.RIGHT;}
        if(next.getX() + 1 == start.getX() || next.getX() - start.getX() > 1){return Game.LEFT;}
        if(next.getY() == start.getY() + 1 || start.getY() - next.getY() > 1){return Game.UP;}
        if(next.getY() + 1 == start.getY() || next.getY() - start.getY() > 1){return Game.DOWN;}
        return Game.UP;
    }

    private static void updateGhosts(PacmanGame game) {
        GhostCL[] ghosts = game.getGhosts(0);
        Pixel2D[] ghostPositions = new Pixel2D[ghosts.length];
        for (int i = 0; i < ghosts.length; i++) {
            ghostPositions[i] = ghostPos(ghosts[i]);
        }
        _ghostsPos = ghostPositions;
        double eat = game.getGhosts(1)[1].remainTimeAsEatable(0);
        if(eat > 0){_isEatable = true;}
        else{_isEatable = false;}
    }

    private static Index2D ghostPos (GhostCL g){
        String pos = g.getPos(1);
        int x = Integer.parseInt(pos.split(",")[0]);
        int y = Integer.parseInt(pos.split(",")[1]);
        Index2D p1 = new Index2D(x, y);
        return p1;
    }

    private static int[] ghostDist(PacmanGame game){
        int[] dists = new int[_ghostsPos.length];
        int[][] arr2D = game.getGame(0);
        Index2D pacPos = _pacPos;
        Map board = new Map(arr2D);
        Map2D allD = board.allDistance(pacPos, OBS_VALUE);
        for (int i = 0; i < _ghostsPos.length; i++) {
            Pixel2D gPos = _ghostsPos[i];
            dists[i] = allD.getPixel(gPos);
        }
        return dists;
    }

    private static int indexClosestGhost(int[] dists){
        int ans = dists[0];
        for (int i = 1; i < dists.length; i++) {
            if (dists[i] < ans) {
                ans = i;
            }
        }
        return ans;
    }

}