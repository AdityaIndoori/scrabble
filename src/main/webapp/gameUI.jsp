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

        function connect() {
            var socket = new SockJS('/chat');
            stompClient = Stomp.over(socket);
            stompClient.connect({}, function(frame) {
                setConnected(true);
                console.log('Connected: ' + frame);

                var gameid = <%= session.getAttribute("gameid") %>;

                stompClient.subscribe('/topic/'+gameid, function(messageOutput) {
                    showMessageOutput(JSON.parse(messageOutput.body));
                });
            });
        }

        function setConnected(connected) {
            document.getElementById('connect').disabled = connected;
            document.getElementById('disconnect').disabled = !connected;
            document.getElementById('conversationDiv').style.visibility= connected ? 'visible' : 'hidden';
            document.getElementById('response').innerHTML = '';
            showGameRack();
        }

        function sendMessage() {
            console.log("START sendMessage");
            var token = $("input[name='_csrf']").val();
            var http = new XMLHttpRequest();
            var gameid = <%=session.getAttribute("gameid")%>;
            console.log("GAMEUI GAME ID: " + gameid);
            var url = 'https://localhost:8443/app/chat/'+gameid;
            console.log("GAMEUI SEND URL: " + url);
            var username = $("#username").text();
            http.open('POST', url, true);
            http.setRequestHeader('X-CSRF-TOKEN', token);
            var s_word = $("#word").val();
            var s_direction = $('input:radio[name=direction]:checked').val();
            var s_row = $("#row").val();
            var s_column = $("#column").val();
            var submit_data = {"word": s_word, "direction": s_direction, "row": s_row, "column":s_column, "gameid":gameid, "username":username};
            console.log("Submit Data is "+JSON.stringify(submit_data));
            stompClient.send("/app/chat/"+gameid, {}, JSON.stringify(submit_data));
            console.log("END sendMessage");

        }

        function showGameRack() {
            console.log("START showGameRack");
            var token = $("input[name='_csrf']").val();
            var http = new XMLHttpRequest();
            var gameid = <%=session.getAttribute("gameid")%>;
            var url = 'https://localhost:8443/app/chat/'+gameid;
            var username = $("#username").text();
            http.open('POST', url, true);
            http.setRequestHeader('X-CSRF-TOKEN', token);
            var s_word = $("#word").val();
            var s_direction = $('input:radio[name=direction]:checked').val();
            var s_row = $("#row").val();
            var s_column = $("#column").val();
            var submit_data = {"word": "showGameRack", "direction": "", "row": "", "column":"", "gameid":gameid, "username":username};
            console.log("Submit Data is "+JSON.stringify(submit_data));
            stompClient.send("/app/chat/"+gameid, {}, JSON.stringify(submit_data));
            console.log("END showGameRack");
        }



        function showMessageOutput(messageOut) {
        if(messageOut!=null){
            console.log("START showMessageOutput");
            var response = document.getElementById('response');
            var p = document.createElement('p');
            p.style.wordWrap = 'break-word';
            var messageOutput = messageOut.wsoutput;
            console.log(messageOutput);
            $("#gameboard tr").remove();
            if(messageOut.outputTable!=null)
                buildHtmlTable(messageOut.outputTable, '#gameboard');
             $("#scores").text("P1 Score: "+ messageOut.p1Score+ " - " + "P2 Score: "+ messageOut.p2Score);

            parseGameRack(messageOut.gameRack);

            if(messageOut.outputTable!=null){
            p.appendChild(document.createTextNode(messageOutput.username + ": " + messageOutput.word +" at Row|Col: "+messageOutput.row+"|"+messageOutput.column+ " (" + messageOutput.time + ")"));
            response.appendChild(p);
            }
            console.log("END showMessageOutput");
            }
        }

        function parseGameRack(gameRack){
        if(gameRack!=null){
            var un = $("#username").text()
            var gameRack2 = gameRack.replace("{", "");
            var gameRack3 = gameRack2.replace("}", "");
            console.log("GameRack3: "+ gameRack3);
            var gameRacks = gameRack3.split(",");
            if(gameRacks[0].includes(un))
                parseGameRack2(gameRacks[0])
            else if(gameRacks[1].includes(un))
                parseGameRack2(gameRacks[1])
            }
        }

        function parseGameRack2(gameRack){
            var gameRack2 = gameRack.split(":");
            console.log("parseGameRack2is: "+ gameRack2[1]);
            $("#gameRack").text(gameRack2[1]);
        }


        function disconnect() {
            if(stompClient != null) {
                stompClient.disconnect();
                setConnected(false);
                console.log("Disconnected");
                document.location.href = "https://localhost:8443/disconnect/";
            }
            else{
                //alert("Could not load game ui");
            }
        }
        
        function buildHtmlTable(myList, tableID) {
        	if(myList!=null){
            console.log("Inside buildHtml = "+ myList);
             var columns = addAllColumnHeaders(myList, tableID);

             for (var i = 0 ; i < myList.length ; i++) {
                 var row$ = $('<tr/>');
                 for (var colIndex = 0 ; colIndex < columns.length ; colIndex++) {
                     var cellValue = myList[i][columns[colIndex]];

                     if (cellValue == null) { cellValue = ""; }

                     row$.append($('<td/>').html(cellValue));
                 }
                 $(tableID).append(row$);
             }
             }
         }


         // Adds a header row to the table and returns the set of columns.
         // Need to do union of keys from all records as some records may not contain
         // all records
         function addAllColumnHeaders(myList, tableID)
          {
          if(myList!=null){
              var columnSet = [];
              var headerTr$ = $('<tr/>');

              for (var i = 0 ; i < myList.length ; i++) {
                  var rowHash = myList[i];
                  for (var key in rowHash) {
                      if ($.inArray(key, columnSet) == -1){
                          columnSet.push(key);
                          headerTr$.append($('<th/>').html(key-1));
                      }
                  }
              }
              $(tableID).append(headerTr$);

              return columnSet;
              }
          }



    </script>

</head>
    <body onload="disconnect();connect();">
    <h2>Welcome: <div id="username">${pageContext.request.userPrincipal.name}</div></h2><br>
    <h2>GAMEID: <%=session.getAttribute("gameid")%></h2><br>

    <h2>Board:</h2><br>
    <table id="gameboard" border="1"></table><br>
    <h2>Rack:</h2>
    <h3 id="gameRack"> </h3><br>
    <h2>Scores:</h2>
    <h3 id="scores">"P1 Score: 0 - P2 Score: 0"</h3><br>
    Word: <input type="text" id="word" name="word"><br>
    Row: <input type="number" id="row" name="row" min=0 max=9><br>
    Column: <input type="number" id="column" name="column" min=0 max=9><br>
    Horizontal: <input type="radio" value="HORIZONTAL" name="direction"><br>
    Vertical: <input type="radio" value="VERTICAL" name="direction"><br>
    <button id="submitmove" onclick="sendMessage()">Submit Move</button>
    <br>
    <div>
        <button id="connect" onclick="connect();">Connect</button>
        <button id="disconnect" disabled="disabled" onclick="disconnect();">
            Disconnect
        </button>
    </div>
    <div id="conversationDiv">
        <!--  <input type="text" id="text" placeholder="Write a message..."/>
        <button id="sendMessage" onclick="sendMessage();">Send</button>-->
        <h2>OUTPUT:</h2>
        <p id="response"></p>
    </div>
    </body>
</html>