#!/bin/bash

set -euo pipefail

# Prevent accidental execution outside of Travis:
if [ -z "${TRAVIS+false}" ]
then
  echo "TRAVIS environment variable is not set"
  exit 1
fi

#setting some required jdk urls 
export JDK9_EA_STABLE_URL=http://download.java.net/java/jdk9/archive/178/binaries/jdk-9+178_linux-x64_bin.tar.gz
export JDK9_EA_URL=http://download.java.net/java/jdk9/archive/178/binaries/jdk-9+178_linux-x64_bin.tar.gz
export JDK8_EA_URL=http://download.java.net/java/jdk8u152/archive/b05/binaries/jdk-8u152-ea-bin-b05-linux-x64-20_jun_2017.tar.gz

# Switch to desired JDK, download if required:
function install_jdk {
  JDK_URL=$1

  FILENAME="${JDK_URL##*/}"

  rm -rf /tmp/jdk/$JDK
  mkdir -p /tmp/jdk/$JDK

  if [ ! -f "/tmp/jdk/$FILENAME" ]
  then
    curl -L $JDK_URL -o /tmp/jdk/$FILENAME
  fi

  tar -xzf /tmp/jdk/$FILENAME -C /tmp/jdk/$JDK --strip-components 1

  if [ -z "${2+false}" ]
  then
    export JAVA_HOME="/tmp/jdk/$JDK"
    export JDK_HOME="${JAVA_HOME}"
    export JAVAC="${JAVA_HOME}/bin/javac"
    export PATH="${JAVA_HOME}/bin:${PATH}"
  fi
}

function install_ibm_jdk {

uname -a
lsb_release -a

echo "space on this box: "
df -h

echo "space on this drive:"
df -h .

wget http://public.dhe.ibm.com/ibmdl/export/pub/systems/cloud/runtimes/java/8.0.4.7/linux/x86_64/ibm-java-sdk-8.0-4.7-x86_64-archive.bin

mv ibm-java-sdk-8.0-4.7-x86_64-archive.bin /tmp/ibmjdk

chmod 755 /tmp/ibmjdk/ibm-java-sdk-8.0-4.7-x86_64-archive.bin

printf "4\n1\n\n\n\n\nY\n\n\n\n" | /tmp/ibmjdk/ibm-java-sdk-8.0-4.7-x86_64-archive.bin

export JAVA_HOME="/tmp/ibmjdk/ibm-java-x86_64-80"
export JDK_HOME="${JAVA_HOME}"
export JAVAC="${JAVA_HOME}/bin/javac"
export PATH="${JAVA_HOME}/bin:${PATH}"

}


source $HOME/.jdk_switcher_rc
case "$JDK" in
6)
  jdk_switcher use openjdk6
  ;;
7|8)
  jdk_switcher use oraclejdk${JDK}
  ;;
8-ea)
  install_jdk $JDK8_EA_URL
  ;;
9-ea)
  install_jdk $JDK9_EA_URL
  ;;
9-ea-stable)
  install_jdk $JDK9_EA_STABLE_URL
  ;;
8-ibm)
  install_ibm_jdk
  ;;
esac

# Do not use "~/.mavenrc" set by Travis (https://github.com/travis-ci/travis-ci/issues/3893),
# because it prevents execution of JaCoCo during integration tests for jacoco-maven-plugin,
# and "-XMaxPermSize" not supported by JDK 9
export MAVEN_SKIP_RC=true

# Build:
# TODO(Godin): see https://github.com/jacoco/jacoco/issues/300 about "bytecode.version"
case "$JDK" in
6)
  mvn -V -B -e clean verify apache-rat:check clirr:check checkstyle:check findbugs:check javadoc:javadoc -Dbytecode.version=1.6
  ;;
7)
  mvn -V -B -e clean verify apache-rat:check clirr:check checkstyle:check findbugs:check javadoc:javadoc -Dbytecode.version=1.7
  ;;
8 | 8-ea | 8-ibm)
  mvn -V -B -e clean verify apache-rat:check clirr:check checkstyle:check findbugs:check javadoc:javadoc -Dbytecode.version=1.8 -Decj=${ECJ:-}
  ;;
9-ea | 9-ea-stable)
  # Groovy version should be updated to get rid of "--add-opens" options (see https://twitter.com/CedricChampeau/status/807285853580103684)
  export MAVEN_OPTS="--add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.io=ALL-UNNAMED"

  # see https://bugs.openjdk.java.net/browse/JDK-8131041 about "java.locale.providers"
  mvn -V -B -e clean verify apache-rat:check clirr:check checkstyle:check findbugs:check javadoc:javadoc -Dbytecode.version=1.9 \
    -DargLine=-Djava.locale.providers=JRE,SPI
  ;;
*)
  echo "Incorrect JDK [$JDK]"
  exit 1;
  ;;
esac
