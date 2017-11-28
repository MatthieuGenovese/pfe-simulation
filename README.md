# kobdig-meili

## Introduction :

1. Installer Postgreql
2. Installer Postgis
3. Créer une base de données appeleée tomsa et un utilisateur appelé tomsa

## Execution :

1. Créer une commande maven : spring-boot:run
2. Lancer la commande
3. Dans un terminal, pour lancer la simulation :
curl -H "Content-Type: application/json" -d '{"type": "StateSimulatorMessage", "value": {"nbrHousehold": 10, "nbrInvestor": 10, "nbrPromoter": 10, "num":3, "id":2}}' localhost:8080/state
