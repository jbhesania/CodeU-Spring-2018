package codeu.controller;

import codeu.model.data.User;
import codeu.model.store.basic.UserStore;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class RegisterServletTest {

    private RegisterServlet registerServlet;
    private HttpServletRequest mockRequest;
    private HttpServletResponse mockResponse;
    private RequestDispatcher mockRequestDispatcher;
    private UserStore mockUserStore;
    private HttpSession mockSession;
    private ArgumentCaptor<User> userArgumentCaptor;

    @Before
    public void setup() {
        registerServlet = new RegisterServlet();
        mockRequest = Mockito.mock(HttpServletRequest.class);
        mockResponse = Mockito.mock(HttpServletResponse.class);
        mockRequestDispatcher = Mockito.mock(RequestDispatcher.class);
        mockUserStore = Mockito.mock(UserStore.class);
        mockSession = Mockito.mock(HttpSession.class);
        userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        Mockito.when(mockRequest.getRequestDispatcher("/WEB-INF/view/register.jsp"))
            .thenReturn(mockRequestDispatcher);
        Mockito.when(mockRequest.getParameter("username")).thenReturn("test username");
    }

    @Test
    public void testDoGet() throws IOException, ServletException {
        registerServlet.doGet(mockRequest, mockResponse);

        Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
    }

    @Test
    public void testDoPost_BadUsername() throws IOException, ServletException {
        Mockito.when(mockRequest.getParameter("username")).thenReturn("bad !@#$% username");

        registerServlet.doPost(mockRequest, mockResponse);

        Mockito.verify(mockRequest)
            .setAttribute("error", "Please enter only letters, numbers, and spaces.");
        Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
    }

    @Test
    public void testDoPost_ExistingUser() throws IOException, ServletException {
        Mockito.when(mockUserStore.isUserRegistered("test username")).thenReturn(true);
        registerServlet.setUserStore(mockUserStore);

        Mockito.when(mockRequest.getSession()).thenReturn(mockSession);

        registerServlet.doPost(mockRequest, mockResponse);

        Mockito.verify(mockUserStore, Mockito.never()).addUser(Mockito.any(User.class));
    }

    @Test
    public void testDoPost_NewUser() throws IOException, ServletException {
        Mockito.when(mockUserStore.isUserRegistered("test username")).thenReturn(false);
        registerServlet.setUserStore(mockUserStore);
    
        Mockito.when(mockRequest.getSession()).thenReturn(mockSession);
    
        registerServlet.doPost(mockRequest, mockResponse);
    
        Mockito.verify(mockUserStore).addUser(userArgumentCaptor.capture());
        Assert.assertEquals(userArgumentCaptor.getValue().getName(), "test username");
    }
}
