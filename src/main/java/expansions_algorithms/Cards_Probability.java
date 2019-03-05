package expansions_algorithms;

import card_types.Rarity;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class Cards_Probability {

    public final static double normal_draw = 0.92;
    public final static double animated_draw = 0.08;

    public static BigDecimal getDraw_probability(int draw_index, Rarity rarity){

        BigDecimal draw_probability = new BigDecimal(0.0, MathContext.DECIMAL128);

        switch(rarity){

            case BRONZE:
                if(draw_index <= 7){
                    draw_probability = new BigDecimal(0.675, MathContext.DECIMAL128);
                }else{
                    draw_probability = new BigDecimal(0.0, MathContext.DECIMAL128);
                }
                break;

            case SILVER:
                if(draw_index <= 7){
                    draw_probability = new BigDecimal(0.25, MathContext.DECIMAL128);
                }else{
                    draw_probability = new BigDecimal(0.925, MathContext.DECIMAL128);
                }
                break;

            case GOLD:
                if(draw_index <= 7){
                    draw_probability = new BigDecimal(0.06, MathContext.DECIMAL128);
                }else{
                    draw_probability = new BigDecimal(0.06, MathContext.DECIMAL128);
                }
                break;

            case LEGENDARY:
                if(draw_index <= 7){
                    draw_probability = new BigDecimal(0.015, MathContext.DECIMAL128);
                }else{
                    draw_probability = new BigDecimal(0.015, MathContext.DECIMAL128);
                }
                break;
        }

        return draw_probability;
    }

    public static BigDecimal nCr(final int n, final int k){

        BigDecimal result = new BigDecimal(1.0, MathContext.DECIMAL128);

        for( int i = 0; i < k; i++){

            result = result.multiply((BigDecimal.valueOf(n).subtract(BigDecimal.valueOf(i)))).divide(BigDecimal.valueOf(i).add(BigDecimal.valueOf(1.0)), MathContext.DECIMAL128);

            //result = result.multiply((BigDecimal.valueOf(n-i)).divide(BigDecimal.valueOf((i+1)), RoundingMode.HALF_UP));

        }

        return result;
    }

    public static double getExpected_Draws(Rarity rarity) {

        double expected_draw = 0.0;

        switch (rarity) {

            case BRONZE:
                final double bronze_draw = .675;
                final double bronze_last_draw = .0;

                expected_draw = bronze_draw * 7 + bronze_last_draw * 1;
                break;

            case SILVER:
                final double silver_draw = .25;
                final double silver_last_draw = .925;

                expected_draw = silver_draw * 7 + silver_last_draw * 1;
                break;

            case GOLD:
                final double gold_draw = .06;
                final double gold_last_draw = .06;

                expected_draw = gold_draw * 7 + gold_last_draw * 1;
                break;

            case LEGENDARY:
                final double legendary_draw = .015;
                final double legendary_last_draw = .015;

                expected_draw = legendary_draw * 7 + legendary_last_draw * 1;
                break;

        }
        return expected_draw;
    }
}
