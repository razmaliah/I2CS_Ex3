package MyGame;

import Algo.*;
import exe.ex3.game.Game;

public interface PacGame {

    void init(int level, String name, int dt);

    void play();

    void pasue();

    void stop();

    void move(int direction);

    int getGameStatus();

    Map2D getMap();

    Pixel2D getPacmanPos();

    double getTime();

    int getScore();

    int getDotsLeft();

    Ghosts[] getGhosts();

    void addGhost(Ghosts g);

    void setColors(int obsColor, int dotColor, int powerColor, int MapColor);

    void drawGame();
}
