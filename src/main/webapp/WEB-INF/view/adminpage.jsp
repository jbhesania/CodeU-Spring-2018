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
            <li>Users: ${userSize}</li>
            <li>Conversations: ${conversationSize}</li>
            <li>Messages: ${messagesSize}</li>
            <li>Newest user: ${newestUser}</li>
        </ul>

    </div>
  </body>
</html> 
