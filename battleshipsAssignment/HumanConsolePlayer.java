import java.util.Scanner;
/**
 * Write a description of class HumanConsolePlayer here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class HumanConsolePlayer implements PlayerInterface
{
    private String name;
    private Scanner input;

    public HumanConsolePlayer(String name)
    {
        this.name  = name;
        input = new Scanner(System.in);
    }
    
    public Placement choosePlacement(ShipInterface ship, BoardInterface board) throws PauseException{ //beware ship overlap
        System.out.println("You are placing a ship of size: "+ ship.getSize());
        boolean error = true;
        while(error){
            error = false;
            try{
                System.out.println("Enter the X Co-ordinate: ");
                String in1 = input.nextLine();
                if (in1.toLowerCase().equals("pause")){
                    throw new PauseException();
                }                
                int x = Integer.parseInt(in1);
                if (x < 0 || x >10){
                    throw new InvalidPositionException();
                }
                System.out.println("Enter the Y Co-ordinate: ");
                String in2 = input.nextLine();
                if (in2.toLowerCase().equals("pause")){
                    throw new PauseException();
                }
                int y = Integer.parseInt(in2); 
                if (y < 0 || y >10){
                    throw new InvalidPositionException();
                }
                System.out.println("Is this ship vertical (enter 'true') or horizontal (enter 'false')? ");
                String in3 = input.nextLine();
                if(!(in3.toLowerCase().equals("true") || in3.toLowerCase().equals("false") || in3.toLowerCase().equals("pause"))){
                    throw new InvalidPositionException();
                }
                if (in3.toLowerCase().equals("pause")){
                    throw new PauseException();
                }
                boolean isVertical = Boolean.parseBoolean(in3);                
                Position p = new Position(x,y);
                Placement place = new Placement(p,isVertical);
                return place;
            } catch (InvalidPositionException pos) {
                error = true;
                System.out.println("Please enter an number between 1 and 10 or 'Pause' ");
            } catch (NumberFormatException number) {
                error = true;
                System.out.println("Please enter an number between 1 and 10 or 'Pause' ");
            }            
        }
        return null;
    }

    public Position chooseShot() throws PauseException{
        boolean error = true;
        while(error){
            error = false;
            try{
                Position shot = new Position(1,1);
                System.out.println("Enter X co-ordinate of shot: ");
                String in1 = input.nextLine();
                if (in1.toLowerCase().equals("pause")){
                    throw new PauseException();
                }
                int x = Integer.parseInt(in1);
                shot.setX(x);
                System.out.println("Enter Y co-ordinate of shot: ");
                String in2 = input.nextLine();
                if (in2.toLowerCase().equals("pause")){
                    throw new PauseException();
                }
                int y = Integer.parseInt(in2);
                shot.setY(y);
                return shot;
            }
            catch(InvalidPositionException pos){
                error = true;
                System.out.println("Please enter an number between 1 and 10 or 'Pause' ");
            }
            catch(NumberFormatException number){
                error = true;
                System.out.println("Please enter an number between 1 and 10 or 'Pause' ");                
            }
        }
        return null;
    }

    public void shotResult(Position position, ShotStatus status){
        switch(status){
            case HIT: System.out.println("Shot: "+ position.toString()+" Hit");break;
            case MISS: System.out.println("Shot: "+ position.toString()+" Miss");break;
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
