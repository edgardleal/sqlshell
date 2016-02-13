

default:
	mvn package

install:
	./src/main/shell/install.sh

test:
	mvn test

clean:
	mvn clean
