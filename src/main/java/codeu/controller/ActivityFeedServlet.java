package codeu.controller;

import codeu.model.data.Conversation;
import codeu.model.data.Message;
import codeu.model.data.User;

import java.time.Instant;
import java.util.*;

import codeu.model.store.persistence.PersistentDataStoreException;
import codeu.model.store.persistence.PersistentStorageAgent;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet class responsible for the activity feed
 */
public class ActivityFeedServlet extends HttpServlet {

    private PersistentStorageAgent persistentStorageAgent;

    /**
     * These are comparator methods for sorting lists based on time
     */
    class sortTimeC implements Comparator<Conversation> {
        public int compare(Conversation c1, Conversation c2) {
            return c1.getCreationTime().compareTo(c2.getCreationTime());
        }
    }
    class sortTimeM implements Comparator<Message> {
        public int compare(Message m1, Message m2) {
            return m1.getCreationTime().compareTo(m2.getCreationTime());
        }
    }

    class sortTimeU implements Comparator<User> {
        public int compare(User u1, User u2) {
            return u1.getCreationTime().compareTo(u2.getCreationTime());
        }
    }
    /**
     * Called when first setting up the servlet and
     * will initialize the persistentStorageAgent
     */
    @Override
    public void init() throws ServletException {
        super.init();
        setPersistentStorageAgent(PersistentStorageAgent.getInstance());
    }

    /**
     * A helper method that is called in the init() method of this servlet that
     * sets the persistentStorageAgent that will be used.
     */
    void setPersistentStorageAgent(PersistentStorageAgent persistentStorageAgent) {
        this.persistentStorageAgent = persistentStorageAgent;
    }
/**
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        generateActivity(request, response);
        request.getRequestDispatcher("/WEB-INF/view/activityfeed.jsp").forward(request, response);
    }
*/
    /**
     * This method retrieves recent activity and stores it in a string arraylist
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException{
        String username = (String) request.getSession().getAttribute("user");
        if (username == null) {
            //user is not logged in, do not let them view activity feed
            response.sendRedirect("/login");
            return;
        }
        else {
            try {
                //initializing variables
                List<Conversation> permConversationList = persistentStorageAgent.loadConversations();
                List<User> permUserList = persistentStorageAgent.loadUsers();

                List<Conversation> conversationList = persistentStorageAgent.loadConversations();
                List<Message> messageList = persistentStorageAgent.loadMessages();
                List<User> userList = persistentStorageAgent.loadUsers();
                List<String> activityList = new ArrayList<>();

                //sorting arrays
                conversationList.sort(new sortTimeC());
                messageList.sort(new sortTimeM());
                userList.sort(new sortTimeU());

                //initializing iterators
                ListIterator<Conversation> conversationListIterator = conversationList.listIterator(conversationList.size());
                ListIterator<Message> messageListIterator = messageList.listIterator(messageList.size());
                ListIterator<User> userListIterator = userList.listIterator(userList.size());

                while (emptyCheck(conversationListIterator, messageListIterator, userListIterator) && activityList.size() < 50) {
                    Conversation conversation = null;
                    Message message = null;
                    User user = null;

                    int input;

                    if (conversationListIterator.hasPrevious()) {
                        conversation = conversationListIterator.previous();
                    }
                    if (messageListIterator.hasPrevious()) {
                        message = messageListIterator.previous();
                    }
                    if (userListIterator.hasPrevious()) {
                        user = userListIterator.previous();
                    }
                    input = compareInputs(conversation, message, user);
                    //depending on which is the earliest, add different strings to the activityList
                    String time = null;
                    String event = null;
                    if (input == 1) {
                        time = conversation.getCreationTime().toString() + ": ";
                        event = findUser(permUserList, conversation.getOwnerId()).getName() + " created a new conversation: "
                                + conversation.getTitle();

                        //reset not chosen
                        if (messageListIterator.hasNext()) {
                            messageListIterator.next();
                        }
                        if (userListIterator.hasNext()) {
                            userListIterator.next();
                        }

                        conversationListIterator.remove();
                    }
                    if (input == 2) {
                        time = message.getCreationTime().toString() + ": ";
                        event = findUser(permUserList, message.getAuthorId()).getName() + " sent a message to "
                                + findConversation(permConversationList, message.getConversationId()) + ": "
                                + message.getContent();

                        //reset not chosen
                        if (conversationListIterator.hasNext()) {
                            conversationListIterator.next();
                        }
                        if (userListIterator.hasNext()) {
                            userListIterator.next();
                        }

                        messageListIterator.remove();

                    }
                    if (input == 3) {
                        time = user.getCreationTime().toString() + ": ";
                        event = user.getName() + " joined!";

                        //reset not chosen
                        if (conversationListIterator.hasNext()) {
                            conversationListIterator.next();
                        }
                        if (messageListIterator.hasNext()) {
                            messageListIterator.next();
                        }

                        userListIterator.remove();
                    }

                    activityList.add(time + event);
                }
                request.setAttribute("activity", activityList);
                request.getRequestDispatcher("/WEB-INF/view/activityfeed.jsp").forward(request, response);
            }
            catch (PersistentDataStoreException e) {
                throw new IOException(e);
            }
        }
    }

    /**
     * This method checks to see if all three lists (conversation, message, and user) are empty returns true if not empty
     */
    public boolean emptyCheck(ListIterator<Conversation> conversationListIterator,
                               ListIterator<Message> messageListIterator,
                               ListIterator<User> userListIterator) {
        return (conversationListIterator.hasPrevious() || messageListIterator.hasPrevious() || userListIterator.hasPrevious());
    }

