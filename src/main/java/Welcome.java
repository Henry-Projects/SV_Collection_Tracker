import java.io.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import card_types.Rarity;
import cards.Negative_Owned_Cards_Exception;
import cards.Owned_Cards;
import expansions_algorithms.Cards_List_Methods;
import parser.Expansion_Parser;
import parser.Owned_Cards_Parser;
import parser.Parsed_Row_Exception;

@WebServlet(urlPatterns = { "/Welcome" })


/*This servlet has a doGet and doPost method.
The end user will first see the page generated from doGet.
Submitting the form from that page will redirect them to the page generated from doPost.
Submitting the form from the doPost page will redirect them to
that same page again resulting in a loop with the doPost page.*/

public class Welcome extends HttpServlet {

    //list to store the list of owned cards created once in the doGet method
    private List<Owned_Cards> servlet_list = new ArrayList<>();

    /*This method generates the first page the end user sees after going to localhost:8080/SV_Collection_Tracker/Welcome.
    It has a pre-filled text area with all the available cards with quantity 0 for both normal and animated.
    The end user can overwrite that text area with their saved collection (in the same format) if they are not a new user.
    The import button will send the text as a parameter to the second method doPost in this class.*/
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        try {

            //This makes a list of available cards originating from the API.
            List<Owned_Cards> owned_cards = Owned_Cards_Parser.makeList_of_Owned_Cards();
            this.servlet_list = owned_cards;

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
            //This creates one table with one row with two cells.
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

            //If any exceptions is caught when generating the text for the text area, it will print the below statement.
        }catch(Exception e){
            response.setContentType("text/html");
            PrintWriter pw = response.getWriter();

            pw.println("<!DOCTYPE html><html lang=\"en\"><head></head><body>Please review the Expansions.txt file in the resources folder or check https://shadowverse-portal.com/api/v1/cards?format=json&lang=en to see if it is up.</body></html>");
        }
    }



    /*This method generates the second page after the form from the first page above is submitted.
    It has the same pre-filled text area from the first page in addition to statistics regarding the cards from the text area.
    The text area this time is read only to prevent corruption. The end user can copy and save the text when they are done with this app.
    The possible parameters that can be sent from here are
    the text from the text area, the selected expansion, and the card count for normal and animated
    Submitting the form here will redirect the end user to this page again with the new parameters being used.*/
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {


        //Get the three types of parameters from the previous submitted form
        String owned_cards_text = request.getParameter("owned_cards_text");
        String selected_expansion = request.getParameter("selected_expansion");
        //String status_message = request.getParameter("status_message");

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



        List<Owned_Cards> imported_owned_cards = new ArrayList<>();

        //This try catch handles possible exceptions resulting from importing the text from the text area.
        //If any exceptions is caught, it will redirect to a page listing only the possible cause of the exception.
        try {

            imported_owned_cards = Owned_Cards_Parser.importOwned_Cards_List(owned_cards_text, this.servlet_list);

            //This group of variables is for the inner try catch that handles the possible exceptions
            //resulting from the card count parameters
            String card_name_for_exception_list = "";
            String card_param_name_for_exception_list = "";
            String rarity_string_for_exception_list="";
            String expansion_getter = "";
            List<String> cards_updated_list = new ArrayList<>();
            List<String> cards_not_updated_list = new ArrayList<>();
            List<String> cards_param_updated_list = new ArrayList<>();
            List<String> cards_param_not_updated_list = new ArrayList<>();
            List<String> rarity_string_updated = new ArrayList<>();
            List<String> rarity_string_not_updated = new ArrayList<>();


            //This if then statement is required to differentiate between submitting the form through the update button or one of the expansion buttons.
            if (selected_expansion == null) {
                //This loops through the card count parameters to update the list of owned cards. If an exception is caught,
                //that particular card will not be updated. The loop will continue to loop through the remaining cards.
                //A list of cards are updated and cards that are not updated due to an exception will be displayed afterwards.

                //pw.println(status_message);

                for (String card_name_parameter : card_name_parameters) {
                    try {

                        if (card_name_parameter.contains("|Normal_Count")) {

                            String card_name = card_name_parameter.replace("|Normal_Count", "");

                            card_param_name_for_exception_list = card_name_parameter;
                            card_name_for_exception_list = card_name;
                            rarity_string_for_exception_list = " - Normal: ";

                            int inputted_card_count = Integer.valueOf(request.getParameter(card_name_parameter));

                            for (int i = 0; i < imported_owned_cards.size(); i++) {
                                if (imported_owned_cards.get(i).getName().equals(card_name)) {

                                    expansion_getter = imported_owned_cards.get(i).getExpansion();

                                    imported_owned_cards.set(i, new Owned_Cards(card_name, imported_owned_cards.get(i).getExpansion(),
                                            imported_owned_cards.get(i).getRarity(), imported_owned_cards.get(i).getBase_id(),
                                            inputted_card_count + imported_owned_cards.get(i).getNormal(), imported_owned_cards.get(i).getAnimated()));
                                }
                            }

                            cards_updated_list.add(card_name);
                            cards_param_updated_list.add(card_name_parameter);
                            rarity_string_updated.add(" - Normal: ");

                        } else if (card_name_parameter.contains("|Animated_Count")) {

                            String card_name = card_name_parameter.replace("|Animated_Count", "");

                            card_param_name_for_exception_list = card_name_parameter;
                            card_name_for_exception_list = card_name;
                            rarity_string_for_exception_list = " - Animated: ";

                            int inputted_card_count = Integer.valueOf(request.getParameter(card_name_parameter));

                            for (int i = 0; i < imported_owned_cards.size(); i++) {

                                if (imported_owned_cards.get(i).getName().equals(card_name)) {

                                    expansion_getter = imported_owned_cards.get(i).getExpansion();

                                    imported_owned_cards.set(i, new Owned_Cards(card_name, imported_owned_cards.get(i).getExpansion(),
                                            imported_owned_cards.get(i).getRarity(), imported_owned_cards.get(i).getBase_id(),
                                            imported_owned_cards.get(i).getNormal(), inputted_card_count + imported_owned_cards.get(i).getAnimated()));
                                }
                            }

                            cards_updated_list.add(card_name);
                            cards_param_updated_list.add(card_name_parameter);
                            rarity_string_updated.add(" - Animated: ");

                        }
                    } catch (NumberFormatException | Negative_Owned_Cards_Exception e) {

                        //These methods keep track of cards that are not updated due to an exception.
                        cards_param_not_updated_list.add(card_param_name_for_exception_list);
                        cards_not_updated_list.add(card_name_for_exception_list);
                        rarity_string_not_updated.add(rarity_string_for_exception_list);
                    }
                }
                //If there were card count parameters and no exceptions were thrown, print the following status at the top.
                if (!card_name_parameters.isEmpty() & cards_not_updated_list.isEmpty()) {

                    /*pw.println("<input type=\"hidden\" name=\"status_message\" value=\"");
                    pw.println("<B><center>Status:</B> Card count update for " + expansion_getter + " was a success!</center>");
                    pw.println("<B><center>Cards Updated (" + cards_updated_list.size() + ") :</B> <font color=&quot;green&quot;>");
                    for (int i = 0; i < cards_updated_list.size(); i++) {
                        if (i < cards_updated_list.size() - 1) {
                            pw.print(cards_updated_list.get(i) + rarity_string_updated.get(i) + request.getParameter(cards_param_updated_list.get(i)) + ", ");
                        } else {
                            pw.print(cards_updated_list.get(i) + rarity_string_updated.get(i) + request.getParameter(cards_param_updated_list.get(i)) + "</font></center>");
                        }
                    }
                    pw.println("\">");*/


                    pw.println("<B><center>Status:</B> Card count update for " + expansion_getter + " was a success!</center>");
                    pw.print("<B><center>Cards Updated (" + cards_updated_list.size() + ") :</B> <font color=\"green\">");
                    for (int i = 0; i < cards_updated_list.size(); i++) {
                        if (i < cards_updated_list.size() - 1) {
                            pw.print(cards_updated_list.get(i) + rarity_string_updated.get(i) + request.getParameter(cards_param_updated_list.get(i)) + ", ");
                        } else {
                            pw.print(cards_updated_list.get(i) + rarity_string_updated.get(i) + request.getParameter(cards_param_updated_list.get(i)) + "</font></center>");
                        }
                    }
                    //If there were card count parameters and exceptions were thrown, print the following status at the top.
                } else if (!card_name_parameters.isEmpty() & !cards_not_updated_list.isEmpty()) {
                    pw.print("<B><center>Status:</B> Invalid character inputted for (" + cards_not_updated_list.size() + ") card(s): <font color=\"red\"><B>");
                    for (int i = 0; i < cards_not_updated_list.size(); i++) {
                        if (i < cards_not_updated_list.size() - 1) {
                            pw.print(cards_not_updated_list.get(i) + rarity_string_not_updated.get(i) + request.getParameter(cards_param_not_updated_list.get(i)) + ", ");
                        } else {
                            pw.println(cards_not_updated_list.get(i) + rarity_string_not_updated.get(i) + request.getParameter(cards_param_not_updated_list.get(i)) + "</font></B></center>");
                        }
                    }
                    pw.println("<center>Please review <B>" + expansion_getter + "</B> again.</center>");
                    pw.print("<B><center>Cards Updated (" + cards_updated_list.size() + ") :</B> <font color=\"green\">");

                    //There can be card count parameters of "" but no updated cards.
                    if(cards_updated_list.isEmpty()){
                        pw.println("</font></center>");
                    }

                    for (int i = 0; i < cards_updated_list.size(); i++) {
                        if (i < cards_updated_list.size() - 1) {
                            pw.print(cards_updated_list.get(i) + rarity_string_updated.get(i) + request.getParameter(cards_param_updated_list.get(i)) + ", ");
                        } else {
                            pw.println(cards_updated_list.get(i) + rarity_string_updated.get(i) + request.getParameter(cards_param_updated_list.get(i)) + "</font></center>");
                        }
                    }
                }
             //If there were card count parameters but one of the expansion buttons was selected instead of the update button, print the following.
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



            //This next block creates the statistics showing which pack to buy and the required vials to complete the expansion.
            //It also creates the statistics to the right of the text area summarizing the various grand total vials.
            List<String> expansion_name = Expansion_Parser.get_expansion_lists("name");

            DecimalFormat two_dec = new DecimalFormat("#,###.00");
            two_dec.setMaximumFractionDigits(2);

            DecimalFormat comma_sep = new DecimalFormat("#,###");

            BigDecimal max_expected_vials = BigDecimal.ZERO;
            int required_grand_total_vials = 0;
            int extras_liquefy_animated_grand_total_vials = 0;
            int extras_keep_animated_grand_total_vials = 0;

            for (String expansion : expansion_name) {

                max_expected_vials = max_expected_vials.max(Cards_List_Methods.getExpected_Vials_Total(imported_owned_cards, expansion).setScale(2, RoundingMode.HALF_UP));
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
            pw.println("        <input type=\"submit\" value=\"Update\" style=\"height:30px; width:70px\">");
            pw.println("</center>");

            pw.println("<br><br>");
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

                if (Cards_List_Methods.getExpected_Vials_Total(imported_owned_cards, expansion).setScale(2, RoundingMode.HALF_UP).equals(max_expected_vials)) {
                    pw.println("            <tr><td style=\"color:blue\"><b><input type=submit name=\"selected_expansion\" value=\"" + expansion + "\" style=\"color:white;background:green\"></b></td></tr>");
                } else {
                    pw.println("            <tr><td><b><input type=submit name=\"selected_expansion\" value=\"" + expansion + "\"></b></td></tr>");
                }

                for (Rarity rarity : Rarity.values()) {
                    pw.println("            <tr><td>" + two_dec.format(Cards_List_Methods.getExpected_Vials_by_Rarity(imported_owned_cards, expansion, rarity).setScale(2, RoundingMode.HALF_UP)) + "</td></tr>");
                }

                if (Cards_List_Methods.getExpected_Vials_Total(imported_owned_cards, expansion).setScale(2, RoundingMode.HALF_UP).equals(max_expected_vials)) {
                    pw.println("            <tr><td style=\"color:blue\"><b>" + two_dec.format(Cards_List_Methods.getExpected_Vials_Total(imported_owned_cards, expansion).setScale(2, RoundingMode.HALF_UP)) + "</b></td></tr>");
                } else {
                    pw.println("            <tr><td><b>" + two_dec.format(Cards_List_Methods.getExpected_Vials_Total(imported_owned_cards, expansion).setScale(2,RoundingMode.HALF_UP)) + "</b></td></tr>");
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
                pw.println("<br><br><B><center>Select an expansion!</center></B>");
            } else {
                pw.println("<br><br><B><center>" + selected_expansion + "</center></B>:");


                //This next block sorts the cards that were imported from the text area and list the text fields for
                //inputting the card count.
                List<Owned_Cards> sorted_cards = imported_owned_cards.stream()
                        .sorted((Owned_Cards card1, Owned_Cards card2) -> card1.getName().compareTo(card2.getName()))
                        .collect(Collectors.toList());

                pw.println("<center><table><tr>");

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
                pw.println("</tr></table></center>");
            }

            pw.println("</form>");

            //If any exceptions are caught while importing the text from the text area from the doGet page,
            //a new page will be generated instead
            //that attempts to show precisely where the exception occurred from the text.
        }catch(Parsed_Row_Exception e) {
            pw.println("Import unsuccessful. Please review row " + e + ". Format should be name|non negative integer|non negative integer. Also remove any unnecessary empty lines or use a backup if possible.");
        }catch(Negative_Owned_Cards_Exception e){
            pw.println("Import unsuccessful. Please review " + e + ". Format should be name|non negative integer|non negative integer.");
        }catch(Exception e){
            pw.println("Import unsuccessful. Please review the txt file for unnecessary empty lines or use a backup if possible.");
        }
        pw.println("</body>");
        pw.println("</html>");

        //update servlet list to new owned cards list with new count
        this.servlet_list = imported_owned_cards;
    }
}
