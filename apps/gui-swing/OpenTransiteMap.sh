#!/usr/bin/perl

my $os = $^O;
my $pathSep = ";";

if ( $os == "darwin" ) {
	$pathSep=":";
}

my $dir = 'lib';

opendir(DIR, $dir) or die $!;

my @classPath = split($pathSep, $ENV{'CLASSPATH'});
push(@classPath,"gui-swing-0.9.jar");
while (my $file = readdir(DIR)) {
	push(@classPath, "lib/$file");
}

$ENV{'CLASSPATH'} = join($pathSep,@classPath);

my @args=("java", "-Xmx2048m", "me.openMap.OpenTransitMap" );
exec @args
 