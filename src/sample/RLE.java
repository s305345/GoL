package sample;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Lise Estelle on 11.04.2016.
 */
public class RLE {
    private String rle;
    private CellGrid cells;
    private int column;
    private int row;

    public RLE(String rle, CellGrid cells,int columns, int rows) {
        this.rle = rle;
        this.cells = cells;
        this.column = columns;
        this.row = rows;
    }

    public CellGrid getCells() {
        return cells;
    }

    public int getWidth() {
        StringBuffer result = new StringBuffer(this.rle);
        Pattern p = Pattern.compile("x = (\\d+)");
        Matcher m = p.matcher(result);
        m.find();
        return Integer.parseInt(m.group(1));
    }

    public int getHeight() {
        StringBuffer result = new StringBuffer(this.rle);
        Pattern p = Pattern.compile("y = (\\d+)");
        Matcher m = p.matcher(result);
        m.find();
        return Integer.parseInt(m.group(1));
    }

    public String getCode() {
        String result = this.rle.replaceAll("^#(.|\\s)+(x = \\d+, y = \\d+(, rule = \\w+/\\w+)?)\\s?", "");
        return result.replaceAll("\\s?", "");
    }

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
