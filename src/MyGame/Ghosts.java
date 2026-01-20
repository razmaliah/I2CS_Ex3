package MyGame;

import Algo.*;
import Algo.Index2D;
import Algo.Pixel2D;

public class Ghosts {

    private Pixel2D _pos= new Index2D(0,0);

    public Ghosts(){
        _pos = new Index2D(11,11);
    }

    public Pixel2D get_pos(){
        return this._pos;
    }

}
