import java.util.ArrayList;
import java.io.Serializable;

/**
 * Write a description of class Ship here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Ship implements ShipInterface, Serializable
{
    private int size;
    private boolean sunk;
    private ShipStatus[] offsetStatus;
    
    public Ship(int size)
    {
        this.size = size;
        offsetStatus = new ShipStatus[size];
        for (int i = 0; i < size; i++){
            offsetStatus[i] = ShipStatus.INTACT;
        }
    }
    
    public int getSize(){
        return size;
    }
    
    public boolean isSunk(){                            //is the ship sunk
        if (offsetStatus[0] == ShipStatus.SUNK){
            return true;
        }else{
            return false;
        }
    }
    
    public boolean allHit(){                            //are all the offsets of a ship maked hit (used to change to sunk)
        for (ShipStatus status : offsetStatus){
            if (status != ShipStatus.HIT){
                return false;
            }    
        }
        return true;
    }
    
    public void shoot(int offset) throws InvalidPositionException{
        if(offset<0 || offset>10){
            throw new InvalidPositionException();
        }
        if (!isSunk()){
            offsetStatus[offset] = ShipStatus.HIT;
            if (allHit()){
                for (int i = 0; i < size; i++){
                    offsetStatus[i] = ShipStatus.SUNK;
                }
            }
        }
    }
    
    public ShipStatus getStatus(int offset) throws InvalidPositionException{
        if (offset>=size || offset<0){
            throw new InvalidPositionException();
        }
        return offsetStatus[offset];
    }
}
