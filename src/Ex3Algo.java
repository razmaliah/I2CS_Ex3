
import exe.ex3.game.Game;
import exe.ex3.game.GhostCL;
import exe.ex3.game.PacManAlgo;
import exe.ex3.game.PacmanGame;

import java.awt.*;

/**
 * This is the major algorithmic class for Ex3 - the PacMan game:
 *
 * This code is a very simple example (random-walk algorithm).
 * Your task is to implement (here) your PacMan algorithm.
 */
public class Ex3Algo implements PacManAlgo {
    private int _count;
    public static PacmanGame _game = null;
    public static Map2D _map = null;
    public static final int OBS_VALUE = 1;
    public static final int FOOD_VALUE = 3;
    public static final int POWER_VALUE = 5;
    public static Index2D _pacPos = null;
    public static Pixel2D[] _ghostsPos = null;
    public static boolean _isEatable = false;
    private static final int CLOSE_GHOST_DIST = 2;
    public static Pixel2D[] _greenDots = {new Index2D(3,5), new Index2D(3,18), new Index2D(19,5), new Index2D(19,18)};

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
        _game = game;
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
        updatePac();
        updateGhosts();
        updateMap();

        int distt = distToClosestGhost(ghostDist());
        if(distt < CLOSE_GHOST_DIST){
            if(_isEatable){
               return huntClosestGhostDir();
            }
            else{
               if(distToClosestGreenDot() < distToClosestGhost(ghostDist())){
                   return dirToClosestGreenDot();
               }
               else{
                   return run();
               }
            }
        }

        Pixel2D[] cPP = closestPP();
        Pixel2D start = _pacPos;
        Pixel2D next = cPP[1];
        int dir = getDir(start,next);
        updateGhosts();
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

    public static Pixel2D[] closestPP() {
        Pixel2D[] ans = null;
        updatePac();
        updateMap();
        Pixel2D target = closestFood();
        ans = _map.shortestPath(_pacPos, target, OBS_VALUE);
        return ans;
    }


    public static void updatePac() {
        String pos = _game.getPos(1);
        int x = Integer.parseInt(pos.split(",")[0]);
        int y = Integer.parseInt(pos.split(",")[1]);
        Index2D p1 = new Index2D(x, y);
        _pacPos = p1;
    }

    public static Index2D closestFood() {
        Index2D ans = new Index2D(0,0);
        updateMap();
        updatePac();
        Map2D allD = _map.allDistance(_pacPos, OBS_VALUE);
        allD.setPixel(ans,1000); // set an obstacle to large value
        for (int i = 0; i < _map.getWidth(); i++) {
            for (int j = 0; j < _map.getHeight(); j++) {
                Pixel2D pc = new Index2D(i, j);
                if (_map.getPixel(pc) == FOOD_VALUE && allD.getPixel(pc) < allD.getPixel(ans)) {
                    ans = new Index2D(pc);
                }
            }
        }
        return ans;
    }

    public static int getDir(Pixel2D start, Pixel2D next) {
        if(next.getX() == start.getX() + 1 || start.getX() - next.getX() > 1){return Game.RIGHT;}
        if(next.getX() + 1 == start.getX() || next.getX() - start.getX() > 1){return Game.LEFT;}
        if(next.getY() == start.getY() + 1 || start.getY() - next.getY() > 1){return Game.UP;}
        if(next.getY() + 1 == start.getY() || next.getY() - start.getY() > 1){return Game.DOWN;}
        return Game.UP;
    }

    public static void updateGhosts() {
        GhostCL[] ghosts = _game.getGhosts(0);
        Pixel2D[] ghostPositions = new Pixel2D[ghosts.length];
        for (int i = 0; i < ghosts.length; i++) {
            ghostPositions[i] = ghostPos(ghosts[i]);
        }
        _ghostsPos = ghostPositions;
        double eat = _game.getGhosts(1)[0].remainTimeAsEatable(0);
        if(eat > 0){_isEatable = true;}
        else{_isEatable = false;}
    }

    public static Index2D ghostPos (GhostCL g){
        String pos = g.getPos(1);
        int x = Integer.parseInt(pos.split(",")[0]);
        int y = Integer.parseInt(pos.split(",")[1]);
        Index2D p1 = new Index2D(x, y);
        return p1;
    }

