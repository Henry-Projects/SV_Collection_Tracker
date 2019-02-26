package expansions_algorithms;

import cards.*;
import card_types.*;
import parser.Available_Card_Parser;

import java.util.ArrayList;
import java.util.List;

public class Cards_List_Methods {

    public static List<Owned_Cards> makeList_of_Owned_Cards(){

        List<Available_Cards> available_cards = Available_Card_Parser.parse_shadowout_text_file();

        for(int card_index = 0; card_index < available_cards.size() - 1; card_index++){
            for(int card_index2 = card_index + 1; card_index2 < available_cards.size(); card_index2++){
                if(available_cards.get(card_index).getBase_id().equals(available_cards.get(card_index2).getBase_id())){
                    available_cards.remove(card_index);
                    card_index =- 1;
                    break;
                }
            }
        }

        List<Owned_Cards> owned_cards = new ArrayList<>();

        for(Available_Cards card:available_cards){
            owned_cards.add(new Owned_Cards(card.name,card.expansion,card.type,card.base_id));
        }

        return owned_cards;
    }


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
