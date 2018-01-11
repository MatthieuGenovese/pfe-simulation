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
        <label for="household">Nombre de household:</label> <input type="text" name="household" />
    </p>
    <p>
            <label  for="investor">Nombre de investor:</label>  <input type="text" name="investor" />
    </p>
    <p>
            <label  for="promoter">Nombre de promoteur:</label>  <input type="text" name="promoter" />
    </p>
    <p>
            <label  for="etape">Nombre de étapes:</label>  <input type="text" name="etape" />
    </p>
    <p>
            <label  for="listT">Liste des transports:</label>  <input type="text" name="listT" />
    </p>
    <p>
           <label  for="listE">Liste des equipements:</label> <input type="text" name="listE" />
    </p>
    <p>
                  <label  for="fileHousehold">File of households :</label> <input type="text" name="fileHousehold">
                 </p>
                    <p>
                            <label  for="fileInvestor">File of investors :</label>  <input type="text" name="fileInvestor" />
                        </p>
                        <p>
                            <label  for="filePromoter">File of promoters :</label>  <input type="text" name="filePromoter" />
                        </p>
                        <p>
                                    <input type="submit" value="Envoyer" />
                            </p>
</form>
<form method="post">
<p>
        <label for="action">Entité:</label> <select name="action" id="type">
                                            <option value="confirmer">confirmer</option>
                                            <option value="réinitialiser">réinitlialiiser</option>

                                            </select>
    </p>

                        <p>
                                    <input type="submit" value="Envoyer" />
                            </p>
</form>

</div>
</body>
</html>
