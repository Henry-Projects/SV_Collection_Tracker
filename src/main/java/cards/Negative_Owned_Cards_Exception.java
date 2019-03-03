package cards;

public class Negative_Owned_Cards_Exception extends Exception {

    private String card_name;

    public Negative_Owned_Cards_Exception(String card_name){
        this.card_name = card_name;
    }

    public String toString() {return card_name;}
}
