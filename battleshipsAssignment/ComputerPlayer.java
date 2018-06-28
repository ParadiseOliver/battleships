import java.util.Random;

/**
 * Write a description of class ComputerPlayer here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class ComputerPlayer implements PlayerInterface
{
    private String name;

    public ComputerPlayer(String name)
    {
        this.name = name;
    }

    public Placement choosePlacement(ShipInterface ship, BoardInterface board) throws PauseException{
        Random random = new Random();
        int x = random.nextInt(10-ship.getSize())+1;
        int y = random.nextInt(10-ship.getSize())+1;
        int vertical = random.nextInt(2);
        boolean isVertical;
        try{
            Position position = new Position(x,y);
            if (vertical == 0){
                isVertical = true;
            }
            else {
                isVertical = false;
            }
            Placement place = new Placement(position, isVertical);
            return place;
        }
        catch (InvalidPositionException pos){
            return null;
        }
    }

    public Position chooseShot() throws PauseException{
        Random random = new Random();
        try {
            int x = random.nextInt(10)+1;
            int y = random.nextInt(10)+1;
            Position shot = new Position(x,y);
            return shot;            
        }
        catch (InvalidPositionException pos){
        }
        return null;
    }

    public void shotResult(Position position, ShotStatus status){
        try{     
            switch(status){
                case HIT: System.out.println("Shot: "+ position.toString()+" Hit");break;
                case MISS: System.out.println("Shot: "+ position.toString()+" Miss");break;
            }
        }
        catch(NullPointerException nul){            
        }
    }

    public void opponentShot(Position position){
        try{
            System.out.println("Opponent shot at: "+ position.toString());
        }
        catch(NullPointerException nul){
        }
    }

    public String toString(){
        return name;
    }
}
