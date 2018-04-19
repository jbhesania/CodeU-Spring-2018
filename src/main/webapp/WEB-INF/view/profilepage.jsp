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
<%@ page import="codeu.model.data.User" %>
<%@ page import="codeu.model.store.basic.UserStore" %>
<%
User pageUser = (User) request.getAttribute("pageUser");
User sessionUser = (User) request.getAttribute("sessionUser");
%>

<!DOCTYPE html>
<html>
<head>
  <title><%= pageUser.getName() %></title>
  <link rel="stylesheet" href="/css/main.css" type="text/css">

  <style>
    #chat {
      background-color: white;
      height: 500px;
      overflow-y: scroll
    }
  </style>
</head>

<body>
  <jsp:include page="/WEB-INF/nav.jsp" />

  <div id="container">
    <h1><%= pageUser.getName() %>'s Profile Page
      <a href="" style="float: right">&#8635;</a></h1>
    
  <% if(sessionUser != null){
       if(sessionUser.follows(pageUser.getId())){ %>
         <form action="" method=POST>
           <button type="submit" name="follow" value="false">Unfollow</button>
         </form>
       <% } else { %>
          <form action="" method=POST>
            <button type="submit" name="follow" value="true">Follow</button>
          </form>
       <% }
     }
     else { %>
       <p><a href="/login">Login</a> to send a message.</p>
       <p> <% request.getSession().getAttribute("user"); %></p>
  <% } %>

  </div>

</body>
</html>
