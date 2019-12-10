<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>GameUI</title>
    <script src="welcomeJS.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.4.0/sockjs.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.js"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
    <script type="text/javascript">
        var stompClient = null;

        function submitmove() {
            var token = $("input[name='_csrf']").val();
                var http = new XMLHttpRequest();
                var gameid = <%=session.getAttribute("gameid")%>;
                console.log("GAMEUI GAME ID: " + gameid);
                var url = 'https://localhost:8443/submitmove/'+gameid; //TODO: Create an API to receive the submitmove
                http.open('POST', url, true);
                http.setRequestHeader('X-CSRF-TOKEN', token);
                var s_word = $("#word").val();
                var s_direction = $('input:radio[name=direction]:checked').val();
                var s_row = $("#row").val();
                var s_column = $("#column").val();
                var submit_data = {"word": s_word, "direction": s_direction, "row": s_row, "column":s_column};
                console.log("Submit Data is "+JSON.stringify(submit_data));
                <%-- stompClient.send("/app/chat/"+gameid, {},
                          JSON.stringify(submit_data));--%>
                          }
    </script>

</head>
    <body>
    <h2>Welcome: ${pageContext.request.userPrincipal.name}</h2><br>
    <h2>GAMEID: <%=session.getAttribute("gameid")%></h2><br>

    <h2>board:</h2><br>
    <table id="gameboard" border="1"></table><br>

    Word: <input type="text" id="word" name="word"><br>
    Row: <input type="number" id="row" name="row" min=1 max=7><br>
    Column: <input type="number" id="column" name="column" min=1 max=7><br>
    Horizontal: <input type="radio" value="HORIZONTAL" name="direction"><br>
    Vertical: <input type="radio" value="VERTICAL" name="direction"><br>
    <button id="submitmove" onclick="submitmove()">Submit Move</button>

    </body>
</html>