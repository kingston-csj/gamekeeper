FROM ubuntu:20.04

ENV JDK_VERSION 17
ENV JDK_HOME /usr/local/java
ENV URL "https://download.java.net/openjdk/jdk17/ri/openjdk-17+35_linux-x64_bin.tar.gz"
RUN mkdir -p  $JDK_HOME
RUN apt-get update && \
    apt-get install -y wget && \
    wget $URL && \ 
    tar -zxvf openjdk-17+35_linux-x64_bin.tar.gz -C $JDK_HOME 

ENV JAVA_HOME=$JDK_HOME/jdk-17
ENV PATH=$PATH:$JAVA_HOME/bin 

WORKDIR /app

#CMD ["java","-jar","admin.jar","--spring.profiles.active=dev", "&"]
CMD java -jar admin.jar --spring.profiles.active=dev 