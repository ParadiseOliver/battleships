import java.io.Serializable;

public class Placement implements Serializable
{
    private Position position;
    private boolean isVertical;
    
    public Placement(Position position, boolean isVertical){
        this.position = position;
        this.isVertical = isVertical;
    }
    
    public Position getPosition(){
        return position;
    }
    
    public boolean isVertical(){
        return isVertical;
    }
}
