import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;

@WebServlet(urlPatterns = { "/hello" })
public class HelloServlet3 extends GenericServlet {

    public void service(ServletRequest request,
                        ServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();
        pw.println("<B>Hello! Intellij");
        pw.close();
        System.out.println("hi");
    }
}
