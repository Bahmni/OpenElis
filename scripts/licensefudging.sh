#/bin/sh
cd ..

#get all java files that do not have the word Mozilla in them
grep -RL Mozilla . | grep -v svn-base | grep "^.*\.java$" > ../javafiles

#put license information into them
for file in `cat ../javafiles`; do cat ~/license-java $file > /tmp/tempfile; mv /tmp/tempfile $file; done

#find all java files
find . -name *.java > ../javafiles

#Replace /** with /* so that it shows up as license/comments and not javadoc
for file in `cat ../javafiles`; do sed -i -e '1s|/\*\*|/\*|g' $file; done

#Remove all sed temp files
find . -name *-e | xargs rm
