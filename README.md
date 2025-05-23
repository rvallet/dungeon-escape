# Dungeon Escape

Dungeon Escape est un jeu de donjon textuel en Java où le joueur doit s'échapper en explorant des salles, combattant des ennemis et résolvant des énigmes.

## Fonctionnalités

- Exploration de salles
- Combat contre des ennemis
- Gestion d'inventaire
- Sauvegarde des scores (via MongoDB)
- Commande en ligne de commande

## Technologies utilisées

- Java 21
- Maven 3.8
- Spring Boot 3.4
- ~~MongoDB 8 (en cours d'implémentation)~~

## Démarrer le projet

### Clonage du dépôt
Pour cloner le dépôt, utilisez la commande suivante dans votre dossier d'applications :
```bash
git clone https://github.com/rvallet/dungeon-escape.git
```

### Linux : Script automatisé (effectue le build maven si nécéssaire et lancement du jeu)
```bash
cd dungeon-escape
chmod +x launch.sh
./launch.sh
```

### Construction du projet (sous Windows à l'aide de Git Bash ou equivalent)
Construire le projet avec Maven :
```bash
mvn clean install
```

### Exécution du projet (sous Windows à l'aide de Git Bash ou equivalent)
Lancer le projet à partir du repertoire 'dungeon-escape' :
```bash
java -jar $(ls -t target/*.jar | head -n 1)
```

### Licence
Ce projet est sous licence MIT. Voir le fichier [LICENSE](LICENSE) pour plus de détails.