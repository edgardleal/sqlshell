# sqlshell
Simple command line application to execute SQL commands in terminal

# Building 

To compile you will need to have the following tools:   
* java/javac ( jdk8 > ) 
* Maven ( mvn ) 3 > 
* make ( optional ) 

## Compiling 
``` mvn compile ```

## Building 
``` mvn package ```

## Installing ( if you want use make ) 
``` make install ```

# Executing 

``` ./run.sh alias 'Select * from table' ```   

``` ./run.sh alias  ```  will open vim to create SQL command in a temporary file

# Executin with others renders

``` render=csv ./run.sh alias 'Select * from table' ```   
