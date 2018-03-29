package codeu.controller;

import codeu.model.data.User;
import codeu.model.store.basic.UserStore;
import org.mindrot.jbcrypt.BCrypt;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
* Profile Page class responsible for displaying profile pages.
*/
public class ProfilePageServlet extends HttpServlet {

 @Override
 public void doGet(HttpServletRequest request, HttpServletResponse response)
     throws IOException, ServletException {
   request.getRequestDispatcher("/WEB-INF/view/profilepage.jsp").forward(request, response);
 }
}
