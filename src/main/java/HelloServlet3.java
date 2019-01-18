import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import available_cards.basic.BasicBronze;
import cardtype.Rarity;

@WebServlet(urlPatterns = { "/hello" })
public class HelloServlet3 extends GenericServlet {

    public void service(ServletRequest request,
                        ServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();
        pw.println("<B>" + BasicBronze.bronze1.getLiquefy_value());
        pw.close();
        System.out.println("hi3");
    }
}
