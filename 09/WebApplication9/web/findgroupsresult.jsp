<%@taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <link rel="stylesheet" type="text/css" href="styles/style.css"/>
        <link rel="shortcut icon" href="images/favicon.png" type="image/png"/>
        <title>University</title>
    </head>
    <body>
        <h1>Groups - search result</h1>
        <table>
            <tr>
                <th class="label">ID</th>
                <th class="label">Name</th>
            </tr>
            <x:parse xml="${sessionScope.groups}" var="groups"/>
            <x:forEach select="$groups/root/group" var="group">
                <tr>
                    <td class="label"><x:out select="$group/@groupid"/></td>
                    <td class="label"><x:out select="$group/@groupname"/></td>
                </tr>
            </x:forEach>
            <tr>
                <td class="label" colspan="2">
                    Go to <a href="index.jsp">home page</a>
                </td>
            </tr>
        </table>
    </body>
</html>
