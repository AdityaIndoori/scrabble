setTimeout(function() {
    $("#error").hide();
}, 3000);

function submitClick(){
            var token = $("input[name='_csrf']").val();
            var http = new XMLHttpRequest();
            var url = 'https://localhost:8443/aditya/';
            var params = 'Test123';
            http.open('POST', url, true);
            http.setRequestHeader('X-CSRF-TOKEN', token);

            http.onreadystatechange = function() {//Call a function when the state changes.
                if(http.readyState == 4 && http.status == 200) {
                    alert(http.responseText);
                }
            }
            http.send(params);
        }

function startGame(){
    document.location.href = "https://localhost:8443/startgame/";
}

function joinGame(){
    document.location.href = "https://localhost:8443/joingame/";
}

function showLeaderBoard(){
	
	// To clear up previously rendered table 
	$("#leaderboard tr").remove(); 
	
    var token = $("input[name='_csrf']").val();
    var http = new XMLHttpRequest();
    var url = 'https://localhost:8443/leaderboard/';
    http.open('GET', url, true);
    http.setRequestHeader('X-CSRF-TOKEN', token);

    http.onreadystatechange = function() {//Call a function when the state changes.
        if(http.readyState == 4 && http.status == 200) {
            //alert(http.responseText); TODO: Get response from leaderBoard API and show in a table
            console.log("Leaderboard JSON: "+http.responseText);
            var myList = http.responseText;
            console.log("My List: "+myList + "\n Length="+ myList.length);
            var myList2 = JSON.parse(myList);
            console.log("My List After trim: ", myList2.length);
            buildHtmlTable(myList2, '#leaderboard');
        }
    }
    http.send();
    
    document.getElementById('leaderdiv').style.display = "block";
}

function showGameIDs(){
	// To clear up previously rendered table 
	$("#gamelist tr").remove(); 
	
    var token = $("input[name='_csrf']").val();
    var http = new XMLHttpRequest();
    var url = 'https://localhost:8443/gamelist/';
    http.open('GET', url, true);
    http.setRequestHeader('X-CSRF-TOKEN', token);

    http.onreadystatechange = function() {//Call a function when the state changes.
        if(http.readyState == 4 && http.status == 200) {
            //alert(http.responseText); TODO: Get response from leaderBoard API and show in a table
            console.log("Leaderboard JSON: "+http.responseText);
            var myList = http.responseText;
            console.log("My List: "+myList + "\n Length="+ myList.length);
            var myList2 = JSON.parse(myList);
            console.log("My List After trim: ", myList2.length);
            buildHtmlTable(myList2, '#gamelist');
        }
    }
    http.send();
    
    document.getElementById('gamelistdiv').style.display = "block";
}

function showMoves(gameID){
	// To clear up previously rendered table 
	$("#moveslist tr").remove(); 
	
	
    console.log("Game ID when selected: "+gameID);

    var token = $("input[name='_csrf']").val();
    var http = new XMLHttpRequest();
    var url = 'https://localhost:8443/gamemoves/'+gameID;
    http.open('GET', url, true);
    http.setRequestHeader('X-CSRF-TOKEN', token);

    http.onreadystatechange = function() {//Call a function when the state changes.
        if(http.readyState == 4 && http.status == 200) {
            //alert(http.responseText); TODO: Get response from leaderBoard API and show in a table
            console.log("Leaderboard JSON: "+http.responseText);
            var myList = http.responseText;
            console.log("My List: "+myList + "\n Length="+ myList.length);
            var myList2 = JSON.parse(myList);
            console.log("My List After trim: ", myList2.length);
            buildHtmlTable(myList2, '#moveslist');
        }
    }
    http.send();
    
    document.getElementById('movelistdiv').style.display = "block";
}

function logoutClick(){
    document.forms['logoutForm'].submit()
}

function chatClick(){
    console.log("URL: " + window.location.href);
    var url = window.location.href;
    if(url.endsWith("/"))
	    window.location.href = window.location.href+"websocket/chat";
    else
        window.location.href = window.location.href+"/websocket/chat";
}

function buildHtmlTable(myList, tableID) {
	
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


 // Adds a header row to the table and returns the set of columns.
 // Need to do union of keys from all records as some records may not contain
 // all records
 function addAllColumnHeaders(myList, tableID)
  {
      var columnSet = [];
      var headerTr$ = $('<tr/>');

      for (var i = 0 ; i < myList.length ; i++) {
          var rowHash = myList[i];
          for (var key in rowHash) {
              if ($.inArray(key, columnSet) == -1){
                  columnSet.push(key);
                  headerTr$.append($('<th/>').html(key));
              }
          }
      }
      $(tableID).append(headerTr$);

      return columnSet;
  }