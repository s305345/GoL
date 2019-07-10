package gol;

/**
 * Created by rahmanshahidi on 06/05/16.
 */

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


public class CellGridTest {
    CellGrid cellGrid;

    @Before
    public void setUp() {
        cellGrid = new CellGrid(4, 4);
        cellGrid.setState(true, 1 ,1);
        cellGrid.setState(true, 2, 2);
        cellGrid.setState(true, 3, 3);

    }

    @Test
    public void testGetWidth() {
        int expectedWidth = 4;
        cellGrid.getWidth();
        assertEquals(expectedWidth, cellGrid.getWidth());
    }

    @Test
    public void testGetHeight() {
        int expectedHeight = 4;
        cellGrid.getHeight();
        assertEquals(expectedHeight, cellGrid.getHeight());
    }

    @Test
    public void testReset() {
        CellGrid expectedReset = new CellGrid(4, 4);
        cellGrid.reset(cellGrid.getWidth(), cellGrid.getHeight());

        for (int i = 0; i < cellGrid.getWidth(); i++) {
            for (int j = 0; j < cellGrid.getHeight(); j++) {
                assertEquals(expectedReset.isAlive(i, j), cellGrid.isAlive(i, j));
            }
        }
    }
}

