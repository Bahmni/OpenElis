scriptdir=$(dirname $0)
dest='-e ssh root@192.168.33.10:/home/jss/apache-tomcat-7.0.22/webapps/openelis'
sh $scriptdir/rsync-with-options.sh $scriptdir/../openelis/WebContent/ $dest




