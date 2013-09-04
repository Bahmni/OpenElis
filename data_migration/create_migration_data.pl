#!/usr/bin/perl
use strict;
use warnings;

use Spreadsheet::Read;

my $workbook = ReadData ("lab_data.xlsx");

open(my $file, '>>', 'migration_data.sql') or die 'Could not create migration_data.sql file. Check your permissions';
my $sheet = $workbook -> [1];
my ( $row_min, $row_max ) = $sheet->{row_range};
my $row_max = $sheet->{maxrow};

for (my $row = 2 ; $row<= $row_max; $row++) 
{	
	create_migration_scripts_for_line($row, $file, $workbook);
}
close $file;


sub create_migration_scripts_for_line
{
	my ($line, $file, $workbook) = @_;

	my $select = "SELECT ";


	my $test_section_name = $workbook->[1]{"A" .$line};

	if ($test_section_name eq '')
	{
		return ;
	}

	print $file "-- Begin Line: $line \n";

	my $sample_type_name = $workbook->[1]{"B" .$line};
	my $panel_name = $workbook->[1]{"C" .$line};
	my $test_name = $workbook->[1]{"D" .$line};
	my $unit_measure = $workbook->[1]{"F" .$line};

	my $lower_limit_normal = $workbook->[1]{"G" .$line};
	my $upper_limit_normal = $workbook->[1]{"H" .$line};

	my $lower_limit_valid = $workbook->[1]{"J" .$line};
	my $upper_limit_valid = $workbook->[1]{"K" .$line};


	my $test_section = $select . "insert_test_section('" . $test_section_name . "'); \n";
	print $file $test_section;
	
	my $sample_type = $select . "insert_sample_type('" . $sample_type_name . "'); \n";
	print $file $sample_type;

	my $panel = $select . "insert_panel('" . $panel_name . "'); \n";
	print $file $panel;

 	my $relation_panel_sampletype = $select . "create_relationship_panel_sampletype('" . $panel_name . "','" . $sample_type_name . "'); \n";
 	print $file $relation_panel_sampletype;

 	my $relation_panel_test = $select . "create_relationship_panel_test('" . $panel_name . "','" . $test_name . "'); \n"; 
 	print $file $relation_panel_test;

 	my $relation_sample_test = $select . "create_relationship_sample_test('" . $sample_type_name . "','" . $test_name . "'); \n";
 	print $file $relation_sample_test;

	my $unit = $select . "insert_unit_of_measure('" . $unit_measure. "'); \n";
	print $file $unit;

	if ($lower_limit_normal ne '' && $upper_limit_normal ne '') 
	{
		my $test_limits = $select . "insert_result_limit_normal_range('" . $test_name . "'," . $lower_limit_normal . "," . $upper_limit_normal ."); \n";
		print $file $test_limits;
	}


	if ($lower_limit_valid ne '' && $upper_limit_valid ne '') 
    {
		my $test_valid_limits = $select . "insert_result_limit_valid_range('" . $test_name . "'," . $lower_limit_valid . "," . $upper_limit_valid ."); \n";
		print $file $test_valid_limits;
	}		

}

