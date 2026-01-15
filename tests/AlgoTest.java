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
    }

    @Test
    public void updatePacTest() {
        _algo.updatePac(_game);
        Index2D p1 = _algo._pacPos;
        Index2D p2 = new Index2D(11, 14); // start position
        assertTrue(p1.equals(p2));
    }

    @Test
    public void closestFoodTest() {
        int[][] mapArr = _game.getGame(1);
        _algo.updatePac(_game);
        Map2D map = new Map(mapArr);
        Map2D alld = map.allDistance(_algo._pacPos, _algo.OBS_VALUE);
        Index2D foodPos = _algo.closestFood(map, alld);
        Index2D op1 = new Index2D(10, 14); // option 1
        Index2D op2 = new Index2D(12, 14); // option 2
        assertTrue(foodPos.equals(op1) || foodPos.equals(op2));
    }

    @Test
    public void closestPPTest() {
        _algo.updatePac(_game);
        Pixel2D[] ans = _algo.closestPP(_game);
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
}