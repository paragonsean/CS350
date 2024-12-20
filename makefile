# Default target
all: Snapshot.jar

# Create Snapshot.jar from the compiled class files
Snapshot.jar: bin/edu/odu/cs/cs350/enp/Snapshot.class bin/edu/odu/cs/cs350/enp/DateValidator.class bin/edu/odu/cs/cs350/enp/TestDateValidator.class bin/edu/odu/cs/cs350/enp/Course.class bin/edu/odu/cs/cs350/enp/CSVReader.class bin/edu/odu/cs/cs350/enp/TestSnapshot.class bin/edu/odu/cs/cs350/enp/TestCourse.class bin/edu/odu/cs/cs350/enp/SemesterSnapshotReader.class bin/edu/odu/cs/cs350/enp/TestSemesterSnapshotReader.class bin/edu/odu/cs/cs350/enp/ReportGenerator.class bin/edu/odu/cs/cs350/enp/TestReportGenerator.class bin/edu/odu/cs/cs350/enp/EnrollmentProjection.class bin/edu/odu/cs/cs350/enp/TestEnrollmentProjection.class
	cd bin; jar --create --file ../$@ .

# Compile individual Java files to their corresponding class files
bin/edu/odu/cs/cs350/enp/%.class: src/edu/odu/cs/cs350/enp/%.java
	javac -g -cp 'bin/:src/:lib/*' -sourcepath src -d bin $<

# Clean the bin directory and jar files
clean:
	-rm -rf bin/edu/odu/cs/cs350/enp/*.class *.jar

# Run JUnit tests using the ConsoleLauncher
test: Snapshot.jar
	java -cp 'Snapshot.jar:lib/*' org.junit.platform.console.ConsoleLauncher -c edu.odu.cs.cs350.enp.TestSnapshot -c edu.odu.cs.cs350.enp.TestCourse -c edu.odu.cs.cs350.enp.TestDateValidator -c edu.odu.cs.cs350.enp.TestSemesterSnapshotReader -c edu.odu.cs.cs350.enp.TestReportGenerator -c edu.odu.cs.cs350.enp.TestEnrollmentProjection