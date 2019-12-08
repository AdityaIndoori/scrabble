<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>Create an account</title>
    <script src="welcomeJS.js"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
</head>
<body>
  <div class="container">
    <c:if test="${pageContext.request.userPrincipal.name != null}">
        <form id="logoutForm" method="POST" action="${contextPath}/logout">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        </form>
        <br>

        <button class="btn btn-lg btn-primary btn-block" onclick="logoutClick()">Logout</button>

        <h2>Welcome ${pageContext.request.userPrincipal.name}>Logout</a></h2>

        <button class="btn btn-lg btn-primary btn-block" onclick="leaderBoardClick();">Show Leaderboard</button>

        <button class="btn btn-lg btn-primary btn-block" onclick="movesClick();">Show Moves</button>

        <button class="btn btn-lg btn-primary btn-block" onclick="gameClick();">Start Game</button>

        <button class="btn btn-lg btn-primary btn-block" onclick="joinClick();">Join Game</button>

        <button class="btn btn-lg btn-primary btn-block" onclick="submitClick();">Test Button</button>

    </c:if>
  </div>
</body>
</html>