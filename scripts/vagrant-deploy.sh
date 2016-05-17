PATH_OF_CURRENT_DIRECTORY="$(pwd)"
BAHMNI_HOME="$PATH_OF_CURRENT_DIRECTORY/../"
OPENELIS_HOME="$BAHMNI_HOME/OpenElis"

ant setupDB test dist
mkdir "$OPENELIS_HOME/openelis/dist/openelis"
cd "$OPENELIS_HOME/openelis/dist/"
unzip -q openelis.war -d "./openelis/."

cd "$BAHMNI_HOME/bahmni-vagrant/"

vagrant ssh -c "sudo service bahmni-lab stop"
vagrant ssh -c "sudo rm -rf /opt/bahmni-lab/bahmni-lab/*"
vagrant ssh -c "sudo cp -r /bahmni/OpenElis/openelis/dist/openelis/* /opt/bahmni-lab/bahmni-lab/."
vagrant ssh -c "sudo chown -R vagrant:vagrant /opt/bahmni-lab/bahmni-lab/*"
vagrant ssh -c "sudo service bahmni-lab start"

cd "$PATH_OF_CURRENT_DIRECTORY"