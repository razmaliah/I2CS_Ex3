package MyGame;

import Algo.*;
import exe.ex3.game.Game;

public class PacManGame implements PacGame{

    public static final int OBS_COLOR = 1;
    public static final int DOT_COLOR = 2;
    public static final int POWER_COLOR = 3;
    public static final int MOVE_RIGHT = 1;
    public static final int MOVE_UP = 2;
    public static final int MOVE_LEFT = 3;
    public static final int MOVE_DOWN = 4;
    private int level = 0;
    private String name = "";
    private double resolutionNorm = 1.0;
    private int dt = 100;
    private int timeLimit = 0;
    Map2D _gameMap = new Map(23,21, 0);
    Ghosts[] _ghosts = new Ghosts[4];
    Pixel2D _pacmanPos = new Index2D(1,1);


    @Override
    public void init(int level, String name, int dt) {

    }

    @Override
    public void play() {

    }

    @Override
    public void pasue() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void move(int direction) {

    }

    @Override
    public int getGameStatus() {
        return 0;
    }

    @Override
    public Map2D getMap() {
        return null;
    }

    @Override
    public Pixel2D getPacmanPos() {
        return null;
    }

    @Override
    public double getTime() {
        return 0;
    }

    @Override
    public int getScore() {
        return 0;
    }

    @Override
    public int getDotsLeft() {
        return 0;
    }

    @Override
    public Ghosts[] getGhosts() {
        return new Ghosts[0];
    }

    @Override
    public void addGhost(Ghosts g) {

    }

    @Override
    public void setColors(int obsColor, int dotColor, int powerColor, int MapColor) {

    }

    @Override
    public void drawGame() {

    }

    private void setupMap(){

    }
}
