<%@page contentType="text/html" pageEncoding="UTF-8"%>
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
        <form method="post" action="index.jsp">
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
                        <a href="readgroups.jsp">Read groups</a>
                    </td>
                    <td class="menuitem">
                        <a href="readstudents.jsp">Read students</a>
                    </td>
                </tr>
                <tr>
                    <td class="menuitem">
                        <a href="findgroups.jsp">Find groups by name</a>
                    </td>
                    <td class="menuitem">
                        <a href="findstudents.jsp">Find students by name</a>
                    </td>
                </tr>
                <tr>
                    <td class="menuitem">
                        <a href="updategroup.jsp">Update group</a>
                    </td>
                    <td class="menuitem">
                        <a href="updatestudent.jsp">Update student</a>
                    </td>
                </tr>
                <tr>
                    <td class="menuitem">
                        <a href="deletegroup.jsp">Delete group</a>
                    </td>
                    <td class="menuitem">
                        <a href="deletestudent.jsp">Delete student</a>
                    </td>
                </tr>
            </table>
        </form>
    </body>
</html>
