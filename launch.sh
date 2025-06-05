#!/bin/bash
# Script de lancement du jeu Dungeon Escape avec option debug.
# Vérifie si le jar est présent et à jour, sinon compile avec Maven.

# Vérification de l'argument 'debug'
DEBUG=false
if [ "$1" == "debug" ]; then
  DEBUG=true
fi

# Fonction pour afficher en mode debug
function debug_echo {
  if $DEBUG; then
    echo "$1"
  fi
}

# Récupération du dernier jar dans le dossier target
JAR_FILE=$(ls -t target/*.jar 2>/dev/null | head -n 1)

if [ -f "pom.xml" ]; then
  # Vérifier la date du jar et du pom.xml si le jar existe
  if [ -f "$JAR_FILE" ]; then
    # Comparaison des dates
    if [ "$JAR_FILE" -nt "target/pom.xml" ]; then
      debug_echo "Le jar ($JAR_FILE) est plus récent que le pom.xml (target/pom.xml)."
    else
      debug_echo "Le jar ($JAR_FILE) est obsolète par rapport au pom.xml."
    fi
  fi

  # Si le jar est absent ou obsolète, compile
  if [ -z "$JAR_FILE" ] || [ ! "$JAR_FILE" -nt "target/pom.xml" ]; then
    echo "Le jar est absent ou obsolète, compilation en cours..."
    mvn clean install -q > /dev/null 2>&1
    if [ $? -ne 0 ]; then
      echo "Échec de la compilation Maven."
      exit 1
    fi
    JAR_FILE=$(ls -t target/*.jar 2>/dev/null | head -n 1)
  fi

  # Récupération de la version du jar via MANIFEST
  if [ -f "$JAR_FILE" ]; then
    JAR_VERSION=$(unzip -p "$JAR_FILE" META-INF/MANIFEST.MF | grep "Implementation-Version" | awk '{print $2}')
  else
    echo "Aucun fichier jar trouvé après compilation."
    exit 1
  fi

  debug_echo "Version du jar : $JAR_VERSION"

  # Récupération de la version du pom.xml
  POM_VERSION=$(grep -A5 "<groupId>com.rva</groupId>" pom.xml | grep "<version>" | sed -E 's/.*<version>([^<]*)<\/version>.*/\1/')

  debug_echo "Version du pom.xml : $POM_VERSION"

  # Vérification des versions
  if [ "$JAR_VERSION" != "$POM_VERSION" ]; then
    echo "Les versions diffèrent, recompilation nécessaire..."
    mvn clean install -q > /dev/null 2>&1
    if [ $? -ne 0 ]; then
      echo "Échec lors de la recompilation Maven."
      exit 1
    fi
    JAR_FILE=$(ls -t target/*.jar 2>/dev/null | head -n 1)
  fi
else
  # Si pas de pom.xml, on ne peut pas vérifier
  echo "Aucun fichier pom.xml trouvé. Vérification non effectuée."
fi

# Si le jar n'est toujours pas trouvé
if [ -z "$JAR_FILE" ] || [ ! -f "$JAR_FILE" ]; then
  echo "Le jar n'a pas été trouvé dans le dossier target."
  exit 1
fi

# Lancement du jeu
echo "Lancement de Dungeon Escape..."
java -jar "$JAR_FILE"