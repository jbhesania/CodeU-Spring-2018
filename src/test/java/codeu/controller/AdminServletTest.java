package codeu.controller;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.RequestDispatcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import codeu.controller.AdminPageServlet;

public class AdminServletTest {

    private AdminPageServlet adminPageServlet;
    private HttpServletRequest mockRequest;
    private RequestDispatcher mockRequestDispatcher;
    private HttpServletResponse mockResponse;

    @Before
    public void setup() throws IOException {
        adminPageServlet = new AdminPageServlet();
        mockRequest = Mockito.mock(HttpServletRequest.class);
        mockRequestDispatcher = Mockito.mock(RequestDispatcher.class);
        mockResponse = Mockito.mock(HttpServletResponse.class);
        Mockito.when(mockRequest.getRequestDispatcher("/WEB-INF/view/adminpage.jsp"))
            .thenReturn(mockRequestDispatcher);
    }

    @Test
    public void testDoGet() throws IOException, ServletException {
    adminPageServlet.doGet(mockRequest, mockResponse);

        Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
    }
}