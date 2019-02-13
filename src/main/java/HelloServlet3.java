import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;

import java.util.List;
import card_types.*;
import parser.*;

@WebServlet(urlPatterns = { "/hello" })
public class HelloServlet3 extends GenericServlet {

    public void service(ServletRequest request,
                        ServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();
        /*
        EnumSet<BasicBronze> card_list;
        card_list = EnumSet.allOf(BasicBronze.class);
        for(BasicBronze c : card_list) {
            pw.println("<B>" + c.getLiquefy_values() + " " + c.getRarity() + " " + c.toString() + "<br>");
        */

        //Path path = Paths.get("ShadowOut.txt");

        List<Cards> available_cards = Card_Parser.parse_shadowout_text_file();

        for(int card_index = 0; card_index < available_cards.size(); card_index++){
            if(card_index>0){
                if(available_cards.get(card_index).getName().contains(available_cards.get(card_index-1).getName())){
                    available_cards.remove(card_index);
                    card_index =- 1;
                }
            }
        }

        for(Cards card:available_cards){
            pw.println("<br>" + card.getName() + "&nbsp;&nbsp;&nbsp;&nbsp;" + card.getExpansion() + "&nbsp;&nbsp;&nbsp;&nbsp;" + card.getRarity());
        }
        pw.println("<br>" + available_cards.size());
        pw.close();
    }
}