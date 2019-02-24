package expansions_algorithms;

import cards.*;
import card_types.*;
import java.util.List;

public class Cards_List_Algorithms {

    public static double getExpected_Vials(List<Owned_Cards> owned_cards, String expansion, Rarity rarity){

        double completed_count = 0.0;
        double incomplete_count = 0.0;

        for(Owned_Cards card:owned_cards){
            if(card.expansion.equals(expansion) & card.getRarity_String().equals(rarity.name())){
                if(card.is_Completed()) {
                    completed_count++;
                }else{
                    incomplete_count++;
                }
            }
        }

        double completed_ratio = completed_count/(completed_count + incomplete_count);
        double incomplete_ratio = incomplete_count/(completed_count + incomplete_count);
        double expected_normal_liquefy_value = Cards_Probability.getExpected_Draws(rarity) * Cards_Probability.normal_draw * rarity.getLiquefyNormal_value();
        double expected_animated_liquefy_value = Cards_Probability.getExpected_Draws(rarity) * Cards_Probability.animated_draw * rarity.getLiquefyAnimated_value();
        double expected_normal_create_value = Cards_Probability.getExpected_Draws(rarity) * rarity.getCreate_value();

        return ((expected_normal_liquefy_value + expected_animated_liquefy_value) * completed_ratio) +
                (expected_normal_create_value * incomplete_ratio);
    }

    public static int getRequired_vials(List<Owned_Cards> owned_cards, String expansion, Rarity rarity){

        int required_vials = 0;

            for (Owned_Cards card : owned_cards) {
                if (card.expansion.equals(expansion) & card.getRarity_String().equals(rarity.name())) {
                            required_vials += card.getVials_required();

                }
            }
        return required_vials;
    }

    public static int getExtras_LiquefyAnimated_vials(List<Owned_Cards> owned_cards, String expansion, Rarity rarity){

        int extra_vials = 0;

        for (Owned_Cards card : owned_cards) {
            if (card.expansion.equals(expansion) & card.getRarity_String().equals(rarity.name())) {
                extra_vials += card.getExtras_LiquefyAnimated_value();

            }
        }
        return extra_vials;
    }

    public static int getExtras_KeepAnimated_vials(List<Owned_Cards> owned_cards, String expansion, Rarity rarity){

        int extra_vials = 0;

        for (Owned_Cards card : owned_cards) {
            if (card.expansion.equals(expansion) & card.getRarity_String().equals(rarity.name())) {
                extra_vials += card.getExtras_KeepAnimated_value();

            }
        }
        return extra_vials;
    }

}
