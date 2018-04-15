<!DOCTYPE html>
<html>
  <head>
  <title>Activity Feed</title>
  <link rel="stylesheet" href="/css/main.css">
  <%@ page import ="java.util.ArrayList"%>
  <%@ page import ="java.util.List"%>
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
      <a href="/activityfeed.jsp">Activity Feed</a>
      <a href="/adminpage.jsp">Admin Page</a>
    </nav>

    <div id="container">
        <h1> Activity Feed </h1>

            <% if(request.getAttribute("error") != null){ %>

                <h2 style="color:red"><%= request.getAttribute("error") %></h2>

            <% } %>

            <% List<String> updates = (List<String>) request.getAttribute("activity"); %>

            <% if(updates == null || updates.isEmpty()){ %>
                <p> No Updates :( </p>
            <% } %>


            <ul class="mdl-list">

                <% for (String event: updates) { %>
                    <% out.print(event); %> <br>

                <% } %>

            </ul>

    </div>
  </body>
</html> 
