<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
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
        <h1>Some error occurred!</h1>
        <form>
            <table>
                <tr>
                    <td class="label">
                        System cannot perform the requested action. Please contact administrator.
                    </td>
                </tr>
                <tr>
                    <td class="label">
                        Details: <c:out value="${sessionScope.errorMessage}"/>
                    </td>
                </tr>
                <tr>
                    <td class="label" colspan="2">
                        Go to <a href="index.jsp">home page</a>
                    </td>
                </tr>
            </table>
        </form>
    </body>
</html>
