package parser;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Expansion_Parser {

    static public List<String> get_expansion_lists(String list_to_return){

        String original_parsed_string = "";

        try {
            original_parsed_string = new String(Files.readAllBytes(Paths.get(Expansion_Parser.class.getResource("/Expansions.txt").toURI())), "UTF-8");
        } catch (Exception e) {
            System.out.println("Check text file or path for expansion parsing");
        }

        List<String> expansion_id_list = new ArrayList<>();
        List<String> expansion_name_list = new ArrayList<>();

        List<String> parsed_rows = new ArrayList<>(Arrays.asList(original_parsed_string.split("\\r?\\n")));

        try {
            for (String expansion_rows : parsed_rows) {

                    expansion_id_list.add(expansion_rows.substring(0, expansion_rows.indexOf("|")));
                    expansion_name_list.add(expansion_rows.substring(expansion_rows.indexOf("|") + 1));

            }
        }catch(Exception e){
            System.out.println("Check expansions parsing algorithm");
        }

        List<String> returned_list = new ArrayList<>();

        if(list_to_return.equals("id")) {
            returned_list.addAll(expansion_id_list);
        }

        if(list_to_return.equals("name")) {
            returned_list.addAll(expansion_name_list);
        }

        return returned_list;

    }
}
