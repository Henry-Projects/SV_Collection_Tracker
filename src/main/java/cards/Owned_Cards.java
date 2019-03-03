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

    public Owned_Cards(String name, String expansion, Rarity type, String base_id, int normal, int animated)throws Negative_Owned_Cards_Exception {
        super(name, expansion, type, base_id);

        if(normal < 0 | animated < 0){
            throw new Negative_Owned_Cards_Exception(name);
        }else{
            this.normal = normal;
            this.animated = animated;
        }
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

    public int getExtras_LiquefyAnimated_value() {

        int extra_vials = 0;

        if(this.normal + this.animated > 3){
            if(this.normal >= 3){
                extra_vials = (this.normal * super.type.getLiquefyNormal_value() + this.animated * super.type.getLiquefyAnimated_value()) -
                        (3 * super.type.getLiquefyNormal_value());
            }else if(this.normal == 2){
                extra_vials = (this.normal * super.type.getLiquefyNormal_value() + this.animated * super.type.getLiquefyAnimated_value()) -
                        (2 * super.type.getLiquefyNormal_value()) - (super.type.getLiquefyAnimated_value());
            }else if (this.normal == 1) {
                extra_vials = (this.normal * super.type.getLiquefyNormal_value() + this.animated * super.type.getLiquefyAnimated_value()) -
                        (super.type.getLiquefyNormal_value()) - (2 * super.type.getLiquefyAnimated_value());
            }else{
                extra_vials = (this.normal * super.type.getLiquefyNormal_value() + this.animated * super.type.getLiquefyAnimated_value()) -
                        (3 * super.type.getLiquefyAnimated_value());
            }
        }

        return extra_vials;
    }

    public int getExtras_KeepAnimated_value(){

        int extra_vials = 0;

        if(this.normal + this.animated > 3){
            if(this.animated >= 3){
                extra_vials = (this.normal * super.type.getLiquefyNormal_value() + this.animated * super.type.getLiquefyAnimated_value()) -
                        (3 * super.type.getLiquefyAnimated_value());
            }else if(this.animated == 2){
                extra_vials = (this.normal * super.type.getLiquefyNormal_value() + this.animated * super.type.getLiquefyAnimated_value()) -
                        (2 * super.type.getLiquefyAnimated_value()) - (super.type.getLiquefyNormal_value());
            }else if (this.animated == 1) {
                extra_vials = (this.normal * super.type.getLiquefyNormal_value() + this.animated * super.type.getLiquefyAnimated_value()) -
                        (super.type.getLiquefyAnimated_value()) - (2 * super.type.getLiquefyNormal_value());
            }else{
                extra_vials = (this.normal * super.type.getLiquefyNormal_value() + this.animated * super.type.getLiquefyAnimated_value()) -
                        (3 * super.type.getLiquefyNormal_value());
            }
        }

        return extra_vials;
    }

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
