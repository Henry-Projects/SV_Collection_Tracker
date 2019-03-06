package expansions_algorithms;

import cards.*;
import card_types.*;
import parser.Available_Card_Parser;

import java.math.BigDecimal;
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


    public static BigDecimal getExpected_Vials_by_Rarity(List<Owned_Cards> owned_cards, String expansion, Rarity rarity){

        BigDecimal expected_vial = BigDecimal.ZERO;
        BigDecimal card_count_by_rarity = BigDecimal.ZERO;


        for(Owned_Cards card:owned_cards){
            if(card.getExpansion().equals(expansion) & card.getRarity() == rarity){
                card_count_by_rarity = card_count_by_rarity.add(BigDecimal.ONE);
            }
        }

        for(Owned_Cards card:owned_cards){
            if(card.getExpansion().equals(expansion) & card.getRarity() == rarity){
                expected_vial = expected_vial.add(card.getVials_expected(card_count_by_rarity));
            }
        }

        return expected_vial;
    }

    public static BigDecimal getExpected_Vials_Total(List<Owned_Cards> owned_cards, String expansion){

        BigDecimal expected_vials = BigDecimal.ZERO;

        for(Rarity rarity:Rarity.values()){
            expected_vials = expected_vials.add(Cards_List_Methods.getExpected_Vials_by_Rarity(owned_cards,expansion,rarity));
        }


        return expected_vials;
    }

    public static int getRequired_Vials_by_Rarity(List<Owned_Cards> owned_cards, String expansion, Rarity rarity){

        int required_vials = 0;

            for (Owned_Cards card : owned_cards) {
                if (card.expansion.equals(expansion) & card.getRarity() == rarity) {
                            required_vials += card.getVials_required();

                }
            }
        return required_vials;
    }

    public static int getRequired_Vials_Total(List<Owned_Cards> owned_cards, String expansion){

        int required_vials = 0;

        for (Owned_Cards card : owned_cards) {
            if (card.expansion.equals(expansion)) {
                required_vials += card.getVials_required();

            }
        }
        return required_vials;
    }


    public static int getExtras_LiquefyAnimated_Vials_Total(List<Owned_Cards> owned_cards, String expansion){

        int extra_vials = 0;

        for (Owned_Cards card : owned_cards) {
            if (card.expansion.equals(expansion)) {
                extra_vials += card.getExtras_LiquefyAnimated_value();

            }
        }
        return extra_vials;
    }


    public static int getExtras_KeepAnimated_Vials_Total(List<Owned_Cards> owned_cards, String expansion){

        int extra_vials = 0;

        for (Owned_Cards card : owned_cards) {
            if (card.expansion.equals(expansion)) {
                extra_vials += card.getExtras_KeepAnimated_value();

            }
        }
        return extra_vials;
    }
}
