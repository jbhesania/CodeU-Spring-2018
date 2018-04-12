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
    String pageUserName = requestUrl.substring("/users/".length());
    // The user who's profile page we are on
    User pageUser = userStore.getUser(pageUserName);
    User sessionUser = null;

    if(request.getSession().getAttribute("user") != null) {
      String sessionUserName = (String) request.getSession().getAttribute("user");
      // The user that is performing the follow
      sessionUser = userStore.getUser(sessionUserName);
    }
    
    request.setAttribute("sessionUser", sessionUser);
    request.setAttribute("pageUser", pageUser);
    request.getRequestDispatcher("/WEB-INF/view/profilepage.jsp").forward(request, response);
 }

 /**
  * Implements the follow functionality
  */
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    
    String requestUrl = request.getRequestURI();
    String pageUserName = requestUrl.substring("/users/".length());
    // The user who's profile page we are on
    User pageUser = userStore.getUser(pageUserName);

    // The user that is performing the follow
    String sessionUserName = (String) request.getSession().getAttribute("user");
    User sessionUser = userStore.getUser(sessionUserName);

    /* TODO : be able to access this data from jsp file instead of recollecting
    User sessionUser = (User) request.getAttribute("sessionUser");
    User pageUser = (User) request.getAttribute("pageUser");
    */ 

    //Now add user to followerUser's hashtable
    if(request.getParameter("follow").equals("true")) {
      sessionUser.follow(pageUser);
    }
    else {
      sessionUser.unfollow(pageUser);
    }

    response.sendRedirect(requestUrl);
  }
}
