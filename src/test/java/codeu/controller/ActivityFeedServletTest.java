package codeu.controller;

import codeu.model.data.User;
import codeu.model.data.Message;
import codeu.model.data.Conversation;
import codeu.model.store.persistence.PersistentDataStoreException;
import codeu.model.store.persistence.PersistentStorageAgent;
import org.apache.tools.ant.taskdefs.condition.Http;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;

import java.time.Instant;
import java.util.*;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.*;

public class ActivityFeedServletTest {

    private ActivityFeedServlet activityFeedServlet;
    private HttpServletRequest mockRequest;
    private RequestDispatcher mockRequestDispatcher;
    private HttpServletResponse mockResponse;

    @Before
    public void setup() throws IOException {
        activityFeedServlet = new ActivityFeedServlet();
        mockRequest = Mockito.mock(HttpServletRequest.class);
        mockRequestDispatcher = Mockito.mock(RequestDispatcher.class);
        mockResponse = Mockito.mock(HttpServletResponse.class);
        Mockito.when(mockRequest.getRequestDispatcher("/WEB-INF/view/activityfeed.jsp"))
            .thenReturn(mockRequestDispatcher);
    }

    @Test
    public void testDoGet_NullUser() throws IOException, ServletException {
        HttpSession mockSession = Mockito.mock(HttpSession.class);

        Mockito.when(mockRequest.getSession()).thenReturn(mockSession);
        Mockito.when(mockSession.getAttribute("user")).thenReturn(null);

        activityFeedServlet.doGet(mockRequest, mockResponse);

        Mockito.verify(mockResponse, Mockito.times(1)).sendRedirect("/login");
    }

