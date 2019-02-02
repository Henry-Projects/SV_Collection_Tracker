package card_parser;

import card_types.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Parser {

    static public List<Cards> parse_text_file() {

        List<Cards> parsed_cards = new ArrayList<>();
        String original_parsed_string="";

        try {
            original_parsed_string = new String(Files.readAllBytes(Paths.get(Parser.class.getResource("/ShadowOut.txt").toURI())), "UTF-8");
        } catch (Exception e) {
            System.out.println("Check text file or path for parsing");
        }

        List<String> parsed_rows = new ArrayList<>(Arrays.asList(original_parsed_string.split("\\r?\\n")));

        try {
            for (String card : parsed_rows) {

                if (card.substring(card.length() - 1).equals("1")) {
                    parsed_cards.add(new Cards(card.substring(0, card.indexOf("|")),
                            card.substring(card.indexOf("|") + 1, card.indexOf("|", card.indexOf("|") + 1)),
                            Rarity.NORMAL_BRONZE));
                }
            }
        }catch(Exception e){
            System.out.println("Check parsing algorithm");
        }
        return parsed_cards;
    }
}