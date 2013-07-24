scriptdir=$(dirname $0)
src='-e ssh root@192.168.33.10:/bahmni/OpenElis/junit'
sh $scriptdir/rsync-with-options.sh $src .