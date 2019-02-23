package cards;

import card_types.Rarity;

public class Owned_Cards extends Available_Cards{

    private int normal;
    private int animated;

    public Owned_Cards(String name, String expansion, Rarity type, String base_id){
        super(name, expansion, type, base_id);
        this.normal = 0;
        this.animated = 0;
    }

    public Owned_Cards(String name, String expansion, Rarity type, String base_id, int normal, int animated) {
        super(name, expansion, type, base_id);
        this.normal = normal;
        this.animated = animated;
    }

    public int getNormal() { return this.normal;}

    public int getAnimated() { return this.animated;}

    public boolean is_Completed() {

        boolean completed = false;

        if(this.normal + this.animated >= 3){
        completed = true;
    }

    return completed;

    }

    public int getVial_value() { return this.normal * super.type.getLiquefy_value() + this.animated * super.type.getLiquefyAnimated_value();}

    public int getVials_required() {

        int vials_required;

        if(this.normal + this.animated >= 3){
            vials_required = 0;
        }
        else{
            vials_required = (3 - (this.normal + this.animated)) * super.type.getCreate_value();
        }

        return vials_required;
    }
}
