import java.io.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import card_types.Rarity;
import cards.Owned_Cards;
import expansions_algorithms.Cards_List_Methods;
import parser.Expansion_Parser;
import parser.Owned_Cards_Parser;
import parser.Parsed_Row_Exception;

@WebServlet(urlPatterns = { "/Welcome" })
public class Welcome extends HttpServlet {

    public void doGet(HttpServletRequest request,
                       HttpServletResponse response)
            throws IOException {

        try {
            List<Owned_Cards> owned_cards = Cards_List_Methods.makeList_of_Owned_Cards();


            response.setContentType("text/html");
            PrintWriter pw = response.getWriter();

            pw.println("<!DOCTYPE html>");
            pw.println("<html lang=\"en\">");
            pw.println("<head>");
            pw.println("<style>");

            pw.println("td {\n" +
                    "text-align: center;\n" +
                    "vertical-align: middle;\n" +
                    "}");

            pw.println("</style>");
            pw.println("    <meta charset=\"UTF-8\">");
            pw.println("    <title>SV_Collection_Tracker: Welcome</title>");
            pw.println("</head>");
            pw.println("<body>");
            pw.println("<center>");
            pw.println("    <form name=\"Welcome\"");
            pw.println("          method=\"post\"");
            pw.println("          action=\"http://localhost:8080/SV_Collection_Tracker/Welcome\">");
            pw.println("        <table>");
            pw.println("            <tr>");
            pw.println("                <td><B>Paste saved text here:<br>It is recommended<br> to make read only .txt file<br> backups with timestamps.</B></td>");
            pw.println("                <td><textarea name=\"owned_cards_text\" rows=\"10\" cols=\"30\">" + Owned_Cards_Parser.getOwned_Cards_Text(owned_cards) + "</textarea></td>");
            pw.println("            </tr>");
            pw.println("        </table>");
            pw.println("        <input type=\"submit\" value=\"Import\">");
            pw.println("    </form>");
            pw.println("</center>");
            pw.println("</body>");
            pw.println("</html>");
        }catch(Exception e){
            response.setContentType("text/html");
            PrintWriter pw = response.getWriter();

            pw.println("<!DOCTYPE html><html lang=\"en\"><head></head><body>Please review the Expansions.txt or ShadowOut.txt files in the resources folder.</body></html>");
        }
    }



