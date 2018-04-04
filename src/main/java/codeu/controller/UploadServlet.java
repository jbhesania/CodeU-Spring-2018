package codeu.controller;

/**
 * Servlet class responsible for uploading data.
 */

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;



@WebServlet("/UploadServlet")
@MultipartConfig(fileSizeThreshold=1024*1024*2, // 2MB
        maxFileSize=1024*1024*10,      // 10MB
        maxRequestSize=1024*1024*50)   // 50MB
public class UploadServlet extends HttpServlet {
    /**
     * Name of the directory where uploaded files will be saved, relative to
     * the web application directory.
     */
    private static final String UPLOAD_LOCATION_PROPERTY_KEY = "upload.location";
    private String uploadsDirName;
    private final static Logger LOGGER =
            Logger.getLogger(UploadServlet.class.getCanonicalName());

    /**
     * handles file upload
     */
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {

        // Create path components to save the file
//        final String path = System.getProperty("java.io.tmpdir");
        final Part filePart = request.getPart("file");
        final String fileName = getFileName(filePart);

        InputStream filecontent = null;
        final PrintWriter writer = response.getWriter();

        try {
            filecontent = filePart.getInputStream();

            DataInputStream in = new DataInputStream(filecontent);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;

            while ((line = br.readLine()) != null) {
                writer.println(line);
            }
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
        final String partHeader = part.getHeader("content-disposition");
        LOGGER.log(Level.INFO, "Part Header = {0}", partHeader);
        for (String content : part.getHeader("content-disposition").split(";")) {
            if (content.trim().startsWith("filename")) {
                return content.substring(
                        content.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }
}