    /**
     * This method compares the time stamps of 3 inputs and returns parameter number of the most recent event
     * (1, 2, or 3)
     */
    public int compareInputs(Conversation conversation, Message message, User user) {
        //if only 1 valid input
        if (conversation == null && message == null) {
            return 3;
        }
        if (conversation == null && user == null) {
            return 2;
        }
        if (message == null && user == null) {
            return 1;
        }
        //if only 2 inputs to compare
        if (conversation == null) {
            if ((message.getCreationTime()).isAfter(user.getCreationTime())) {
                return 2;
            }
            else {
                return 3;
            }
        }
        if (message == null) {
            if ((conversation.getCreationTime()).isAfter(user.getCreationTime())) {
                return 1;
            }
            else {
                return 3;
            }
        }
        if (user == null) {
            if ((conversation.getCreationTime()).isAfter(message.getCreationTime())) {
                return 1;
            }
            else {
                return 2;
            }
        }
        //compare all 3 inputs
        Instant conversationTime = conversation.getCreationTime();
        Instant messageTime = message.getCreationTime();
        Instant userTime = user.getCreationTime();

        int currentMin = 0;

        ArrayList<Instant> timeList = new ArrayList<> ();
        timeList.add(conversationTime);
        timeList.add(messageTime);
        timeList.add(userTime);

        for (int i = 0; i < timeList.size(); i++) {
            if (timeList.get(currentMin).isBefore(timeList.get(i))) {
                currentMin = i;
            }
        }

        return currentMin + 1;

    }

    /**
     * This is a helper method to help find and retrieve the user with the UUID inputted
     */
    public User findUser (List<User> userList, UUID userID) {
        for (User user : userList) {
            if (user.getId().equals(userID)) {
                return user;
            }
        }
        return null;
    }
     /**
      * This is a helper method to help find and retrieve the conversation with the UUID inputted
      */
     public String findConversation (List<Conversation> conversationList, UUID conversationID) {
         for (Conversation conversation : conversationList) {
             if (conversation.getId().equals(conversationID)) {
                 return conversation.getTitle();
             }
         }
         return null;
     }


}