<%@taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    String studentId = request.getParameter("studentId");
    if (studentId != null) {
        session.setAttribute("studentId", studentId);
        response.sendRedirect("submitstudentdeleting.jsp");
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
        <h1>Select student for deleting</h1>
        <form method="post" action="startstudentdeleting.jsp">
            <table>
                <tr>
                    <th class="label">ID</th>
                    <th class="label">Name</th>
                    <th class="label">Group ID</th>
                </tr>
                <x:parse xml="${sessionScope.students}" var="students"/>
                <x:forEach select="$students/root/student" var="student">
                    <tr>
                        <td class="label"><x:out select="$student/@studentid"/></td>
                        <td class="label"><x:out select="$student/@studentname"/></td>
                        <td class="label"><x:out select="$student/@groupid"/></td>
                    </tr>
                </x:forEach>
                <tr>
                    <td class="label" colspan="3">
                        <select name="studentId" value="${param.studentId}">
                            <x:parse xml="${sessionScope.students}" var="students"/>
                            <x:forEach select="$students/root/student" var="student">
                                <option><x:out select="$student/@studentid"/></option>
                            </x:forEach>
                        </select>
                        <input type="submit" value="Delete" name="operation"/>
                    </td>
                </tr>
                <tr>
                    <td class="label" colspan="3">
                        Go to <a href="index.jsp">home page</a>
                    </td>
                </tr>
            </table>
        </form>
    </body>
</html>
