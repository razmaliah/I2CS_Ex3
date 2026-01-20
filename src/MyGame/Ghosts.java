package MyGame;

import Algo.*;


public class Ghosts {

    private Pixel2D _pos= new Index2D(0,0);
    private boolean _eatable = false;

    public Ghosts(){
        _pos = new Index2D(11,11);
    }

    public Pixel2D getPos(){
        return this._pos;
    }

    public void setPos(Pixel2D pos){
        this._pos = pos;
    }

    public boolean isEatable() {
        return _eatable;
    }

    public void setEatable(boolean eatable) {
        this._eatable = eatable;
    }

    public void moveRandom(Map2D map, int obs){
        int dir = (int)(Math.random()*4) +1;
        Pixel2D pr = new Index2D(_pos.getX()+1, _pos.getY());
        if(pr.getX()==map.getWidth()){
            pr = new Index2D(0, _pos.getY());
        }
        Pixel2D pl = new Index2D(_pos.getX()-1, _pos.getY());
        if(pl.getX() <0){
            pl = new Index2D(map.getWidth()-1, _pos.getY());
        }
        Pixel2D pu = new Index2D(_pos.getX(), _pos.getY()+1);
        Pixel2D pd = new Index2D(_pos.getX(), _pos.getY()-1);
        if(dir == 1 && map.getPixel(pr) != obs){
            _pos = pr;
        }
        if(dir == 2 && map.getPixel(pl) != obs){
            _pos = pl;
        }
        if(dir == 3 && map.getPixel(pu) != obs){
            _pos = pu;
        }
        if(dir == 4 && map.getPixel(pd) != obs){
            _pos = pd;
        }
        else{
            moveRandom(map,obs);
        }
    }
}
