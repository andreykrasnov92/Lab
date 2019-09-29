<%@taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page errorPage="errorMessage.jsp"%>
<%
    String studentId = request.getParameter("studentId"),
            studentName = request.getParameter("studentName"),
            groupId = request.getParameter("groupId");
    boolean isEmpty1 = false, isEmpty2 = false, isEmpty3 = false;
    if (studentId != null && studentName != null && groupId != null) {
        isEmpty1 = studentId.isEmpty();
        isEmpty2 = studentName.isEmpty();
        isEmpty3 = groupId.isEmpty();
        if (!isEmpty1 && !isEmpty2 && !isEmpty3) {
            session.setAttribute("studentId", studentId);
            session.setAttribute("studentName", studentName);
            session.setAttribute("groupId", groupId);
            response.sendRedirect("controller.jsp?message=updateStudent");
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
        <h1>Student updating</h1>
        <form id="form1" method="post">
            <table>
                <tr>
                    <td class="label">Enter the student id:</td>
                    <td>
                        <input type="text" name="studentId" class="<%= isEmpty1 ? "emptystring" : "string"%>" value="${sessionScope.studentId}"/>
                    </td>
                    <% if (isEmpty1) {%>
                    <td class="errormessage">Enter the string!</td>
                    <% }%>
                </tr>
                <tr>
                    <td class="label">Enter the student name:</td>
                    <td>
                        <input type="text" name="studentName" class="<%= isEmpty2 ? "emptystring" : "string"%>" value="${sessionScope.studentName}"/>
                    </td>
                    <% if (isEmpty2) {%>
                    <td class="errormessage">Enter the string!</td>
                    <% }%>
                </tr>
                <tr>
                    <td class="label">Enter the group id:</td>
                    <td>
                        <input type="text" name="groupId" class="<%= isEmpty3 ? "emptystring" : "string"%>" value="${sessionScope.groupId}"/>
                    </td>
                    <% if (isEmpty3) {%>
                    <td class="errormessage">Enter the string!</td>
                    <% }%>
                </tr>
            </table>
            <table>
                <tr>
                    <td><input type="submit" form="form1" name="operation1" value="Update" formaction="performstudentupdating.jsp"/></td>
                    <td><input type="submit" form="form1" name="operation2" value="Cancel" formaction="controller.jsp?message=cancelStudentUpdating"/></td>
                </tr>
            </table>
        </form>
    </body>
</html>
