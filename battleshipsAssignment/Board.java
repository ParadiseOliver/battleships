import java.util.HashMap;
import java.util.ArrayList;
import java.util.HashSet;
import java.io.Serializable;

/**
 * Write a description of class Board here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Board implements BoardInterface, Serializable
{
    private HashMap<ShipInterface,Placement> ships;
    private ArrayList<String> usedShots; 
    
    public Board()
    {
       ships = new HashMap<ShipInterface,Placement>();
       usedShots = new ArrayList<String>();       
    }
    
    public boolean allPlaced(){                 //are all 5 ships placed.
        if (ships.keySet().size() == 5){
            return true;
        }else{
            return false;
        }
    }
    
    public int numberPlaced(){              //how many ships are placed (used to continue/load game where not all 
        return ships.keySet().size();       // ships were placed when paused/saved)
    }
    
    public ArrayList<String> used(){
        return usedShots;
    }
    
    public void placeShip(ShipInterface ship, Position position, boolean isVertical) throws InvalidPositionException, ShipOverlapException {
        if(position.getX()<0 || (position.getX()+ship.getSize()-1>10 && !isVertical) || position.getY()<0 || (position.getY()+ship.getSize()-1>10 && isVertical)){
            throw new InvalidPositionException();
        }
        if (isVertical){
            Position temp = new Position(1,1);
            temp.setX(position.getX());
            temp.setY(position.getY());
            for (int i = 0; i < ship.getSize(); i++){                                
                temp.setY(position.getY()+i);
                if (getStatus(temp) != ShipStatus.NONE){
                    throw new ShipOverlapException();
                }
            }
        }
        else {
            Position temp = new Position(1,1);
            temp.setX(position.getX());
            temp.setY(position.getY());
            for (int i = 0; i < ship.getSize(); i++){
                temp.setX(position.getX()+i);
                if (getStatus(temp) != ShipStatus.NONE){
                    throw new ShipOverlapException();
                }
            }
        }
        Placement pos = new Placement(position,isVertical);
        ships.put(ship, pos);
    }
    
    public void shoot(Position position) throws InvalidPositionException{       
        try{
            if (!usedShots.contains(position.toString())){
                usedShots.add(position.toString());
            }
            for (ShipInterface ship : ships.keySet()){
                if (ships.get(ship).isVertical() == true){
                    for (int i = 0; i<ship.getSize();i++){
                        Position temp = new Position(1,1);
                        temp.setX(ships.get(ship).getPosition().getX());
                        temp.setY(ships.get(ship).getPosition().getY());
                        temp.setY(temp.getY()+i);
                        if (position.toString().equals(temp.toString())){
                            ship.shoot(i);
                            break;
                        }                        
                    }
                }
                else{
                    for (int i = 0; i<ship.getSize();i++){
                        Position temp = new Position(1,1);
                        temp.setX(ships.get(ship).getPosition().getX());
                        temp.setY(ships.get(ship).getPosition().getY());
                        temp.setX(temp.getX()+i);
                        if (position.toString().equals(temp.toString())){
                            ship.shoot(i);
                            break;
                        }                        
                    }
                }
            }
        }
        catch (InvalidPositionException pos){
            System.out.println("Invalid position:");
        }
        catch (NullPointerException nul){            
        }
    }
    
    public ShipStatus getStatus(Position position) throws InvalidPositionException{
        try{
            for (ShipInterface ship : ships.keySet()){
                if (ships.get(ship).isVertical() == true){
                    for (int i = 0; i<ship.getSize();i++){
                        Position temp = new Position(1,1);
                        temp.setX(ships.get(ship).getPosition().getX());
                        temp.setY(ships.get(ship).getPosition().getY());
                        temp.setY(temp.getY()+i);
                        if (temp.toString().equals(position.toString())){
                            return ship.getStatus(i);
                        }                    
                    }
                }
                else{
                    for (int i = 0; i<ship.getSize();i++){
                        Position temp = new Position(1,1);
                        temp.setX(ships.get(ship).getPosition().getX());
                        temp.setY(ships.get(ship).getPosition().getY());
                        temp.setX(temp.getX()+i);                    
                        if (position.toString().equals(temp.toString())){
                            return ship.getStatus(i);
                        }
                    }
                }
            }
            
        }
        catch(NullPointerException nul){
            return null;
        }
        return ShipStatus.NONE;
    }
    
    public boolean allSunk(){
        for (ShipInterface ship : ships.keySet()){
            if(!ship.isSunk()){
                return false;
            }
        }
        return true;
    }

    public BoardInterface clone(){
        Board clonedBoard = new Board();
        for (ShipInterface ship: ships.keySet()){
            try{
                clonedBoard.placeShip(ship, ships.get(ship).getPosition(), ships.get(ship).isVertical());
            }
            catch (ShipOverlapException overlap){
                System.out.println("Ships overlap:");
            }
            catch (InvalidPositionException pos){
                System.out.println("Invalid position:");
            }
        }
        return clonedBoard;
    }
    
    public boolean isMiss(Position position){           //whether a position would hit or miss (used to display shots)
        try{
            if (getStatus(position) == ShipStatus.NONE){
                return true;
            }
            else{
                return false;
            }
        }
        catch (InvalidPositionException pos){
            System.out.println("Invalid position:");
        }
        return true; //shouldnt be used but needed a return statement
    }
    
    public String shotString(){             //the board display shown when making shots
        String display = "X  1 2 3 4 5 6 7 8 9 10 \n";
        try{ 
            Position temp = new Position(1,1);
            for (int i = 0; i<10; i++){
                display += Integer.toString(i+1);
                if (i != 9){
                    display += " ";
                }
                temp.setY(i+1);
                for (int j = 0; j<10; j++){
                    temp.setX(j+1);                    
                    if (getStatus(temp) == ShipStatus.HIT){
                        display += " X";
                    }
                    else if (getStatus(temp) == ShipStatus.SUNK){
                        display += " S";
                    }
                    else if (isMiss(temp)){
                        if(usedShots.contains(temp.toString())){
                            display += " M";   
                        }
                        else{
                            display += " -";
                        }
                    }
                    else if (getStatus(temp) == ShipStatus.INTACT){
                        display += " -";
                    }                 
                }
                display += "\n";
            }            
        }
        catch (InvalidPositionException pos){
            System.out.println("Invalid position: ");
        }
        return display;
    }    
    
    public String toString(){   
        String display = "X  1 2 3 4 5 6 7 8 9 10 \n";
        try{ 
            Position temp = new Position(1,1);
            for (int i = 0; i<10; i++){
                display += Integer.toString(i+1);
                if (i != 9){
                    display += " ";
                }
                temp.setY(i+1);
                for (int j = 0; j<10; j++){
                    temp.setX(j+1);
                    if (getStatus(temp) == ShipStatus.NONE){
                        display += " -";
                    }
                    else if (getStatus(temp) == ShipStatus.HIT){
                        display += " X";
                    }
                    else if (getStatus(temp) == ShipStatus.SUNK){
                        display += " S";
                    }
                    else if (getStatus(temp) == ShipStatus.INTACT){
                        display += " I";
                    }                    
                }
                display += "\n";
            }            
        }
        catch (InvalidPositionException pos){
            System.out.println("Invalid position:");
        }
        return display;
    }
}