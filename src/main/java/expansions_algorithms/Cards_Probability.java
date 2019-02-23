package expansions_algorithms;

import card_types.Rarity;

public class Cards_Probability {

    public final static double normal_draw = .92;
    public final static double animated_draw = .08;

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
