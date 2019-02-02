package card_types;

import card_types.Rarity;

public class Cards {

    private String name;
    private String expansion;
    private Rarity type;

    public Cards(String name, String expansion, Rarity type){
        this.name = name;
        this.expansion = expansion;
        this.type = type;
    }

    public String getName(){
        return this.name;
    }

    public String getExpansion(){
        return this.expansion;
    }

    public int getLiquefy_value(){
        return this.type.getLiquefy_value();
    }

    public int getCreate_value(){
        return this.type.getCreate_value();
    }

}
