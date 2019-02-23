package expansions_algorithms;

import cards.*;
import card_types.*;
import java.util.List;

public class Cards_Calculator {

    public static double getExpected_Vials(List<Owned_Cards> owned_cards, String expansion, Rarity rarity){

        double expected_vials = 0;

        for(Owned_Cards card:owned_cards){
            if(card.expansion.equals(expansion) & card.getRarity().equals(rarity.name())){

                if(card.is_Completed()) {
                    expected_vials =+ Cards_Probability.getExpected_Draws(rarity) * Cards_Probability.normal_draw * rarity.getLiquefy_value() +
                            Cards_Probability.getExpected_Draws(rarity) * Cards_Probability.animated_draw * rarity.getLiquefyAnimated_value();
                }else{
                    expected_vials =+ Cards_Probability.getExpected_Draws(rarity) * rarity.getCreate_value();
                }
            }
        }
        return expected_vials;
    }



}
