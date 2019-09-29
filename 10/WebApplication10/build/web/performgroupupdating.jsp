<%@taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page errorPage="errorMessage.jsp"%>
<%
    String groupId = request.getParameter("groupId"), groupName = request.getParameter("groupName");
    boolean isEmpty1 = false, isEmpty2 = false;
    if (groupId != null && groupName != null) {
        isEmpty1 = groupId.isEmpty();
        isEmpty2 = groupName.isEmpty();
        if (!isEmpty1 && !isEmpty2) {
            session.setAttribute("groupId", groupId);
            session.setAttribute("groupName", groupName);
            response.sendRedirect("controller.jsp?message=updateGroup");
        }
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
        <h1>Group updating</h1>
        <form id="form1" method="post">
            <table>
                <tr>
                    <td class="label">Enter the group id:</td>
                    <td>
                        <input type="text" name="groupId" class="<%= isEmpty1 ? "emptystring" : "string"%>" value="${sessionScope.groupId}"/>
                    </td>
                    <% if (isEmpty1) {%>
                    <td class="errormessage">Enter the string!</td>
                    <% }%>
                </tr>
                <tr>
                    <td class="label">Enter the group name:</td>
                    <td>
                        <input type="text" name="groupName" class="<%= isEmpty2 ? "emptystring" : "string"%>" value="${sessionScope.groupName}"/>
                    </td>
                    <% if (isEmpty2) {%>
                    <td class="errormessage">Enter the string!</td>
                    <% }%>
                </tr>
            </table>
            <table>
                <tr>
                    <td><input type="submit" form="form1" name="operation1" value="Update" formaction="performgroupupdating.jsp"/></td>
                    <td><input type="submit" form="form1" name="operation2" value="Cancel" formaction="controller.jsp?message=cancelGroupUpdating"/></td>
                </tr>
            </table>
        </form>
    </body>
</html>
