<%@ page import="java.util.List" %>
<%@ page import="cn.updev.Events.Static.IEvent" %>
<%--
  Created by IntelliJ IDEA.
  User: hypo
  Date: 15-11-22
  Time: 下午11:02
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
  List<IEvent> events = (List<IEvent>) request.getAttribute("events");
  Integer groupId = (Integer) request.getAttribute("groupId");
%>
<!DOCTYPE html>
<html>
<head>
  <%@include file="../static/head.jsp"%>
  <title>编辑事件组信息 - SpongeTime仪表盘</title>
</head>
<body>
<%@include file="../static/nav.jsp"%>
<%@include file="../static/sideber.jsp"%>
<div class="col-sm-9 col-sm-offset-3 col-lg-10 col-lg-offset-2 main">
  <div class="row">
    <ol class="breadcrumb">
      <li><a href="/admin"><span class="glyphicon glyphicon-home"></span></a></li>
      <li>Dashboard</li>
      <li>管理任务</li>
      <li class="active">编辑事件组</li>
    </ol>
  </div><!--/.row-->

  <div class="row">
    <div class="col-lg-12">
      <h3 class="page-header">编辑事件组</h3>
      <p>您正在编辑事件组 <span class="label label-info">${groupTitle}</span>，请注意，保存操作需谨慎</p>
    </div>
  </div><!--/.row-->
  <div class="row">
    <form action="/admin/saveEventGroup" method="POST">
      <input type="hidden" name="user" value="1">
      <div class="col-lg-4 col-lg-offset-1" style="padding-top: 25px;">
        <div class="form-group">
          <label for="title">事件组名</label>
          <input type="text" class="form-control" id="title" name="groupTitle" placeholder="标题" value="${groupTitle}">
        </div>
        <div class="form-group">
          <label for="expectTime">理想完成时间</label>
          <input type="date" class="form-control" id="expectTime" name="groupExpect" placeholder="时间" value="${groupExpectTime}">
          <small>* 事件通知时间，进度预期计算参考。</small>
        </div>
        <div class="form-group">
          <label for="weight">事件组权重</label>
          <% Integer weight = (Integer) request.getAttribute("weight");%>
          <select class="form-control" id="weight" name="weight">
            <option value="3" <%=weight == 3 ? "selected ":""%>style="color:#5cb85c;">不紧急不重要</option>
            <option value="2" <%=weight == 2 ? "selected ":""%>style="color:#5bc0de;">不紧急但重要</option>
            <option value="1" <%=weight == 1 ? "selected ":""%>style="color:#f0ad4e;">紧急&nbsp;&nbsp;&nbsp;&nbsp;不重要</option>
            <option value="0" <%=weight == 0 ? "selected ":""%>style="color:#d9534f;">紧急&nbsp;&nbsp;且&nbsp;&nbsp;重要</option>
          </select>
          <small>* TODO列表排序依据。</small>
        </div>
        <button type="submit" class="btn btn-info" title="请确保事件列表为您所期望状态后保存">保存</button>
        <a href="/admin/eventsmanage?groupId=<%=groupId%>" class="btn btn-default" title="取消后本页所有修改将不会被保存">取消</a>
      </div><!--/.col-lg-4 col-lg-offset-1-->
      <div class="col-lg-5 col-lg-offset-1" style="padding-bottom: 45px; padding-top: 24px;">
        <div class="eventlist">
          <%
            if(events != null){
              Integer no = 0;
              for(IEvent event : events){
                no++;
          %>
          <div class="form-group event" id="event_<%=no%>">
            <label for="title">事件No. <%=no%></label>
            <input type="text" class="form-control" name="eventTitle<%=no%>" placeholder="任务<%=no%>" value="<%=event.getEventTitle()%>">
          </div>
          <%
            }}
          %>
        </div>
        <hr/>
        <div class="row">
          <div class="col-md-3 col-md-offset-9">
            <button type="button" class="btn btn-default add_event" title="新增事件"><span class="glyphicon glyphicon-plus" aria-hidden="true"></span></button>
            <button type="button" class="btn btn-default del_event" title="删减事件"><span class="glyphicon glyphicon-minus" aria-hidden="true"></span></button>
          </div>
        </div>
      </div><!--/.col-lg-5 col-lg-offset-1-->
    </form>
  </div><!--/.row-->
</div>	<!--/.main-->
<script src="/static/js/jquery-1.11.1.min.js"></script>
<script src="/static/js/bootstrap.min.js"></script>
<script src="/static/js/chart.min.js"></script>
<script src="/static/js/chart-data.js"></script>
<script src="/static/js/easypiechart.js"></script>
<script src="/static/js/easypiechart-data.js"></script>
<script src="/static/js/bootstrap-datepicker.js"></script>
<script>
  function add_event() {
    $(".add_event").click(function(){
      var event_num = $(".event").length;
      if (event_num >= 100)
        alert("抱歉，每个事件组目前支持最大事件数为100。");
      else {
        $(".eventlist").append(
                "<div class='form-group event' id='event_" + (event_num + 1) + "'>"+
                "<label>"+
                "事件 No. " + (event_num + 1) +
                "</label>"+
                "<input type='text' class='form-control' name='eventTitle" + (event_num + 1) +
                "' placeholder='任务" + (event_num + 1) + "'>"+
                "</div>"
        );
      }
    });
  }
  function del_event() {
    $(".del_event").click(function() {
      var event_num = $(".event").length;
      if(event_num > 2) {
        $("#event_" + event_num).remove();
      }
      else
        alert("事件组中的事件数不能少于2个。_(:з」∠)_");
    });
  }
  $(document).ready(function(){
    add_event();
    del_event();
  });
</script>
</body>
</html>
