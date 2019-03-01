import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import card_types.Rarity;
import cards.Owned_Cards;
import expansions_algorithms.Cards_List_Methods;
import parser.Expansion_Parser;
import parser.Owned_Cards_Parser;

@WebServlet(urlPatterns = { "/Welcome" })
public class Welcome extends HttpServlet {

    public void doGet(HttpServletRequest request,
                       HttpServletResponse response)
            throws ServletException, IOException {

        List<Owned_Cards> owned_cards = Cards_List_Methods.makeList_of_Owned_Cards();

        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();

        pw.println("<!DOCTYPE html>");
        pw.println("<html lang=\"en\">");
        pw.println("<head>");
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
        pw.println("                <td><B>Copy Paste File Here:</B></td>");
        pw.println("                <td><textarea name=\"owned_cards_text\" rows=\"10\" cols=\"30\">" +   Owned_Cards_Parser.getOwned_Cards_Text(owned_cards)  + "</textarea></td>");
        pw.println("            </tr>");
        pw.println("        </table>");
        pw.println("        <input type=\"submit\" value=\"Import\">");
        pw.println("    </form>");
        pw.println("</center>");
        pw.println("</body>");
        pw.println("</html>");
    }



    public void doPost(HttpServletRequest request,
                       HttpServletResponse response)
            throws ServletException, IOException {

        String owned_cards_text = request.getParameter("owned_cards_text");
        String selected_expansion = request.getParameter("selected_expansion");

        List<Owned_Cards> imported_owned_cards = Owned_Cards_Parser.importOwned_Cards_List(owned_cards_text);

        //try {
            if (selected_expansion == null) {
                for (int i = 0; i < imported_owned_cards.size(); i++) {
                    if (request.getParameter(imported_owned_cards.get(i).getName() + "|Normal_Count") != null) {
                        int card_count;
                        if(request.getParameter(imported_owned_cards.get(i).getName() + "|Normal_Count").equals("")){
                            card_count = 0;
                        }else{
                            card_count = Integer.valueOf(request.getParameter(imported_owned_cards.get(i).getName() + "|Normal_Count"));
                        }
                        imported_owned_cards.set(i, new Owned_Cards(imported_owned_cards.get(i).getName(), imported_owned_cards.get(i).getExpansion(),
                                imported_owned_cards.get(i).getRarity(), imported_owned_cards.get(i).getBase_id(),
                                card_count + imported_owned_cards.get(i).getNormal(), imported_owned_cards.get(i).getAnimated()));
                    }
                    if (request.getParameter(imported_owned_cards.get(i).getName() + "|Animated_Count") != null) {
                            int card_count;
                            if(request.getParameter(imported_owned_cards.get(i).getName() + "|Animated_Count").equals("")){
                                card_count = 0;
                            }else{
                                card_count = Integer.valueOf(request.getParameter(imported_owned_cards.get(i).getName() + "|Animated_Count"));
                            }
                        imported_owned_cards.set(i, new Owned_Cards(imported_owned_cards.get(i).getName(), imported_owned_cards.get(i).getExpansion(),
                                imported_owned_cards.get(i).getRarity(), imported_owned_cards.get(i).getBase_id(),
                                imported_owned_cards.get(i).getNormal(), card_count + imported_owned_cards.get(i).getAnimated()));
                    }
                }
            }
        //}catch(NumberFormatException e){}

        List<String> expansion_name = Expansion_Parser.get_expansion_lists("name");

        DecimalFormat two_dec = new DecimalFormat("#,###.00");
        two_dec.setMaximumFractionDigits(2);

        DecimalFormat comma_sep = new DecimalFormat("#,###");

        double max_expected_vials = 0.0;
        int required_grand_total_vials = 0;
        int extras_liquefy_animated_grand_total_vials = 0;
        int extras_keep_animated_grand_total_vials = 0;

        for(String expansion:expansion_name){

            max_expected_vials = Math.max(max_expected_vials, Cards_List_Methods.getExpected_Vials_Total(imported_owned_cards,expansion));
            required_grand_total_vials += Cards_List_Methods.getRequired_Vials_Total(imported_owned_cards,expansion);
            extras_liquefy_animated_grand_total_vials += Cards_List_Methods.getExtras_LiquefyAnimated_Vials_Total(imported_owned_cards,expansion);
            extras_keep_animated_grand_total_vials += Cards_List_Methods.getExtras_KeepAnimated_Vials_Total(imported_owned_cards,expansion);

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
        pw.println("<center>");
        pw.println("    <form name=\"Welcome\"");
        pw.println("          method=\"post\"");
        pw.println("          action=\"http://localhost:8080/SV_Collection_Tracker/Welcome\">");
        pw.println("        <table>");
        pw.println("            <tr>");
        pw.println("                <td><B>Copy Paste File Here:</B></td>");
        pw.println("                <td><textarea name=\"owned_cards_text\" rows=\"10\" cols=\"30\">" +   Owned_Cards_Parser.getOwned_Cards_Text(imported_owned_cards)  + "</textarea></td>");
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


        for(String expansion:expansion_name){

            pw.println("     <td>");
            pw.println("        <table>");

            if(Cards_List_Methods.getExpected_Vials_Total(imported_owned_cards,expansion)==max_expected_vials){
                pw.println("            <tr><td style=\"color:blue\"><b><input type=submit name=\"selected_expansion\" value=\"" + expansion + "\" style=\"color:white;background:green\"></b></td></tr>");
            }else{
                pw.println("            <tr><td><b><input type=submit name=\"selected_expansion\" value=\"" + expansion + "\"></b></td></tr>");
            }

            for(Rarity rarity:Rarity.values()){
                pw.println("            <tr><td>" + two_dec.format(Cards_List_Methods.getExpected_Vials_by_Rarity(imported_owned_cards,expansion, rarity)) + "</td></tr>");
            }

            if(Cards_List_Methods.getExpected_Vials_Total(imported_owned_cards,expansion)==max_expected_vials){
                pw.println("            <tr><td style=\"color:blue\"><b>" + two_dec.format(Cards_List_Methods.getExpected_Vials_Total(imported_owned_cards,expansion)) + "</b></td></tr>");
            }else{
                pw.println("            <tr><td><b>" + two_dec.format(Cards_List_Methods.getExpected_Vials_Total(imported_owned_cards,expansion)) + "</b></td></tr>");
            }

            int required_cards_total = 0;
            for(Rarity rarity:Rarity.values()){
                int required_cards = Cards_List_Methods.getRequired_Vials_by_Rarity(imported_owned_cards,expansion, rarity)/rarity.getCreate_value();
                required_cards_total += required_cards;

                pw.println("            <tr><td>" + comma_sep.format(Cards_List_Methods.getRequired_Vials_by_Rarity(imported_owned_cards,expansion, rarity)) + " (" + required_cards + ")" + "</td></tr>");
            }

            pw.println("            <tr><td><b>" + comma_sep.format(Cards_List_Methods.getRequired_Vials_Total(imported_owned_cards,expansion)) + " (" + required_cards_total + ")" + "</b></td></tr>");

            pw.println("        </table>");
            pw.println("     </td>"); }

        pw.println("      </tr>");
        pw.println("   </table>");
        pw.println("</center>");

        if(selected_expansion == null){
            pw.println("Select an expansion!");
        }else{
            pw.println("<B>" +selected_expansion +"</B>:");

            List<Owned_Cards> sorted_cards = imported_owned_cards.stream()
                    .sorted((Owned_Cards card1, Owned_Cards card2) -> card1.getName().compareTo(card2.getName()))
                    .collect(Collectors.toList());

            pw.println("<table><tr>");

            for(Rarity rarity:Rarity.values()) {
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
        pw.println("</body>");
        pw.println("</html>");

    }
}
