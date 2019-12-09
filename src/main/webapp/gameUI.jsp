<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>GameUI</title>
    <script src="welcomeJS.js"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
</head>
    <body>
    <h2>GAMEID: <%=session.getAttribute("gameid")%></h2>
    <br>
    <h2>User: ${pageContext.request.userPrincipal.name}</h2>
    </body>
</html>