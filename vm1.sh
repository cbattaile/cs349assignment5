ps -ef | grep java | grep -v grep | awk '{print $2}' | xargs kill
ps -ef | grep rmiregistry | grep -v grep | awk '{print $2}' | xargs kill
javac *.java
rmiregistry &
java SuperMaster 54.161.104.10