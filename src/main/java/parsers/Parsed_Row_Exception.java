package parsers;

public class Parsed_Row_Exception extends Exception {

    private int row;
    private String parsed_row;

    Parsed_Row_Exception(int row, String parsed_row){
        this.row = row;
        this.parsed_row = parsed_row;
    }

    public String toString() {
        return this.row + ": " + parsed_row;
    }
}
