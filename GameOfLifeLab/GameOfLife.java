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
    
    private int ROWS = 30;
    private int COLS = 30;
    
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
        construct();
        
    }
    public GameOfLife(int rows, int cols)
    {
        // create the grid, of the specified size, that contains Actors
        ROWS = rows;
        COLS = cols;
        construct();
    }
    private void construct(){
        // create the grid, of the specified size, that contains Actors
        BoundedGrid<Actor> grid = null;
        try{
            grid = new BoundedGrid<Actor>(ROWS, COLS);
        } catch(IllegalArgumentException e){
            JOptionPane.showMessageDialog(null, "You cannot have a world of size zero!");
            return;
        }
        
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
        Grid<Actor> grid = world.getGrid();
        
        // create and add rocks (a type of Actor) to the three intial locations
        Rock rock1 = new Rock();
        Location loc1 = new Location(Y1, X1);
        rock1.putSelfInGrid(grid, loc1);
        actors.add(new Object[]{rock1, loc1});
        
        Rock rock2 = new Rock();
        Location loc2 = new Location(Y2, X2);
        rock2.putSelfInGrid(grid, loc2);
        actors.add(new Object[]{rock2, loc2});
        
        Rock rock3 = new Rock();
        Location loc3 = new Location(Y3, X3);
        rock3.putSelfInGrid(grid, loc3);
        actors.add(new Object[]{rock3, loc3});
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
        
        // create the grid, of the specified size, that contains Actors
        Grid<Actor> grid = world.getGrid();
        int rows = getNumRows();
        int cols = getNumCols();
        
        actors.clear();
        
        // insert magic here...
        Grid<Actor> grid2 = new BoundedGrid<Actor>(rows, cols);
        for(int col=0; col<cols; col++){
            for(int row=0; row<rows; row++){
                Location cpos = new Location(row, col);
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
        
        for(Object[] obja : actors){
            Actor actor = (Actor)obja[0];
            Location loc = (Location)obja[1];
            try{
                actor.removeSelfFromGrid();
            } catch(IllegalStateException e){
                // Ignore
            }
            actor.putSelfInGrid(grid2, loc);
        }
        world.setGrid(grid2);
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
                int cols = Integer.parseInt(JOptionPane.showInputDialog(null, "How many columns wide should the grid be?"));
                GameOfLife game = new GameOfLife(rows, cols);
            } catch(NumberFormatException e){
                JOptionPane.showMessageDialog(null, "That's not a number!");
            }
                
        } else if(args.length == 2){
            int rows = Integer.parseInt(args[0]);
            int cols = Integer.parseInt(args[1]);
            GameOfLife game = new GameOfLife(rows, cols);
        } else{
            JOptionPane.showMessageDialog(null, "Invalid argument count!");
        }
    }

}
