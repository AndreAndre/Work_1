<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %> <%--Без этой строки почему то не работает --%>

<html>
<head>
    <title>Список домов</title>
</head>
<body>
<h1>${text_h1}</h1>


<table border="1">
<c:forEach items="${houses}" var="houseEntity">
        <tr>
            <td>${houseEntity.id}</td>
            <td>${houseEntity.address}</td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
