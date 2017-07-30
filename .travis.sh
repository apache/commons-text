#!/bin/bash

set -euo pipefail

# Prevent accidental execution outside of Travis:
if [ -z "${TRAVIS+false}" ]
then
  echo "TRAVIS environment variable is not set"
  exit 1
fi

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

wget https://iwm.dhe.ibm.com/sdfdl/1v2/regs2/linuxjavasdks/java/java8/8/0/3/12/linuxia32/Xa.2/Xb.N2_vvPzUu_2JEIyVPPqRybA1WF-OqrzhAy4sMq3E9ws/Xc.8/0/3/12/linuxia32/ibm-java-sdk-8.0-3.12-i386-archive.bin/Xd./Xf.LPr.D1vk/Xg.9247250/Xi.swg-sdk8/XY.regsrvs/XZ.SWELeiUcWhSb2NGHFYK0DaN0aBc/ibm-java-sdk-8.0-3.12-i386-archive.bin

chmod 755 ibm-java-sdk-8.0-3.12-i386-archive.bin

printf "4\n1\n\n\n\n\nY\n\n\n\n" | ./ibm-java-sdk-8.0-3.12-i386-archive.bin

export JAVA_HOME="`pwd`/ibm-java-i386-80"
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
  mvn -V -B -e verify -Dbytecode.version=1.6
  ;;
7)
  mvn -V -B -e verify -Dbytecode.version=1.7
  ;;
8 | 8-ea | 8-ibm)
  mvn -V -B -e verify -Dbytecode.version=1.8 -Decj=${ECJ:-}
  ;;
9-ea | 9-ea-stable)
  # Groovy version should be updated to get rid of "--add-opens" options (see https://twitter.com/CedricChampeau/status/807285853580103684)
  export MAVEN_OPTS="--add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.io=ALL-UNNAMED"

  # see https://bugs.openjdk.java.net/browse/JDK-8131041 about "java.locale.providers"
  mvn -V -B -e verify -Dbytecode.version=1.9 \
    -DargLine=-Djava.locale.providers=JRE,SPI
  ;;
*)
  echo "Incorrect JDK [$JDK]"
  exit 1;
  ;;
esac