    /**
     *  tests 3 different input types (3 with same time)
     */
    @Test
    public void testDoGet_BaseCase3DifferentInputs3SameTime() throws IOException, ServletException {
        PersistentStorageAgent mockStorage = Mockito.mock(PersistentStorageAgent.class);
        activityFeedServlet.setPersistentStorageAgent(mockStorage);

        UUID uuidUser = UUID.randomUUID();
        UUID uuidConversation = UUID.randomUUID();

        //mock objects to use
        Conversation mockConversation = Mockito.mock(Conversation.class);
        Message mockMessage = Mockito.mock(Message.class);
        User mockUser = Mockito.mock(User.class);

        HttpSession mockSession = Mockito.mock(HttpSession.class);
        Mockito.when(mockRequest.getSession()).thenReturn(mockSession);
        Mockito.when(mockSession.getAttribute("user")).thenReturn("username");

        //setting up lists for traversal
        List<Conversation> permConversationList = new ArrayList<>();
        permConversationList.add(mockConversation);

        List<Conversation> conversationList = new ArrayList<>();
        conversationList.add(mockConversation);

        List<Message> messageList = new ArrayList<>();
        messageList.add(mockMessage);

        List<User> permUserList = new ArrayList<>();
        permUserList.add(mockUser);

        List<User> userList = new ArrayList<>();
        userList.add(mockUser);

        //setting up the data to use
        try {
            Mockito.when(mockStorage.loadConversations()).thenReturn(permConversationList, conversationList);
            Mockito.when(mockStorage.loadMessages()).thenReturn(messageList);
            Mockito.when(mockStorage.loadUsers()).thenReturn(permUserList, userList);
        }
        catch (PersistentDataStoreException e) {
            throw new IOException(e);
        }
        Instant userTime = Instant.now();
        Instant conversationTime = userTime;
        Instant messageTime = userTime;

        //to check if the updates are in the correct order
        List<String> checkUpdates = new ArrayList<>();
        checkUpdates.add(conversationTime.toString() + ": user name created a new conversation: conversation title");
        checkUpdates.add(userTime.toString() + ": user name joined!");
        checkUpdates.add(messageTime.toString() + ": user name sent a message to conversation title: message content");

        //mockConversation attributes
        Mockito.when(mockConversation.getCreationTime()).thenReturn(conversationTime);
        Mockito.when(mockConversation.getTitle()).thenReturn("conversation title");
        Mockito.when(mockConversation.getId()).thenReturn(uuidConversation);
        Mockito.when(mockConversation.getOwnerId()).thenReturn(uuidUser);

        //mockMessage attributes
        Mockito.when(mockMessage.getCreationTime()).thenReturn(messageTime);
        Mockito.when(mockMessage.getAuthorId()).thenReturn(uuidUser);
        Mockito.when(mockMessage.getConversationId()).thenReturn(uuidConversation);
        Mockito.when(mockMessage.getContent()).thenReturn("message content");

        //mockUser attributes
        Mockito.when(mockUser.getCreationTime()).thenReturn(userTime);
        Mockito.when(mockUser.getId()).thenReturn(uuidUser);
        Mockito.when(mockUser.getName()).thenReturn("user name");

        //setting up a way to retrive activity attribute from mock request
        final Map<String, Object> attributes = new ConcurrentHashMap<String, Object>();
        // Mock setAttribute
        Mockito.doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                String key = invocation.getArgument(0);
                Object value = invocation.getArgument(1);
                attributes.put(key, value);
                System.out.println("put attribute key="+key+", value="+value);
                return null;
            }
        }).when(mockRequest).setAttribute(Mockito.anyString(), Mockito.any());

        // Mock getAttribute
        Mockito.doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                String key = invocation.getArgument(0);
                Object value = attributes.get(key);
                System.out.println("get attribute value for key="+key+" : "+value);
                return value;
            }
        }).when(mockRequest).getAttribute(Mockito.anyString());


        activityFeedServlet.doGet(mockRequest, mockResponse);

        Mockito.verify(mockRequest, Mockito.atLeastOnce()).setAttribute(eq("activity"), any(List.class));
        List<String> updates = (List<String>) mockRequest.getAttribute("activity");
        Assert.assertEquals(checkUpdates, updates);
        Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
    }

    /**
     *  tests 3 different input types (2 with same time 1 with different time)
     */
    @Test
    public void testDoGet_BaseCase3DifferentInputs2SameTime() throws IOException, ServletException {
        PersistentStorageAgent mockStorage = Mockito.mock(PersistentStorageAgent.class);
        activityFeedServlet.setPersistentStorageAgent(mockStorage);

        UUID uuidUser = UUID.randomUUID();
        UUID uuidConversation = UUID.randomUUID();

        //mock objects to use
        Conversation mockConversation = Mockito.mock(Conversation.class);
        Message mockMessage = Mockito.mock(Message.class);
        User mockUser = Mockito.mock(User.class);

        HttpSession mockSession = Mockito.mock(HttpSession.class);
        Mockito.when(mockRequest.getSession()).thenReturn(mockSession);
        Mockito.when(mockSession.getAttribute("user")).thenReturn("username");

        //setting up lists for traversal
        List<Conversation> permConversationList = new ArrayList<>();
        permConversationList.add(mockConversation);

        List<Conversation> conversationList = new ArrayList<>();
        conversationList.add(mockConversation);

        List<Message> messageList = new ArrayList<>();
        messageList.add(mockMessage);

        List<User> userList = new ArrayList<>();
        userList.add(mockUser);

        //setting up the data to use
        try {
            Mockito.when(mockStorage.loadConversations()).thenReturn(permConversationList, conversationList);
            Mockito.when(mockStorage.loadMessages()).thenReturn(messageList);
            Mockito.when(mockStorage.loadUsers()).thenReturn(userList);
        }
        catch (PersistentDataStoreException e) {
            throw new IOException(e);
        }
        Instant userTime = Instant.now();
        Instant conversationTime = userTime.plusSeconds(5);
        Instant messageTime = userTime.plusSeconds(5);

        //to check if the updates are in the correct order
        List<String> checkUpdates = new ArrayList<>();
        checkUpdates.add(conversationTime.toString() + ": user name created a new conversation: conversation title");
        checkUpdates.add(messageTime.toString() + ": user name sent a message to conversation title: message content");
        checkUpdates.add(userTime.toString() + ": user name joined!");

        //mockConversation attributes
        Mockito.when(mockConversation.getCreationTime()).thenReturn(conversationTime);
        Mockito.when(mockConversation.getTitle()).thenReturn("conversation title");
        Mockito.when(mockConversation.getId()).thenReturn(uuidConversation);
        Mockito.when(mockConversation.getOwnerId()).thenReturn(uuidUser);

        //mockMessage attributes
        Mockito.when(mockMessage.getCreationTime()).thenReturn(messageTime);
        Mockito.when(mockMessage.getAuthorId()).thenReturn(uuidUser);
        Mockito.when(mockMessage.getConversationId()).thenReturn(uuidConversation);
        Mockito.when(mockMessage.getContent()).thenReturn("message content");

        //mockUser attributes
        Mockito.when(mockUser.getCreationTime()).thenReturn(userTime);
        Mockito.when(mockUser.getId()).thenReturn(uuidUser);
        Mockito.when(mockUser.getName()).thenReturn("user name");

        //setting up a way to retrive activity attribute from mock request
        final Map<String, Object> attributes = new ConcurrentHashMap<String, Object>();
        // Mock setAttribute
        Mockito.doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                String key = invocation.getArgument(0);
                Object value = invocation.getArgument(1);
                attributes.put(key, value);
                System.out.println("put attribute key="+key+", value="+value);
                return null;
            }
        }).when(mockRequest).setAttribute(Mockito.anyString(), Mockito.any());

        // Mock getAttribute
        Mockito.doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                String key = invocation.getArgument(0);
                Object value = attributes.get(key);
                System.out.println("get attribute value for key="+key+" : "+value);
                return value;
            }
        }).when(mockRequest).getAttribute(Mockito.anyString());


        activityFeedServlet.doGet(mockRequest, mockResponse);

        Mockito.verify(mockRequest, Mockito.atLeastOnce()).setAttribute(eq("activity"), any(List.class));
        List<String> updates = (List<String>) mockRequest.getAttribute("activity");
        Assert.assertEquals(checkUpdates, updates);
        Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
    }

    /**
     *  tests 3 different input types (each with different time)
     */
    @Test
    public void testDoGet_BaseCase3DifferentInputs0SameTime() throws IOException, ServletException {
        PersistentStorageAgent mockStorage = Mockito.mock(PersistentStorageAgent.class);
        activityFeedServlet.setPersistentStorageAgent(mockStorage);

        UUID uuidUser = UUID.randomUUID();
        UUID uuidConversation = UUID.randomUUID();

        //mock objects to use
        Conversation mockConversation = Mockito.mock(Conversation.class);
        Message mockMessage = Mockito.mock(Message.class);
        User mockUser = Mockito.mock(User.class);

        HttpSession mockSession = Mockito.mock(HttpSession.class);
        Mockito.when(mockRequest.getSession()).thenReturn(mockSession);
        Mockito.when(mockSession.getAttribute("user")).thenReturn("username");

        //setting up lists for traversal
        List<Conversation> conversationList = new ArrayList<>();
        conversationList.add(mockConversation);

        List<Message> messageList = new ArrayList<>();
        messageList.add(mockMessage);

        List<User> userList = new ArrayList<>();
        userList.add(mockUser);

        //setting up the data to use
        try {
            Mockito.when(mockStorage.loadConversations()).thenReturn(conversationList);
            Mockito.when(mockStorage.loadMessages()).thenReturn(messageList);
            Mockito.when(mockStorage.loadUsers()).thenReturn(userList);
        }
        catch (PersistentDataStoreException e) {
            throw new IOException(e);
        }
        Instant userTime = Instant.now();
        Instant conversationTime = userTime.plusSeconds(5);
        Instant messageTime = userTime.plusSeconds(10);

        //to check if the updates are in the correct order
        List<String> checkUpdates = new ArrayList<>();
        checkUpdates.add(messageTime.toString() + ": user name sent a message to conversation title: message content");
        checkUpdates.add(conversationTime.toString() + ": user name created a new conversation: conversation title");
        checkUpdates.add(userTime.toString() + ": user name joined!");

        //mockConversation attributes
        Mockito.when(mockConversation.getCreationTime()).thenReturn(conversationTime);
        Mockito.when(mockConversation.getTitle()).thenReturn("conversation title");
        Mockito.when(mockConversation.getId()).thenReturn(uuidConversation);
        Mockito.when(mockConversation.getOwnerId()).thenReturn(uuidUser);

        //mockMessage attributes
        Mockito.when(mockMessage.getCreationTime()).thenReturn(messageTime);
        Mockito.when(mockMessage.getAuthorId()).thenReturn(uuidUser);
        Mockito.when(mockMessage.getConversationId()).thenReturn(uuidConversation);
        Mockito.when(mockMessage.getContent()).thenReturn("message content");

        //mockUser attributes
        Mockito.when(mockUser.getCreationTime()).thenReturn(userTime);
        Mockito.when(mockUser.getId()).thenReturn(uuidUser);
        Mockito.when(mockUser.getName()).thenReturn("user name");

        //setting up a way to retrive activity attribute from mock request
        final Map<String, Object> attributes = new ConcurrentHashMap<String, Object>();
        // Mock setAttribute
        Mockito.doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                String key = invocation.getArgument(0);
                Object value = invocation.getArgument(1);
                attributes.put(key, value);
                System.out.println("put attribute key="+key+", value="+value);
                return null;
            }
        }).when(mockRequest).setAttribute(Mockito.anyString(), Mockito.any());

        // Mock getAttribute
        Mockito.doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                String key = invocation.getArgument(0);
                Object value = attributes.get(key);
                System.out.println("get attribute value for key="+key+" : "+value);
                return value;
            }
        }).when(mockRequest).getAttribute(Mockito.anyString());


        activityFeedServlet.doGet(mockRequest, mockResponse);

        Mockito.verify(mockRequest, Mockito.atLeastOnce()).setAttribute(eq("activity"), any(List.class));
        List<String> updates = (List<String>) mockRequest.getAttribute("activity");
        Assert.assertEquals(checkUpdates, updates);
        Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
    }

    /**
     *  tests 3 inputs, 2 of the user type and 1 of conversation type (each with different time)
     */
    @Test
    public void testDoGet_BaseCase3Inputs2Same1Different() throws IOException, ServletException {
        PersistentStorageAgent mockStorage = Mockito.mock(PersistentStorageAgent.class);
        activityFeedServlet.setPersistentStorageAgent(mockStorage);

        UUID uuidUser1 = UUID.randomUUID();
        UUID uuidUser2 = UUID.randomUUID();
        UUID uuidConversation = UUID.randomUUID();

        //mock objects to use
        Conversation mockConversation = Mockito.mock(Conversation.class);
        User mockUser1 = Mockito.mock(User.class);
        User mockUser2 = Mockito.mock(User.class);

        HttpSession mockSession = Mockito.mock(HttpSession.class);
        Mockito.when(mockRequest.getSession()).thenReturn(mockSession);
        Mockito.when(mockSession.getAttribute("user")).thenReturn("username");

        //setting up lists for traversal
        List<Conversation> conversationList = new ArrayList<>();
        conversationList.add(mockConversation);

        List<Message> messageList = new ArrayList<>();

        List<User> userList = new ArrayList<>();
        userList.add(mockUser1);
        userList.add(mockUser2);

        //setting up the data to use
        try {
            Mockito.when(mockStorage.loadConversations()).thenReturn(conversationList);
            Mockito.when(mockStorage.loadMessages()).thenReturn(messageList);
            Mockito.when(mockStorage.loadUsers()).thenReturn(userList);
        }
        catch (PersistentDataStoreException e) {
            throw new IOException(e);
        }
        Instant userTime1 = Instant.now();
        Instant userTime2 = userTime1.plusSeconds(5);
        Instant conversationTime = userTime1.plusSeconds(10);

        //to check if the updates are in the correct order
        List<String> checkUpdates = new ArrayList<>();
        checkUpdates.add(conversationTime.toString() + ": user name 1 created a new conversation: conversation title");
        checkUpdates.add(userTime2.toString() + ": user name 2 joined!");
        checkUpdates.add(userTime1.toString() + ": user name 1 joined!");

        //mockConversation attributes
        Mockito.when(mockConversation.getCreationTime()).thenReturn(conversationTime);
        Mockito.when(mockConversation.getTitle()).thenReturn("conversation title");
        Mockito.when(mockConversation.getId()).thenReturn(uuidConversation);
        Mockito.when(mockConversation.getOwnerId()).thenReturn(uuidUser1);

        //mockUser1 attributes
        Mockito.when(mockUser1.getCreationTime()).thenReturn(userTime1);
        Mockito.when(mockUser1.getId()).thenReturn(uuidUser1);
        Mockito.when(mockUser1.getName()).thenReturn("user name 1");


        //mockUser2 attributes
        Mockito.when(mockUser2.getCreationTime()).thenReturn(userTime2);
        Mockito.when(mockUser2.getId()).thenReturn(uuidUser2);
        Mockito.when(mockUser2.getName()).thenReturn("user name 2");

        //setting up a way to retrive activity attribute from mock request
        final Map<String, Object> attributes = new ConcurrentHashMap<String, Object>();
        // Mock setAttribute
        Mockito.doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                String key = invocation.getArgument(0);
                Object value = invocation.getArgument(1);
                attributes.put(key, value);
                System.out.println("put attribute key="+key+", value="+value);
                return null;
            }
        }).when(mockRequest).setAttribute(Mockito.anyString(), Mockito.any());

        // Mock getAttribute
        Mockito.doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                String key = invocation.getArgument(0);
                Object value = attributes.get(key);
                System.out.println("get attribute value for key="+key+" : "+value);
                return value;
            }
        }).when(mockRequest).getAttribute(Mockito.anyString());


        activityFeedServlet.doGet(mockRequest, mockResponse);

        Mockito.verify(mockRequest, Mockito.atLeastOnce()).setAttribute(eq("activity"), any(List.class));
        List<String> updates = (List<String>) mockRequest.getAttribute("activity");
        Assert.assertEquals(checkUpdates, updates);
        Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
    }

    /**
     *  tests 3 same type inputs (each with different time)
     */
    @Test
    public void testDoGet_BaseCase3SameInputs() throws IOException, ServletException {
        PersistentStorageAgent mockStorage = Mockito.mock(PersistentStorageAgent.class);
        activityFeedServlet.setPersistentStorageAgent(mockStorage);

        UUID uuidUser1 = UUID.randomUUID();
        UUID uuidUser2 = UUID.randomUUID();
        UUID uuidUser3 = UUID.randomUUID();

        //mock objects to use
        User mockUser1 = Mockito.mock(User.class);
        User mockUser2 = Mockito.mock(User.class);
        User mockUser3 = Mockito.mock(User.class);

        HttpSession mockSession = Mockito.mock(HttpSession.class);
        Mockito.when(mockRequest.getSession()).thenReturn(mockSession);
        Mockito.when(mockSession.getAttribute("user")).thenReturn("username");

        //setting up lists for traversal
        List<Conversation> conversationList = new ArrayList<>();

        List<Message> messageList = new ArrayList<>();

        List<User> userList = new ArrayList<>();
        userList.add(mockUser1);
        userList.add(mockUser2);
        userList.add(mockUser3);

        //setting up the data to use
        try {
            Mockito.when(mockStorage.loadConversations()).thenReturn(conversationList);
            Mockito.when(mockStorage.loadMessages()).thenReturn(messageList);
            Mockito.when(mockStorage.loadUsers()).thenReturn(userList);
        }
        catch (PersistentDataStoreException e) {
            throw new IOException(e);
        }
        Instant userTime1 = Instant.now();
        Instant userTime2 = userTime1.plusSeconds(5);
        Instant userTime3 = userTime1.plusSeconds(10);

        //to check if the updates are in the correct order
        List<String> checkUpdates = new ArrayList<>();
        checkUpdates.add(userTime3.toString() + ": user name 3 joined!");
        checkUpdates.add(userTime2.toString() + ": user name 2 joined!");
        checkUpdates.add(userTime1.toString() + ": user name 1 joined!");

        //mockUser1 attributes
        Mockito.when(mockUser1.getCreationTime()).thenReturn(userTime1);
        Mockito.when(mockUser1.getId()).thenReturn(uuidUser1);
        Mockito.when(mockUser1.getName()).thenReturn("user name 1");

        //mockUser2 attributes
        Mockito.when(mockUser2.getCreationTime()).thenReturn(userTime2);
        Mockito.when(mockUser2.getId()).thenReturn(uuidUser2);
        Mockito.when(mockUser2.getName()).thenReturn("user name 2");

        //mockUser3 attributes
        Mockito.when(mockUser3.getCreationTime()).thenReturn(userTime3);
        Mockito.when(mockUser3.getId()).thenReturn(uuidUser3);
        Mockito.when(mockUser3.getName()).thenReturn("user name 3");

        //setting up a way to retrive activity attribute from mock request
        final Map<String, Object> attributes = new ConcurrentHashMap<String, Object>();
        // Mock setAttribute
        Mockito.doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                String key = invocation.getArgument(0);
                Object value = invocation.getArgument(1);
                attributes.put(key, value);
                System.out.println("put attribute key="+key+", value="+value);
                return null;
            }
        }).when(mockRequest).setAttribute(Mockito.anyString(), Mockito.any());

        // Mock getAttribute
        Mockito.doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                String key = invocation.getArgument(0);
                Object value = attributes.get(key);
                System.out.println("get attribute value for key="+key+" : "+value);
                return value;
            }
        }).when(mockRequest).getAttribute(Mockito.anyString());


        activityFeedServlet.doGet(mockRequest, mockResponse);

        Mockito.verify(mockRequest, Mockito.atLeastOnce()).setAttribute(eq("activity"), any(List.class));
        List<String> updates = (List<String>) mockRequest.getAttribute("activity");
        Assert.assertEquals(checkUpdates, updates);
        Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
    }

    @Test
    public void testEmptyCheck_Empty() {
        ListIterator<Conversation> mockConversationIte = Mockito.mock(ListIterator.class);
        ListIterator<Message> mockMessageIte = Mockito.mock(ListIterator.class);
        ListIterator<User> mockUserIte = Mockito.mock(ListIterator.class);

        Mockito.when(mockConversationIte.hasPrevious()).thenReturn(false);
        Mockito.when(mockMessageIte.hasPrevious()).thenReturn(false);
        Mockito.when(mockUserIte.hasPrevious()).thenReturn(false);

        assertFalse(activityFeedServlet.emptyCheck(mockConversationIte,mockMessageIte,mockUserIte));


    }
    @Test
    public void testEmptyCheck_NotEmpty() {
        ListIterator<Conversation> mockConversationIte = Mockito.mock(ListIterator.class);
        ListIterator<Message> mockMessageIte = Mockito.mock(ListIterator.class);
        ListIterator<User> mockUserIte = Mockito.mock(ListIterator.class);

        Mockito.when(mockConversationIte.hasPrevious()).thenReturn(true);
        Mockito.when(mockMessageIte.hasPrevious()).thenReturn(true);
        Mockito.when(mockUserIte.hasPrevious()).thenReturn(true);

        assertTrue(activityFeedServlet.emptyCheck(mockConversationIte,mockMessageIte,mockUserIte));
    }

    @Test
    public void testCompareInputs_OneInputs() {
        Conversation mockConversation = Mockito.mock(Conversation.class);
        Instant time = Instant.now();
        Mockito.when(mockConversation.getCreationTime()).thenReturn(time);

        assertEquals(1, activityFeedServlet.compareInputs(mockConversation, null, null ));

    }

    @Test
    public void testCompareInputs_TwoInputs() {
        Conversation mockConversation = Mockito.mock(Conversation.class);
        Message mockMessage = Mockito.mock(Message.class);

        Instant timeC = Instant.now();
        Instant timeM = timeC.plusSeconds(10);

        Mockito.when(mockConversation.getCreationTime()).thenReturn(timeC);
        Mockito.when(mockMessage.getCreationTime()).thenReturn(timeM);

        assertEquals(2, activityFeedServlet.compareInputs(mockConversation, mockMessage, null ));

    }

    @Test
    public void testCompareInputs_ThreeInputs() {
        Conversation mockConversation = Mockito.mock(Conversation.class);
        Message mockMessage = Mockito.mock(Message.class);
        User mockUser = Mockito.mock(User.class);

        Instant timeC = Instant.now();
        Instant timeM = timeC.plusSeconds(10);
        Instant timeU = timeC.plusSeconds(20);

        Mockito.when(mockConversation.getCreationTime()).thenReturn(timeC);
        Mockito.when(mockMessage.getCreationTime()).thenReturn(timeM);
        Mockito.when(mockUser.getCreationTime()).thenReturn(timeU);


        assertEquals(3, activityFeedServlet.compareInputs(mockConversation, mockMessage, mockUser));
    }

    @Test
    public void findUser_UserExist() {
        User mockUser = Mockito.mock(User.class);
        List<User> userList = Arrays.asList(mockUser);
        UUID userID = UUID.randomUUID();

        Mockito.when(mockUser.getId()).thenReturn(userID);

        assertEquals(mockUser, activityFeedServlet.findUser(userList, userID));
    }

    @Test
    public void findUser_UserDoesNotExist() {
        User mockUser = Mockito.mock(User.class);
        List<User> userList = Arrays.asList(mockUser);
        UUID userID = UUID.randomUUID();

        Mockito.when(mockUser.getId()).thenReturn(userID);

        assertNull(activityFeedServlet.findUser(userList, UUID.randomUUID()));
    }

    @Test
    public void findConversation_ConversationExist() {
        Conversation mockConversation = Mockito.mock(Conversation.class);
        List<Conversation> conversationList = Arrays.asList(mockConversation);
        UUID conversationID = UUID.randomUUID();

        Mockito.when(mockConversation.getId()).thenReturn(conversationID);
        Mockito.when(mockConversation.getTitle()).thenReturn("test");

        assertEquals("test", activityFeedServlet.findConversation(conversationList, conversationID));
    }

    @Test
    public void findConversation_ConversationDoesNotExist() {
        Conversation mockConversation = Mockito.mock(Conversation.class);
        List<Conversation> conversationList = Arrays.asList(mockConversation);
        UUID conversationID = UUID.randomUUID();

        Mockito.when(mockConversation.getId()).thenReturn(conversationID);
        Mockito.when(mockConversation.getTitle()).thenReturn("test");

        assertNull(activityFeedServlet.findConversation(conversationList, UUID.randomUUID()));
    }
}