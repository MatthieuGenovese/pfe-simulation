# kobdig-meili

## Introduction :

1. Installer Postgreql
2. Installer Postgis
3. Créer une base de données appeleée tomsa et un utilisateur appelé tomsa

## Execution :

1. Créer une commande maven : spring-boot:run

## Liste des commandes et leurs arguments :

### Lancer une simulation :

Cela se fait par le biais d'une commande curl, en voici un exemple :
```
curl -H "Content-Type: application/json" -d '{"type": "StateSimulatorMessage", "value": {"nbrHousehold": 50, "nbrInvestor": 50, "nbrPromoter": 50, "num":2, "listOfEquipment":[85,81], "listOfTransport":[176,794], "fileHousehold" : "householdAgent.apl", "fileInvestor" : "investorAgent.apl", "filePromoter" : "promoterAgent.apl"}}' localhost:8080/state
```

Détails des paramètres :  
  -num : c'est le nombre de tours que a accomplir la simulation
  -listOfEquipment : c'est la list d'équipement qui vont être pris en compte lors de la simulation (si ce paramètre n'est pas présent, la simulation se lance avec tous les equipements)  
  -listOfNetwork : même chose que pour equipements.
  
 ### Extraire les données d'une simulation
 
 Cela se fait également par commande curl voici un exemple :
```   
curl -H "Content-Type: application/json" -d '{"type": "ExtractDataMessage", "value": {"entity":"household", "idSimulation":"45"}}' localhost:8080/extract  
```

Avec cette commande, un dossier Simulation49 sera créé dans le dossier results du programme, a l'intérieur de ce dossier, tout le contenu de la collection Mongo des household de la simulation 45 sera extrait au format .csv.  
Les différents paramètres d'extraction sont : household, property, land, promoter, investor.
