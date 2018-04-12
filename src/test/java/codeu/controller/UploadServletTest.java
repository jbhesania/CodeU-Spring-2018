package codeu.controller;

import codeu.model.data.Conversation;
import codeu.model.data.Message;
import codeu.model.data.User;
import codeu.model.store.basic.ConversationStore;
import codeu.model.store.basic.MessageStore;
import codeu.model.store.basic.UserStore;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;


public class UploadServletTest {


    private UploadServlet uploadServlet;
    private HttpServletRequest mockRequest;
    private RequestDispatcher mockRequestDispatcher;
    private HttpServletResponse mockResponse;
    private Part mockPart;

    @Before
    public void setup() throws IOException {
        uploadServlet = new UploadServlet();
        mockRequest = Mockito.mock(HttpServletRequest.class);
        mockRequestDispatcher = Mockito.mock(RequestDispatcher.class);
        mockResponse = Mockito.mock(HttpServletResponse.class);
        mockPart = Mockito.mock(Part.class);
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
    }

    @Test
    public void testDoPost_ImageFile() throws IOException, ServletException {
        Mockito.when(mockRequest.getPart("file")).thenReturn(mockPart);
        Mockito.when(mockPart.getContentType()).thenReturn("image/gif");
        Mockito.when(mockPart.getHeader("content-disposition")).thenReturn("form-data; name=\"dataFile\"; filename=\"photo.gif\"");

        HttpSession mockSession = Mockito.mock(HttpSession.class);
        Mockito.when(mockRequest.getSession()).thenReturn(mockSession);

        uploadServlet.doPost(mockRequest, mockResponse);
        Mockito.verify(mockRequest).setAttribute("error", "Wrong file type.");
        Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
    }

    @Test
    public void testDoPost_WrongFormat() throws IOException, ServletException {
        Mockito.when(mockRequest.getPart("file")).thenReturn(mockPart);
        Mockito.when(mockPart.getContentType()).thenReturn("text/plain");
        Mockito.when(mockPart.getHeader("content-disposition")).thenReturn("form-data; name=\"dataFile\"; filename=\"text.txt\"");

        InputStream mockInputStream = Mockito.mock(InputStream.class);
        mockInputStream = new ByteArrayInputStream("test data".getBytes());
        Mockito.when(mockPart.getInputStream()).thenReturn(mockInputStream);

        HttpSession mockSession = Mockito.mock(HttpSession.class);
        Mockito.when(mockRequest.getSession()).thenReturn(mockSession);

        uploadServlet.doPost(mockRequest, mockResponse);
        Mockito.verify(mockRequest).setAttribute("error", "Incorrect data format.");
        Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
    }

    @Test
    public void testDoPost_CorrectFormat() throws IOException, ServletException {
        Mockito.when(mockRequest.getPart("file")).thenReturn(mockPart);
        Mockito.when(mockPart.getContentType()).thenReturn("text/plain");
        Mockito.when(mockPart.getHeader("content-disposition")).thenReturn("form-data; name=\"dataFile\"; filename=\"text.txt\"");

        InputStream mockInputStream = Mockito.mock(InputStream.class);
        mockInputStream = new ByteArrayInputStream("00.00 00.01\tNAME:\tMessage".getBytes());
        Mockito.when(mockPart.getInputStream()).thenReturn(mockInputStream);

        HttpSession mockSession = Mockito.mock(HttpSession.class);
        Mockito.when(mockRequest.getSession()).thenReturn(mockSession);

        uploadServlet.doPost(mockRequest, mockResponse);

        UserStore mockUserStore = Mockito.mock(UserStore.class);
        ConversationStore mockConversationStore = Mockito.mock(ConversationStore.class);
        MessageStore mockMessageStore = Mockito.mock(MessageStore.class);

        uploadServlet.setUserStore(mockUserStore);
        uploadServlet.setConversationStore(mockConversationStore);
        uploadServlet.setMessageStore(mockMessageStore);

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);

        Mockito.verify(mockUserStore).addUser(userArgumentCaptor.capture());
        Mockito.verify(mockConversationStore).addConversation(Mockito.any(Conversation.class));
        Mockito.verify(mockMessageStore).addMessage(Mockito.any(Message.class));

        Mockito.verify(mockResponse).sendRedirect("/chat/text");
    }


}
