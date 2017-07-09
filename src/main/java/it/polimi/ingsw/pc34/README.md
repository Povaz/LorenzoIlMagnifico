# Prova Finale (Ingegneria del Software)

## Getting Started
- Launch the server by running Server class
- Launch how many Clients you want by running Client class
### How to open a Client session
- Answer to the first question about User Interface: Graphic User Interface or Command Line Interface;
```bash
 Which Interface do you want to use? 1. CLI 2. GUI
```
- then, answer to the second question about the Connection Infrastructure: Remote Method Interface or Socket Connection
```bash
Which Connection Type do you want to use? 1. RMI 2. Socket
```
- Keep in mind that there will be no differences (more or less) choosing RMI or Socket connection during the game
### Command Line Interface 
#### Login/Lobby phase
- It will be shown the list of legal commands that are accepted by the Server in this phase:
```bash
Insert: /login to login, /logout to logout /registration to registrate a new user or /exit to close to application      - Logged: false
```
- As said in this line, command /login is used to ask for a Login procedure, that will proceed like this:
```bash
Insert your Username: Username
Insert your Password: Password
```
- After a successful Login (you will be notified by Server about it), which puts you directly in the Lobby waiting for a new Game to start, (you will be notified by Server about it), command /logout will be unlocked, in order to log out from the Lobby;
- Use command /registration to ask for a Registration Procedure, which is similar to Login Procedure and it is available only when you're not logged yet with an account;
- Use command /exit to close the application;
#### Game Phase 
- When Lobby reaches 2 players waiting, a timer starts. When the timer is finished, the game will start automatically.
- The game will start immediately if Lobby reaches 5 players, even if the timer isn't finished yet.
- As soon as the game starts, after pressing any key, it will be shown the list of legal commands that are accepted by Server in this phase:
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
