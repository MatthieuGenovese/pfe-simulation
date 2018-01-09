<%--
  Created by IntelliJ IDEA.
  User: thomas
  Date: 30/10/17
  Time: 17:30
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <link rel="stylesheet" type="text/css" href="../ressource/css/style.css">
    <title>Bogota Simulator</title>
</head>
<body>
<form method="post">
    <p>
        <label for="entity">Entité:</label> <select name="entity" id="type">
                                            <option value="household">household</option>
                                            <option value="promoter">promoter</option>
                                            <option value="investor">Demande</option>
                                            <option value="land">Offre</option>
                                            <option value="property">Demande</option>
                                            <option value="configuration">Demande</option>

                                            </select>
    </p>
    <p>
       <label  for="id">Identifiant:</label> <input type="number" name="id" />
    </p>
     <p>
        <input type="submit" value="Envoyer" />
     </p>
</form>
</div>
</body>
</html>
