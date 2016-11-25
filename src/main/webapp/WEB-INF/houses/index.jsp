<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %> <%--Без этой строки почему то не работает --%>

<html>
<head>
    <title>Список домов</title>
    <%--<link rel="StyleSheet" type="text/css" href="./css/housesview/style.css">--%>
    <%-- <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/styles.css"> --%>
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/housesview/style.css'/>">
    </head>
    <body>
    /
<%-- Включаем в документ шапку из отдельного файла --%>
<jsp:include page="../template/header.jsp" flush="false"/>

<h1>${text_h1}</h1>

<a  href='http://localhost:8008/house/editable'><img title='Редактировать дом' src='<c:url value="/images/edit_32.png"/>'  width='16px'></a></br>
<table  cellspacing="2" border="1" cellpadding="2" width="960">
    <tr>
        <th>Адрес</th>
        <th>Количество этажей</th>
        <th>Дата постройки</th>
        <th width="120">Операции</th>
    </tr>


<%-- Выбираем что выводить --%>
<c:choose>
    <%-- Выводим просто таблицу --%>
    <c:when test="${op == 'view'}">
        <c:forEach items="${houses}" var="house">
            <tr>
                <td>${house.address}</td>
                <td>${house.floors}</td>
                <td>${house.buildDate}</td>
                <td>
                    <a href='./apartments?houseId=${house.id}'><img src='<c:url value="/images/apartments_32.png"/>' title='Список квартир' width='16px'></a>
                    <a href='/house/editRow?id=${house.id}'><img src='<c:url value="/images/edit_32.png"/>' title='Редактировать дом' width='16px'></a>
                    <a href='/house/remove?id=${house.id}'><img src='<c:url value="/images/remove_32.png"/>' title='Удалить дом' width='16px'></a>
                </td>
            </tr>
        </c:forEach>
    </c:when>
    <%-- Выводим таблицу для редактирования --%>
    <c:when test="${op == 'editableTable'}">
        <form action='/house/update' method='POST' accept-charset="UTF-8">
            <c:forEach items="${houses}" var="house">
                <tr>
                    <input type='hidden' name='houseID' value='${house.id}'>
                    <td><input type='text' placeholder='Адрес нового дома' value='${house.address}' name='address' style='width:100%; height:40px; border:0'></td>
                    <td><input type='number' placeholder='Этажей' value='${house.floors}' min=1 name='floors' style='width:100%; height:40px; border:0'></td>
                    <td><input type='date' placeholder='Дата постройки'value='${house.buildDate}' name='buildDate' style='width:100%; height:40px; border:0'></td>
                    <td></td>
                </tr>
            </c:forEach>
            <input type='submit' value='Сохранить'>
            <input type='reset' value='Отмена (на самом деле сброс)'>
        </form>
    </c:when>
    <%-- Выводим одну запись для редактирования --%>
    <c:when test="${op == 'editableRow'}">
        <form action='/house/update' method='POST' accept-charset="UTF-8">
            <c:forEach items="${houses}" var="house">
                <tr>
                    <c:if test="${id == house.id}">
                        <input type='hidden' name='houseID' value='${house.id}'>
                        <td><input type='text' placeholder='Адрес нового дома' value='${house.address}' name='address' style='width:100%; height:40px; border:0'></td>
                        <td><input type='number' placeholder='Этажей' value='${house.floors}' min=1 name='floors' style='width:100%; height:40px; border:0'></td>
                        <td><input type='date' placeholder='Дата постройки'value='${house.buildDate}' name='buildDate' style='width:100%; height:40px; border:0'></td>
                        <td><input type='submit' value='Сохранить изменения'><input type='reset' value='Отмена (на самом деле сброс)'></td>
                    </c:if>
                    <c:if test="${id != house.id}">
                        <td>${house.address}</td>
                        <td>${house.floors}</td>
                        <td>${house.buildDate}</td>
                        <td></td>
                    </c:if>
                </tr>
            </c:forEach>
        </form>
    </c:when>
    <c:otherwise>
        Не выбрано ни одно условие.
    </c:otherwise>
</c:choose>
    <form action='/house/add' method='POST' accept-charset=\"UTF-8\">
    <tr>
        <td><input type='text' placeholder='Адрес нового дома' name='address' style='width:100%; height:40px; border:0'></td>
        <td><input type='number' placeholder='Этажей' min=1 name='floors' style='width:100%; height:40px; border:0'></td>
        <td><input type='date' placeholder='Дата постройки' name='buildDate' style='width:100%; height:40px; border:0'></td>
        <input type='hidden' name='view' value='edit'>
        <td><input type='submit' value='Добавить дом'></td>
        </tr>
    </form>

</table>

<%-- Включаем в документ футер из отдельного файла --%>
<jsp:include page="../template/footer.jsp" flush="false"/>
</body>
</html>
