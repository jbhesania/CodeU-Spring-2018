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

  <jsp:include page="/WEB-INF/nav.jsp" />

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
