PATH_OF_CURRENT_DIRECTORY="$(pwd)"

source $PATH_OF_CURRENT_DIRECTORY/scripts/vagrant/vagrant_functions.sh

BAHMNI_HOME="$PATH_OF_CURRENT_DIRECTORY/../"
OPENELIS_HOME="$BAHMNI_HOME/OpenElis"

ant dist
mkdir "$OPENELIS_HOME/openelis/dist/openelis"
cd "$OPENELIS_HOME/openelis/dist/"
unzip -q openelis.war -d "./openelis/."

cd "$BAHMNI_HOME/bahmni-vagrant/"

run_in_vagrant -c "sudo service bahmni-lab stop"
run_in_vagrant -c "sudo rm -rf /opt/bahmni-lab/bahmni-lab/*"
run_in_vagrant -c "sudo cp -r /bahmni/OpenElis/openelis/dist/openelis/* /opt/bahmni-lab/bahmni-lab/."
run_in_vagrant -c "sudo chown -R bahmni:bahmni /opt/bahmni-lab/bahmni-lab/*"
run_in_vagrant -c "sudo service bahmni-lab start"

cd "$PATH_OF_CURRENT_DIRECTORY"