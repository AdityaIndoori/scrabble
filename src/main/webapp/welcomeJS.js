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

function logoutClick(){
    document.forms['logoutForm'].submit()
}



function chatClick(){
	window.location.href = window.location.href+"/test/12";
}

