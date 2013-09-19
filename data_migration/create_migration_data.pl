#!/usr/bin/perl
use strict;
use warnings;

use Spreadsheet::Read;

my $workbook = ReadData ("lab_data.xlsx");

unlink('migration_data.sql');

open(my $file, '>>', 'migration_data.sql') or die 'Could not create migration_data.sql file. Check your permissions';

print $file "-- This file is generated automatically. If you need to change it, change the spreedsheet and run the script create_migration_data.pl \n\n\n";

my $sheet = $workbook -> [1];

my $row_max = $sheet->{maxrow};

for (my $row = 2 ; $row<= $row_max; $row++) 
{	
	create_migration_scripts_for_line($row, $file, $workbook);
}
close $file;

sub escape_quote {
	my ($str) = @_;
	$str =~ s/\'/\'\'/g;
	return $str;
}

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

	my $sample_type_name = escape_quote($workbook->[1]{"B" .$line});
	my $panel_name = escape_quote($workbook->[1]{"C" .$line});
	my $test_name = escape_quote($workbook->[1]{"D" .$line});
	my $unit_measure = escape_quote($workbook->[1]{"F" .$line});
	my $result_type = escape_quote($workbook->[1]{"G" .$line});


	my $test_section = $select . "insert_test_section('" . $test_section_name . "'); \n";
	print $file $test_section;
	
	my $sample_type = $select . "insert_sample_type('" . $sample_type_name . "'); \n";
	print $file $sample_type;

	if($panel_name ne '') {
	 	my $relation_panel_sampletype = $select . "create_relationship_panel_sampletype('" . $panel_name . "','" . $sample_type_name . "'); \n";
	 	print $file $relation_panel_sampletype;

	 	my $relation_panel_test = $select . "create_relationship_panel_test('" . $panel_name . "','" . $test_name . "'); \n"; 
	 	print $file $relation_panel_test;
	}

 	my $relation_sample_test = $select . "create_relationship_sample_test('" . $sample_type_name . "','" . $test_name . "'); \n";
 	print $file $relation_sample_test;

	my $unit = $select . "insert_unit_of_measure('" . $unit_measure. "','" . $test_name . "'); \n";
	print $file $unit;

	my $relation_test_section_test = $select . "create_relationship_test_section_test('" . $test_section_name . "','" . $test_name . "'); \n";
	print $file $relation_test_section_test;

	if ($result_type eq 'Numeric') {
		my $lower_limit_normal = $workbook->[1]{"H" .$line};
		my $upper_limit_normal = $workbook->[1]{"I" .$line};

		my $lower_limit_valid = $workbook->[1]{"J" .$line};
		my $upper_limit_valid = $workbook->[1]{"K" .$line};

		if ($lower_limit_normal ne '' && $upper_limit_normal ne '') {
			my $test_limits = $select . "insert_result_limit_normal_range('" . $test_name . "'," . $lower_limit_normal . "," . $upper_limit_normal ."); \n";
			print $file $test_limits;
		}

		if ($lower_limit_valid ne '' && $upper_limit_valid ne '') {
			my $test_valid_limits = $select . "insert_result_limit_valid_range('" . $test_name . "'," . $lower_limit_valid . "," . $upper_limit_valid ."); \n";
			print $file $test_valid_limits;
		}

	} elsif	($result_type eq 'Remark') {
		my $test_result = $select . "add_test_result_type('" . $test_name . "','R'); \n";
		print $file $test_result;
	} elsif ($result_type eq 'Drop-down') {
		my $possible_results = $workbook->[1]{"H" .$line};
		my @values = split(';', $possible_results);
		foreach my $val (@values) {
			print $file $select . "add_test_result_type('" . $test_name . "','D', '" . $val . "'); \n";
		}
	}
}
