<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="servlets.*"%>
<%
    String groupId = request.getParameter("groupId"),
            groupName = request.getParameter("groupName");
    boolean isEmpty1 = false, isEmpty2 = false,
            isWrong1 = false, isWrong2 = false;
    if (groupId != null && groupName != null) {
        isEmpty1 = groupId.isEmpty();
        isEmpty2 = groupName.isEmpty();
        if (!isEmpty1 && !isEmpty2) {
            session.setAttribute("groupId", groupId);
            session.setAttribute("groupName", groupName);
            response.sendRedirect("CreateGroupServlet");
        }
        isWrong1 = isEmpty1;
        isWrong2 = isEmpty2;
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
        <h1>University</h1>
        <form method="post" action="creategroup.jsp">
            <table>
                <tr>
                    <td class="label">Enter the group id:</td>
                    <td>
                        <input type="text" name="groupId" class="<%= isWrong1 ? "emptystring" : "string"%>" value="${param.groupId}"/>
                    </td>
                    <% if (isWrong1) {%>
                    <td class="errormessage">Enter the string!</td>
                    <% }%>
                </tr>
                <tr>
                    <td class="label">Enter the group name:</td>
                    <td>
                        <input type="text" name="groupName" class="<%= isWrong2 ? "emptystring" : "string"%>" value="${param.groupName}"/>
                    </td>
                    <% if (isWrong2) {%>
                    <td class="errormessage">Enter the string!</td>
                    <% }%>
                </tr>
                <tr>
                    <td class="perform" colspan="2"><input type="submit" value="Create" name="operation"/></td>
                </tr>
            </table>
        </form>
    </body>
</html>
