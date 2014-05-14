#/bin/sh
# Adds license information to all java files

PATH_OF_CURRENT_SCRIPT="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
ROOT_DIR=$PATH_OF_CURRENT_SCRIPT/..


#get all java files that do not have the word Mozilla in them
echo "Finding files that do not have license information"
grep -RL Mozilla $ROOT_DIR | grep -v svn-base | grep "^.*\.java$" | grep -v BarbecueRenderer > /tmp/javafiles
echo Number of files found: `cat /tmp/javafiles | wc -l ` 
cat /tmp/javafiles


#put license information into them
echo "Adding license information to files"
for file in `cat /tmp/javafiles`; do cat $ROOT_DIR/scripts/license-java $file > /tmp/tempfile; mv /tmp/tempfile $file; done

#find all java files
find $ROOT_DIR -name *.java > /tmp/javafiles

#Replace /** with /* so that it shows up as license/comments and not javadoc
for file in `cat /tmp/javafiles`; do sed -i -e '1s|/\*\*|/\*|g' $file; done

#Remove all sed temp files
echo "Cleaning up"
find $ROOT_DIR -name *-e | xargs rm

echo "Done"