    public static int[] ghostDist(){
        updateMap();
        updateGhosts();
        updatePac();
        int[] dists = new int[_ghostsPos.length];
        Map2D allD = _map.allDistance(_pacPos, OBS_VALUE);
        for (int i = 0; i < _ghostsPos.length; i++) {
            Pixel2D gPos = _ghostsPos[i];
            int val = distToClosestGhost(ghostDist());
            if (val < 1){
                dists[i] = 100;
            }
            else{
                dists[i] = allD.getPixel(gPos);
            }
        }
        return dists;
    }

    public static int indexClosestGhost(int[] dists){
        int ans = 0;
        for (int i = 1; i < dists.length; i++) {
            if (dists[i] < ans) {
                ans = i;
            }
        }
        return ans;
    }

    public static int distToClosestGhost(int[] dists){
        int ind = indexClosestGhost(dists);
        return dists[ind];
    }

    public static int huntClosestGhostDir(){
        updatePac();
        updateGhosts();
        updateMap();
        int ind = indexClosestGhost(ghostDist());
        Pixel2D target = _ghostsPos[ind];
        Pixel2D start = _pacPos;
        Pixel2D[] SP = _map.shortestPath(start, target, OBS_VALUE);
        target = SP[1];
        return getDir(start,target);
    }

    public static int distToClosestGreenDot(){
        int ans = 100;
        updateMap();
        updatePac();
        Map2D allD = _map.allDistance(_pacPos, OBS_VALUE);
        for (int i = 0; i < _greenDots.length; i++) {
            if(_map.getPixel(_greenDots[i]) == POWER_VALUE){
                int d = allD.getPixel(_greenDots[i]);
                if(d < ans){
                    ans = d;
                }
            }
        }
        return ans;
    }

    public int dirToClosestGreenDot(){
        updateMap();
        updatePac();
        Map2D allD = _map.allDistance(_pacPos, OBS_VALUE);
        Pixel2D target = null;
        int ansDist = 100;
        for (int i = 0; i < _greenDots.length; i++) {
            if(_map.getPixel(_greenDots[i]) == POWER_VALUE){
                int d = allD.getPixel(_greenDots[i]);
                if(d < ansDist){
                    ansDist = d;
                    target = _greenDots[i];
                }
            }
        }
        Pixel2D start = _pacPos;
        Pixel2D[] SP = _map.shortestPath(start, target, OBS_VALUE);
        target = SP[1];
        return getDir(start,target);
    }

    public static int distBetween(Pixel2D p1, Pixel2D p2){
        Map2D allD = _map.allDistance(p1, OBS_VALUE);
        return allD.getPixel(p2);
    }

    public static void updateMap(PacmanGame game){
        if (_game == null){ return; }
        int[][] arr2D = game.getGame(0);
        _map = new Map(arr2D);
    }

    public static void updateMap(){
        updateMap(_game);
    }

    public static int run() {
        updatePac();
        updateGhosts();
        updateMap();
        int ind = indexClosestGhost(ghostDist());
        Pixel2D target = _ghostsPos[ind];
        Pixel2D start = _pacPos;
        Pixel2D[] SP = _map.shortestPath(start, target, OBS_VALUE);
        target = SP[1];
        int dirGhost = getDir(start, target);
        if (dirGhost == Game.UP) {
            return runToDown();
        }
        if (dirGhost == Game.LEFT) {
            return runToRight();
        }
        if (dirGhost == Game.DOWN) {
            return runToUp();
        }
        if( dirGhost == Game.RIGHT) {
            return runToLeft();
        }
        return randomDir();
    }

    private static int runToRight () {
        updatePac();
        updateMap();
        Pixel2D start = _pacPos;
        Pixel2D target = new Index2D((start.getX() + 1), start.getY());
        if (_map.getPixel(target) == OBS_VALUE) {
            return runToUp();
        }
        return Game.RIGHT;
    }

    private static int runToUp () {
        updatePac();
        updateMap();
        Pixel2D start = _pacPos;
        Pixel2D target = new Index2D(start.getX(), (start.getY() + 1));
        if (_map.getPixel(target) == OBS_VALUE) {
            return runToLeft();
        }
        return Game.UP;
    }

    private static int runToLeft () {
        updatePac();
        updateMap();
        Pixel2D start = _pacPos;
        Pixel2D target = new Index2D((start.getX() - 1), start.getY());
        if (_map.getPixel(target) == OBS_VALUE) {
            return runToDown();
        }
        return Game.LEFT;
    }

    private static int runToDown () {
        updatePac();
        updateMap();
        Pixel2D start = _pacPos;
        Pixel2D target = new Index2D(start.getX(), (start.getY() - 1));
        if (_map.getPixel(target) == OBS_VALUE) {
            return runToRight();
        }
        return Game.DOWN;
    }

}