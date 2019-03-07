package expansions_algorithms;

import card_types.Rarity;

import java.math.BigDecimal;
import java.math.MathContext;

public class Cards_Probability {

    public final static double normal_draw = 0.92;
    public final static double animated_draw = 0.08;

    public static BigDecimal getDraw_probability(Rarity rarity){

        BigDecimal draw_probability = BigDecimal.ZERO;

        switch(rarity){

            case BRONZE:

                draw_probability = BigDecimal.valueOf(0.675);
                break;

            case SILVER:

                draw_probability = BigDecimal.valueOf(((7*.25)+(.925))/8);
                break;

            case GOLD:

                draw_probability = BigDecimal.valueOf(0.06);
                break;

            case LEGENDARY:

                draw_probability = BigDecimal.valueOf(0.015);
                break;
        }

        return draw_probability;
    }

    public static BigDecimal nCr(final int n, final int k){

        BigDecimal result = BigDecimal.ONE;

        for( int i = 0; i < k; i++){

            result = result.multiply((BigDecimal.valueOf(n).subtract(BigDecimal.valueOf(i)))).divide(BigDecimal.valueOf(i).add(BigDecimal.valueOf(1.0)), MathContext.DECIMAL64);

        }

        return result;
    }
}
