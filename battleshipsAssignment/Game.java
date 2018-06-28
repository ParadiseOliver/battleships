import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.File;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;

/**
 * Write a description of class Game here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Game implements GameInterface
{
    private static Game game;
    private static Board player1Board;
    private static Board player2Board;
    private static PlayerInterface player1;
    private static PlayerInterface player2;
    private static PlayerInterface winner;
    private static int[] shipSizes = {2,3,3,4,5};

    public Game(PlayerInterface player1, PlayerInterface player2)
    {
        player1Board = new Board();
        player2Board = new Board();
        this.player1 = player1;
        this.player2 = player2;
    }

    public static void main(String[] args){    
        System.out.println();
        System.out.println("....BATTLESHIPS....");
        System.out.println("Main Menu, please type: ");
        System.out.println("1, to create players;");
        System.out.println("2, to load a game;");
        System.out.println("3, to continue the game;");
        System.out.println("4, to save the game;");
        System.out.println("5, to start a new game;");
        System.out.println("6, to exit the program.");
        System.out.println();

        Scanner input = new Scanner(System.in);        
        boolean error = true;
        while (error){
            error = false;

            String choice = input.nextLine();
            if(choice.equals("1")){
                System.out.println("Create player 1:");
                player1 = createPlayer();
                System.out.println("Create player 2:");
                player2 = createPlayer();
                main(args);
            }
            else if(choice.equals("2")){                
                try{                    
                    String fileName = "";
                    while(fileName.trim().length() == 0){
                        System.out.println("Enter name of file to load from: ");
                        fileName = input.nextLine();
                    }
                    if(fileName.toLowerCase().equals("pause")){
                        throw new PauseException(); 
                    }
                    else{
                        game = new Game(player1,player2);
                        game.loadGame(fileName);
                        System.out.println("File loaded. Select continue to play this game.");
                        main(args);                     
                    }
                } 
                catch (PauseException pause){                    
                    main(args);
                }
                catch (IOException io){                    
                }
            }
            else if(choice.equals("3")){                
                if (player1 == null || player2 == null){
                    System.out.println("No current player, default types will now be selected.");
                    System.out.println("To change this, go to the pause menu and select create players (1).");
                    player1 = new HumanConsolePlayer(defaultPlayer());
                    player2 = new ComputerPlayer(defaultPlayer());
                }
                if (game != null){
                    System.out.println("Continuing game...");
                    winner = game.play();
                }
                else{
                    System.out.println("No ongoing game to continue");
                    main(args);
                }
            }
            else if(choice.equals("4")){
                try{
                    if (game != null){
                        System.out.println("Enter name of file to save to: ");
                        String fileName = input.nextLine();
                        if(fileName.toLowerCase().equals("pause")){
                            throw new PauseException(); 
                        }
                        else{
                            game.saveGame(fileName);
                        }
                    }
                    main(args);
                } 
                catch (PauseException pause){
                    main(args);
                }
                catch (IOException io){
                }
            }
            else if(choice.equals("5")){ 
                if (player1 == null || player2 == null){
                    System.out.println("No current player, default types will now be selected.");
                    System.out.println("To change this, go to the pause menu and select create players (1).");
                    player1 = new HumanConsolePlayer(defaultPlayer());
                    player2 = new ComputerPlayer(defaultPlayer());
                }
                System.out.println("Starting new game...");
                player1Board = new Board();
                player2Board = new Board();
                game = new Game(player1, player2);
                winner = game.play();
                game = null;
                player1 = null;
                player2 = null;
                main(args);
            }
            else if(choice.equals("6")){
                System.out.println("Exiting the game...");
                System.exit(0);
            }
            else {
                error = true;
                System.out.println("Invalid input, please enter again:");
                main(args);
            }
        }
    }
    
    public static String defaultPlayer(){
        Scanner input = new Scanner(System.in);
        String name = "";
        try{
            while (name.trim().length() == 0){
                System.out.println("Type name for player: ");
                name = input.nextLine();
            }
            if(name.toLowerCase().equals("pause")){
                throw new PauseException();
            }            
        }
        catch (PauseException pause){
            String[] args = {};
            main(args);
        }
        return name;
    }
    
    public static PlayerInterface createPlayer(){
        Scanner input = new Scanner(System.in);
        PlayerInterface player = null;
        String name = "";
        try{
            while (name.trim().length() == 0){
                System.out.println("Type name for player: ");
                name = input.nextLine();
            }
            if(name.toLowerCase().equals("pause")){
                throw new PauseException();
            }

            System.out.println("Enter the type of player ('Human' or 'Computer'): ");                    
            boolean validType = false;
            while(!validType){
                String type = input.nextLine();
                if (type.toLowerCase().equals("human")){
                    player = new HumanConsolePlayer(name);
                    validType = true;
                }
                else if (type.toLowerCase().equals("computer")){
                    validType = true;
                    player = new ComputerPlayer(name);
                }
                else if (type.toLowerCase().equals("pause")){
                    validType = true;
                    throw new PauseException();
                }
                else{
                    validType = false;
                    System.out.println("Not a valid player type. Please enter again: ");
                }
            }            
        }
        catch (PauseException pause){
            String[] args = {};
            main(args);
        }
        return player;
    }

    public void placeShips(Board board,PlayerInterface player, PlayerInterface other){
        System.out.println(board.toString());
        boolean exception = true;
        for (int i = board.numberPlaced(); i<5; i++){
            exception = true;
            while (exception){
                exception = false;
                try{
                    Placement shipPlacement = player.choosePlacement(new Ship(shipSizes[i]), board.clone());                            
                    board.placeShip(new Ship(shipSizes[i]), shipPlacement.getPosition(), shipPlacement.isVertical());
                    System.out.println(board.toString());
                }
                catch(ShipOverlapException overlap){
                    exception = true;
                    System.out.println("Placement overlaps with another ship. Enter a new placement: ");
                }                        
                catch(InvalidPositionException pos){
                    exception = true;
                    System.out.println("Invalid position chosen. Enter a new position: ");
                }
                catch (PauseException pause){  
                    String[] args = {};
                    main(args);
                }
            }
        }
    }

    public PlayerInterface play(){
        placeShips(player1Board, player1, player2);
        placeShips(player2Board, player2, player1);

        boolean gameWon = false;
        while (!gameWon){
            try{
                Position player1Shot = null;
                Position player2Shot = null;
                System.out.println(player1.toString()+"'s previous shots:");
                System.out.println(player2Board.shotString());
                boolean used1 = true;
                while(used1){                    
                    player1Shot = player1.chooseShot(); 
                    if (player2Board.used().contains(player1Shot.toString())){
                        used1 = true;
                    }
                    else{
                        used1 = false;
                    }
                }
                if (player1Shot.getX()<1 || player1Shot.getY()<1 || player1Shot.getX()>10 || player1Shot.getY()>10){
                    System.out.println("Illegal shot. You forfeit the game!");
                    return player2;
                }
                player2Board.shoot(player1Shot);
                if (player2Board.getStatus(player1Shot) == ShipStatus.NONE){
                    player1.shotResult(player1Shot, ShotStatus.MISS);
                    System.out.println();
                }
                else {
                    player1.shotResult(player1Shot, ShotStatus.HIT);
                    System.out.println();                    
                }
                if (player2Board.allSunk()){
                    gameWon = true;
                    System.out.println(player1.toString().toUpperCase()+" WINS");
                    return player1;
                }
                System.out.println(player2.toString()+"'s previous shots:");
                System.out.println(player1Board.shotString());
                player2.opponentShot(player1Shot);
                boolean used2 = true;
                while(used2){                    
                    player2Shot = player2.chooseShot();
                    if (player1Board.used().contains(player2Shot.toString())){
                        used2 = true;
                    }
                    else{
                        used2 = false;
                    }
                }
                if (player2Shot.getX()<1 || player2Shot.getY()<1 || player2Shot.getX()>10 || player2Shot.getY()>10){
                    System.out.println("Illegal shot. You forfeit the game!");
                    return player1;
                }
                player1Board.shoot(player2Shot);
                if (player1Board.getStatus(player2Shot) == ShipStatus.NONE){
                    player2.shotResult(player2Shot, ShotStatus.MISS);
                    System.out.println();
                }
                else {
                    player2.shotResult(player2Shot, ShotStatus.HIT);
                    System.out.println();
                }
                if (player1Board.allSunk()){
                    gameWon = true;
                    System.out.println(player2.toString().toUpperCase()+" WINS");
                    return player2;
                }
                player1.opponentShot(player2Shot);
            }
            catch (PauseException pause){
                String[] args = {};
                main(args);
                return null;
            }
            catch (InvalidPositionException pos){                
            }
        }
        return null;
    }

    public void saveGame(String filename) throws IOException{
        Board board1 = player1Board;
        Board board2 = player2Board;
        ArrayList<Board> boards = new ArrayList<Board>();
        boards.add(board1);
        boards.add(board2);
        try{
            FileOutputStream fos = new FileOutputStream(new File(filename+".ser"));
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(boards);
            oos.close();  
            fos.close();
        } catch(FileNotFoundException file) {
            System.out.println("Unable to open file '" + filename + "'");                
        }
        catch(IOException ex) {
            System.out.println("Error reading file '" + filename + "' instansiating with curent string");                  
        }
    }

    public void loadGame(String filename) throws IOException{
        try{
            FileInputStream fis = new FileInputStream(new File(filename+".ser"));
            ObjectInputStream ois = new ObjectInputStream(fis);
            ArrayList<Board> boards = new ArrayList<Board>();
            boards = (ArrayList<Board>) ois.readObject();
            player1Board = boards.get(0);
            player2Board = boards.get(1);
            ois.close();
            fis.close();            
        } 
        catch (FileNotFoundException noFile){
            System.out.println("biwefb");
        }
        catch(IOException ex){
            System.out.println("Error reading file '" + filename + "' instansiating with curent string");                              
        } 
        catch(ClassNotFoundException ex){
            System.out.println("Unable to open file '" + filename + "'");
        }

    }
}
