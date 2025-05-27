#!/bin/bash
# Script de lancement du jeu Dungeon Escape.
# Ce script vérifie si le jar est présent, et s'il est à jour.
# sinon il le compile avec Maven puis le lance.

# Récupération du dernier jar dans le dossier target
JAR_FILE=$(ls -t target/*.jar 2>/dev/null | head -n 1)

if [ -f "pom.xml" ]; then
    # Si le jar n'existe pas dans target, ou s'il est plus ancien que le pom, on compile
    if [ -z "$JAR_FILE" ] || [ "$JAR_FILE" -ot target/pom.xml ]; then
      echo "Le jar est absent ou obsolète, compilation en cours..."
      mvn clean install
      if [ $? -ne 0 ]; then
        echo "Échec de la compilation Maven."
        exit 1
      fi
    fi

  # Récupération de la version du jeu dans le jar (via MANIFEST.MF)
  JAR_VERSION=$(unzip -p "$JAR_FILE" META-INF/MANIFEST.MF | grep "Implementation-Version" | awk '{print $2}')

  if [ -z "$JAR_VERSION" ]; then
    echo "Impossible de récupérer la version dans le jar."
  fi

  # Récupération de la version du pom.xml
  POM_VERSION=$(grep -A5 "<groupId>com.rva</groupId>" pom.xml | grep "<version>" | sed -E 's/.*<version>([^<]*)<\/version>.*/\1/')

  if [ -z "$POM_VERSION" ]; then
    echo "Impossible de récupérer la version dans le pom.xml."
  fi

  # Comparaison des versions et compilation si nécessaire
  if [ "$JAR_VERSION" != "$POM_VERSION" ]; then
    mvn clean install
    if [ $? -ne 0 ]; then
      echo "Échec lors de la recompile Maven."
      exit 1
    fi
  fi
  JAR_FILE=$(ls -t target/*.jar 2>/dev/null | head -n 1)
fi

# Lancement du jeu
if [ ! -f "$JAR_FILE" ]; then
  echo "Le jar n'a pas été trouvé dans le dossier target."
  exit 1
fi
echo "Lancement de Dungeon Escape..."
java -jar "$JAR_FILE"