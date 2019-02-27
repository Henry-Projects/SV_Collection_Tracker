import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.List;

import cards.Owned_Cards;
import expansions_algorithms.Cards_List_Methods;
import parser.Owned_Cards_Parser;

@WebServlet(urlPatterns = { "/Welcome" })
public class Welcome extends HttpServlet {

    public void service(HttpServletRequest request,
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
        pw.println("    <form name=\"FormTest\"");
        pw.println("          method=\"post\"");
        pw.println("          action=\"http://localhost:8080/SV_Collection_Tracker/Tracking\">");
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
}
