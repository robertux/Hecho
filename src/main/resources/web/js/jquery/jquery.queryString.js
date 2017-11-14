/**
 * Taken from: https://stackoverflow.com/questions/901115/how-can-i-get-query-string-values-in-javascript#comment7448147_3855394
**/
(function ($) {
    $.QueryString = (function (a) {
        var i,
            p,
            b = {};
        if (a === "") { return {}; }
        for (i = 0; i < a.length; i += 1) {
            p = a[i].split('=');
            if (p.length === 2) {
                b[p[0]] = decodeURIComponent(p[1].replace(/\+/g, " "));
            }
        }
        return b;
    }(window.location.search.substr(1).split('&')));
}(jQuery));