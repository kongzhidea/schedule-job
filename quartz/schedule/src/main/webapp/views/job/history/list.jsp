<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge,chrome=1">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <title>${param.jobName}任务历史列表</title>
    <jsp:include page="/views/inc/head.jsp"/>

    <script src="http://www.my97.net/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
</head>
<body class="no-padding">

<jsp:include page="/views/inc/header.jsp"/>

<div id="sb-site" class="top-padding">
    <div class="container-fluid">
        <div class="row-fluid">

            <div class="page-header">
                <h1>
                    <a href="/job/history/list">所有任务历史列表</a>
                </h1>
            </div>
            <a class="btn btn-link" data-target="#user-search-form" data-toggle="collapse">显示隐藏查询栏</a>
            <div id="user-search-form" class="collapse in">
                <form action="/job/history/list" class="form-horizontal" >
                    <p>

                        <label class="mgl50">
                             任务名: <input class="control"  name="jobName" type="text" value="${param.jobName}" />
                        </label>
                        <label class="mgl50">
                             任务组: <input class="control"  style="width:60px;" name="jobGroup" type="text" value="${param.jobGroup}" />
                        </label>
                        <label class="mgl50">
                             开始时间起: <input class="control Wdate"  name="queryStartTime"  onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',startDate: '%y-%M-%d 00:00:00'})" value="${param.queryStartTime}" />
                        </label>
                        <label class="mgl50">
                             结束时间止: <input class="control Wdate"  name="queryEndTime"  onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',startDate: '%y-%M-%d 00:00:00'})" value="${param.queryEndTime}" />
                        </label>

                        <input class="btn btn-primary"  type="submit" value="查找" />
                    </p>
                </form>
            </div>
            <jsp:include page="/views/inc/fenye.jsp"/>
            <table class="table table-striped">
                <thead>
                <tr>
                    <th>Id</th>
                    <th>任务名</th>
                    <th>任务组</th>
                    <th>状态</th>
                    <th>开始时间</th>
                    <th>结束时间</th>
                    <th>执行主机名</th>
                    <th>执行主机IP</th>
                    <th>维护人</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${list}" var="item">
                    <tr>
                        <td>${item.id}</td>
                        <td><a href="/job/list?jobName=${item.jobName}" target="_blank">${item.jobName}</a></td>
                        <td>${item.jobGroup}</td>
                        <td>${item.jobStatusDesc}</td>
                         <td><fmt:formatDate value="${item.startTime}" pattern="yyyy-MM-dd HH:mm:ss" /> </td>
                          <td><fmt:formatDate value="${item.endTime}" pattern="yyyy-MM-dd HH:mm:ss" /> </td>
                        <td>${item.hostName}</td>
                        <td>${item.ip}</td>
                        <td>${item.updateUserName}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            <jsp:include page="/views/inc/fenye.jsp"/>
        </div>
    </div>
</div>

<script src="/static/js/common/fenye.js"></script>

<jsp:include page="/views/inc/leftbar.jsp" />
</body>
</html>
