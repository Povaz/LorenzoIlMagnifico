# Prova Finale (Ingegneria del Software)

## Getting Started
- Launch the server by running Server class
- Launch how many Clients you want by running Client class
### How to open a Client session
- Answer to the first question about User Interface: Graphic User Interface or Command Line Interface;
```bash
 Which Interface do you want to use? 1. CLI 2. GUI
```
- then, answer to the second question about the Connection Infrastructure: RMI or Socket Connection
```bash
Which Connection Type do you want to use? 1. RMI 2. Socket
```

### Customize your project files and Import them in Eclipse
- Open the `pom.xml` file in a text editor and substitute the two occurrences of **pcXX** with your assigned **team_code**.
- Import it in Eclipse as Maven Project:
  * from Eclipse, select `File > Import... > Existing Maven Project`
  * click `Browse...` and select the directory where you cloned the project
  * make sure the project is listed and selected under `Projects`
  * select `Finish`
  * you should now see the project **team_code** listed in the Package Explorer view of Eclipse
- from the Package Explorer view, rename packages under `src/main/java` and `src/test/java` substituting **pcXX** with your assigned **team_code**
- customize the `README.md`
- in order to check that everything worked fine, try to build with Maven:
  + from Eclipse (Package Explorer view):
    * right-click on the project
    * select `Run as > Maven build...`
    * type `clean package` into the `Goal` field
    * click `Run`
  + from command line:
    * move to your project directory (you should be in the same folder as `pom.xml` file)
    * type `mvn clean package`
  + wait for the build to complete and make sure you have a build success

### Commit and push your changes:
  ```
  git commit -am "customize project"
  git push origin master
  ```
