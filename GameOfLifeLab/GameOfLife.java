import info.gridworld.actor.Actor;
import info.gridworld.actor.ActorWorld;
import info.gridworld.actor.Rock;
import info.gridworld.grid.*;
//import info.gridworld.grid.Grid;
//import info.gridworld.grid.Location;
import java.util.ArrayList;
import javax.swing.*;
import java.lang.*;

/**
 * Game of Life starter code. Demonstrates how to create and populate the game using the GridWorld framework.
 * Also demonstrates how to provide accessor methods to make the class testable by unit tests.
 * 
 * @author Alex Anderson
 * @version November 17, 2015
 */
public class GameOfLife
{
    // the world comprised of the grid that displays the graphics for the game
    private ActorWorld world;
    
    private int ROWS = 0;
    private int COLS = 0;
    
    private Grid<Actor> grid = null;
    
    private int maxX, maxY, minX, minY;
    
    private ArrayList<Thread> threads = new ArrayList<Thread>();
    
    private int x=0;
    
    private ArrayList<Object[]> actors = new ArrayList<Object[]>();
    
    /**
     * Default constructor for objects of class GameOfLife
     * 
     * @post    the game will be initialized and populated with the initial state of cells
     * 
     */
    public GameOfLife()
    {
        
        grid = new UnboundedGrid<Actor>();
        construct();
    }
    public GameOfLife(int rows, int cols)
    {
        // create the grid, of the specified size, that contains Actors
        ROWS = rows;
        COLS = cols;
        
        
        try{
            grid = new BoundedGrid<Actor>(ROWS, COLS);
        } catch(IllegalArgumentException e){
            JOptionPane.showMessageDialog(null, "You cannot have a world of zero or negative size!");
            return;
        }
        construct();
    }
    private void construct(){
        
        // create a world based on the grid
        world = new ActorWorld(grid){
            public void step(){
                createNextGeneration();
            }
        };
        // populate the game
        try{
            populateGame();
        } catch(IllegalArgumentException e){
            // Ignore
        }
        
        // display the newly constructed and populated world
        world.show();
    }
    
    /**
     * Creates the actors and inserts them into their initial starting positions in the grid
     *
     * @pre     the grid has been created
     * @post    all actors that comprise the initial state of the game have been added to the grid
     * 
     */
    void populateGame() throws IllegalArgumentException
    {
        // constants for the location of the three cells initially alive
        final int X1 = 0, Y1 = 0;
        final int X2 = 0, Y2 = 2;
        final int X3 = 1, Y3 = 2;

        // the grid of Actors that maintains the state of the game
        //  (alive cells contains actors; dead cells do not)
        
        // create and add rocks (a type of Actor) to the three intial locations
        Rock rock1 = new Rock();
        Location loc1 = new Location(Y1, X1);
        rock1.putSelfInGrid(grid, loc1);
        
        Rock rock2 = new Rock();
        Location loc2 = new Location(Y2, X2);
        rock2.putSelfInGrid(grid, loc2);
        
        Rock rock3 = new Rock();
        Location loc3 = new Location(Y3, X3);
        rock3.putSelfInGrid(grid, loc3);
        
    }

    /**
     * Generates the next generation based on the rules of the Game of Life and updates the grid
     * associated with the world
     *
     * @pre     the game has been initialized
     * @post    the world has been populated with a new grid containing the next generation
     * 
     */
    void createNextGeneration()
    {
        /** You will need to read the documentation for the World, Grid, and Location classes
         *      in order to implement the Game of Life algorithm and leverage the GridWorld framework.
         */
        
        grid = world.getGrid();
        
        // create the grid, of the specified size, that contains Actors
        int rows = getNumRows();
        int cols = getNumCols();
        
        actors.clear();
        
        maxX = 0;
        maxY = 0;
        minX = 0;
        minY = 0;
        for(Location loc : grid.getOccupiedLocations()){
            int x = loc.getCol();
            int y = loc.getRow();
            if(x > maxX){
                maxX = x;
            }
            else if(x < minX){
                minX = x;
            }
            if(y > maxY){
                maxY = y;
            } else if(y < minY){
                minY = y;
            }
        }
        
        maxX += 2;
        maxY += 2;
        minX -= 2;
        minY -= 2;
        
        // insert magic here...
        Grid<Actor> grid2 = new UnboundedGrid<Actor>();
        for(int col=minX; col<maxX; col++){
            for(int row=minY; row<maxY; row++){
                Location cpos = new Location(row, col);
                if(grid.isValid(cpos)){
                    int nC = grid.getNeighbors(cpos).size();
                    Actor ac = grid.get(cpos);
                    if(ac != null){
                        if(nC >= 2 && nC <= 3){
                            actors.add(new Object[]{ac, cpos});
                        }
                    } else{
                        if(nC == 3){
                            actors.add(new Object[]{new Rock(), cpos});
                        }
                    }
                }
            }
        }
        for(Location loc : grid.getOccupiedLocations()){
            grid.get(loc).removeSelfFromGrid();
        }
        for(Object[] obja : actors){
            Actor actor = (Actor)obja[0];
            Location loc = (Location)obja[1];
            actor.putSelfInGrid(grid, loc);
        }
    }
    
    /**
     * Returns the actor at the specified row and column. Intended to be used for unit testing.
     *
     * @param   row the row (zero-based index) of the actor to return
     * @param   col the column (zero-based index) of the actor to return
     * @pre     the grid has been created
     * @return  the actor at the specified row and column
     */
    public Actor getActor(int row, int col)
    {
        Location loc = new Location(row, col);
        Actor actor = world.getGrid().get(loc);
        return actor;
    }

    /**
     * Returns the number of rows in the game board
     *
     * @return    the number of rows in the game board
     */
    public int getNumRows()
    {
        return world.getGrid().getNumRows();
    }
    
    /**
     * Returns the number of columns in the game board
     *
     * @return    the number of columns in the game board
     */
    public int getNumCols()
    {
        return world.getGrid().getNumCols();
    }
    
    
    /**
     * Creates an instance of this class. Provides convenient execution.
     *
     */
    public static void main(String[] args)
    {
        
        if(args.length == 0){
            try{
                int rows = Integer.parseInt(JOptionPane.showInputDialog(null, "How many rows high should the grid be?"));
                if(rows == -1){
                    GameOfLife game = new GameOfLife();
                    return;
                }
                int cols = Integer.parseInt(JOptionPane.showInputDialog(null, "How many columns wide should the grid be?"));
                GameOfLife game = new GameOfLife(rows, cols);
            } catch(NumberFormatException e){
                JOptionPane.showMessageDialog(null, "That's not a number!");
            }
                
        } else if(args.length == 2){
            int rows = Integer.parseInt(args[0]);
            int cols = Integer.parseInt(args[1]);
            GameOfLife game = new GameOfLife(rows, cols);
        } else if(args.length == 1 && (args[0].equals("infinite") || args[0].equals("inf") || args[0].equals("unbounded"))){
            GameOfLife game = new GameOfLife();
        } else{
            JOptionPane.showMessageDialog(null, "Invalid argument count!");
        }
        return;
        
       //new GameOfLife();
    }

}
