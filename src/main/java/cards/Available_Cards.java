package cards;

import card_types.Rarity;

public class Available_Cards {

    public String name;
    public String expansion;
    public Rarity type;
    public String base_id;

    public Available_Cards(String name, String expansion, Rarity type, String base_id){
        this.name = name;
        this.expansion = expansion;
        this.type = type;
        this.base_id = base_id;
    }

    public Available_Cards(){ }

    public String getName(){
        return this.name;
    }

    public String getExpansion(){
        return this.expansion;
    }

    public String getRarity() {return this.type.name(); }

    public String getBase_id() {return this.base_id;}

    public int getLiquefy_value(){
        return this.type.getLiquefy_value();
    }

    public int getCreate_value(){
        return this.type.getCreate_value();
    }

}
