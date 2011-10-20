<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" %>

<%@ page import="com.rapleaf.hank.coordinator.*" %>
<%@ page import="com.rapleaf.hank.Hank" %>
<%
Coordinator coord = (Coordinator)getServletContext().getAttribute("coordinator");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
  <title>Management Home (Hank)</title>

  <jsp:include page='_head.jsp' />
</head>
<body>

  <jsp:include page='_top_nav.jsp' />


  <h1>Hank</h1>

  <div class='box-section'>
    <h2>System Summary</h2>

    <div class='box-section-content'>
      <%= coord.getDomains().size() %> <a href='domains.jsp'>domains</a>,
      <%= coord.getDomainGroups().size() %> <a href='domain_groups.jsp'>domain groups</a>,
      and <%= coord.getRingGroups().size() %> <a href='ring_groups.jsp'>ring groups</a>.
    </div>

  </div>

  <div class='box-section'>
    <h2>Coordinator</h2>
    <div class='box-section-content' style="font-family: courier new">
      <%= coord %>
    </div>
  </div>

  <div class='box-section'>
    <h2>Version Information</h2>
    <div class='box-section-content'>
      Hank version <%= Hank.getVersion() %>, commit <%= Hank.getGitCommit() %>
    </div>
    <div>
      Please report bugs on <a href="https://github.com/bryanduxbury/hank/issues">GitHub issues page</a>.
    </div>
  </div>

<jsp:include page="_footer.jsp"/>

</body>
</html>
