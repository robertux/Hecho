url = window.location.href;
var arguments = url.split('#')[1].split('&');
var token = arguments.filter(function(elm) { elm.indexOf('access_token') >= 0; }).split('=')[1];