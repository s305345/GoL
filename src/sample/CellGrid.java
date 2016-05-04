package sample;

import java.util.ArrayList;
import java.util.stream.IntStream;

public class CellGrid {
    private ArrayList<ArrayList<Boolean>> cells;

    public CellGrid(int w, int h) {
        cells = new ArrayList<>();
        for (int i = 0; i < w; i++){
            cells.add(i,new ArrayList<>());
            for (int j = 0; j < h; j++){
                cells.get(i).add(j,false);
            }
        }
    }

    public void reset(int w, int h) {
        cells = new ArrayList<>();
        for (int i = 0; i < w; i++){
            cells.add(i,new ArrayList<>());
            for (int j = 0; j < h; j++){
                cells.get(i).add(j,false);
            }
        }
    }

    public void addCell(int x, int y) {
        cells.get(x).set(y, true);
    }

    public void removeCell(int x, int y){
        cells.get(x).set(y,false);
    }

    public boolean isAlive(int x, int y){
        try {
            return cells.get(x).get(y);
        }catch (IndexOutOfBoundsException oob){
            return false;
        }
    }

    public int isAlive(boolean isAlive){
        if (isAlive)
            return 1;
        else
            return 0;
    }

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

    public void setState(boolean state, int x, int y){
        if (!isAlive(x,y) && state)
            addCell(x,y);
        if (isAlive(x,y) && !state)
            removeCell(x,y);
    }

    public void nextGen(int x, int y, CellGrid nextGen, int[] alive, int[] dead){
        if (isAlive(x,y) && IntStream.of(alive).anyMatch(i -> i == getNeighbours(x,y))) {
            nextGen.addCell(x,y);
        }
        else if (!isAlive(x,y) && IntStream.of(dead).anyMatch(i -> i == getNeighbours(x,y))){
            nextGen.addCell(x,y);
        }
    }

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