    public void doPost(HttpServletRequest request,
                       HttpServletResponse response)
            throws IOException {

        String owned_cards_text = request.getParameter("owned_cards_text");
        String selected_expansion = request.getParameter("selected_expansion");

        List<String> card_name_parameters = Collections.list(request.getParameterNames());
        card_name_parameters.remove("owned_cards_text");
        card_name_parameters.remove("selected_expansion");
        for(int i = 0; i < card_name_parameters.size(); i++){
            if(request.getParameter(card_name_parameters.get(i)) == null | request.getParameter(card_name_parameters.get(i)).equals("")){
                card_name_parameters.remove(i);
                i -= 1;
            }
        }

        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();


        pw.println("<!DOCTYPE html>");
        pw.println("<html lang=\"en\">");
        pw.println("<head>");
        pw.println("<style>");

        pw.println("td {\n" +
                "text-align: center;\n" +
                "vertical-align: middle;\n" +
                "}");

        pw.println("</style>");
        pw.println("    <meta charset=\"UTF-8\">");
        pw.println("    <title>SV_Collection_Tracker</title>");
        pw.println("</head>");
        pw.println("<body>");


        List<Owned_Cards> imported_owned_cards;

        try {

            imported_owned_cards = Owned_Cards_Parser.importOwned_Cards_List(owned_cards_text);

            String card_name_for_exception_list = "";
            String expansion_getter = "";
            List<String> cards_updated_list = new ArrayList<>();
            List<String> cards_not_updated_list = new ArrayList<>();

            if (selected_expansion == null) {
                for (String card_name_parameter : card_name_parameters) {
                    try {

                        if (card_name_parameter.contains("|Normal_Count")) {

                            card_name_for_exception_list = card_name_parameter;
                            int inputted_card_count = Integer.valueOf(request.getParameter(card_name_parameter));
                            String card_name = card_name_parameter.replace("|Normal_Count", "");
                            cards_updated_list.add(card_name);


                            for (int i = 0; i < imported_owned_cards.size(); i++) {
                                if (imported_owned_cards.get(i).getName().equals(card_name)) {
                                    imported_owned_cards.set(i, new Owned_Cards(card_name, imported_owned_cards.get(i).getExpansion(),
                                            imported_owned_cards.get(i).getRarity(), imported_owned_cards.get(i).getBase_id(),
                                            inputted_card_count + imported_owned_cards.get(i).getNormal(), imported_owned_cards.get(i).getAnimated()));
                                    expansion_getter = imported_owned_cards.get(i).getExpansion();
                                }
                            }

                        } else if (card_name_parameter.contains("|Animated_Count")) {

                            card_name_for_exception_list = card_name_parameter;
                            int inputted_card_count = Integer.valueOf(request.getParameter(card_name_parameter));
                            String card_name = card_name_parameter.replace("|Animated_Count", "");
                            cards_updated_list.add(card_name);

                            for (int i = 0; i < imported_owned_cards.size(); i++) {
                                if (imported_owned_cards.get(i).getName().equals(card_name)) {
                                    imported_owned_cards.set(i, new Owned_Cards(card_name, imported_owned_cards.get(i).getExpansion(),
                                            imported_owned_cards.get(i).getRarity(), imported_owned_cards.get(i).getBase_id(),
                                            imported_owned_cards.get(i).getNormal(), inputted_card_count + imported_owned_cards.get(i).getAnimated()));
                                    expansion_getter = imported_owned_cards.get(i).getExpansion();
                                }
                            }
                        }
                    } catch (NumberFormatException e) {
                        cards_not_updated_list.add(card_name_for_exception_list);
                    }
                }
                if (!card_name_parameters.isEmpty() & cards_not_updated_list.isEmpty()) {
                    pw.println("<B><center>Status:</B> Card count update for " + expansion_getter + " was a success!</center>");
                    pw.print("<B><center>Cards Updated (" + cards_updated_list.size() + ") :</B> <font color=\"green\">");
                    for (int i = 0; i < cards_updated_list.size(); i++) {
                        if (i < cards_updated_list.size() - 1) {
                            pw.print(cards_updated_list.get(i) + ", ");
                        } else {
                            pw.print(cards_updated_list.get(i) + "</font></center>");
                        }
                    }
                } else if (!card_name_parameters.isEmpty() & !cards_not_updated_list.isEmpty()) {
                    pw.print("<B><center>Status:</B> Invalid character inputted for (" + cards_not_updated_list.size() + ") card(s): <font color=\"red\"><B>");
                    for (int i = 0; i < cards_not_updated_list.size(); i++) {
                        if (i < cards_not_updated_list.size() - 1) {
                            pw.print(cards_not_updated_list.get(i) + ", ");
                        } else {
                            pw.println(cards_not_updated_list.get(i) + "</font></B></center>");
                        }
                    }
                    pw.println("<center>Please review <B>" + expansion_getter + "</B> again.</center>");
                    pw.print("<B><center>Cards Updated (" + cards_updated_list.size() + ") :</B> <font color=\"green\">");
                    for (int i = 0; i < cards_updated_list.size(); i++) {
                        if (i < cards_updated_list.size() - 1) {
                            pw.print(cards_updated_list.get(i) + ", ");
                        } else {
                            pw.println(cards_updated_list.get(i) + "</font></center>");
                        }
                    }
                }
            } else if (!card_name_parameters.isEmpty()) {
                pw.println("<center><B>Warning:</B> Input detected for " + selected_expansion + " but not updated! Click on the update button to update.</center>");
                pw.print("<center>The following counts were not imported (" + card_name_parameters.size() + ") : <B><font color=\"red\">");
                for (int i = 0; i < card_name_parameters.size(); i++) {
                    if (i < card_name_parameters.size() - 1) {
                        pw.print(card_name_parameters.get(i) + ":" + request.getParameter(card_name_parameters.get(i)) + ", ");
                    } else {
                        pw.print(card_name_parameters.get(i) + ":" + request.getParameter(card_name_parameters.get(i)) + "</B></font></center>");
                    }
                }
            }


            List<String> expansion_name = Expansion_Parser.get_expansion_lists("name");

            DecimalFormat two_dec = new DecimalFormat("#,###.00");
            two_dec.setMaximumFractionDigits(2);

            DecimalFormat comma_sep = new DecimalFormat("#,###");

            double max_expected_vials = 0.0;
            int required_grand_total_vials = 0;
            int extras_liquefy_animated_grand_total_vials = 0;
            int extras_keep_animated_grand_total_vials = 0;

            for (String expansion : expansion_name) {

                max_expected_vials = Math.max(max_expected_vials, Cards_List_Methods.getExpected_Vials_Total(imported_owned_cards, expansion));
                required_grand_total_vials += Cards_List_Methods.getRequired_Vials_Total(imported_owned_cards, expansion);
                extras_liquefy_animated_grand_total_vials += Cards_List_Methods.getExtras_LiquefyAnimated_Vials_Total(imported_owned_cards, expansion);
                extras_keep_animated_grand_total_vials += Cards_List_Methods.getExtras_KeepAnimated_Vials_Total(imported_owned_cards, expansion);

            }


            pw.println("<center>");
            pw.println("    <br><br><form name=\"Welcome\"");
            pw.println("          method=\"post\"");
            pw.println("          action=\"http://localhost:8080/SV_Collection_Tracker/Welcome\">");
            pw.println("        <table>");
            pw.println("            <tr>");
            pw.println("                <td><B>Copy text here:<br>It is recommended<br> to make .txt file<br> backups with timestamps.</B></td>");
            pw.println("                <td><textarea readonly name=\"owned_cards_text\" rows=\"10\" cols=\"30\">" + Owned_Cards_Parser.getOwned_Cards_Text(imported_owned_cards) + "</textarea></td>");
            pw.println("                <td><table><tr><td><B>Total Required Vials</B></td></tr><tr><td>" + comma_sep.format(required_grand_total_vials) + "</td></tr></table></td>");
            pw.println("                <td><table><tr><td><B>Extras Vials (Liquefy Animated)</B></td></tr><tr><td>" + comma_sep.format(extras_liquefy_animated_grand_total_vials) + "</td></tr></table></td>");
            pw.println("                <td><table><tr><td><B>Extras Vials (Keep Animated)</B></td></tr><tr><td>" + comma_sep.format(extras_keep_animated_grand_total_vials) + "</td></tr></table></td>");
            pw.println("            </tr>");
            pw.println("        </table>");
            pw.println("        <input type=\"submit\" value=\"Update\">");
            pw.println("</center>");


            pw.println("<center>");
            pw.println("    <table>");
            pw.println("        <tr>");
            pw.println("            <td>");
            pw.println("                <table>");
            pw.println("                    <tr><td style=\"text-align: left;\"><b>Expansions:</b></td></tr>");
            pw.println("                    <tr><td style=\"text-align: left;\">Expected Vials Bronze: </td></tr>");
            pw.println("                    <tr><td style=\"text-align: left;\">Expected Vials Silver: </td></tr>");
            pw.println("                    <tr><td style=\"text-align: left;\">Expected Vials Gold: </td></tr>");
            pw.println("                    <tr><td style=\"text-align: left;\">Expected Vials Legendary: </td></tr>");
            pw.println("                    <tr><td style=\"text-align: left;\"><b>Expected Vials Total: </b></td></tr>");
            pw.println("                    <tr><td style=\"text-align: left;\">Required Vials Bronze: </td></tr>");
            pw.println("                    <tr><td style=\"text-align: left;\">Required Vials Silver: </td></tr>");
            pw.println("                    <tr><td style=\"text-align: left;\">Required Vials Gold: </td></tr>");
            pw.println("                    <tr><td style=\"text-align: left;\">Required Vials Legendary: </td></tr>");
            pw.println("                    <tr><td style=\"text-align: left;\"><b>Required Vials Total: </b></td></tr>");
            pw.println("               </table>");
            pw.println("            </td>");


            for (String expansion : expansion_name) {

                pw.println("     <td>");
                pw.println("        <table>");

                if (Cards_List_Methods.getExpected_Vials_Total(imported_owned_cards, expansion) == max_expected_vials) {
                    pw.println("            <tr><td style=\"color:blue\"><b><input type=submit name=\"selected_expansion\" value=\"" + expansion + "\" style=\"color:white;background:green\"></b></td></tr>");
                } else {
                    pw.println("            <tr><td><b><input type=submit name=\"selected_expansion\" value=\"" + expansion + "\"></b></td></tr>");
                }

                for (Rarity rarity : Rarity.values()) {
                    pw.println("            <tr><td>" + two_dec.format(Cards_List_Methods.getExpected_Vials_by_Rarity(imported_owned_cards, expansion, rarity)) + "</td></tr>");
                }

                if (Cards_List_Methods.getExpected_Vials_Total(imported_owned_cards, expansion) == max_expected_vials) {
                    pw.println("            <tr><td style=\"color:blue\"><b>" + two_dec.format(Cards_List_Methods.getExpected_Vials_Total(imported_owned_cards, expansion)) + "</b></td></tr>");
                } else {
                    pw.println("            <tr><td><b>" + two_dec.format(Cards_List_Methods.getExpected_Vials_Total(imported_owned_cards, expansion)) + "</b></td></tr>");
                }

                int required_cards_total = 0;
                for (Rarity rarity : Rarity.values()) {
                    int required_cards = Cards_List_Methods.getRequired_Vials_by_Rarity(imported_owned_cards, expansion, rarity) / rarity.getCreate_value();
                    required_cards_total += required_cards;

                    pw.println("            <tr><td>" + comma_sep.format(Cards_List_Methods.getRequired_Vials_by_Rarity(imported_owned_cards, expansion, rarity)) + " (" + required_cards + ")" + "</td></tr>");
                }

                pw.println("            <tr><td><b>" + comma_sep.format(Cards_List_Methods.getRequired_Vials_Total(imported_owned_cards, expansion)) + " (" + required_cards_total + ")" + "</b></td></tr>");

                pw.println("        </table>");
                pw.println("     </td>");
            }

            pw.println("      </tr>");
            pw.println("   </table>");
            pw.println("</center>");

            if (selected_expansion == null) {
                pw.println("Select an expansion!");
            } else {
                pw.println("<B>" + selected_expansion + "</B>:");

                List<Owned_Cards> sorted_cards = imported_owned_cards.stream()
                        .sorted((Owned_Cards card1, Owned_Cards card2) -> card1.getName().compareTo(card2.getName()))
                        .collect(Collectors.toList());

                pw.println("<table><tr>");

                for (Rarity rarity : Rarity.values()) {
                    pw.println("<td style=\"vertical-align:top\">");
                    pw.println("<table><tr>");
                    pw.println("<td>" + rarity + "</td>");
                    pw.println("<td>Quantity (N, A)</td>");
                    pw.println("<td>Normal</td>");
                    pw.println("<td>Animated</td>");
                    pw.println("</tr>");
                    for (Owned_Cards card : sorted_cards) {
                        if (card.getExpansion().equals(selected_expansion) & card.getRarity().equals(rarity)) {
                            pw.println("<tr>");
                            pw.println("<td>" + card.getName() + "</td>");
                            pw.println("<td>(" + card.getNormal() + ", " + card.getAnimated() + ")</td>");
                            pw.println("<td> <input type=\"text\" name=\"" + card.getName() + "|Normal_Count\" size=\"3\"></td>");
                            pw.println("<td> <input type=\"text\" name=\"" + card.getName() + "|Animated_Count\" size=\"3\"></td>");
                            pw.println("</tr>");
                        }
                    }
                    pw.println("</table>");
                    pw.println("</td>");
                }
                pw.println("</tr></table>");
            }

            pw.println("</form>");
        }catch(Parsed_Row_Exception e){
            pw.println("Import unsuccessful. Please review row " + e + ". Format should be name|integer|integer. Also remove any unnecessary empty lines or use a backup if possible.");
        }catch(Exception e){
            pw.println("Import unsuccessful. Please review the txt file for unnecessary empty lines or use a backup if possible.");
        }
        pw.println("</body>");
        pw.println("</html>");

    }
}
