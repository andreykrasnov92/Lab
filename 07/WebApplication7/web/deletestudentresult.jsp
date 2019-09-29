<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    response.setHeader("Refresh", "5; URL=index.jsp");
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="styles/style.css"/>
        <link rel="shortcut icon" href="images/favicon.png" type="image/png"/>
        <script type="text/javascript" src="scripts/script.js"></script>
        <title>University</title>
    </head>
    <body>
        <h1>University</h1>
        <form>
            <table>
                <tr>
                    <td class="label">
                        Student successfully deleted!
                    </td>
                </tr>
            </table>
            <table>
                <tr>
                    <td class="label">
                        You will be redirected at <a href="index.jsp">index.jsp</a> in 5 seconds
                    </td>
                </tr>
            </table>
        </form>
    </body>
</html>
