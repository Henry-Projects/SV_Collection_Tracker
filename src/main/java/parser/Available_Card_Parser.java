package parser;

import card_types.*;
import cards.Available_Cards;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Available_Card_Parser {

    static public List<Available_Cards> parse_shadowout_text_file() {

        List<Available_Cards> parsed_cards = new ArrayList<>();
        String original_parsed_string="";

        try {
            original_parsed_string = new String(Files.readAllBytes(Paths.get(Available_Card_Parser.class.getResource("/ShadowOut.txt").toURI())), "UTF-8");
        } catch (Exception e) {
            System.out.println("Check text file or path for card parsing");
        }

        List<String> parsed_rows = new ArrayList<>(Arrays.asList(original_parsed_string.split("\\r?\\n")));

        List<String> expansion_id = Expansion_Parser.get_expansion_lists("id");
        List<String> expansion_name = Expansion_Parser.get_expansion_lists("name");

        try {
            for (String card : parsed_rows) {

            int expansion_index = card.indexOf("|");
            int rarity_index = card.indexOf("|",expansion_index + 1);
            int base_id_index = card.indexOf("|",rarity_index + 1);

                if ( expansion_id.contains(card.substring(expansion_index + 1, rarity_index))) {
                    switch (card.substring(rarity_index + 1, base_id_index)) {
                        case "1":
                            parsed_cards.add(new Available_Cards(card.substring(0, expansion_index),
                                    expansion_name.get(expansion_id.indexOf(card.substring(expansion_index + 1, rarity_index))),
                                    Rarity.BRONZE,
                                    card.substring(base_id_index + 1)));
                            break;
                        case "2":
                            parsed_cards.add(new Available_Cards(card.substring(0, expansion_index),
                                    expansion_name.get(expansion_id.indexOf(card.substring(expansion_index + 1, rarity_index))),
                                    Rarity.SILVER,
                                    card.substring(base_id_index + 1)));
                            break;
                        case "3":
                            parsed_cards.add(new Available_Cards(card.substring(0, expansion_index),
                                    expansion_name.get(expansion_id.indexOf(card.substring(expansion_index + 1, rarity_index))),
                                    Rarity.GOLD,
                                    card.substring(base_id_index + 1)));
                            break;
                        case "4":
                            parsed_cards.add(new Available_Cards(card.substring(0, expansion_index),
                                    expansion_name.get(expansion_id.indexOf(card.substring(expansion_index + 1, rarity_index))),
                                    Rarity.LEGENDARY,
                                    card.substring(base_id_index + 1)));
                            break;
                    }
                }
            }
        }catch(Exception e){
            System.out.println("Check card parsing algorithm");
        }
        return parsed_cards;
    }
}