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
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import codeu.model.data.Conversation;
import codeu.model.data.Message;
import codeu.model.data.User;
import codeu.model.store.basic.ConversationStore;
import codeu.model.store.basic.MessageStore;
import codeu.model.store.basic.UserStore;

import codeu.controller.AdminPageServlet;

public class AdminServletTest {

    private AdminPageServlet adminPageServlet;
    private HttpServletRequest mockRequest;
    private RequestDispatcher mockRequestDispatcher;
    private HttpServletResponse mockResponse;
    private ConversationStore mockConversationStore;
    private MessageStore mockMessageStore;
    private UserStore mockUserStore;

    @Before
    public void setup() throws IOException {
        adminPageServlet = new AdminPageServlet();
        mockRequest = Mockito.mock(HttpServletRequest.class);
        mockRequestDispatcher = Mockito.mock(RequestDispatcher.class);
        mockResponse = Mockito.mock(HttpServletResponse.class);
        Mockito.when(mockRequest.getRequestDispatcher("/WEB-INF/view/adminpage.jsp"))
            .thenReturn(mockRequestDispatcher);

        mockConversationStore = Mockito.mock(ConversationStore.class);
        adminPageServlet.setConversationStore(mockConversationStore);
    
        mockMessageStore = Mockito.mock(MessageStore.class);
        adminPageServlet.setMessageStore(mockMessageStore);
    
        mockUserStore = Mockito.mock(UserStore.class);
        adminPageServlet.setUserStore(mockUserStore);

    }

    @Test
    public void testDoGet() throws IOException, ServletException {

        // int conversationSize;
        adminPageServlet.doGet(mockRequest, mockResponse);

        Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);

    }
}