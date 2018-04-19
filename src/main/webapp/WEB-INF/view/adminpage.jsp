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
