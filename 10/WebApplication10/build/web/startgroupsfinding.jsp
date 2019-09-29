<%@taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page errorPage="errorMessage.jsp"%>
<%
    String groupName = request.getParameter("groupName");
    if (groupName != null) {
        session.setAttribute("groupName", groupName);
        response.sendRedirect("controller.jsp?message=performGroupsFinding");
    }
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <link rel="stylesheet" type="text/css" href="styles/style.css"/>
        <link rel="shortcut icon" href="images/favicon.png" type="image/png"/>
        <title>University</title>
    </head>
    <body>
        <h1>Find groups by name</h1>
        <form method="post" action="startgroupsfinding.jsp">
            <table>
                <tr>
                    <td class="label">
                        <select name="groupName" value="${param.groupName}">
                            <x:parse xml="${sessionScope.groups}" var="groups"/>
                            <x:forEach select="$groups/root/group" var="group">
                                <option><x:out select="$group/@groupname"/></option>
                            </x:forEach>
                        </select>
                        <input type="submit" value="Find groups" name="operation"/>
                    </td>
                </tr>
                <tr>
                    <td class="label">
                        Go to <a href="index.jsp">home page</a>
                    </td>
                </tr>
            </table>
        </form>
    </body>
</html>
