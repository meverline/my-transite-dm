#!/usr/bin/perl

my $dir = 'lib';

opendir(DIR, $dir) or die $!;

my @classPath = split(";", $ENV{'CLASSPATH'});
push(@classPath,"gui-swing-0.9.jar");
while (my $file = readdir(DIR)) {
	push(@classPath, "lib/$file");
}

$ENV{'CLASSPATH'} = join(";",@classPath);

my @args=("java", "me.transit.parser.TransitFeedParser"  $ARGV[1]);
exec @args
