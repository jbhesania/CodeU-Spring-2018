package codeu.controller;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


public class UploadServletTest {


    private UploadServlet uploadServlet;
    private HttpServletRequest mockRequest;
    private RequestDispatcher mockRequestDispatcher;
    private HttpServletResponse mockResponse;

    @Before
    public void setup() throws IOException {
        uploadServlet = new UploadServlet();
        mockRequest = Mockito.mock(HttpServletRequest.class);
        mockRequestDispatcher = Mockito.mock(RequestDispatcher.class);
        mockResponse = Mockito.mock(HttpServletResponse.class);
        Mockito.when(mockRequest.getRequestDispatcher("/WEB-INF/view/adminpage.jsp"))
                .thenReturn(mockRequestDispatcher);
    }

    @Test
    public void testDoGet() throws IOException, ServletException {
        uploadServlet.doGet(mockRequest, mockResponse);

        Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
    }

    @Test
    public void testDoPost_NullFile() throws IOException, ServletException {
        Mockito.when(mockRequest.getPart("file")).thenReturn(null);

        HttpSession mockSession = Mockito.mock(HttpSession.class);
        Mockito.when(mockRequest.getSession()).thenReturn(mockSession);

        uploadServlet.doPost(mockRequest, mockResponse);
        Mockito.verify(mockRequest).setAttribute("error", "No file uploaded.");
        Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);


//        Mockito.when(request.getContentType()).thenReturn("multipart/form-data; boundary=someBoundary");

    }

}
