package card_types;

import card_types.Rarity;

public class Cards {

    private String name;
    private String expansion;
    private Rarity type;
    private String base_id;

    public Cards(String name, String expansion, Rarity type, String base_id){
        this.name = name;
        this.expansion = expansion;
        this.type = type;
        this.base_id = base_id;
    }

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
