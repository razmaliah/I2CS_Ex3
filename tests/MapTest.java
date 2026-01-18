
import Algo.Index2D;
import Algo.Map;
import Algo.Map2D;
import Algo.Pixel2D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.*;
/**
 * Intro2CS, 2026A, this is a very
 */
class MapTest {
    /**
     */
    private int[][] _map_3_3 = {{0,1,0}, {1,0,1}, {0,1,0}};
    private Map2D _m0, _m1, _m3_3;
    @BeforeEach
    public void setuo() {
        _m3_3 = new Map(_map_3_3);
    }
    @Test
    @Timeout(value = 1, unit = SECONDS)
    void testInit() {
        int[][] bigarr = new int [500][500];
        _m1 = new Map(bigarr);
        assertEquals(bigarr.length, _m1.getWidth());
        assertEquals(bigarr[0].length, _m1.getHeight());
        assertThrowsExactly(RuntimeException.class, () -> {
            _m0 = new Map(0,5,0);
        });
        int[][] arr2 = new int[5][0];
        assertThrowsExactly(RuntimeException.class, () -> {
            _m0 = new Map(arr2);
        });
        int [] ar1 = new int[5];
        int [] ar2 = new int[4];
        int [][] arr3 = {ar1, ar2};
        assertThrowsExactly(RuntimeException.class, () -> {
            _m0 = new Map(arr3);
        });
    }

    @Test
    void testInit2() {
        _m0 = new Map(_map_3_3);
        _m1 = new Map(_map_3_3);
        assertEquals(_m0,_m1);
        _m0 = new Map(100);
        assertEquals(100, _m0.getWidth());
        assertEquals(100, _m0.getHeight());
    }
    @Test
    void testGetMap() {
        _m0 = new Map(_map_3_3);
        int[][] arr = _m0.getMap();
        assertArrayEquals(_map_3_3, arr);
    }
    @Test
    void testGetWidthHeight() {
        _m0 = new Map(_map_3_3);
        assertEquals(3, _m0.getWidth());
        assertEquals(3, _m0.getHeight());
        _m0 = new Map(50,60,5);
        assertEquals(50, _m0.getWidth());
        assertEquals(60, _m0.getHeight());
    }

    @Test
    void testGetPixel() {
        _m0 = new Map(_map_3_3); // {{0,1,0}, {1,0,1}, {0,1,0}}
        Pixel2D p = new Index2D(1,1);
        assertEquals(0, _m0.getPixel(p));
        p = new Index2D(0,1);
        assertEquals(1, _m0.getPixel(p));
        assertEquals(1, _m0.getPixel(2,1));
    }
    @Test
    void testSetPixel() {
        _m0 = new Map(_map_3_3); // {{0,1,0}, {1,0,1}, {0,1,0}}
        Pixel2D p = new Index2D(1,1);
        _m0.setPixel(p, -5);
        assertEquals(-5, _m0.getPixel(p));
        _m0.setPixel(0,1,-1);
        assertEquals(-1, _m0.getPixel(0,1));
        Pixel2D pErr = new Index2D(3,3);
        assertThrowsExactly(RuntimeException.class, () -> {
            _m0.setPixel(pErr,10);
        });
        assertThrowsExactly(RuntimeException.class, () -> {
            _m0.setPixel(3,3,10);
        });
    }

    @Test
    void testEquals() {
        _m0 = new Map(_map_3_3);
        _m1 = new Map(_map_3_3);
        assertTrue(_m0.equals(_m1));
        _m0 = new Map(5,5,0);
        assertFalse(_m0.equals(_m1));
        _m1 = new Map(5,5,0);
        _m1.setPixel(2,2,1);
        assertFalse(_m0.equals(_m1));
    }
    @Test
    void testIsInside() {
        _m0 = new Map(100);
        Pixel2D p = new Index2D(50,50);
        assertTrue(_m0.isInside(p));
        p = new Index2D(99,99);
        assertTrue(_m0.isInside(p));
        p = new Index2D(0,0);
        assertTrue(_m0.isInside(p));
        p = new Index2D(100,100);
        assertFalse(_m0.isInside(p));
        p = new Index2D(-5,3);
        assertFalse(_m0.isInside(p));
        p = new Index2D(5,500);
        assertFalse(_m0.isInside(p));
        p = new Index2D(20,-1);
        assertFalse(_m0.isInside(p));
    }





