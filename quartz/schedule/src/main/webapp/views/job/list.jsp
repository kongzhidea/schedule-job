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
    <title>任务列表</title>
    <jsp:include page="/views/inc/head.jsp"/>
</head>
<body class="no-padding">

<jsp:include page="/views/inc/header.jsp"/>

<div id="sb-site" class="top-padding">
    <div class="container-fluid">
        <div class="row-fluid">

            <div class="page-header">
                <h1>
                    <a href="/job/list">任务列表</a>
                </h1>
                <span>
                    <a href="view?id=0" target="_blank">添加任务</a>
                    <a href="cron_expression" target="_blank">任务表达式详解</a>
                </span>
            </div>
            <a class="btn btn-link" data-target="#user-search-form" data-toggle="collapse">显示隐藏查询栏</a>
            <div id="user-search-form" class="collapse in">
                <form action="/job/list" class="form-horizontal" >
                    <p>
                        <label >
                            Id: <input class="control"  name="id" type="text" value="${param.id}" style="width: 50px;"/>
                        </label>

                        <label class="mgl50">
                             任务名: <input class="control"  name="jobName" type="text" value="${param.jobName}" />
                        </label>

                        <label class="mgl50">
                             任务组: <input class="control"  name="jobGroup" type="text" value="${param.jobGroup}" />
                        </label>

                        <label >
                            状态:
                            <select  name="status">
                                <option value="" >全部</option>
                                <option value="1" <c:if test="${param.status==1}">selected="selected"</c:if>>调度中</option>
                                <option value="2" <c:if test="${param.status==2}">selected="selected"</c:if>>暂停</option>
                            </select>
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
                    <th>表达式</th>
                    <th>描述</th>
                    <th>维护人</th>
                    <th>更新时间</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${list}" var="item">
                    <tr>
                        <td>${item.id}</td>
                        <td>${item.jobName}</td>
                        <td>${item.jobGroup}</td>
                        <td>${item.statusDesc}</td>
                        <td>${item.scheduleTime}</td>
                        <td>${item.description}</td>
                        <td>${item.updateUserName}</td>
                        <td><fmt:formatDate value="${item.uptime}" pattern="yyyy-MM-dd HH:mm:ss" /> </td>
                        <td>
                            <a href="view?id=${item.id}" target="_blank">编辑</a>

                            <a href="javascript:void(0);" class="delete"
                                   data-id="${item.id}" data-name="${item.jobName}">删除</a>
                            <c:if test="${item.status == 0}">
                                <a href="javascript:void(0);" class="delete"
                                   data-id="${item.id}" data-name="${item.jobName}">删除</a>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            <jsp:include page="/views/inc/fenye.jsp"/>
        </div>
    </div>
</div>

<script type="text/javascript">
    $(document).ready(function () {
        $(".delete").click(function(){
              var id = $(this).attr("data-id");
              var name = $(this).attr("data-name");
              bootbox.confirm("确认删除:" + name, function(result){
                  if(result){
                      $.ajax({
                          type: "post",
                          data: {"id":id},
                          url: "delete",
                          success: function (ret) {
                              if(ret.code != 0){
                                  bootbox.alert(ret.msg);
                              }else{
                                  window.location.reload();
                              }

                          },
                          error: function () {
                              bootbox.alert("请求失败");
                          }
                      });
                  }
              });
        });
    });
</script>

<script src="/static/js/common/fenye.js"></script>

<jsp:include page="/views/inc/leftbar.jsp" />
</body>
</html>
