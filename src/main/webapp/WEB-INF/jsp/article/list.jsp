<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ include file="../part/head.jspf" %>

<style>
.title {
	text-align:center;
	margin:30px;
}
.title >.name {
	text-decoration:none;
}
.body {
	height:100%;
}

.table-box > table > thead {
	/*background-color:#343a40;*/
	color:crimson;
	text-align-last:center;
}

.table-box > table > tbody{
	text-align-last:center;
}

table {
	width:100%;
	border-top:3px double rgba(0,0,0,0.1);
	border-collapse: collapse;
}
tr,td {
	border-bottom:1px solid rgba(0,0,0,0.1);
	padding: 10px;
	
}
thead > tr > th{
	border-right:none;
	border-left:none;
}

table > tbody > tr > td > a{
	text-decoration:none;
	color:black;
}

table > tbody > tr > td > a:hover {
	text-decoration:none;
	color:crimson;
}

</style>
<div class = "title h3" style="letter-spacing:-5px;">
	${board.name} 게시판
</div>
<div class="table-box table-box-data con">
    <table class="">
        <colgroup>
            <col width="100" />
            <col width="200" />
            <col width="700" />
        </colgroup>
        <thead>
            <tr>
                <th>번호</th>
                <th>날짜</th>
                <th>제목</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${articles}" var="article">
            	<c:if test="${article.report <= 10}">
                <tr>
                    <td>${article.id}</td>
                    <td>${article.regDate}</td>
                    <td>
                        <a href="${article.getDetailLink(board.code)}" class="block width-100p text-overflow-el">${article.forPrintTitle}</a>
                    </td>
                </tr>
                </c:if>
                <c:if test="${article.report > 10 && article.report < 30}">
                <tr>
                    <td>${article.id}</td>
                    <td>${article.regDate}</td>
                    <td>
                        <a href="${article.getDetailLink(board.code)}" class="block width-100p text-overflow-el">신고가 누적된 게시물입니다.</a>
                    </td>
                </tr>
                </c:if>
                <c:if test="${article.report > 30 }">
                	<td>${article.id}</td>
                	<td>${article.regDate}</td>
                	<td>신고가 누적되어 삭제되었습니다.</td>
                </c:if>
            </c:forEach>
        </tbody>
    </table>
</div>
<div class="btn-box con margin-top-20">
	<c:if test="${board.id == '2'}">
		<c:if test="${member.level == 10}">
   		<a class="btn btn-outline-danger btn-sm" href="./${board.code}-write">글쓰기</a>
		</c:if>
	</c:if>
	<c:if test="${board.code != 'notice'}">
    <a class="btn btn-outline-danger btn-sm" href="./${board.code}-write">글쓰기</a>
	</c:if>
</div>

<%@ include file="../part/foot.jspf" %>