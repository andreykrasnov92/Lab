<%@taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page errorPage="errorMessage.jsp"%>
<%
    String studentName = request.getParameter("studentName");
    if (studentName != null) {
        session.setAttribute("studentName", studentName);
        response.sendRedirect("controller.jsp?message=performStudentsFinding");
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
        <h1>Find students by name</h1>
        <form method="post" action="startstudentsfinding.jsp">
            <table>
                <tr>
                    <td class="label">
                        <select name="studentName" value="${param.studentName}">
                            <x:parse xml="${sessionScope.students}" var="students"/>
                            <x:forEach select="$students/root/student" var="student">
                                <option><x:out select="$student/@studentname"/></option>
                            </x:forEach>
                        </select>
                        <input type="submit" value="Find students" name="operation"/>
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
