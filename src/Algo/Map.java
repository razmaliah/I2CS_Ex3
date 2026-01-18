package Algo;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.Queue;


/**
 * This class represents a 2D map (int[w][h]) as a "screen" or a raster matrix or maze over integers.
 * This is the main class needed to be implemented.
 *
 * @author boaz.benmoshe
 *
 */
public class Map implements Map2D, Serializable{

    private int[][] _map;
    private boolean _cyclicFlag = true;

    /**
	 * Constructs a w*h 2D raster map with an init value v.
	 * @param w - width of the map
	 * @param h - height of the map
	 * @param v - init value of all the entries in the map
	 */
	public Map(int w, int h, int v) {init(w, h, v);}
	/**
	 * Constructs a square map (size*size). all the entries are initialized to 0.
	 * @param size - the size for the width and height of the map
	 */
	public Map(int size) {this(size,size, 0);}
	
	/**
	 * Constructs a map from a given 2D array.
	 * @param data
	 */
	public Map(int[][] data) {
		init(data);
	}

    /**
     * Construct a 2D w*h matrix of integers.
     * v is the init value of all the entries in the 2D array.
     * @throws RuntimeException if w <=0 or h <=0.
     * @param w the width of the underlying 2D array.
     * @param h the height of the underlying 2D array.
     * @param v the init value of all the entries in the 2D array.
     */
	@Override
	public void init(int w, int h, int v) {
        if (w <= 0 || h <= 0) {
            throw new RuntimeException("Invalid dimensions for the map");
        }
        this._map = new int[w][h];
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                this._map[i][j] = v;
	        }
        }
    }
    /**
     * Constructs a new 2D raster map from a given 2D int array (deep copy).
     * @throws RuntimeException if arr == null or if the array is empty or a ragged 2D array.
     * @param arr a 2D int array.
     */
	@Override
	public void init(int[][] arr) {
        if (arr == null || arr.length == 0 || arr[0].length == 0) {
            throw new RuntimeException("Invalid array");
        }
        int height = arr[0].length;
        for (int i = 1; i < arr.length; i++) {
            if (arr[i].length != height) {
                throw new RuntimeException("Ragged array");
            }
        }
        this._map = new int[arr.length][height];
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < height; j++) {
                this._map[i][j] = arr[i][j];
            }
        }
    }

    /**
     * @return a 2D array represent the map (deep copy).
     */
	@Override
	public int[][] getMap() {
		int[][] ans = new int[this._map.length][this._map[0].length];
        for (int i = 0; i < this._map.length; i++) {
            for (int j = 0; j < this._map[0].length; j++) {
                ans[i][j] = this._map[i][j];
            }
        }
		return ans;
	}

    /**
     * @return the width of this 2D map (first coordinate).
     */
	@Override
	public int getWidth() {
        return this._map.length;
    }

    /**
     * @return the height of this 2D map (second coordinate).
     */
	@Override
	public int getHeight() {
        return this._map[0].length;
    }
    /**
     * @param x the x coordinate (first coordinate) - width
     * @param y the y coordinate (second coordinate) - height
     * @return the value of the map[x][y].
     */
	@Override
	public int getPixel(int x, int y) {
        return this._map[x][y];
    }
    /**
     * @param p Pixel 2D represent the x,y coordinate
     * @return the value of the map[p.x][p.y].
     */
	@Override
	public int getPixel(Pixel2D p) {
        int x = p.getX();
        int y = p.getY();
        return this._map[x][y];
	}

    /**
     * Set the [x][y] coordinate of the map to v.
     * @param x the x coordinate
     * @param y the y coordinate
     * @param v the value that the entry at the coordinate [x][y] is set to.
     */
	@Override
	public void setPixel(int x, int y, int v) {
        Pixel2D p = new Index2D(x, y);
        if(!isInside(p)){
            throw new RuntimeException("Coordinate out of bounds");
        }
        this._map[x][y] = v;
    }

    /**
     * Set the [x][y] coordinate (Algo.Pixel2D) of the map to v.
     * @param p the coordinate in the map.
     * @param v the value that the entry at the coordinate [p.x][p.y] is set to.
     */
	@Override
	public void setPixel(Pixel2D p, int v) {
        if(!isInside(p)){
            throw new RuntimeException("Coordinate out of bounds");
        }
        int x = p.getX();
        int y = p.getY();
        this._map[x][y] = v;
	}

    /**
     * checks if the given 2D coordinate is inside this map.
     * @param p the 2D coordinate.
     * @return true if p is inside this map.
     */
    @Override
    public boolean isInside(Pixel2D p) {
        boolean ans = true;
        if (p == null) {return false;}
        int x = p.getX();
        int y = p.getY();
        if (x < 0 || y < 0 || x >= this.getWidth() || y >= this.getHeight()) {
            ans = false;
        }
        return ans;
    }

    @Override
    public boolean isCyclic() {
        return _cyclicFlag;
    }

    @Override
    public void setCyclic(boolean cy) {
        this._cyclicFlag = cy;
    }

    /**
     * This method returns true if and only if this Algo.Map has the same dimensions as p.
     * @param p - other map to compare to.
     * @return true if this Algo.Map has the same dimensions as p.
     */


    public boolean sameDimensions(Map2D p){
        boolean ans = false;
        if (this.getWidth() == p.getWidth() && this.getHeight() == p.getHeight()){
            ans = true;
        }
        return ans;
    }
    /**
     * check if this map is equal to another map.
     * @param ob the reference object with which to compare. if ob is not instance of Algo.Map2D return false.
     * @return true if the maps are equal (same dimensions and same values in all entries).
     */
    @Override
    public boolean equals(Object ob) {
        boolean ans = true;
        if(!(ob instanceof Map2D)) {
            return false;
        }
        Map2D p = (Map2D) ob;
        if(!this.sameDimensions(p)) {
            ans = false;
        }
        else {
            for (int i = 0; i < this.getWidth(); i++) {
                for (int j = 0; j < this.getHeight(); j++) {
                    if (this.getPixel(i, j) != p.getPixel(i, j)) {
                        ans = false;
                        break;
                    }
                }
                if (!ans) {
                    break;
                }
            }
        }
        return ans;
    }

    /**
     * Fill the connected component of p in the new color (new_v).
     * Note: the connected component of p are all the pixels in the map with the same "color" of map[p] which are connected to p.
     * Note: two pixels (p1,p2) are connected if there is a path between p1 and p2 with the same color (of p1 and p2).
     * for more info see - https://en.wikipedia.org/wiki/Flood_fill
     * @param xy the source pixel to start from.
     * @param new_v the new "color" to be filled in p's connected component.
     * @return the number of "filled" pixels.
     */
	@Override
	public int fill(Pixel2D xy, int new_v) {
		int ans = 0;
        if(xy == null || !isInside(xy)){return ans;}
        int old_v = this.getPixel(xy);
        if(old_v == new_v){return ans;}
        this.setPixel(xy, new_v);
        ans++;
        Pixel2D up = new Index2D(xy.getX(), xy.getY()+1);
        Pixel2D down = new Index2D(xy.getX(), xy.getY()-1);
        Pixel2D left = new Index2D(xy.getX()-1, xy.getY());
        Pixel2D right = new Index2D(xy.getX()+1, xy.getY());
        if (this._cyclicFlag && !isInside(up)){
            up = new Index2D(up.getX(),0);
        }
        if (this._cyclicFlag && !isInside(down)){
            down = new Index2D(down.getX(),this.getHeight()-1);
        }
        if(this._cyclicFlag && !isInside(left)){
            left = new Index2D(this.getWidth()-1, left.getY());
        }
        if(this._cyclicFlag && !isInside(right)){
            right = new Index2D(0, right.getY());
        }
        if(isInside(up) && this.getPixel(up) == old_v){ans += fill(up, new_v);}
        if(isInside(down) && this.getPixel(down) == old_v){ans += fill(down, new_v);}
        if(isInside(left) && this.getPixel(left) == old_v){ans += fill(left, new_v);}
        if(isInside(right) && this.getPixel(right) == old_v){ans += fill(right, new_v);}
		return ans;
	}

	@Override
	/**
	 * BFS like shortest the computation based on iterative raster implementation of BFS, see:
	 * https://en.wikipedia.org/wiki/Breadth-first_search
     *
     * compute the shortest valid path between two pixels (start, target) while avoiding obstacles.
     * A path is an ordered set of pixels where each consecutive pixels in the path are neighbors in this map.
     * Two pixels are neighbors in the map, iff they are a single pixel apart (up,down, left, right).
     * If this map is cyclic:
     * 1. the pixel to the left of (0,i) is (getWidth()-1,i).
     * 2. the pixel to the right of (getWidth()-1,i) is (0,i).
     * 3. the pixel above (j,getHeight()-1) is (j,0).
     * 4. the pixel below (j,0) is (j,getHeight()-1).
     * Where 0<=i<getWidth(), 0<=j<getWidth().
     * @param start the start pixel for the path
     * @param target the end pixel for the path
     * @param obsColor the color represent the obstacle
     * @return Pixel array represent the shortest path, includes the start and target pixel.
	 */
	public Pixel2D[] shortestPath(Pixel2D start, Pixel2D target, int obsColor) {
		Pixel2D[] ans = null;
        if (start == null || target == null || !isInside(start) || !isInside(target)){return ans;}
        if (start.equals(target)){
            ans = new Pixel2D[1];
            ans[0] = start;
            return ans;
        }
        Map tempMap = new Map(this.getMap());
        tempMap.resetMap(obsColor);
        tempMap.setPixel(start, 0);
        tempMap.markSteps(start, target);
        int howManySteps = tempMap.getPixel(target);
        if (howManySteps < 0) {return null;} // no path found
        ans = new Pixel2D[howManySteps+1];      // from start to target is steps +1 pixels
        Pixel2D curr = target;
        for(int i=howManySteps; i>=0; i--){
            ans[i] = curr;
            curr = tempMap.goBack(curr);
        }
		return ans;
	}

    /**
     * Compute a new map (with the same dimension as this map) witch every entry value equals to
     * the shortest path distance (obstacle avoiding) from the start point to that entry.
     * Note : obsColor entries will remain as obsColor in the returned map. (if obsColor is 2 it's not 2 steps away, it's an obstacle)
     * @param start the source (starting) pixel
     * @param obsColor the color representing obstacles
     * @return a new map with all the shortest path distances from the starting pixel to each entry in this map.
     */

    @Override

    public Map2D allDistance(Pixel2D start, int obsColor) {
        if (start == null || !isInside(start)){ return null;}
        Map ans = new Map(this.getMap());
        ans.resetMap(obsColor);
        ans.setPixel(start, 0);
        Queue<Pixel2D> queue = new LinkedList<>();
        queue.add(start);

        while (!queue.isEmpty()) {
            Pixel2D current = queue.poll();
            int dist = ans.getPixel(current);
            Pixel2D[] neighbors = getAllPotentialNeighbors(current);
            for (int i=0;i<neighbors.length;i++){
                Pixel2D next = neighbors[i];
                if (ans.isInside(next) && ans.getPixel(next) == -1) {
                    ans.setPixel(next, dist + 1);
                    queue.add(next);
                }
            }
        }
        return ans;
    }

    /**
    public Algo.Map2D allDistance(Algo.Pixel2D start, int obsColor) {
        Algo.Map ans = null;
        if (start == null || !isInside(start)){return ans;}
        ans = new Algo.Map(this.getMap());
        ans.resetMap(obsColor);
        ans.setPixel(start, 0);
        Algo.Pixel2D target = new Algo.Index2D(ans.getWidth()-1 - start.getX(),ans.getHeight()-1 - start.getY());   // not relevant target, just to use markSteps function
        if (start.equals(target)){
            target = new Algo.Index2D(0,0);
        }
        ans.markSteps(start, target);
        for(int i=0;i<ans.getWidth();i++){
            for(int j=0;j<ans.getHeight();j++){
                if(ans.getPixel(i,j) == -2){
                    ans.setPixel(i,j,obsColor);      // set obstacles back to obsColor
                }
            }
        }
        return ans;
    }
    **/

    ////////////////////// Private Methods ///////////////////////



    /**
     * Resets the map for the shortest path function.
     * all obstacle pixels (obstColor) are set to -2.
     * all other pixels are set to -1.
     * @param obstColor the color represent obstacles
     **/
    private void resetMap(int obstColor){
        for(int i=0;i<this.getWidth();i++){
            for(int j=0;j<this.getHeight();j++){
                if(this.getPixel(i,j) == obstColor){
                    this.setPixel(i,j,-2);
                }
                else{
                    this.setPixel(i,j,-1);
                }
            }
        }
    }

    /**
     * this function mark the neighbors pixel to p (up, down, right, left) to be p value +1
     * represent the number of step to the neighbors pixels (for example if p value is 3 it's 3 step away from the start.
     * @param p the pixel to mark his neighbors
     * @return the number of changed pixels
     */

    /**
    private int markNeighbors(Algo.Pixel2D p){
        int howManyMarked =0;
        Algo.Pixel2D up = new Algo.Index2D(p.getX(), p.getY()-1);
        Algo.Pixel2D down = new Algo.Index2D(p.getX(), p.getY()+1);
        Algo.Pixel2D left = new Algo.Index2D(p.getX()-1, p.getY());
        Algo.Pixel2D right = new Algo.Index2D(p.getX()+1, p.getY());
        if(this._cyclicFlag && up.getY()<0){ up = new Algo.Index2D(p.getX(), this.getHeight()-1);}
        if(this._cyclicFlag && down.getY()>=this.getHeight()) { down = new Algo.Index2D(p.getX(), 0);}
        if(this._cyclicFlag && left.getX()<0){ left = new Algo.Index2D(this.getWidth()-1, p.getY());}
        if(this._cyclicFlag && right.getX()>=this.getWidth()){ right = new Algo.Index2D(0, p.getY());}
        int myValue = this.getPixel(p);

        if(isInside(up) && (this.getPixel(up) == -1 || this.getPixel(up) > myValue +1)){
            this.setPixel(up, myValue +1);
            howManyMarked++;
        }
        if(isInside(down) && (this.getPixel(down) == -1 || this.getPixel(down) > myValue +1)){
            this.setPixel(down, myValue +1);
            howManyMarked++;
        }
        if(isInside(left) && (this.getPixel(left) == -1 || this.getPixel(left) > myValue +1)) {
            this.setPixel(left, myValue + 1);
            howManyMarked++;
        }
        if(isInside(right) && (this.getPixel(right) == -1 || this.getPixel(right) > myValue +1)){
            this.setPixel(right, myValue +1);
            howManyMarked++;
        }
        return howManyMarked;
    }
     **/

    private int markNeighbors(Pixel2D p) {
        int howManyMarked = 0;
        int myValue = this.getPixel(p);
        Pixel2D[] neighbors = getAllPotentialNeighbors(p);
        for (int i = 0; i< neighbors.length; i++) {
            Pixel2D next = neighbors[i];
            if (isInside(next)) {
                int nextVal = this.getPixel(next);
                if (nextVal == -1 || nextVal > myValue + 1) {
                    this.setPixel(next, myValue + 1);
                    howManyMarked++;
                }
            }
        }
        return howManyMarked;
    }

    /**
     * this recursive function mark all valid color pixels (none obstacles) to be the lowest number of steps from start pixel
     * @param start the start pixel to count steps from
     * @param target the target pixel for the path
     */




    /**
    private void markSteps(Algo.Pixel2D start,Algo.Pixel2D target){
        if(!isInside(start) || !isInside(target)){return;}
        if(start.equals(target)){return;}
        int howMany = markNeighbors(start);
        if (howMany == 0) {
            return;
        }
        Algo.Pixel2D up = new Algo.Index2D(start.getX(), start.getY()-1);
        Algo.Pixel2D down = new Algo.Index2D(start.getX(), start.getY()+1);
        Algo.Pixel2D left = new Algo.Index2D(start.getX()-1, start.getY());
        Algo.Pixel2D right = new Algo.Index2D(start.getX()+1, start.getY());

        if(this._cyclicFlag && up.getY()<0){up = new Algo.Index2D(start.getX(), this.getHeight()-1);}
        if(this._cyclicFlag && down.getY()>=this.getHeight()) {down = new Algo.Index2D(start.getX(), 0);}
        if(this._cyclicFlag && left.getX()<0){left = new Algo.Index2D(this.getWidth()-1, start.getY());}
        if(this._cyclicFlag && right.getX()>=this.getWidth()){right = new Algo.Index2D(0, start.getY());}

        if (isInside(up) && getPixel(up) == getPixel(start) +1) {
            markSteps(up, target);
        }
        if (isInside(down)&& getPixel(down) == getPixel(start) +1) {
            markSteps(down, target);
        }
        if (isInside(left)&& getPixel(left) == getPixel(start) +1) {
            markSteps(left, target);
        }
        if (isInside(right)&& getPixel(right) == getPixel(start) +1) {
            markSteps(right, target);
        }
    }



**/

    /**
     * this function reverse the steps from target and return the neighbor pixel the has target value -1 (represent the steps)
     * @param target pixel to reverse a step
     * @return the reversed step neighbor pixel
     */
    private Pixel2D goBack(Pixel2D target){
        Pixel2D ans = null;
        int myValue = this.getPixel(target);
        Pixel2D up = new Index2D(target.getX(), target.getY()-1);
        Pixel2D down = new Index2D(target.getX(), target.getY()+1);
        Pixel2D left = new Index2D(target.getX()-1, target.getY());
        Pixel2D right = new Index2D(target.getX()+1, target.getY());
        if(this._cyclicFlag && up.getY()<0){
            up = new Index2D(target.getX(), this.getHeight()-1);
        }
        if(this._cyclicFlag && down.getY()>=this.getHeight()) {
            down = new Index2D(target.getX(), 0);
        }
        if(this._cyclicFlag && left.getX()<0){
            left = new Index2D(this.getWidth()-1, target.getY());
        }
        if(this._cyclicFlag && right.getX()>=this.getWidth()){
            right = new Index2D(0, target.getY());
        }

        if(isInside(up) && this.getPixel(up) == myValue -1){
            ans = up;
        }
        else if(isInside(down) && this.getPixel(down) == myValue -1){
            ans = down;
        }
        else if(isInside(left) && this.getPixel(left) == myValue -1){
            ans = left;
        }
        else if(isInside(right) && this.getPixel(right) == myValue -1){
            ans = right;
        }
        return ans;
    }

    //////////////////////////////////////////////////////////////



    private void markSteps(Pixel2D start, Pixel2D target) {
        if (!isInside(start) || !isInside(target)) return;
        Queue<Pixel2D> queue = new LinkedList<>();
        queue.add(start);
        while (!queue.isEmpty()) {
            Pixel2D current = queue.poll();
            if (current.equals(target)) continue;
            int howMany = markNeighbors(current);
            if (howMany == 0) continue;
            Pixel2D[] neighbors = getAllPotentialNeighbors(current);
            for (int i =0; i<neighbors.length;i++) {
                Pixel2D next = neighbors[i];
                if (isInside(next) && getPixel(next) == getPixel(current) + 1) {
                    queue.add(next);
                }
            }
        }
    }

    private Pixel2D[] getAllPotentialNeighbors(Pixel2D p) {
        Pixel2D[] ans;
        int x = p.getX();
        int y = p.getY();
        Pixel2D up = new Index2D(x, y - 1);
        Pixel2D down = new Index2D(x, y + 1);
        Pixel2D left = new Index2D(x - 1, y);
        Pixel2D right = new Index2D(x + 1, y);
        if (this._cyclicFlag) {
            if (up.getY() < 0){up = new Index2D(x, this.getHeight() - 1);}
            if (down.getY() >= this.getHeight()){ down = new Index2D(x, 0);}
            if (left.getX() < 0) {left = new Index2D(this.getWidth() - 1, y);}
            if (right.getX() >= this.getWidth()){ right = new Index2D(0, y);}
        }
        ans = new Pixel2D[]{up, down, left, right};
        return ans;
    }


                            //////////////////////////////////////////////////////////////


    /**
     * print the map
     */
    private void printMap(){
        for(int i=0; i< this.getWidth(); i++){
            for(int j=0; j< this.getHeight(); j++){
                System.out.print(this.getPixel(i,j)+" ");
            }
            System.out.println();
        }
        System.out.println();
    }

    static void main(String[] args) {
        Map map = new Map(100,100,0);
        Index2D p1 = new Index2D(8,3);
        Pixel2D p2 = new Index2D(8,12);
        Pixel2D p3 = new Index2D(1,12);
        Pixel2D p4 = new Index2D(1,3);
        Pixel2D p5 = new Index2D(6,3);
        Pixel2D p6 = new Index2D(6,10);
        Pixel2D p7 = new Index2D(3,10);
        Pixel2D p8 = new Index2D(3,5);



        map.printMap();

        Pixel2D start = new Index2D(0,0);
        Pixel2D target = new Index2D(5,7);
        Pixel2D[] ans = map.shortestPath(start, target, -2);
        for(int i=0; i<ans.length; i++){
            map.setPixel(ans[i], 1);
        }
        map.printMap();
        System.out.println("how many steps: " + (ans.length -1));


    }

}
