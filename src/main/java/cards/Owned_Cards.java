package cards;

import card_types.Rarity;
import expansions_algorithms.Cards_Probability;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;


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

    public BigDecimal getVials_expected(BigDecimal total_count_by_this_rarity){

        BigDecimal vials_expected = new BigDecimal(0.0, MathContext.DECIMAL64);

        BigDecimal liquefy_expected = BigDecimal.valueOf((Cards_Probability.normal_draw * super.type.getLiquefyNormal_value()) +
                (Cards_Probability.animated_draw * super.type.getLiquefyAnimated_value()));
        BigDecimal create_expected = BigDecimal.valueOf(super.type.getCreate_value());


        BigDecimal owned_quantity = new BigDecimal(Math.min(3, this.normal + this.animated));

        if(super.type == Rarity.BRONZE){
            for (int i = 1; i <= 7; i++){
                //Combination formula
                BigDecimal combination = Cards_Probability.nCr(7, i);
                //(probability of drawing this card/total count of this card's rarity in this expansion)^i
                BigDecimal draw_true = (Cards_Probability.getDraw_probability(super.type).divide(total_count_by_this_rarity, MathContext.DECIMAL64)).pow(i);
                //(1-probability of drawing this card/total count of this card's rarity in this expansion)^(8-i)
                BigDecimal draw_false = (BigDecimal.valueOf(1.0)
                        .subtract(Cards_Probability.getDraw_probability(super.type).divide(total_count_by_this_rarity, MathContext.DECIMAL64))).pow(7 - i);
                //MIN(MIN(3-Owned,3),i)
                BigDecimal expected_create = ((BigDecimal.valueOf(3.0).subtract(owned_quantity)).min(BigDecimal.valueOf(3.0))).min(BigDecimal.valueOf(i));
                //MIN(8-expected_created,i-expected_created)
                BigDecimal expected_liquefy = ((BigDecimal.valueOf(7.0).subtract(expected_create)).min(BigDecimal.valueOf(i).subtract(expected_create)));

                //vials_expected += combination*draw_true*draw_false*(expected_create*create_expected+expected_liquefy*liquefy_expected)
                vials_expected = vials_expected.add(combination.multiply(draw_true).multiply(draw_false).multiply(((expected_create).multiply(create_expected))
                        .add(expected_liquefy.multiply(liquefy_expected))));

            }
        }else {
            for (int i = 1; i <= 8; i++) {

                //Combination formula
                BigDecimal combination = Cards_Probability.nCr(8, i);
                //(probability of drawing this card/total count of this card's rarity in this expansion)^i
                BigDecimal draw_true = (Cards_Probability.getDraw_probability(super.type).divide(total_count_by_this_rarity, MathContext.DECIMAL64)).pow(i);
                //(1-probability of drawing this card/total count of this card's rarity in this expansion)^(8-i)
                BigDecimal draw_false = (BigDecimal.valueOf(1.0)
                        .subtract(Cards_Probability.getDraw_probability(super.type).divide(total_count_by_this_rarity, MathContext.DECIMAL64))).pow(8 - i);
                //MIN(MIN(3-Owned,3),i)
                BigDecimal expected_create = ((BigDecimal.valueOf(3.0).subtract(owned_quantity)).min(BigDecimal.valueOf(3.0))).min(BigDecimal.valueOf(i));
                //MIN(8-expected_created,i-expected_created)
                BigDecimal expected_liquefy = ((BigDecimal.valueOf(8.0).subtract(expected_create)).min(BigDecimal.valueOf(i).subtract(expected_create)));

                //vials_expected += combination*draw_true*draw_false*(expected_create*create_expected+expected_liquefy*liquefy_expected)
                vials_expected = vials_expected.add(combination.multiply(draw_true).multiply(draw_false).multiply(((expected_create).multiply(create_expected))
                        .add(expected_liquefy.multiply(liquefy_expected))));

            }
        }
        return vials_expected;
    }
}
