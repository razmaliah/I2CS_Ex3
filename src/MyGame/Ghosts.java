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
}
