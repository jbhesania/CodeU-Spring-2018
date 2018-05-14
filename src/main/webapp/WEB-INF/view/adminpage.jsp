<!DOCTYPE html>
<html>
  <head>
  <title>Admin Page</title>
  <link rel="stylesheet" href="/css/main.css">
  <style>
    label {
      display: inline-block;
      width: 100px;
    }
  </style>
  </head>
  <body>

    <jsp:include page="/WEB-INF/nav.jsp" />

    <div id="container">
        <h1>Administration</h1>
        <hr>
    <% if (request.getSession().getAttribute("admin") != null && (boolean) request.getSession().getAttribute("admin")) { %>
            <h2>Statistics</h2>
            <p>Here are some site statistics</p>
            <ul>
                <li>Users: ${userSize}</li>
                <li>Conversations: ${conversationSize}</li>
                <li>Messages: ${messagesSize}</li>
                <li>Newest user: ${newestUser}</li>
            </ul>

            <h2>Import Data</h2>
            <% if(request.getAttribute("error") != null){ %>
                <h2 style="color:red"><%= request.getAttribute("error") %></h2>
            <% } %>
            <form action = "UploadServlet" method = "post" enctype = "multipart/form-data">
                File:
                <input type="file" name="file" id="file" /> <br/>
                <input type="submit" value="Upload" name="upload" id="upload" />
            </form>
        </div>
    <% } else { %>
        <p> You do not have the permissions to view this page. </p>
    <% } %>
  </body>
</html> 
