package sample;

import java.util.ArrayList;
import java.util.stream.IntStream;

public class CellGrid {
    private ArrayList<ArrayList<Boolean>> cells;

    /**
     * Constructs a new empty 2D Arraylist.
     *
     * @param w width
     * @param h height
     */
    public CellGrid(int w, int h) {
        cells = new ArrayList<>();
        for (int i = 0; i < w; i++){
            cells.add(i,new ArrayList<>());
            for (int j = 0; j < h; j++){
                cells.get(i).add(j,false);
            }
        }
    }

    /**
     * Resets the game, a new Arraylist is created where every cell is false.
     * therefore the game gets reset.
     *
     * @param w width
     * @param h height
     */
    public void reset(int w, int h) {
        cells = new ArrayList<>();
        for (int i = 0; i < w; i++){
            cells.add(i,new ArrayList<>());
            for (int j = 0; j < h; j++){
                cells.get(i).add(j,false);
            }
        }
    }

    /**
     * adds a cell to the canvas.
     *
     * @param x position to the cell
     * @param y position to the cell
     */
    public void addCell(int x, int y) {
        cells.get(x).set(y, true);
    }

    /**
     * removes a cell that has previously been added to the canvas.
     *
     * @param x position to the cell
     * @param y position to the cell
     */
    public void removeCell(int x, int y){
        cells.get(x).set(y,false);
    }

    /**
     * checks if the cell is dead or alive on the given position
     *
     * @param x coordination x
     * @param y coordination y
     * @return returns if a cell is dead or alive.
     */
    public boolean isAlive(int x, int y){
        try {
            return cells.get(x).get(y);
        }catch (IndexOutOfBoundsException oob){
            return false;
        }
    }

    /**
     * uses the boolean isAlive
     *
     * @param isAlive checks boolean value to is alive
     * @return returns 1 if the cell is true and 0 if the cell is false.
     */
    public int isAlive(boolean isAlive){
        if (isAlive)
            return 1;
        else
            return 0;
    }

    /**
     * performes a neighbours test, and checks every cell
     * that is around for a alive cell.
     *
     * @param x coordination x
     * @param y coordination y
     * @return the count of cells that is alive.
     */
    public int getNeighbours(int x, int y){
        int count = 0;

        count += isAlive(isAlive(x-1,y-1));
        count += isAlive(isAlive(x, y - 1));
        count += isAlive(isAlive(x + 1, y - 1));

        count += isAlive(isAlive(x - 1, y));
        count += isAlive(isAlive(x + 1, y));

        count += isAlive(isAlive(x - 1, y + 1));
        count += isAlive(isAlive(x, y + 1));
        count += isAlive(isAlive(x + 1, y + 1));

        return count;
    }

    /**
     * sets a state after the neighbor check and adds or removes
     * cells from canvas.
     */
    public void setState(boolean state, int x, int y){
        if (!isAlive(x,y) && state)
            addCell(x,y);
        if (isAlive(x,y) && !state)
            removeCell(x,y);
    }

    public int getHeight(){
        return cells.get(0).size();
    }

    public int getWidth(){
        return cells.size();
    }

    /**
     * Updates the array and draws the updates on the canvas.
     *
     * @param x       position to the cell
     * @param y       position to the cell
     * @param nextGen multipurpose method, saves things on this method, runs isalive, and save the results of nexgen.
     * @param alive   rules
     * @param dead    rules
     */
    public void nextGen(int x, int y, CellGrid nextGen, int[] alive, int[] dead){
        if (isAlive(x,y) && IntStream.of(alive).anyMatch(i -> i == getNeighbours(x,y))) {
            nextGen.addCell(x,y);
        }
        else if (!isAlive(x,y) && IntStream.of(dead).anyMatch(i -> i == getNeighbours(x,y))){
            nextGen.addCell(x,y);
        }
    }

    /**
     * makes changes to the 2D Arraylist and changes it to the right size
     * adds rows and colums.
     *
     * @param w width
     * @param h height
     */
    public void editSize(int w, int h){
        int x = cells.size();
        int y = cells.get(0).size();
        for (int i = 0; i < w; i++){
            if (i >= x)
                cells.add(new ArrayList<>());
            for (int j = 0; j < h; j++){
                if (i < x){
                    if (j >= y)
                        cells.get(i).add(false);
                }else
                    cells.get(i).add(false);

            }
        }
    }

}
