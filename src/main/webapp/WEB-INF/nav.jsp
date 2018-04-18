<nav>
    <a id="navTitle" href="/">CodeU Chat App</a>
    <a href="/conversations">Conversations</a>9
    <% if(request.getSession().getAttribute("user") != null){ %>
        <a>Hello <%= request.getSession().getAttribute("user") %>!</a>
        <script>
            function logOut(){
                request.getSession().setAttribute("user", null);
            }
        </script>
        <button onclick="logOut()">Log Out</button>
    <% } else{ %>
        <a href="/login">Login</a>
        <a href="/register">Register</a>
    <% } %>
    <a href="/about.jsp">About</a>
    <a href="/adminpage">Admin Page</a>
    <a href="/testdata">Load Test Data</a>
</nav>