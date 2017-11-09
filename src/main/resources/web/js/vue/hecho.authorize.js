var url = window.location.href;
if (url.indexOf('#') >= 0) {
    var arguments = url.split('#')[1].split('&');
    var token = arguments.filter(function(elm) { elm.indexOf('access_token') >= 0; }).split('=')[1];

     $.ajax({url: "/api/:syncProvider/sync", method: "POST", data: {"token": token}, dataType: "json"}).done(function(data) {
        if (data.code === 0) {
            alert("Authorized!");
            window.location.href = "/";
        } else {
            alert("Error! " + data.reason);
        }
    });
}