
import java.util.ArrayList;

/**
 * More or less a helper class. Think of it as a brain for the AI to store values.
 *
 * @author Mark
 */
public class AI {
    
    private static double alpha = Integer.MAX_VALUE;
    private static double beta = Integer.MIN_VALUE;
    private static Board[] listOfBoards;
    private static boolean firstCheck = true;
    private static Board best;
    private static int lookAheadNumber;
    
    public static void add(int index, Board pathwayBoard){
        listOfBoards[index] = pathwayBoard;
    }
    
    public static void setLookAhead(int inLookAheadNumber){
        lookAheadNumber = inLookAheadNumber;
    }
    
    public static int getLookAhead(){
        return lookAheadNumber;
    }
    
    public static void createArray(int size){ //the size equals the lookAhead Number + 1
        listOfBoards = new Board[size];
    }
    public static void remove(int index){
        listOfBoards[index] = null;
    }
    
    public static void clear(){
        for(int i = 0; i < listOfBoards.length; i++)
            listOfBoards[i] = null;
    }
    
    public static Board[] getListOfBoards(){
        return listOfBoards;
    }
    
    public static void setAlpha(double inAlpha){
        alpha = inAlpha;
    }
    
    public static void setBeta(double inBeta){
        beta = inBeta;
    }
    
    public static double getAlpha(){
        return alpha;
    }
    
    public static double getBeta(){
        return beta;
    }
    
    public static void runFirstCheck(){
        firstCheck = false;
    }
    
    public static boolean isFirstCheck(){
        return firstCheck;
    }
    
    public static void chooseBestBoard(int index){
        best = listOfBoards[index];
    }
    
    public static Board getBest(){
        return listOfBoards[lookAheadNumber-1];
    }
    
    public static void resetAI(){
        firstCheck = true;
        AI.clear();
        alpha = Integer.MIN_VALUE;
        beta = Integer.MAX_VALUE;
        
    }
}
