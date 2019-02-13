package parser;

import card_types.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Card_Parser {

    static public List<Cards> parse_shadowout_text_file() {

        List<Cards> parsed_cards = new ArrayList<>();
        String original_parsed_string="";

        try {
            original_parsed_string = new String(Files.readAllBytes(Paths.get(Card_Parser.class.getResource("/ShadowOut.txt").toURI())), "UTF-8");
        } catch (Exception e) {
            System.out.println("Check text file or path for card parsing");
        }

        List<String> parsed_rows = new ArrayList<>(Arrays.asList(original_parsed_string.split("\\r?\\n")));

        List<String> expansion_id = Expansion_Parser.get_expansion_lists("id");
        List<String> expansion_name = Expansion_Parser.get_expansion_lists("name");

        try {
            for (String card : parsed_rows) {

                if ( expansion_id.contains(card.substring(card.indexOf("|") + 1, card.indexOf("|", card.indexOf("|") + 1)))) {
                    switch (card.substring(card.length() - 1)) {
                        case "1":
                            parsed_cards.add(new Cards(card.substring(0, card.indexOf("|")),
                                    expansion_name.get(expansion_id.indexOf(card.substring(card.indexOf("|") + 1, card.indexOf("|", card.indexOf("|") + 1)))),
                                    Rarity.NORMAL_BRONZE));
                            break;
                        case "2":
                            parsed_cards.add(new Cards(card.substring(0, card.indexOf("|")),
                                    expansion_name.get(expansion_id.indexOf(card.substring(card.indexOf("|") + 1, card.indexOf("|", card.indexOf("|") + 1)))),
                                    Rarity.NORMAL_SILVER));
                            break;
                        case "3":
                            parsed_cards.add(new Cards(card.substring(0, card.indexOf("|")),
                                    expansion_name.get(expansion_id.indexOf(card.substring(card.indexOf("|") + 1, card.indexOf("|", card.indexOf("|") + 1)))),
                                    Rarity.NORMAL_GOLD));
                            break;
                        case "4":
                            parsed_cards.add(new Cards(card.substring(0, card.indexOf("|")),
                                    expansion_name.get(expansion_id.indexOf(card.substring(card.indexOf("|") + 1, card.indexOf("|", card.indexOf("|") + 1)))),
                                    Rarity.NORMAL_LEGENDARY));
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