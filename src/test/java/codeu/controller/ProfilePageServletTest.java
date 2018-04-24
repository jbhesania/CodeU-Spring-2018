// Copyright 2017 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package codeu.controller;

import codeu.model.data.User;
import codeu.model.store.basic.UserStore;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class ProfilePageServletTest {

  private ProfilePageServlet profilePageServlet;
  private HttpServletRequest mockRequest;
  private HttpServletResponse mockResponse;
  private RequestDispatcher mockRequestDispatcher;


  @Before
  public void setup() {
    profilePageServlet = new ProfilePageServlet();
    mockRequest = Mockito.mock(HttpServletRequest.class);
    mockResponse = Mockito.mock(HttpServletResponse.class);
    mockRequestDispatcher = Mockito.mock(RequestDispatcher.class);

    Mockito.when(mockRequest.getRequestDispatcher("/WEB-INF/view/profilepage.jsp"))
        .thenReturn(mockRequestDispatcher);
  }

  @Test
  public void testDoGet() throws IOException, ServletException {
    HttpSession mockSession = Mockito.mock(HttpSession.class);
    UserStore mockUserStore = Mockito.mock(UserStore.class);
    profilePageServlet.setUserStore(mockUserStore);

    User mockPageUser = Mockito.mock(User.class);
    User mockSessionUser = Mockito.mock(User.class);

    Mockito.when(mockRequest.getRequestURI()).thenReturn("/users/testuser");
    Mockito.when(mockUserStore.getUser("testuser")).thenReturn(mockPageUser);

    Mockito.when(mockRequest.getSession()).thenReturn(mockSession);
    Mockito.when(mockSession.getAttribute("user")).thenReturn("sessionUserName");
    Mockito.when(mockUserStore.getUser("sessionUserName")).thenReturn(mockSessionUser);


    profilePageServlet.doGet(mockRequest, mockResponse);

    Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
    Mockito.verify(mockRequest).setAttribute("sessionUser", mockSessionUser);
    Mockito.verify(mockRequest).setAttribute("pageUser", mockPageUser);
  }

  @Test
  public void testDoGet_NullSessionUser() throws IOException, ServletException {
    UserStore mockUserStore = Mockito.mock(UserStore.class);
    profilePageServlet.setUserStore(mockUserStore);
    User mockPageUser = Mockito.mock(User.class);
    HttpSession mockSession = Mockito.mock(HttpSession.class);

    Mockito.when(mockRequest.getRequestURI()).thenReturn("/users/testuser");
    Mockito.when(mockUserStore.getUser("testuser")).thenReturn(mockPageUser);

    Mockito.when(mockRequest.getSession()).thenReturn(mockSession);
    Mockito.when(mockSession.getAttribute("user")).thenReturn(null);


    profilePageServlet.doGet(mockRequest, mockResponse);

    Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
    Mockito.verify(mockRequest).setAttribute("sessionUser", null);
    Mockito.verify(mockRequest).setAttribute("pageUser", mockPageUser);
  }

  @Test
  public void testDoPost_Follow() throws IOException, ServletException {
    HttpSession mockSession = Mockito.mock(HttpSession.class);
    UserStore mockUserStore = Mockito.mock(UserStore.class);
    profilePageServlet.setUserStore(mockUserStore);

    User mockPageUser = Mockito.mock(User.class);
    User mockSessionUser = Mockito.mock(User.class);

    Mockito.when(mockRequest.getRequestURI()).thenReturn("/users/testuser");
    Mockito.when(mockUserStore.getUser("testuser")).thenReturn(mockPageUser);

    Mockito.when(mockRequest.getSession()).thenReturn(mockSession);
    Mockito.when(mockSession.getAttribute("user")).thenReturn("sessionUserName");
    Mockito.when(mockUserStore.getUser("sessionUserName")).thenReturn(mockSessionUser);
    Mockito.when(mockRequest.getParameter("follow")).thenReturn("true");
    Mockito.when(mockRequest.getParameter("admin")).thenReturn(null);
    
    profilePageServlet.doPost(mockRequest, mockResponse);

    Mockito.verify(mockSessionUser).follow(mockPageUser);
    Mockito.verify(mockResponse).sendRedirect("/users/testuser");
  }

  @Test
  public void testDoPost_Unfollow() throws IOException, ServletException {
    HttpSession mockSession = Mockito.mock(HttpSession.class);
    UserStore mockUserStore = Mockito.mock(UserStore.class);
    profilePageServlet.setUserStore(mockUserStore);

    User mockPageUser = Mockito.mock(User.class);
    User mockSessionUser = Mockito.mock(User.class);

    Mockito.when(mockRequest.getRequestURI()).thenReturn("/users/testuser");
    Mockito.when(mockUserStore.getUser("testuser")).thenReturn(mockPageUser);

    Mockito.when(mockRequest.getSession()).thenReturn(mockSession);
    Mockito.when(mockSession.getAttribute("user")).thenReturn("sessionUserName");
    Mockito.when(mockUserStore.getUser("sessionUserName")).thenReturn(mockSessionUser);
    Mockito.when(mockRequest.getParameter("follow")).thenReturn("false");
    Mockito.when(mockRequest.getParameter("admin")).thenReturn(null);
    
    profilePageServlet.doPost(mockRequest, mockResponse);

    Mockito.verify(mockSessionUser).unfollow(mockPageUser);
    Mockito.verify(mockResponse).sendRedirect("/users/testuser");
  }

  @Test
  public void testDoPost_MakeAdmin() throws IOException, ServletException {
    HttpSession mockSession = Mockito.mock(HttpSession.class);
    UserStore mockUserStore = Mockito.mock(UserStore.class);
    profilePageServlet.setUserStore(mockUserStore);

    User mockPageUser = Mockito.mock(User.class);
    User mockSessionUser = Mockito.mock(User.class);

    Mockito.when(mockRequest.getRequestURI()).thenReturn("/users/testuser");
    Mockito.when(mockUserStore.getUser("testuser")).thenReturn(mockPageUser);

    Mockito.when(mockRequest.getSession()).thenReturn(mockSession);
    Mockito.when(mockSession.getAttribute("user")).thenReturn("cari");
    Mockito.when(mockUserStore.getUser("cari")).thenReturn(mockSessionUser);
    Mockito.when(mockRequest.getParameter("follow")).thenReturn(null);
    Mockito.when(mockRequest.getParameter("admin")).thenReturn("true");

    profilePageServlet.doPost(mockRequest, mockResponse);

    Mockito.verify(mockPageUser).makeAdmin();
    Mockito.verify(mockResponse).sendRedirect("/users/testuser");
  }

  @Test
  public void testDoPost_RemoveAdmin() throws IOException, ServletException {
    HttpSession mockSession = Mockito.mock(HttpSession.class);
    UserStore mockUserStore = Mockito.mock(UserStore.class);
    profilePageServlet.setUserStore(mockUserStore);

    User mockPageUser = Mockito.mock(User.class);
    User mockSessionUser = Mockito.mock(User.class);

    Mockito.when(mockRequest.getRequestURI()).thenReturn("/users/cari");
    Mockito.when(mockUserStore.getUser("cari")).thenReturn(mockPageUser);

    Mockito.when(mockRequest.getSession()).thenReturn(mockSession);
    Mockito.when(mockSession.getAttribute("user")).thenReturn("lloza");
    Mockito.when(mockUserStore.getUser("lloza")).thenReturn(mockSessionUser);
    Mockito.when(mockRequest.getParameter("follow")).thenReturn(null);
    Mockito.when(mockRequest.getParameter("admin")).thenReturn("false");

    profilePageServlet.doPost(mockRequest, mockResponse);

    Mockito.verify(mockPageUser).removeAdmin();
    Mockito.verify(mockResponse).sendRedirect("/users/cari");
  }


}
