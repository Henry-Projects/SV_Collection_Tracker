package parser;

import cards.Available_Cards;
import cards.Negative_Owned_Cards_Exception;
import cards.Owned_Cards;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Owned_Cards_Parser {

    //get the imported text version of owned cards from a list of owned cards
    public static StringBuffer getOwned_Cards_Text(List<Owned_Cards> owned_cards){

        StringBuffer cards_and_count = new StringBuffer("");

        for(Owned_Cards card:owned_cards){
            cards_and_count.append(card.getName() + "|" + card.getNormal() + "|" + card.getAnimated() + "\n");
        }

        return cards_and_count;
    }

    //get a merged list of owned cards from the imported text and the servlet list
    public static List<Owned_Cards> importOwned_Cards_List (String owned_cards_text, List<Owned_Cards> servlet_list)throws Parsed_Row_Exception,Negative_Owned_Cards_Exception{

        List<Owned_Cards> new_owned_cards = servlet_list;
        //List<Owned_Cards> new_owned_cards = makeList_of_Owned_Cards();

        List<String> parsed_rows = new ArrayList<>(Arrays.asList(owned_cards_text.split("\\r?\\n")));

        int exception_row_index = 0;
        String exception_parsed_row = "";

        try {
            for(String card_text_row:parsed_rows){

                exception_row_index += 1;
                exception_parsed_row = card_text_row;
                int normal_count_beg_index = card_text_row.indexOf("|");
                int animated_count_beg_index = card_text_row.indexOf("|", normal_count_beg_index + 1);


                //for(int i = 0; i < new_owned_cards.size(); i++){
                for(int i = 0; i < new_owned_cards.size(); i++){

                    if (card_text_row.substring(0, normal_count_beg_index).equals(new_owned_cards.get(i).getName())) {

                        new_owned_cards.set(i, new Owned_Cards(new_owned_cards.get(i).getName(), new_owned_cards.get(i).getExpansion(),
                                new_owned_cards.get(i).getRarity(), new_owned_cards.get(i).getBase_id(),
                                Integer.valueOf(card_text_row.substring(normal_count_beg_index + 1, animated_count_beg_index).trim()),
                                Integer.valueOf(card_text_row.substring(animated_count_beg_index + 1).trim())));
                    }
                }


            }
        }catch(NumberFormatException | Negative_Owned_Cards_Exception e){
            throw new Parsed_Row_Exception(exception_row_index, parsed_rows.get(exception_row_index - 1));
        }catch(Exception e){
            throw new Parsed_Row_Exception(exception_row_index, exception_parsed_row);
        }
        return new_owned_cards;
    }

    //make a new list of owned cards from a list of available cards
    public static List<Owned_Cards> makeList_of_Owned_Cards(){

        List<Available_Cards> available_cards = Available_Card_Parser.getList_of_Available_Cards();

        //remove those cards with duplicate base_id (leader cards, reprinted cards)
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
}
