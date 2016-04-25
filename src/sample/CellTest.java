package sample;

import org.junit.*;

import static org.junit.Assert.*;

/**
 * Created by Prabdeep on 18.04.2016.
 */
public class CellTest {
    Cell cell;

    @Before
    public void setUp() {
        cell = new Cell(false);
    }

    @Test
    public void testIsAlive() {
        boolean expectedIsAlive = false;
        cell.isAlive();
        assertEquals(expectedIsAlive, cell.isAlive());
    }

    @Test
    public void testSetState() {
        boolean expectedSetState = true;
        cell.setState(true);
        assertEquals(expectedSetState, cell.isAlive());
    }
}