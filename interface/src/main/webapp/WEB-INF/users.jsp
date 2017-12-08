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
    <title>Bogota Simulator</title>
</head>
<body>
<center>
<div>
<form method="post">
<form method="post" action="traitement.php">
    <p>
        <label>Nombre de household</label> : <input type="text" name="household" />
    </p>
    <p>
            <label>Nombre de investor</label> : <input type="text" name="investor" />
    </p>
    <p>
            <label>Nombre de promoteur</label> : <input type="text" name="promoter" />
    </p>
    <p>
            <label>Nombre de étapes</label> : <input type="text" name="etape" />
    </p>
    <p>
            <label>Liste des transports</label> : <input type="text" name="listT" />
    </p>
    <p>
           <label>Liste des equipements</label> : <input type="text" name="listE" />
    </p>
    <p>
            <input type="submit" value="Envoyer" />
    </p>
</form>
</form>
</div>
</center>
</body>
</html>
