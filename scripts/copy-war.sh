scriptdir=$(dirname $0)
ssh root@192.168.33.10 'rm -rf /home/jss/apache-tomcat-8.0.12/webapps/openelis'
scp $scriptdir/../openelis/dist/openelis.war root@192.168.33.10:/home/jss/apache-tomcat-8.0.12/webapps