    @Test
    void testFill() {
        _m0 = new Map(10);
        for(int i=0; i<10;i++){
            _m0.setPixel(i,8,1);
        }
        Pixel2D p = new Index2D(2,2);
        int res = _m0.fill(p,2); // cyclic = true
        boolean ans = true;
        for(int i=0;i<10;i++){
            for(int j=0;j<10;j++){
                if (j!=8 && _m0.getPixel(i,j) !=2){
                    ans = false;
                }
                if (j == 8 && _m0.getPixel(i,j) !=1) {
                    ans = false;
                }
            }
        }
        assertTrue(ans);
        assertEquals(90, res);
        res = _m0.fill(p,3); // cyclic = false
        assertEquals(80, res);
        res = _m0.fill(p,3); // cyclic = false
        assertEquals(0, res);
    }
    /**
     * test shortestPath function in 4 different cases:
     * 1. 11X11 map with vertical obstacle without one Pixel, non-cyclic
     * 2. 11X11 map with vertical obstacle without one Pixel, cyclic
     * 3. when start pixel equals to target pixel
     * 4. blocking the path completely (no valid path)
     */
    @Test
    void testShortestPath() {
        _m0 = new Map(11);
        for(int i=1;i<_m0.getHeight();i++){
            _m0.setPixel(5,i,1);
        }
        Pixel2D start = new Index2D(4,10);
        Pixel2D target = new Index2D(6,10);
        Pixel2D[] ans = _m0.shortestPath(start, target, 1); // cyclic = false
        int res = _m0.getHeight()*2 +1;     //the path going all the way down 2 steps right and all the way up
        assertEquals(res,ans.length);

        ans = _m0.shortestPath(start, target, 1);// cyclic = true
        res = 5;     // 4 steps + 1 (up,right*2,down)
        assertEquals(res,ans.length);

        ans = _m0.shortestPath(start, start, 1);// cyclic = false
        assertEquals(1,ans.length);         // check start equals target

        _m0.setPixel(5,0,1);
        ans = _m0.shortestPath(start, target, 1); // cyclic = false
        assertNull(ans); // no valid path
    }

    /**
     * test allDistance function in 3 different cases:
     * 1. small map with center point without obstacles
     * 2. larger map with vertical obstacle, non-cyclic
     * 3. larger map with vertical obstacle, cyclic
     */
    @Test
    void testAllDistance() {
        _m0 = new Map(5);
        Pixel2D p = new Index2D(2,2);
        _m0 = _m0.allDistance(p,1); // cyclic = false
        int[][] dists = _m0.getMap();
        int[][] expected = {
                {4,3,2,3,4},
                {3,2,1,2,3},
                {2,1,0,1,2},
                {3,2,1,2,3},
                {4,3,2,3,4}
        };
        for(int i=0;i<5;i++){
            assertArrayEquals(expected[i], dists[i]);
        }
        _m0 = new Map(11);
        for(int i=0;i<_m0.getHeight();i++){
            _m0.setPixel(5,i,1);
        }
        p = new Index2D(0,0);
        _m1 = _m0.allDistance(p,1); // cyclic = true
        _m0 = _m0.allDistance(p,1); // cyclic = false
        boolean ans0 = true, ans1 = true;
        for(int i=0;i<_m0.getWidth();i++) {
            for (int j = 0; j < _m0.getHeight(); j++) {
                Pixel2D curr = new Index2D(i, j);
                if (i<5 && _m0.getPixel(curr) != (i + j)) { // before obstacle line
                    ans0 = false;
                    ans1 = false;
                }
                if (i==5 && _m0.getPixel(curr) != 1) { // the obstacle line
                    ans0 = false;
                    ans1 = false;
                }
                if (i>5) { // after obstacle line
                    if(_m0.getPixel(curr) != -1) {
                        ans0 = false;
                    }
                    if (j<=5 && _m1.getPixel(curr) != ((_m1.getWidth()-i) + j)) {
                        ans1 = false;
                    }
                    if(j>5 && _m1.getPixel(curr) != ((_m1.getWidth()-i) + ( _m1.getHeight()-j))) {
                        ans1 = false;
                    }
                }
            }
        }
        assertTrue(ans0);
        assertTrue(ans1);
    }


}