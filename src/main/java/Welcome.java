import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.text.DecimalFormat;
import java.util.List;

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
                pw.println("            <tr><td style=\"color:blue\"><b><input type=submit name=\"selected_expansion\" value=\"" + expansion + "\"></b></td></tr>");
            }else{
                pw.println("            <tr><td><b><input type=submit name=\"selected_expansion\" value=\"" + expansion + "\"></b></td></tr>");
            }


            pw.println("            <tr><td>" + two_dec.format(Cards_List_Methods.getExpected_Vials_by_Rarity(imported_owned_cards,expansion, Rarity.BRONZE)) + "</td></tr>");
            pw.println("            <tr><td>" + two_dec.format(Cards_List_Methods.getExpected_Vials_by_Rarity(imported_owned_cards,expansion, Rarity.SILVER)) + "</td></tr>");
            pw.println("            <tr><td>" + two_dec.format(Cards_List_Methods.getExpected_Vials_by_Rarity(imported_owned_cards,expansion, Rarity.GOLD)) + "</td></tr>");
            pw.println("            <tr><td>" + two_dec.format(Cards_List_Methods.getExpected_Vials_by_Rarity(imported_owned_cards,expansion, Rarity.LEGENDARY)) + "</td></tr>");

            if(Cards_List_Methods.getExpected_Vials_Total(imported_owned_cards,expansion)==max_expected_vials){
                pw.println("            <tr><td style=\"color:blue\"><b>" + two_dec.format(Cards_List_Methods.getExpected_Vials_Total(imported_owned_cards,expansion)) + "</b></td></tr>");
            }else{
                pw.println("            <tr><td><b>" + two_dec.format(Cards_List_Methods.getExpected_Vials_Total(imported_owned_cards,expansion)) + "</b></td></tr>");
            }

            int required_bronze = Cards_List_Methods.getRequired_Vials_by_Rarity(imported_owned_cards,expansion, Rarity.BRONZE)/Rarity.BRONZE.getCreate_value();
            int required_silver = Cards_List_Methods.getRequired_Vials_by_Rarity(imported_owned_cards,expansion, Rarity.SILVER)/Rarity.SILVER.getCreate_value();
            int required_gold = Cards_List_Methods.getRequired_Vials_by_Rarity(imported_owned_cards,expansion, Rarity.GOLD)/Rarity.GOLD.getCreate_value();
            int required_legendary = Cards_List_Methods.getRequired_Vials_by_Rarity(imported_owned_cards,expansion, Rarity.LEGENDARY)/Rarity.LEGENDARY.getCreate_value();
            int required_total = required_bronze + required_silver + required_gold + required_legendary;

            pw.println("            <tr><td>" + comma_sep.format(Cards_List_Methods.getRequired_Vials_by_Rarity(imported_owned_cards,expansion, Rarity.BRONZE)) + " (" + required_bronze + ")" + "</td></tr>");
            pw.println("            <tr><td>" + comma_sep.format(Cards_List_Methods.getRequired_Vials_by_Rarity(imported_owned_cards,expansion, Rarity.SILVER)) + " (" + required_silver + ")" + "</td></tr>");
            pw.println("            <tr><td>" + comma_sep.format(Cards_List_Methods.getRequired_Vials_by_Rarity(imported_owned_cards,expansion, Rarity.GOLD)) + " (" + required_gold + ")" + "</td></tr>");
            pw.println("            <tr><td>" + comma_sep.format(Cards_List_Methods.getRequired_Vials_by_Rarity(imported_owned_cards,expansion, Rarity.LEGENDARY)) + " (" + required_legendary + ")" + "</td></tr>");
            pw.println("            <tr><td><b>" + comma_sep.format(Cards_List_Methods.getRequired_Vials_Total(imported_owned_cards,expansion)) + " (" + required_total + ")" + "</b></td></tr>");

            pw.println("        </table>");
            pw.println("     </td>"); }

        pw.println("      </tr>");
        pw.println("   </table>");
        pw.println("</center>");
        
        if(selected_expansion == null){
            pw.println("Select an expansion!");
        }else{
            pw.println(selected_expansion);
        }

        pw.println("</form>");
        pw.println("</body>");
        pw.println("</html>");

        pw.println();

        /*for(Owned_Cards card:imported_owned_cards){

                pw.println("<br>" + card.getName() + "&nbsp;&nbsp;&nbsp;&nbsp;" + card.getExpansion() + "&nbsp;&nbsp;&nbsp;&nbsp;" + card.getRarity_String() +
                        "&nbsp;&nbsp;&nbsp;&nbsp;" + card.getVial_value() + "&nbsp;&nbsp;&nbsp;&nbsp;" + card.getVials_required() + "&nbsp;&nbsp;&nbsp;&nbsp;" +
                        card.getExtras_LiquefyAnimated_value() + "&nbsp;&nbsp;&nbsp;&nbsp;" + card.getExtras_KeepAnimated_value());

        }*/

    }
}
