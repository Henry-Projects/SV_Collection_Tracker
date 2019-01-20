package available_cards.basic;

import card_types.Rarity;

public enum BasicBronze {

    b1("Bronze1"),
    b2("Bronze2"),
    b3("Bronze3");

    private String name;

    private BasicBronze(String s){
        name = s;
    }

    public String toString() {
        return this.name;
    }

    public String getRarity(){
        return String.valueOf(Rarity.NORMAL_BRONZE);
    }

    public int getLiquefy_values(){
        return Rarity.NORMAL_BRONZE.getLiquefy_value();
    }

    public int getCreate_values(){
        return Rarity.NORMAL_BRONZE.getCreate_value();
    }
}