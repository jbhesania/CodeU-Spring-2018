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
  /** Store class that gives access to Users. */
  private UserStore userStore;

  /** Set up state for handling chat requests. */
  @Override
  public void init() throws ServletException {
    super.init();
    setUserStore(UserStore.getInstance());
  }

  /**
   * Sets the UserStore used by this servlet. This function provides a common setup method for use
   * by the test framework or the servlet's init() function.
   */
  void setUserStore(UserStore userStore) {
    this.userStore = userStore;
  }

  /**
   * It then forwards to chat.jsp for rendering.
   */
 @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    String requestUrl = request.getRequestURI();
    String userName = requestUrl.substring("/users/".length());

    User user = userStore.getUser(userName);

    request.setAttribute("user", user);
    request.getRequestDispatcher("/WEB-INF/view/profilepage.jsp").forward(request, response);
 }
}
