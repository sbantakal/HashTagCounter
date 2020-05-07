all: Node.class FiboHeap.class IncorrectArgumentsException.class HashTagCounter.class 

	
Node.class: Node.java
	javac -d . -classpath . Node.java

FiboHeap.class: FiboHeap.java
	javac -d . -classpath . FiboHeap.java

IncorrectArgumentsException.class: IncorrectArgumentsException.java
	javac -d . -classpath . IncorrectArgumentsException.java

HashTagCounter.class: HashTagCounter.java
	javac -d . -classpath . HashTagCounter.java

	
clean:
	rm -f *.class





