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

    public BigDecimal getVials_expected(BigDecimal total_count_by_this_rarity){

        BigDecimal vials_expected = new BigDecimal(0.0, MathContext.DECIMAL128);

        BigDecimal liquefy_expected = BigDecimal.valueOf((Cards_Probability.normal_draw * super.type.getLiquefyNormal_value()) + (Cards_Probability.animated_draw * super.type.getLiquefyAnimated_value()));
        BigDecimal create_expected = BigDecimal.valueOf(super.type.getCreate_value());


        BigDecimal owned_quantity = new BigDecimal(Math.min(3, this.normal + this.animated));

        for(int i = 1; i <= 8; i++) {

            BigDecimal combination = Cards_Probability.nCr(8,i);
            BigDecimal draw_true = (Cards_Probability.getDraw_probability(i, super.type).divide(total_count_by_this_rarity, MathContext.DECIMAL128)).pow(i);
            BigDecimal draw_false = (BigDecimal.valueOf(1.0)
                    .subtract(Cards_Probability.getDraw_probability(i, super.type).divide(total_count_by_this_rarity, MathContext.DECIMAL128))).pow(8-i);
            BigDecimal expected_create = ((BigDecimal.valueOf(3.0).subtract(owned_quantity)).min(BigDecimal.valueOf(i).min(BigDecimal.valueOf(3.0))))
                    .multiply(create_expected);
            BigDecimal expected_liquefy = (BigDecimal.valueOf(1.0).subtract((BigDecimal.valueOf(3.0).subtract(owned_quantity)).min(BigDecimal.valueOf(i).min(BigDecimal.valueOf(3.0)))))
                    .multiply(liquefy_expected);

            vials_expected = vials_expected.add(combination.multiply(draw_true).multiply(draw_false).multiply(expected_create.add(expected_liquefy)));

            if(super.type == Rarity.GOLD){
                System.out.println("combination: " + combination + " draw_true: " + draw_true + " draw_false: " + draw_false + "expected create: " + expected_create + "expected liquefy" + expected_liquefy);

            }

            /*vials_expected = vials_expected.add(Cards_Probability.nCr(8, i)
                    .multiply((Cards_Probability.getDraw_probability(i, super.type).divide(total_count_by_this_rarity, RoundingMode.HALF_UP)).pow(i))
                    .multiply((BigDecimal.valueOf(1.0).subtract(Cards_Probability.getDraw_probability(i, super.type).divide(total_count_by_this_rarity, RoundingMode.HALF_UP))).pow(8-i))
                    .multiply((BigDecimal.valueOf(3.0).subtract(owned_quantity)).min(BigDecimal.valueOf(i)).min(BigDecimal.valueOf(3.0))
                    .multiply(create_expected)
                            .add(((BigDecimal.valueOf(i)
                            .subtract(BigDecimal.valueOf(3.0).subtract(owned_quantity).min(BigDecimal.valueOf(i)).min(BigDecimal.valueOf(3)))).multiply(liquefy_expected)))));
        */
        }

            return vials_expected;
    }
}
