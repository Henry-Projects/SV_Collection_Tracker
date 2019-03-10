package parser;

import card_types.*;
import cards.Available_Cards;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Available_Card_Parser {

    static public List<Available_Cards> getList_of_Available_Cards() {

        List<Available_Cards> parsed_cards = new ArrayList<>();

        try{
            URL hp = new URL("https://shadowverse-portal.com/api/v1/cards?format=json&lang=en");

            HttpURLConnection hpCon = (HttpURLConnection) hp.openConnection();

            hpCon.setRequestProperty("Accept-Charset", "utf-8");
            InputStream response = hpCon.getInputStream();

            Scanner scanner = new Scanner(response);
            String sv_string = scanner.useDelimiter("\\A").next();

            List<String> card_name_list = new ArrayList<>();
            List<String> card_set_id_list = new ArrayList<>();
            List<String> rarity_list = new ArrayList<>();
            List<String> base_card_id = new ArrayList<>();

            JsonElement jelement = new JsonParser().parse(sv_string);
            JsonObject jobject = jelement.getAsJsonObject();
            jobject = jobject.getAsJsonObject("data");

            JsonArray jarray = jobject.getAsJsonArray("cards");

            for(JsonElement card_object: jarray){
                card_name_list.add(card_object.getAsJsonObject().get("card_name").getAsString());
                card_set_id_list.add(card_object.getAsJsonObject().get("card_set_id").getAsString());
                rarity_list.add(card_object.getAsJsonObject().get("rarity").getAsString());
                base_card_id.add(card_object.getAsJsonObject().get("base_card_id").getAsString());
            }

            List<String> expansion_id = Expansion_Parser.get_expansion_lists("id");
            List<String> expansion_name = Expansion_Parser.get_expansion_lists("name");

            for(int i = 0; i < card_name_list.size(); i++){
                if ( expansion_id.contains(card_set_id_list.get(i))) {
                    switch (rarity_list.get(i)) {
                        case "1":
                            parsed_cards.add(new Available_Cards(card_name_list.get(i),
                                    expansion_name.get(expansion_id.indexOf(card_set_id_list.get(i))),
                                    Rarity.BRONZE,
                                    base_card_id.get(i)));
                            break;
                        case "2":
                            parsed_cards.add(new Available_Cards(card_name_list.get(i),
                                    expansion_name.get(expansion_id.indexOf(card_set_id_list.get(i))),
                                    Rarity.SILVER,
                                    base_card_id.get(i)));
                            break;
                        case "3":
                            parsed_cards.add(new Available_Cards(card_name_list.get(i),
                                    expansion_name.get(expansion_id.indexOf(card_set_id_list.get(i))),
                                    Rarity.GOLD,
                                    base_card_id.get(i)));
                            break;
                        case "4":
                            parsed_cards.add(new Available_Cards(card_name_list.get(i),
                                    expansion_name.get(expansion_id.indexOf(card_set_id_list.get(i))),
                                    Rarity.LEGENDARY,
                                    base_card_id.get(i)));
                            break;
                    }
                }
            }
        }catch (MalformedURLException e){
            System.out.println("malformed url exception");
        }catch (IOException e){
            System.out.println("IO Exception");
        }

        return parsed_cards;
    }
}