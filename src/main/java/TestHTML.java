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

@WebServlet(urlPatterns = { "/TestHTML" })
public class TestHTML extends HttpServlet {

    public void service(HttpServletRequest request,
                       HttpServletResponse response)
            throws ServletException, IOException {

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
        pw.println("          action=\"http://localhost:8080/ShadowCollectionOptimizer/Test\">");
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
    }
}
