<%@taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page errorPage="errorMessage.jsp"%>
<%
    String studentId = request.getParameter("studentId"),
            studentName = request.getParameter("studentName"),
            groupId = request.getParameter("groupId");
    boolean isEmpty1 = false, isEmpty2 = false, isEmpty3 = false,
            isWrong1 = false, isWrong2 = false, isWrong3 = false;
    if (studentId != null && studentName != null && groupId != null) {
        isEmpty1 = studentId.isEmpty();
        isEmpty2 = studentName.isEmpty();
        isEmpty3 = groupId.isEmpty();
        if (!isEmpty1 && !isEmpty2 && !isEmpty3) {
            session.setAttribute("studentId", studentId);
            session.setAttribute("studentName", studentName);
            session.setAttribute("groupId", groupId);
            response.sendRedirect("controller.jsp?message=createStudent");
        }
        isWrong1 = isEmpty1;
        isWrong2 = isEmpty2;
        isWrong3 = isEmpty3;
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
        <form method="post" action="createstudent.jsp">
            <table>
                <tr>
                    <td class="label">Enter the student id:</td>
                    <td>
                        <input type="text" name="studentId" class="<%= isWrong1 ? "emptystring" : "string"%>" value="${param.studentId}"/>
                    </td>
                    <% if (isWrong1) {%>
                    <td class="errormessage">Enter the string!</td>
                    <% }%>
                </tr>
                <tr>
                    <td class="label">Enter the student name:</td>
                    <td>
                        <input type="text" name="studentName" class="<%= isWrong2 ? "emptystring" : "string"%>" value="${param.studentName}"/>
                    </td>
                    <% if (isWrong2) {%>
                    <td class="errormessage">Enter the string!</td>
                    <% }%>
                </tr>
                <tr>
                    <td class="label">Enter the group id:</td>
                    <td>
                        <input type="text" name="groupId" class="<%= isWrong3 ? "emptystring" : "string"%>" value="${param.groupId}"/>
                    </td>
                    <% if (isWrong3) {%>
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
