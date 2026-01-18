import exe.ex3.game.Game;
import exe.ex3.game.PacmanGame;
import exe.ex3.game.PacManAlgo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.Arrays;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.*;



public class AlgoTest {
    private Game _game;
    private Ex3Algo _algo;

    @BeforeEach
    public void setup() {
        _game = new Game();
        _game.init(GameInfo.CASE_SCENARIO, GameInfo.MY_ID, GameInfo.CYCLIC_MODE, GameInfo.RANDOM_SEED, GameInfo.RESOLUTION_NORM, GameInfo.DT, -1);
        _algo = new Ex3Algo();
        _algo._game = _game;
    }

    @Test
    public void updatePacTest() {
        _algo.updatePac();
        Index2D p1 = _algo._pacPos;
        Index2D p2 = new Index2D(11, 14); // start position
        assertTrue(p1.equals(p2));
    }

    @Test
    public void closestFoodTest() {
        int[][] mapArr = _game.getGame(1);
        _algo.updatePac();
        Map2D map = new Map(mapArr);
        Map2D alld = map.allDistance(_algo._pacPos, _algo.OBS_VALUE);
        Index2D foodPos = _algo.closestFood();
        Index2D op1 = new Index2D(10, 14); // option 1
        Index2D op2 = new Index2D(12, 14); // option 2
        assertTrue(foodPos.equals(op1) || foodPos.equals(op2));
    }

    @Test
    public void closestPPTest() {
        _algo.updatePac();
        Pixel2D[] ans = _algo.closestPP();
        Index2D op1 = new Index2D(10, 14); // option 1
        Index2D op2 = new Index2D(12, 14); // option 2
        Pixel2D[] op1Arr = {_algo._pacPos, op1};
        Pixel2D[] op2Arr = {_algo._pacPos, op2};
        assertTrue(Arrays.equals(ans, op1Arr) || Arrays.equals(ans, op2Arr));
    }

    @Test
    public void getDirTest(){
        Pixel2D p1 = new Index2D(5,5);
        Pixel2D p2 = new Index2D(6,5);
        assertEquals(_algo.getDir(p1,p2), Game.RIGHT);
        Pixel2D p3= new Index2D(0,11);
        Pixel2D p4 = new Index2D(22,11);
        assertEquals(_algo.getDir(p3,p4), Game.LEFT);
    }

    @Test
    public void updateGhostTest() {
        _algo.updateGhosts();
        Pixel2D expected = new Index2D(11, 11);
        Pixel2D g1 = _algo._ghostsPos[0];
        assertTrue(g1.equals(expected));
        assertFalse(_algo._isEatable);
    }


    /**
     * includes ghostDist, distToClosestGhost ,indexClosestGhost
     */
    @Test
    public void distToGhostTest() {
        _algo.updatePac();
        _algo.updateGhosts();
        int dist = _algo.distToClosestGhost();
        assertEquals(3, dist);

        int ind = _algo.indexClosestGhost();
        assertEquals(0, ind);
    }

    @Test
    public void huntGhostTest(){
        _algo.updatePac();
        _algo.updateGhosts();
        int dir = _algo.huntClosestGhostDir();
        assertEquals(_game.DOWN, dir);
    }

    @Test
    public void distToClosestGreenDotTest(){
        _algo.updatePac();
        int dist = _algo.distToClosestGreenDot();
        assertEquals(18, dist); //distance to closest green dot at top left or top right
    }

    @Test
    public void dirToClosestGreenDotTest(){
        _algo.updatePac();
        int dir = _algo.dirToClosestGreenDot();
        assertTrue(dir == _game.LEFT || dir == _game.RIGHT);
    }
}