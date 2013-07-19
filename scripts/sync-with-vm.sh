scriptdir=$(dirname $0)
dest='-e ssh root@192.168.33.10:/bahmni/OpenElis/'
sh $scriptdir/rsync-with-options.sh $scriptdir/../ $dest