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
    <title>
       任务表达式详解
    </title>
    <jsp:include page="/views/inc/head.jsp"/>
</head>
<body class="no-padding">

<jsp:include page="/views/inc/header.jsp"/>
<div id="sb-site" class="top-padding">
    <div class="container-fluid">

     <form >
        <span>表达式：</span> <input   name="cronExpression"  size="50" type="text" value="${scheduleTime}" />
        <input  type="submit" value="提交" />
     </form>
     <br/>
        <span>表达式解析:</span>
        <textarea rows="10">${parsed}</textarea>
     <br/>
    <pre>
        反斜线（/）字符表示增量值。例如，在秒字段中“5/15”代表从第 5 秒开始，每 15 秒一次。
        “？”字符仅被用于天（月）和天（星期）两个子表达式，表示不指定值 当2个子表达式其中之一被指定了值以后，为了避免冲突，需要将另一个子表达式的值设为“？”
            问号（?）字符和字母 L 字符只有在月内日期和周内日期字段中可用。问号表示这个字段不包含具体值。所以，如果指定月内日期，可以在周内日期字段中插入“?”，表示周内日期值无关紧要。
            字母 L 字符是 last 的缩写。放在月内日期字段中，表示安排在当月最后一天执行。在周内日期字段中，如果“L”单独存在，就等于“7”，否则代表当月内周内日期的最后一个实例。所以“0L”表示安排在当月的最后一个星期日执行。
         在月内日期字段中的字母（W）字符把执行安排在最靠近指定值的工作日。把“1W”放在月内日期字段中，表示把执行安排在当月的第一个工作日内。
        井号（#）字符为给定月份指定具体的工作日实例。把“MON#2”放在周内日期字段中，表示把任务安排在当月的第二个星期一。
         星号（*）字符是通配字符，表示该字段可以接受任何可能的值。
         减号(-) 表示一个范围
         逗号(,) 表示一个列表
    
         秒 0-59 , - * /
         分 0-59 , - * /
         小时 0-23 , - * /
         日期 1-31 , - * ? / L W C
        月份 1-12 或者 JAN-DEC , - * /
        星期 1-7 或者 SUN-SAT , - * ? / L C #
        年（可选） 留空, 1970-2099 , - * /
    
         表达式意义 示例:
         "0 0 12 * * ?" 每天中午12点触发
         "0 15 10 ? * *" 每天上午10:15触发
         "0 15 10 * * ?" 每天上午10:15触发
         "0 15 10 * * ? *" 每天上午10:15触发
         "0 15 10 * * ? 2005" 2005年的每天上午10:15触发
         "0 * 14 * * ?" 在每天下午2点到下午2:59期间的每1分钟触发
        "0 0/5 14 * * ?" 在每天下午2点到下午2:55期间的每5分钟触发
        "0 0/5 14,18 * * ?" 在每天下午2点到2:55期间和下午6点到6:55期间的每5分钟触发
        "0 0-5 14 * * ?" 在每天下午2点到下午2:05期间的每1分钟触发
        "0 10,44 14 ? 3 WED" 每年三月的星期三的下午2:10和2:44触发
        "0 15 10 ? * MON-FRI" 周一至周五的上午10:15触发
        "0 15 10 15 * ?" 每月15日上午10:15触发
         "0 15 10 L * ?" 每月最后一日的上午10:15触发
        "0 15 10 ? * 6L" 每月的最后一个星期五上午10:15触发
        "0 15 10 ? * 6L 2002-2005" 2002年至2005年的每月的最后一个星期五上午10:15触发
        "0 15 10 ? * 6#3" 每月的第三个星期五上午10:15触发
     </pre>
    </div>
</div>

<jsp:include page="/views/inc/leftbar.jsp" />

</body>
</html>
