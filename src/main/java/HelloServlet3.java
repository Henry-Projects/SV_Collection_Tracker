import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import cards.Available_Cards;
import cards.Owned_Cards;
import expansions_algorithms.Cards_Calculator;
import parser.*;
import card_types.Rarity;

@WebServlet(urlPatterns = { "/hello" })
public class HelloServlet3 extends GenericServlet {

    public void service(ServletRequest request,
                        ServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();

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

        for(Owned_Cards card:owned_cards) {
            pw.println("<br>" + card.getName() + "&nbsp;&nbsp;&nbsp;&nbsp;" + card.getExpansion() + "&nbsp;&nbsp;&nbsp;&nbsp;" + card.getRarity() +
                    "&nbsp;&nbsp;&nbsp;&nbsp;" + card.getVial_value() + "&nbsp;&nbsp;&nbsp;&nbsp;" + card.getVials_required());
        }

        pw.println("<br>" + owned_cards.size());

        List<String> expansion_name = Expansion_Parser.get_expansion_lists("name");

        DecimalFormat two_dec = new DecimalFormat("0.00");
        two_dec.setMaximumFractionDigits(2);

        for(String expansion:expansion_name) {
            pw.println("<br>" + two_dec.format(Cards_Calculator.getExpected_Vials(owned_cards, expansion, Rarity.BRONZE)
                    + Cards_Calculator.getExpected_Vials(owned_cards, expansion, Rarity.SILVER)
                    + Cards_Calculator.getExpected_Vials(owned_cards, expansion, Rarity.GOLD)
                    + Cards_Calculator.getExpected_Vials(owned_cards, expansion, Rarity.LEGENDARY)));
        }

        //for(Available_Cards card:available_cards){
        //    pw.println("<br>" + card.getName() + "&nbsp;&nbsp;&nbsp;&nbsp;" + card.getExpansion() + "&nbsp;&nbsp;&nbsp;&nbsp;" + card.getRarity());
        //}
        //pw.println("<br>" + available_cards.size());
        pw.close();
    }
}