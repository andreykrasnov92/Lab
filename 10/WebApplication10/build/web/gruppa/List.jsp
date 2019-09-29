<%@taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page errorPage="errorMessage.jsp"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <link rel="stylesheet" type="text/css" href="styles/style.css"/>
        <link rel="shortcut icon" href="images/favicon.png" type="image/png"/>
        <title>University</title>
    </head>
    <body>
        <h1>Groups</h1>
        <form>
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
                        <td><a href="controller.jsp?message=viewGroup&groupid=<x:out select="$group/@groupid"/>">View</a></td>
                        <td><a href="controller.jsp?message=updateGroup&groupid=<x:out select="$group/@groupid"/>">Edit</a></td>
                        <td><a href="controller.jsp?message=deleteGroup&groupid=<x:out select="$group/@groupid"/>">Destroy</a></td>
                        <td><a href="controller.jsp?message=findGroupsByName&groupname=<x:out select="$group/@groupname"/>">Find group by name</a></td>
                    </tr>
                </x:forEach>
                <tr>
                    <td><a href="controller.jsp?message=createGroup">Create new group</a></td>
                </tr>
                <tr>
                    <td class="label" colspan="2">
                        <a href="index.jsp">Home page</a>
                    </td>
                </tr>
            </table>
        </form>
    </body>
</html>
