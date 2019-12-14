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
            //document.getElementById('connect').disabled = connected;
            document.getElementById('disconnect').disabled = !connected;
            document.getElementById('MovesDiv').style.visibility= connected ? 'visible' : 'hidden';
            document.getElementById('response').innerHTML = '';
            showGameRack();
        }

        function sendMessage() {
            //console.log("START sendMessage");
            var token = $("input[name='_csrf']").val();
            var http = new XMLHttpRequest();
            var gameid = <%=session.getAttribute("gameid")%>;
            //console.log("GAMEUI GAME ID: " + gameid);
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
            //console.log("Submit Data is "+JSON.stringify(submit_data));
            stompClient.send("/app/chat/"+gameid, {}, JSON.stringify(submit_data));
            //console.log("END sendMessage");
            
            //Clearing input fields
            document.getElementById('word').innerHTML = "";
            document.getElementById('row').innerHTML = "";
            document.getElementById('column').innerHTML = "";
            

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
            //console.log("Submit Data is "+JSON.stringify(submit_data));
            stompClient.send("/app/chat/"+gameid, {}, JSON.stringify(submit_data));
            //console.log("END showGameRack");
        }

		
       
        

        function showMessageOutput(messageOut) {
        if(messageOut!=null){
            //console.log("START showMessageOutput");
            var response = document.getElementById('response');
            var p = document.createElement('p');
            p.style.wordWrap = 'break-word';
            var messageOutput = messageOut.wsoutput;
            //console.log(messageOutput);
            $("#gameboard tr").remove();
            if(messageOut.outputTable!=null)
                buildHtmlTable(messageOut.outputTable, '#gameboard');
             $("#p1scores").text(messageOut.p1Username+": "+ messageOut.p1Score);
             $("#p2scores").text(messageOut.p2Username + ": "+ messageOut.p2Score);

            parseGameRack(messageOut.gameRack);

            if(messageOut.outputTable!=null && messageOut.error!=="first"){
            p.appendChild(document.createTextNode(messageOutput.username + ": " + messageOutput.word +" at Row|Col: "+messageOutput.row+"|"+messageOutput.column+ " (" + messageOutput.time + ")"));
            response.appendChild(p);
            }
            //console.log("END showMessageOutput");
            }
        }

        function parseGameRack(gameRack){
        if(gameRack!=null){
            var un = $("#username").text()
            var gameRack2 = gameRack.replace("{", "");
            var gameRack3 = gameRack2.replace("}", "");
            //console.log("GameRack3: "+ gameRack3);
            var gameRacks = gameRack3.split(",");
            if(gameRacks[0].includes(un))
                parseGameRack2(gameRacks[0])
            else if(gameRacks[1].includes(un))
                parseGameRack2(gameRacks[1])
            }
        }

        function parseGameRack2(gameRack){
            var gameRack2 = gameRack.split(":");
            //console.log("parseGameRack2is: "+ gameRack2[1]);
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
    <div><h2>Welcome: <span id="username">${pageContext.request.userPrincipal.name}</span></h2></div>
    <h3>GAMEID: <%=session.getAttribute("gameid")%></h3>

    <h3>Board:</h3>
    <table id="gameboard" border="1"></table>
    <br>
    <!-- <h2>Rack:</h2>-->
    <div><span>RACK: </span><span id="gameRack"> </span></div>
    <h4>Scores:</h4>
    <h5 id="p1scores">"P1 Score: 0"</h5>
    <h5 id="p2scores">"P2 Score: 0"</h5>
    <br>
    
    Word: <input type="text" id="word" name="word"><br>
    Row: <input type="number" id="row" name="row" min=0 max=9><br>
    Column: <input type="number" id="column" name="column" min=0 max=9><br>
    Horizontal: <input type="radio" value="HORIZONTAL" name="direction"><br>
    Vertical: <input type="radio" value="VERTICAL" name="direction"><br>
    <button id="submitmove" onclick="sendMessage()">Submit Move</button>
    <br>
    <div>
        <!--<button id="connect" onclick="connect();">Connect</button>-->
        <button id="disconnect" disabled="disabled" onclick="disconnect();">
            Leave
        </button>
    </div>
    <div id="MovesDiv">
        <!--  <input type="text" id="text" placeholder="Write a message..."/>
        <button id="sendMessage" onclick="sendMessage();">Send</button>-->
        <h3>OUTPUT:</h3>
        <p id="response"></p>
    </div>
    </body>
</html>