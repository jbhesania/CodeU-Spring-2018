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

    <nav>
      <a id="navTitle" href="/">CodeU Chat App</a>
      <a href="/conversations">Conversations</a>
      <% if(request.getSession().getAttribute("user") != null){ %>
        <a>Hello <%= request.getSession().getAttribute("user") %>!</a>
      <% } else{ %>
        <a href="/login">Login</a>
        <a href="/register">Register</a>
      <% } %>
      <a href="/about.jsp">About</a>
      <a href="/adminpage.jsp">Admin Page</a>
    </nav>

    <div id="container">
        <h1>Administration</h1>
        <hr>
        <h2>Statistics</h2>
        <p>Here are some site statistics</p>
        <ul>
            <li>Users:</li>
            <li>Conversations:</li>
            <li>Messages:</li>
            <li>Most active user:</li>
            <li>Newest user:</li>
        </ul>

        <h2>Import Data</h2>
        <form action = "UploadServlet" method = "post" enctype = "multipart/form-data">
            File:
            <input type="file" name="file" id="file" /> <br/>
            <input type="submit" value="Upload" name="upload" id="upload" />
        </form>
    </div>
  </body>
</html> 
