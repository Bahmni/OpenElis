scriptdir=$(dirname $0)
dest='-e ssh jss@192.168.33.10:/home/jss/apache-tomcat-8.0.12/webapps/openelis'
sh $scriptdir/rsync-with-options.sh $scriptdir/../openelis/WebContent/css $dest
sh $scriptdir/rsync-with-options.sh $scriptdir/../openelis/WebContent/images $dest
sh $scriptdir/rsync-with-options.sh $scriptdir/../openelis/WebContent/pages $dest
sh $scriptdir/rsync-with-options.sh $scriptdir/../openelis/WebContent/scripts $dest




