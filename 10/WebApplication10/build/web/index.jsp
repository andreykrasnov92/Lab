<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page errorPage="errorMessage.jsp"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <link rel="stylesheet" type="text/css" href="styles/style.css"/>
        <link rel="shortcut icon" href="images/favicon.png" type="image/png"/>
        <script type="text/javascript" src="scripts/script.js"></script>
        <title>University</title>
    </head>
    <body>
        <h1>University</h1>
        <table>
            <tr>
                <td class="menuitem">
                    <a href="creategroup.jsp">Create group</a>
                </td>
                <td class="menuitem">
                    <a href="createstudent.jsp">Create student</a>
                </td>
            </tr>
            <tr>
                <td class="menuitem">
                    <a href="controller.jsp?message=readGroups">Read groups</a>
                </td>
                <td class="menuitem">
                    <a href="controller.jsp?message=readStudents">Read students</a>
                </td>
            </tr>
            <tr>
                <td class="menuitem">
                    <a href="controller.jsp?message=startGroupsFinding">Find groups by name</a>
                </td>
                <td class="menuitem">
                    <a href="controller.jsp?message=startStudentsFinding">Find students by name</a>
                </td>
            </tr>
            <tr>
                <td class="menuitem">
                    <a href="controller.jsp?message=startGroupUpdating">Update group</a>
                </td>
                <td class="menuitem">
                    <a href="controller.jsp?message=startStudentUpdating">Update student</a>
                </td>
            </tr>
            <tr>
                <td class="menuitem">
                    <a href="controller.jsp?message=startGroupDeleting">Delete group</a>
                </td>
                <td class="menuitem">
                    <a href="controller.jsp?message=startStudentDeleting">Delete student</a>
                </td>
            </tr>
        </table>
    </body>
</html>
