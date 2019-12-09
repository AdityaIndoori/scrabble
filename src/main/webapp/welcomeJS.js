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