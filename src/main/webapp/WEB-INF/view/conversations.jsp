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
<%@ page import="codeu.model.data.GroupChat" %>
<%@ page import="java.util.ArrayList" %>

<!DOCTYPE html>
<html>
<head>
  <title>Conversations</title>
  <link rel="stylesheet" href="/css/main.css">
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
        
        <button type="submit">Create</button>
      </form>

      <hr/>
    <% } %>

    <h1>Conversations</h1>

    <%
    List<Conversation> conversations =
      (List<Conversation>) request.getAttribute("conversations");
    List<GroupChat> groupChats = new ArrayList<>();

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

        if (conversation instanceof GroupChat) {
            groupChats.add((GroupChat) conversation);
        } else {
    %>
          <li><a href="/chat/<%= conversation.getTitle() %>">
            <%= conversation.getTitle() %></a></li>
    <%
        }
      }
    %>
      </ul>
    <%
    }
    %>
    <hr/>

    <h1>Group Chats</h1>

    <%
    if( groupChats == null || groupChats.isEmpty()){
    %>
      <p>Create a group chat to get started.</p>
    <% } else { %>
      <ul class="mdl-list">
    <%
      for(GroupChat groupChat : groupChats){
        String username = String.valueOf(request.getSession().getAttribute("user"));
        if (groupChat.containsMember(username)) {
    %>
          <li><a href="/chat/<%= groupChat.getTitle() %>">
            <%= groupChat.getTitle() %></a></li>
    <%
        }
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
