<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>Create an account</title>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
    <script src="welcomeJS.js"></script>
    
    <script type="text/javascript">
    	function errorloader(){
    		var error = "Oops, ERROR!";
    		var local = <%=session.getAttribute("error")%>;
    		if(local === error){
    			document.getElementById("errormessage").innerHTML = error;
    			document.getElementById('error').style.display = "block";
    		}
    	}
    </script>
    
</head>
<body>
  <div class="container">
    <c:if test="${pageContext.request.userPrincipal.name != null}">
        <form id="logoutForm" method="POST" action="${contextPath}/logout">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        </form>

		<h1>SWE681: Scrabble</h1>


        <h2>Welcome ${pageContext.request.userPrincipal.name}</h2>

        <button class="btn btn-lg btn-primary btn-block" onclick="showLeaderBoard();">Show Leaderboard</button>

        <button class="btn btn-lg btn-primary btn-block" onclick="showGameIDs();">Show Moves</button>

        <button class="btn btn-lg btn-primary btn-block" onclick="startGame();">Start Game</button>

        <button class="btn btn-lg btn-primary btn-block" onclick="joinGame();">Join Game</button>

        <button class="btn btn-lg btn-primary btn-block" onclick="showRejoinableGames();">Show Rejoinable Games</button>

		<button class="btn btn-lg btn-primary btn-block" onclick="logoutClick()">Logout</button>

		<img src onerror='errorloader()'>
        <div id="error" style="display: none;"><h2 style="color:red" id="errormessage"></h2></div>
        </c:if>
  </div>

  		<div id="leaderdiv" style="display: none;">
  			<h2>Leaderboard</h2>
        	<table id="leaderboard" border="1"></table><br>
  		</div>
  		
  		<div id="gamelistdiv" style="display: none;">
  			<h2>Game List</h2><br>
        	<table id="gamelist" border="1"></table>
        	<br>
        	<br>
        	Game ID: <input type="text" id="q_gameid" name="q_gameid">
        	<br>
        	<button id="submitgameid" onclick="showMoves($('#q_gameid').val())">Submit GameID</button><br>
  		</div>
        
		<div id="movelistdiv" style="display: none;">
			<h2>Moves</h2><br>
        	<table id="moveslist" border="1"></table>
		</div>

        <div id="rejoinGamelistdiv" style="display: none;">
            <h2>Rejoin a Game</h2><br>
            <table id="rejoinGamelist" border="1"></table>
            <br>
            Game ID: <input type="text" id="re_gameid" name="re_gameid">
            <br>
            <button id="rejoingameid" onclick="reJoinGame($('#re_gameid').val())">Rejoin Game</button><br>
        </div>

</body>
</html>