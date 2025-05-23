#!/bin/bash
# Script de lacement du jeu

# Vérifie si un jar existe dans target/
JAR_FILE=$(ls -t target/*.jar 2>/dev/null | head -n 1)

if [ -z "$JAR_FILE" ]; then
    echo "Aucun jar trouvé dans target/. Lancement de Maven pour compiler..."
    mvn clean install
    if [ $? -ne 0 ]; then
        echo "Échec de la compilation Maven."
        exit 1
    fi
    # Après compilation, on récupère le jar
    JAR_FILE=$(ls -t target/*.jar | head -n 1)
    if [ -z "$JAR_FILE" ]; then
        echo "Aucun jar n'a été généré après Maven."
        exit 1
    fi
fi

echo "Lancement du jeu : $JAR_FILE"
java -jar "$JAR_FILE"