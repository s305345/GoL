package gol;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The RLE class takes in a string of code and decodes it to make a cellGrid.
 * For more information about RLE format visit http://www.conwaylife.com/wiki/Run_Length_Encoded .
 */
public class RLE {
    private String rle;
    private CellGrid cells;
    private int column;
    private int row;

    /**
     * Constructor for the RLE class
     * @param rle  this the string that needs to be decoded.
     * @param cells  This is the CellGrid object that we will have the Pattern on.
     * @param columns  This is the width of the cells inside the Canvas.
     * @param rows  This is the height of the cells inside the Canvas.
     */
    public RLE(String rle, CellGrid cells,int columns, int rows) {
        this.rle = rle;
        this.cells = cells;
        this.column = columns;
        this.row = rows;
    }


    /**
     * This method finds the width of the patterns.
     * @return the width of the given pattern.
     */
    public int getWidth() {
        StringBuffer result = new StringBuffer(this.rle);
        Pattern p = Pattern.compile("x = (\\d+)");
        Matcher m = p.matcher(result);
        m.find();
        return Integer.parseInt(m.group(1));
    }

    /**
     * This method finds the height of the patterns.
     * @return the height of the given pattern.
     */
    public int getHeight() {
        StringBuffer result = new StringBuffer(this.rle);
        Pattern p = Pattern.compile("y = (\\d+)");
        Matcher m = p.matcher(result);
        m.find();
        return Integer.parseInt(m.group(1));
    }

    /**
     * This method uses the string and deletes the comments and information about sizes and rules,
     * to basically only return the code.
     * @return the code
     */
    public String getCode() {
        String result = this.rle.replaceAll("^#(.|\\s)+(x = \\d+, y = \\d+(, rule = \\w+/\\w+)?)\\s?", "");
        return result.replaceAll("\\s?", "");
    }

    /**
     * This method checks the size(height and width) of pattern.
     * It resizes the cellGrid so that the pattern will fit.
     */
    private void sizeNotGood() {
        if (getWidth() < column) {
            column = getWidth();
            this.cells.editSize(column,row);
        }
        if (getHeight() < row) {
            row = getHeight();
            this.cells.editSize(column,row);
        }
    }

    /**
     * This method takes the code and reads it and after that according to the cell condition (dead or alive),
     * makes the necessary update of the cellGrid with the pattern.
     * @return the updated cellGrid.
     */
    public CellGrid codeToCells() {
        sizeNotGood();
        StringBuffer result = new StringBuffer(getCode());
        Pattern p = Pattern.compile("(\\d+|[ob$])");
        Matcher m = p.matcher(result);
        int countY = 0;
        int countX = 0;
        while (m.find()) {
            int number;
            try {
                number = Integer.parseInt(m.group());
                m.find();
            } catch (NumberFormatException exception) {
                number = 1;
            }

            while (number-- != 0) {
                String value = m.group();
                if (value.matches("[b]")) {
                    this.cells.setState(false,countX,countY);
                    countX++;
                } else if (value.matches("[o]")) {
                    this.cells.setState(true,countX,countY);
                    countX++;
                } else if (value.matches("[$]")) {
                    countY++;
                    countX = 0;
                } else {
                    System.out.println("something's wrong");
                }
            }
        }

        return this.cells;
    }
}
