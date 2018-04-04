package codeu.controller;

/**
 * Servlet class responsible for uploading data.
 */

import codeu.model.data.Conversation;
import codeu.model.data.Message;
import codeu.model.data.User;
import codeu.model.store.persistence.PersistentStorageAgent;

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

        if (filePart == null) {
            request.setAttribute("error", "No file uploaded.");
            request.getRequestDispatcher("/WEB-INF/view/adminpage.jsp").forward(request, response);
            return;
        }

        String fileName = getFileName(filePart);
        InputStream filecontent = null;
        final PrintWriter writer = response.getWriter();

        try {
            // creates buffer reader
            filecontent = filePart.getInputStream();
            DataInputStream in = new DataInputStream(filecontent);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            // initializes storage agent to save conversations, users, and messages
            PersistentStorageAgent storeAgent = PersistentStorageAgent.getInstance();
            HashMap<String, User> users = new HashMap<>();


            String line;
            Conversation convo = null;
            Instant convoTime = Instant.now();
            User currUser = null;

            while ((line = br.readLine()) != null) {
                // splits line read into time offset from conversation start, speaker, and message
                String arr[] = line.split("\\t", 3);
                if (arr.length < 3) {
                    continue;
                }

                long timeOffset = (long) Double.parseDouble(arr[0].substring(0, arr[0].indexOf(" "))) * 1000;
                String name = (arr[1].charAt(0) == ' ') ? arr[1] : arr[1].substring(0, arr[1].indexOf(":"));
                String content = arr[2];

                // updates currUser (multiple messages in a row from the same user have "     " as name)
                if (name.charAt(0) != ' ') {
                    if (!users.containsKey(name)) {
                        currUser = new User(UUID.randomUUID(), name, "temppassword", convoTime.plusMillis(timeOffset));
                        users.put(name, currUser);
                        storeAgent.writeThrough(currUser);
                    } else {
                        currUser = users.get(name);
                    }
                }

                // stores conversation if reading first line
                if (convo == null) {
                    convo = new Conversation(UUID.randomUUID(), currUser.getId(), fileName, convoTime);
                    storeAgent.writeThrough(convo);
                }

                Message message = new Message(UUID.randomUUID(), convo.getId(), currUser.getId(), content, convoTime.plusMillis(timeOffset));
                storeAgent.writeThrough(message);
            }
        } catch (NumberFormatException nfe) {
            request.setAttribute("error", "Incorrect file format.");
            request.getRequestDispatcher("/WEB-INF/view/adminpage.jsp").forward(request, response);
            return;
        } finally {
            if (filecontent != null) {
                filecontent.close();
            }
            if (writer != null) {
                writer.close();
            }

        }
    }

    private String getFileName(final Part part) {
        for (String content : part.getHeader("content-disposition").split(";")) {
            if (content.trim().startsWith("filename")) {
                return content.substring(
                        content.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }
}
