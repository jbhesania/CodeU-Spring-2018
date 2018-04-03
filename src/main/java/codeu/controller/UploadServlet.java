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
        final String path = System.getProperty("java.io.tmpdir");
        final Part filePart = request.getPart("file");
        final String fileName = getFileName(filePart).replaceAll("[^A-Za-z0-9]", "");

        OutputStream out = null;
        InputStream filecontent = null;
        final PrintWriter writer = response.getWriter();

        try {
            out = new FileOutputStream(new File(path, "fileName"));
            filecontent = filePart.getInputStream();

            int read = 0;
            final byte[] bytes = new byte[1024];

            while ((read = filecontent.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            writer.println("New file " + fileName + " created at " + path);
            LOGGER.log(Level.INFO, "File{0}being uploaded to {1}",
                    new Object[]{fileName, path});
        } catch (FileNotFoundException fne) {
            writer.println("You either did not specify a file to upload or are "
                    + "trying to upload a file to a protected or nonexistent "
                    + "location.");
            writer.println("<br/> ERROR: " + fne.getMessage());

            LOGGER.log(Level.SEVERE, "Problems during file upload. Error: {0}",
                    new Object[]{fne.getMessage()});
        } finally {
            if (out != null) {
                out.close();
            }
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




        //        // gets absolute path of the web application
//        String appPath = request.getServletContext().getRealPath("");
//        // constructs path of the directory to save uploaded file
//        String savePath = appPath + File.separator + SAVE_DIR;

//        // creates the save directory if it does not exists
//        File fileSaveDir = new File(savePath);
//        if (!fileSaveDir.exists()) {
//            fileSaveDir.mkdir();
//        }

//        for (Part part : request.getParts()) {
//            String fileName = extractFileName(part);
//            // refines the fileName in case it is an absolute path
//            fileName = new File(fileName).getName();
//            fileName = fileName.replaceAll("[^A-Za-z0-9]", "-").trim();
//            part.write(savePath + File.separator + fileName);
//
//            FileInputStream in = null;
//            in = new FileInputStream(savePath + File.separator + fileName);
//
//            try {
////                in = new FileInputStream(savePath + File.separator + fileName);
//                BufferedReader br = new BufferedReader(new InputStreamReader(in));
//                String strLine;
//                while ((strLine = br.readLine()) != null) {
//                    System.out.println(strLine);
//                }
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            } finally {
//                try {
//                    in.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        request.setAttribute("message", "Upload has been done successfully!");
//        getServletContext().getRequestDispatcher("/upload.jsp").forward(
//                request, response);
//    }
    /**
     * Extracts file name from HTTP header content-disposition
     */
    private String extractFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        String[] items = contentDisp.split(";");
        for (String s : items) {
            if (s.trim().startsWith("filename")) {
                return s.substring(s.indexOf("=") + 2, s.length()-1);
            }
        }
        return "";
    }
}
