import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import cards.Available_Cards;
import cards.Owned_Cards;
import expansions_algorithms.Cards_List_Algorithms;
import parser.*;
import card_types.Rarity;

@WebServlet(urlPatterns = { "/Test" })
public class Test extends HttpServlet {

    public void doPost(HttpServletRequest request,
                      HttpServletResponse response)
        throws ServletException, IOException {

        List<Available_Cards> available_cards = Available_Card_Parser.parse_shadowout_text_file();

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

        String card_name = request.getParameter("name");

        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();

        pw.println("<!DOCTYPE html>");
        pw.println("<html lang=\"en\">");
        pw.println("<head>");
        pw.println("    <meta charset=\"UTF-8\">");
        pw.println("    <title>Title</title>");
        pw.println("</head>");
        pw.println("<body>");
        pw.println("<center>");
        pw.println("    <form name=\"FormTest\"");
        pw.println("          method=\"post\"");
        pw.println("          action=\"http://localhost:8080/SV_Collection_Tracker/Test\">");
        pw.println("        <table>");
        pw.println("            <tr>");
        pw.println("                <td><B>Enter Card Name</B></td>");
        pw.println("                <td><textarea name=\"name\" rows=\"10\" cols=\"30\"></textarea></td>");
        pw.println("            </tr>");
        pw.println("        </table>");
        pw.println("        <input type=\"submit\" value=\"Submit\">");
        pw.println("    </form>");
        pw.println("</center>");
        pw.println("</body>");
        pw.println("</html>");

        for(Owned_Cards card:owned_cards){
            if(card.getName().equals(card_name)){
                pw.println("<br>" + card.getName() + "&nbsp;&nbsp;&nbsp;&nbsp;" + card.getExpansion() + "&nbsp;&nbsp;&nbsp;&nbsp;" + card.getRarity_String() +
                        "&nbsp;&nbsp;&nbsp;&nbsp;" + card.getVial_value() + "&nbsp;&nbsp;&nbsp;&nbsp;" + card.getVials_required() + "&nbsp;&nbsp;&nbsp;&nbsp;" +
                        card.getExtras_LiquefyAnimated_value() + "&nbsp;&nbsp;&nbsp;&nbsp;" + card.getExtras_KeepAnimated_value());
            }
        }

        pw.println("<br>" + card_name);
    }
}
