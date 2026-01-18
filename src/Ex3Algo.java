
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
    private static final int MAX_CLOSE_GHOST_DIST = 8;
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

        int distTCG = distToClosestGhost(ghostDist());
        if(distTCG < MAX_CLOSE_GHOST_DIST){
            if(_isEatable){
               return huntClosestGhostDir();
            }
            else{
               if(distToClosestGreenDot() < distTCG){
                   return dirToClosestGreenDot();
               }
               else{
                   return dirToRun();
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

    ///  ///////////////////////////////////// My methods  /////////////////////////////////////

    public static void updatePac() {
        String pos = _game.getPos(1);
        int x = Integer.parseInt(pos.split(",")[0]);
        int y = Integer.parseInt(pos.split(",")[1]);
        Index2D p1 = new Index2D(x, y);
        _pacPos = p1;
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

    public static void updateMap(){
        if (_game == null){ return; }
        int[][] arr2D = _game.getGame(0);
        _map = new Map(arr2D);
    }

    public static Pixel2D[] closestPP() {
        Pixel2D[] ans = null;
        updatePac();
        updateMap();
        Pixel2D target = closestFood();
        ans = _map.shortestPath(_pacPos, target, OBS_VALUE);
        return ans;
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

    public static Index2D ghostPos (GhostCL g){
        String pos = g.getPos(1);
        int x = Integer.parseInt(pos.split(",")[0]);
        int y = Integer.parseInt(pos.split(",")[1]);
        Index2D p1 = new Index2D(x, y);
        return p1;
    }

    public static int[] ghostDist() {
        updateMap();
        updateGhosts();
        updatePac();

        int[] dists = new int[_ghostsPos.length];
        Map2D allD = _map.allDistance(_pacPos, OBS_VALUE);
        for (int i = 0; i < _ghostsPos.length; i++) {
            Pixel2D gPos = _ghostsPos[i];
            int distFromPac = allD.getPixel(gPos);
            if (distFromPac < 1) {
                dists[i] = 100;
            } else {
                dists[i] = distFromPac;
            }
        }
        return dists;
    }

    public static int indexClosestGhost(int[] dists) {
        if (dists == null || dists.length == 0) return -1;
        int minIndex = 0;
        for (int i = 1; i < dists.length; i++) {
            if (dists[i] < dists[minIndex]) {
                minIndex = i;
            }
        }
        return minIndex;
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
        if(target.getX()>8 && target.getX()<14 && target.getY()>10 && target.getY()<13){
           return dirToRun();
        }
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

    public static Map2D combainedGhostDistMaps() {
        updateMap();
        updateGhosts();
        updatePac();

        Map2D[] Maps = new Map2D[_ghostsPos.length];
        for (int i = 0; i < _ghostsPos.length; i++) {
            Maps[i] = _map.allDistance(_ghostsPos[i], OBS_VALUE);
        }
        Map2D combinedMap = new Map(_map.getWidth(), _map.getHeight(), 0);
        for (int i = 0; i < _map.getWidth(); i++) {
            for (int j = 0; j < _map.getHeight(); j++) {
                Pixel2D p = new Index2D(i, j);
                int minDist = 100;
                for (int k = 0; k < Maps.length; k++) {
                    int d = Maps[k].getPixel(p);
                    if (d < minDist) {
                        minDist = d;
                    }
                }
                combinedMap.setPixel(p, minDist);
            }
        }
        return combinedMap;
    }

    public static int dirToRun(){
        updateMap();
        updatePac();
        updateGhosts();
        int dirAns;
        Map2D combinedMap = combainedGhostDistMaps();
        Pixel2D start = _pacPos;
        int myDist = combinedMap.getPixel(start);
        Pixel2D pRight = new Index2D(start.getX() + 1, start.getY());
        Pixel2D pLeft = new Index2D(start.getX() - 1, start.getY());
        Pixel2D pUp = new Index2D(start.getX(), start.getY() + 1);
        Pixel2D pDown = new Index2D(start.getX(), start.getY() - 1);
        if(pRight.getX() == _map.getWidth()){pRight = new Index2D(0, start.getY());}
        if(pLeft.getX() < 0){pLeft = new Index2D(_map.getWidth() - 1, start.getY());}

        if(combinedMap.getPixel(pRight) > myDist){
            dirAns = Game.RIGHT;
        }
        else if(combinedMap.getPixel(pLeft) > myDist){
            dirAns = Game.LEFT;
        }
        else if(combinedMap.getPixel(pUp) > myDist){
            dirAns = Game.UP;
        }
        else if(combinedMap.getPixel(pDown) > myDist){
            dirAns = Game.DOWN;
        }
        else{
            dirAns = randomDir();
        }
        return dirAns;
    }
}