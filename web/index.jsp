<%--
  Created by IntelliJ IDEA.
  User: zhoukp
  Date: 2018/3/16
  Time: 21:45
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>$Title$</title>
  </head>
  <body>
  <h2>文件上传</h2>
  <form action="fileUpload" enctype="multipart/form-data" method="post">
    <input type="file" name="uploadFile">
    <input type="submit" value="上传">
  </form>
  </body>
</html>
