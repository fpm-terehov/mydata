<%-- 
    Document   : errjsp
    Created on : 14.05.2015, 0:58:19
    Author     : fpm-terehov
--%>

<%@page contentType="text/html" pageEncoding="windows-1251"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1251">
        <title>ERROR</title>
        <script type="text/javascript">
            function run() {
                var username = document.getElementById('5');
                var params = location.search;
                var textConten = params.split("=")[1];
                username.textContent = textConten;
            }
         </script>
    </head>
    <body onload="run();">
        <div id = "5">error</div>
    </body>
</html>
