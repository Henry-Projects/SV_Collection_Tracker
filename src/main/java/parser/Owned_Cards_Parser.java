package parser;

import cards.Owned_Cards;
import expansions_algorithms.Cards_List_Methods;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Owned_Cards_Parser {

    public static StringBuffer getOwned_Cards_Text(List<Owned_Cards> owned_cards){


        StringBuffer cards_and_count = new StringBuffer("");

        for(Owned_Cards card:owned_cards){
            cards_and_count.append(card.getName() + "|" + card.getNormal() + "|" + card.getAnimated() + "\n");
        }

        return cards_and_count;
    }


    public static List<Owned_Cards> importOwned_Cards_List(String owned_cards_text){

        List<Owned_Cards> new_owned_cards = Cards_List_Methods.makeList_of_Owned_Cards();

        List<String> parsed_rows = new ArrayList<>(Arrays.asList(owned_cards_text.split("\\r?\\n")));

        for(String card_text_row:parsed_rows){

            int normal_count_beg_index = card_text_row.indexOf("|");
            int animated_count_beg_index = card_text_row.indexOf("|", normal_count_beg_index + 1);

            System.out.println(normal_count_beg_index);
            System.out.println(animated_count_beg_index);
            System.out.println(card_text_row.substring(normal_count_beg_index + 1,animated_count_beg_index));
            System.out.println(card_text_row.substring(animated_count_beg_index + 1));

            for(int i = 0; i < new_owned_cards.size(); i++){

                if(card_text_row.substring(0,normal_count_beg_index).equals(new_owned_cards.get(i).getName())){

                    new_owned_cards.set(i, new Owned_Cards(new_owned_cards.get(i).getName(),new_owned_cards.get(i).getExpansion(),
                            new_owned_cards.get(i).getRarity(),new_owned_cards.get(i).getBase_id(),
                            Integer.valueOf(card_text_row.substring(normal_count_beg_index + 1,animated_count_beg_index)),
                            Integer.valueOf(card_text_row.substring(animated_count_beg_index + 1))));
                }
            }
        }

        return new_owned_cards;
    }

}
