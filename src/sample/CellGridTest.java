package sample;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Prabdeep on 18.04.2016.
 */
public class CellGridTest {
    CellGrid cellGrid;

    @Before
    public void setUp() {
        cellGrid = new CellGrid(4, 4);
        cellGrid.getCell(1, 1).setState(true);
        cellGrid.getCell(2, 2).setState(true);
        cellGrid.getCell(3, 3).setState(true);

    }

    @Test
    public void testGetWidth() {
        int expectedWidth = 4;
        cellGrid.getWidth();
        assertEquals(expectedWidth, cellGrid.getWidth());
    }

    @Test
    public void testGetHeight() {
        int expectedHeight = 3;
        cellGrid.getHeight();
        assertEquals(expectedHeight, cellGrid.getHeight());
    }

    @Test
    public void testReset() {
        CellGrid expectedReset = new CellGrid(4, 4);
        cellGrid.reset();

        for (int i = 0; i < cellGrid.getWidth(); i++) {
            for (int j = 0; j < cellGrid.getHeight(); j++) {
                assertEquals(expectedReset.getCell(i, j).isAlive(), cellGrid.getCell(i, j).isAlive());
            }
        }
    }
}