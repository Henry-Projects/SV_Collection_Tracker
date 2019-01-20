import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import available_cards.basic.BasicBronze;
import java.util.EnumSet;

@WebServlet(urlPatterns = { "/hello" })
public class HelloServlet3 extends GenericServlet {

    public void service(ServletRequest request,
                        ServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();
        EnumSet<BasicBronze> card_list;
        card_list = EnumSet.allOf(BasicBronze.class);
        for(BasicBronze c : card_list) {
            pw.println("<B>" + c.getLiquefy_values() + " " + c.getRarity() + " " + c.toString() + "<br>");
        }
        pw.close();
        System.out.println("hello");
    }
}