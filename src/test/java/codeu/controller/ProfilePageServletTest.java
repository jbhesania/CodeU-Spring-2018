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
import org.mindrot.jbcrypt.BCrypt;
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

    UserStore mockUserStore = Mockito.mock(UserStore.class);
    profilePageServlet.setUserStore(mockUserStore);
    User mockUser = Mockito.mock(User.class);

    Mockito.when(mockRequest.getRequestURI()).thenReturn("/users/testuser");
    Mockito.when(mockUserStore.getUser("testuser")).thenReturn(mockUser);

    profilePageServlet.doGet(mockRequest, mockResponse);


    Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
    Mockito.verify(mockRequest).setAttribute("user", mockUser);
  }
}
