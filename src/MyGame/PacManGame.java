package MyGame;

import Algo.*;

public class PacManGame implements PacGame{

    public static final int OBS_COLOR = 1;
    public static final int DOT_COLOR = 2;
    public static final int POWER_COLOR = 3;
    public static final int MOVE_RIGHT = 1;
    public static final int MOVE_UP = 2;
    public static final int MOVE_LEFT = 3;
    public static final int MOVE_DOWN = 4;
    private long _startTime = System.currentTimeMillis();
    private int _level = 0;
    private String _username = "";
    private int _dt = 100;
    private int _status =0;
    private int _dotsLeft =100;
    private boolean _isEatable = false;
    Map2D _gameMap = new Map(23,21, 0);
    Ghosts[] _ghosts = new Ghosts[4];
    Pixel2D _pacmanPos = new Index2D(1,1);

    public PacManGame(int level, String username, int dt){
        init(level, username, dt);
    }

    @Override
    public void init(int level, String username, int dt) {
        _level = level;
        _username = username;
        _dt = dt;
        setupMap();
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
        return _status;
    }

    @Override
    public Map2D getMap() {
        return _gameMap;
    }

    @Override
    public Pixel2D getPacmanPos() {
        return _pacmanPos;
    }

    @Override
    public double getTime() {
        long now = System.currentTimeMillis();
        long td = now - _startTime;
        double t = (double)td/1000;
        return t;
    }

    @Override
    public int getScore() {
        return 0;
    }

    @Override
    public int getDotsLeft() {
        return _dotsLeft;
    }

    @Override
    public Ghosts[] getGhosts() {
        return _ghosts;
    }

    public boolean isEatable(){
        return _isEatable;
    }

    @Override
    public void addGhost(Ghosts g) {
        Ghosts[] g1 = new Ghosts[_ghosts.length+1];
        for(int i=0; i<_ghosts.length;i++){
            g1[i]= _ghosts[i];
        }
        g1[_ghosts.length] = g;
        _ghosts = g1;
    }

    @Override
    public void setColors(int obsColor, int dotColor, int powerColor, int MapColor) {

    }

    @Override
    public void drawGame() {

    }

    private void setupMap() {
        Map2D map = new Map(23, 22, 0);
        for (int w = 0; w < 11; w++) {                      // left side
            for (int h = 0; h < 22; h++) {
                if (h==21 || h==0){
                    map.setPixel(new Index2D(w, h), OBS_COLOR);}
                if(w==0 && h!=9 && h!=11){
                    map.setPixel(new Index2D(w, h), OBS_COLOR);}
                if(w==1 && (h==4 || h ==10 || h==12 || h==14)){
                    map.setPixel(new Index2D(w, h), OBS_COLOR);}
                if(w==2 && (h==2 || h ==4 || h==6 || h==8 || h==14 || (h>=16 && h<= 19))){
                    map.setPixel(new Index2D(w, h), OBS_COLOR);}
                if(w==3 && (h==4 || h ==6 || h==8 || h==10 || h==12 || h==14)){
                    map.setPixel(new Index2D(w, h), OBS_COLOR);}
                if(w==4 && h!=1 && h!=3 && h!=7 && h!=9 && h!=11 && h!= 13 && h!=20){
                    map.setPixel(new Index2D(w, h), OBS_COLOR);}
                if(w==5 && (h==2 || h ==19)){
                    map.setPixel(new Index2D(w, h), OBS_COLOR);}
                if(w==6 && h!=1 && h!=5 && h!=7 && h!=11 && h!=18 && h!=20){
                    map.setPixel(new Index2D(w, h), OBS_COLOR);}
                if(w==7 && (h==2 || h ==6 || h==15 || h==19)){
                    map.setPixel(new Index2D(w, h), OBS_COLOR);}
                if(w==8 && h!=1 && h!=3 && h!=5 && h!=7 && h!=9 && h!=14 && h!= 16 && h!= 18 && h!=20){
                    map.setPixel(new Index2D(w, h), OBS_COLOR);}
                if(w==9 && h!=1 && h!=3 && h!=5 && h!=7 && h!=9 && h!=11 && h!=12 && h!=14 && h!= 16 && h!= 18 && h!=20){
                    map.setPixel(new Index2D(w, h), OBS_COLOR);}
                if(w==10 && (h==4 || h ==8 || h==10 || h==13 || h==17)){
                    map.setPixel(new Index2D(w, h), OBS_COLOR);}
            }
        }
        for(int w = 12; w < 23; w++){           //mirror copy to right side
            for (int h =0; h<22 ; h++){
                Pixel2D p = new Index2D(22-w,h);
                if(map.getPixel(p) == OBS_COLOR){
                    map.setPixel(new Index2D(w, h), OBS_COLOR);
                }
            }
        }

        for (int i=0; i<22 ; i++){
            if(i!= 1 && i!=5 && i!=9 && (i<11 || i>15) && i!=18){
                map.setPixel(new Index2D(11, i), OBS_COLOR);
            }
        }
        _gameMap = map;
        for(int i=map.getHeight()-1; i>=0 ; i--){
            for(int j=0; j< map.getWidth(); j++){
                System.out.print(map.getPixel(j,i)+" ");
            }
            System.out.println();
        }
        System.out.println();
    }

    private void moveGhosts(){
        for(int i=0;i<_ghosts.length;i++){
            _ghosts[i].moveRandom(_gameMap,OBS_COLOR);
        }
    }

    static void main(String[] args) {
        PacManGame pc = new PacManGame(1,"rrr",1);
        pc.setupMap();
    }
}
