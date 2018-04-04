package codeu.controller;

/**
 * Servlet class responsible for uploading data.
 */

import codeu.model.data.Conversation;
import codeu.model.data.Message;
import codeu.model.data.User;
import codeu.model.store.basic.ConversationStore;
import codeu.model.store.basic.MessageStore;
import codeu.model.store.basic.UserStore;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.time.Instant;
import java.util.HashMap;
import java.util.UUID;


@WebServlet("/UploadServlet")
@MultipartConfig(fileSizeThreshold=1024*1024*2, // 2MB
        maxFileSize=1024*1024*10,      // 10MB
        maxRequestSize=1024*1024*50)   // 50MB
public class UploadServlet extends HttpServlet {

    private ConversationStore conversationStore;
    private MessageStore messageStore;
    private UserStore userStore;

    private HashMap<String, User> users;

    public void init() throws ServletException {
        super.init();
        setConversationStore(ConversationStore.getInstance());
        setMessageStore(MessageStore.getInstance());
        setUserStore(UserStore.getInstance());
        users = new HashMap<>();
    }

    void setConversationStore(ConversationStore conversationStore) {
        this.conversationStore = conversationStore;
    }

    void setMessageStore(MessageStore messageStore) {
        this.messageStore = messageStore;
    }

    void setUserStore(UserStore userStore) {
        this.userStore = userStore;
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        request.getRequestDispatcher("/WEB-INF/view/adminpage.jsp").forward(request, response);
    }

    /**
     * handles file upload
     */
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {

        Part filePart = request.getPart("file");
        String fileName = getFileName(filePart);

        if (filePart == null || fileName == null) {
            request.setAttribute("error", "No file uploaded.");
            request.getRequestDispatcher("/WEB-INF/view/adminpage.jsp").forward(request, response);
            return;
        }

        // creates buffer reader
        InputStream filecontent = filePart.getInputStream();
        DataInputStream in = new DataInputStream(filecontent);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        String line;
        Conversation convo = null;
        Instant convoTime = Instant.now();
        User currUser = null;

        while ((line = br.readLine()) != null) {
            // splits line read into millisecond offset from conversation start, speaker, and message
            String arr[] = line.split("\\t", 3);

            // if line doesn't follow format, continue
            if (arr.length < 3 || !arr[0].contains(" ") || (arr[1].trim().length() > 0 && !arr[1].contains(":"))) {
                continue;
            }

            // if arr[1] not parse-able, continue
            try {
                long plusMillis = (long) Double.parseDouble(arr[0].substring(0, arr[0].indexOf(" "))) * 1000;
                String name = (arr[1].trim().length() == 0) ? arr[1] : arr[1].substring(0, arr[1].indexOf(":"));
                String cleanedMessageContent = Jsoup.clean(arr[2], Whitelist.none());

                // updates currUser if name is not only whitespace, creates new user if not in users
                if (arr[1].trim().length() > 0) {
                    if (!users.containsKey(name)) {
                        currUser = new User(
                                UUID.randomUUID(),
                                name,
                                "temppassword",
                                convoTime.plusMillis(plusMillis));
                        users.put(name, currUser);
                        userStore.addUser(currUser);
                    } else {
                        currUser = users.get(name);
                    }
                }

                // stores conversation if reading first line
                if (convo == null && currUser != null) {
                    convo = new Conversation(
                            UUID.randomUUID(),
                            currUser.getId(),
                            fileName,
                            convoTime);
                    conversationStore.addConversation(convo);
                }

                // adds message only if convo & user have been added
                if (convo != null && currUser != null) {
                    Message message = new Message(
                            UUID.randomUUID(),
                            convo.getId(),
                            currUser.getId(),
                            cleanedMessageContent,
                            convoTime.plusMillis(plusMillis));
                    messageStore.addMessage(message);
                }
            } catch (NumberFormatException nfe) {
                continue;
            }
        }

        br.close();
        filecontent.close();

        // if goes through file without storing convo, then it was formatted incorrectly
        if (convo == null) {
            request.setAttribute("error", "Incorrect file format.");
            request.getRequestDispatcher("/WEB-INF/view/adminpage.jsp").forward(request, response);
            return;
        } else {
            response.sendRedirect("/chat/" + fileName);
        }

    }

    private String getFileName(final Part part) {
        if (part == null) {
            return null;
        }

        for (String content : part.getHeader("content-disposition").split(";")) {
            if (content.trim().startsWith("filename")) {
                return content.substring(
                        content.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }
}
