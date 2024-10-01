all: Snapshot.jar

Snapshot.jar: bin/edu/odu/cs/cs350/Snapshot.class bin/edu/odu/cs/cs350/TestSnapshot.class 
	cd bin; jar --create --file ../$@ .

bin/edu/odu/cs/cs350/%.class: src/edu/odu/cs/cs350/%.java
	javac -g -cp 'bin/:src/:lib/*' -sourcepath src -d bin $<

clean:
	-rm -rf bin *.jar 

test: Snapshot.jar
	java -cp 'Snapshot.jar:lib/*' org.junit.platform.console.ConsoleLauncher -c edu.odu.cs.cs350.TestSnapshot