<nav>
    <a id="navTitle" href="/">Avo-Chat-O</a>
    <a href="/conversations">Conversations</a>
    <% if(request.getSession().getAttribute("user") != null){ %>
        <a>Hello <%= request.getSession().getAttribute("user") %>!</a>
    <% } else{ %>
        <a href="/login">Login</a>
        <a href="/register">Register</a>
    <% } %>
    <a href="/about.jsp">About</a>
    <a href="/activityfeed">Activity Feed</a>

    <% if (Boolean.TRUE.equals(request.getSession().getAttribute("admin"))) { %>
        <a href="/adminpage">Admin Page</a>
        <a href="/testdata">Load Test Data</a>
    <% } %>
</nav>
