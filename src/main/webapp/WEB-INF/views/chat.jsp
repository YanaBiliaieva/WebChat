<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head><title>Title</title></head>
<script src="http://cdn.jsdelivr.net/sockjs/0.3.4/sockjs.min.js"></script>
<script>
    var socket = new SockJS("${sockUrl}");//коннектимся к сокету на сервере
    function ready() {
        document.getElementById("sendButton").addEventListener("click", send);
        document.getElementById("broadcastButton").addEventListener("click", broadcast);
        document.getElementById("logoutButton").addEventListener("click", logout);
    }
    document.addEventListener("DOMContentLoaded", ready);

    socket.onopen = function () {
        console.log("Connection successful");
        registration();

    };
    socket.onclose = function (event) {
        if (event.wasClean) {
            console.log("Connection close");
        } else {
            console.log("Connection close because of error")
        }
        window.location.href = "/";
    };
    socket.onerror = function (error) {
        console.log(error);
        window.location.href = "/";
    };
    socket.onmessage = function (event) {//когда сообщение влезло в сокет
        var json_message = JSON.parse(event.data);
        if (typeof json_message.auth === "undefined") {
            console.log("Bad JSON");
            window.location.href = "/";
        }

        if (json_message.auth == "yes" && typeof json_message.list !== "undefined") {
            var array_active_users = json_message.list;
            var ul_element = document.createElement("ul");
            for (var i = 0; i < array_active_users.length; i++) {
                var li_element = document.createElement("li");
                li_element.className = "active-users";
                li_element.id = array_active_users[i];
                li_element.innerText = array_active_users[i];
                li_element.addEventListener("click", addUser);
                ul_element.appendChild(li_element);
            }
            var div_element = document.getElementById("activeUsers");
            div_element.innerHTML="";
            div_element.appendChild(ul_element);
        }
        if (json_message.auth == "yes" && typeof  json_message.login !== "undefined") {
            var output = json_message.login + ":" + json_message.message;
            document.getElementById("inputMessage").value += output+"\n";
        }
    };
    function registration() {
        var sessionid = getCookie("JSESSIONID");
        var answer = {};
        answer["sessionid"] = sessionid;
        socket.send(JSON.stringify(answer));
    }
    function addUser() {
        var login = event.currentTarget.id;
        document.getElementById("outputMessage").value = login + ":";
    }
    function getCookie(name) {
        var matches = document.cookie.match(new RegExp(
            "(?:^|; )" + name.replace(/([\.$?*|{}\(\)\[\]\\\/\+^])/g, '\\$1') + "=([^;]*)"
        ));
        return matches ? decodeURIComponent(matches[1]) : undefined;
    }
    function send() {
        var array_message = document.getElementById("outputMessage").value.split(":");
        var answer = {};
        answer["login"] = array_message[0];
        answer["message"] = array_message[1];
        socket.send(JSON.stringify(answer));
    }
    function broadcast() {
        var message = document.getElementById("outputMessage").value;
        var answer = {};
        answer["broadcast"] = message;
        socket.send(JSON.stringify(answer));
    }
    function logout() {
        var answer = {};
        answer["logout"] = "";
        socket.selectNode(JSON.stringify(answer));
        window.location.href = "/";
    }
</script>
<body>
<h1>Welcome to chat!</h1>
<table>
    <tr>
        <td>
            <form>
                <textarea id="inputMessage"></textarea>
            </form>
        </td>
        <td>
            <div id="activeUsers">

            </div>
        </td>
    </tr>
    <tr>
        <td>
            <form><input type="text" id="outputMessage"/></form>
        </td>
        <td>
            <form><input type="button" id="sendButton" value="send"/>
                <input type="button" id="broadcastButton" value="broadcast"/>
                <input type="button" id="logoutButton" value="logout"/></form>
        </td>
    </tr>
</table>
</body>
</html>