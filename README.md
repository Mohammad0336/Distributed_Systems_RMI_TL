# To Do List Program with RMI

## Program Description 

This is a simple program designed to create and sort a to do list for a user that has authorization.

### Novel Features

- Authentication: username/password is required to ensure that only authorized users can access the to-do list
- Task Prioritization: tasks are shown priority by sorting higher on the list by deadline

### Functions

- User sign in / registration 
- List manipulation  add | remove | display | download

## How to use the program 

Open a terminal in the server file
# To Do List Program

## Program Description 

This is a simple program designed to create and sort a to do list for a user that has authorization.

### Novel Features

- Authentication: username/password is required to ensure that only authorized users can access the to-do list
- Task Prioritization: tasks are shown priority by sorting higher on the list by deadline

### Functions

- User sign in / registration 
- List manipulation  add | remove | display 

## How to use the program 

Open two terminals within the server file

run these commands in the first terminal
```bash
cd Dsys_a2/Server
rmiregistry
```

run these commands in the second terminal
```bash
cd Dsys_a2/Server
javac *.java
java -Djava.security.policy=policy.txt ServerFile
```

Open a terminal in the client file

run these commands in the third terminal
```bash
cd Dsys_a2/Client
javac *.java
java ClientFile.java
```
