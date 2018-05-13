<%--
  Copyright 2017 Google Inc.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
--%>
<%@ page import="java.util.List" %>
<%@ page import="codeu.model.data.Conversation" %>

<!DOCTYPE html>
<html>
<head>
  <title>Conversations</title>
  <link rel="stylesheet" href="/css/main.css">
<<<<<<< HEAD
<<<<<<< HEAD
  <style>
    form{
      margin: 10px 0px 10px 0px;
    }
    button{
      margin: 10px 0px 10px 0px;
    }
    input{
      margin: 5px 0px 5px 0px;
    }
  </style>
=======
>>>>>>> parent of 66ae662... form
=======
>>>>>>> 0fb2eb3edfde506b0f3faf3d2cac070608d78e76
</head>
<body>

  <jsp:include page="/WEB-INF/nav.jsp" />

  <div id="container">

    <% if(request.getAttribute("error") != null){ %>
        <h2 style="color:red"><%= request.getAttribute("error") %></h2>
    <% } %>

    <% if(request.getSession().getAttribute("user") != null){ %>
      <h1>New Conversation</h1>
      <form action="/conversations" method="POST">
          <div class="form-group">
            <label class="form-control-label">Title:</label>
          <input type="text" name="conversationTitle">
        </div>

<<<<<<< HEAD
        <button type="submit" name="chat">Create</button>
      </form>

<<<<<<< HEAD
      <hr>

      <h1>New Group Chat</h1>
      <form action="/conversations" method="POST">
          <div class="form-group">
            <label class="form-control-label">Group Name:</label>
            <input type="text" name="conversationTitle">
            <br>
            <label class="form-control-label">Group Members:</label>
            <input type="text" name="members">
            <br>
            Please separate member usernames with commas (Ex: "lloza, cari, joyaan, linda")
        </div>

        <button type="submit" name="group">Create Group Chat</button>
=======
        <button type="submit">Create</button>
>>>>>>> parent of 66ae662... form
      </form>

=======
>>>>>>> 0fb2eb3edfde506b0f3faf3d2cac070608d78e76
      <hr/>
    <% } %>

    <h1>Conversations</h1>

    <%
    List<Conversation> conversations =
      (List<Conversation>) request.getAttribute("conversations");
    if(conversations == null || conversations.isEmpty()){
    %>
      <p>Create a conversation to get started.</p>
    <%
    }
    else{
    %>
      <ul class="mdl-list">
    <%
      for(Conversation conversation : conversations){
    %>
      <li><a href="/chat/<%= conversation.getTitle() %>">
        <%= conversation.getTitle() %></a></li>
    <%
      }
    %>
      </ul>
    <%
    }
    %>
    <hr/>
  </div>
</body>
</html>
