scriptdir=$(dirname $0)
dest='-e ssh root@192.168.33.10:$TOMCAT_HOME/webapps/openelis/'
sh $scriptdir/rsync-with-options.sh openelis/WebContent/pages $dest