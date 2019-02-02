import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import available_cards.basic.BasicBronze;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import card_types.*;
import card_parser.*;

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

        List<Cards> available_cards = Parser.parse_text_file();

        for(Cards card:available_cards){
            pw.println("<br>" + card.getName() + "    " + card.getExpansion() + "    " + card.getLiquefy_value());
        }
        pw.close();
    }
}