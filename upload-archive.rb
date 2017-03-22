require 'colorize'

puts 'Uploading artifact to oss.sonatype.org'.red

puts '---> Cleaning library project'.green
exit_code = system './gradlew multipicker:clean -q'
puts '<-- ' + exit_code.to_s.blue

puts '---> Uploading...'.green
exit_code = system './gradlew multipicker:uploadArchives -q'
puts '<--- ' + exit_code.to_s.blue