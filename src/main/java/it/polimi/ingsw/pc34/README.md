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
```bash
Type: /playturn for an Action; /chat to send message;
```
- Use command /playturn to access the Action procedure, from here you will be guided from Server in order to insert the correct input;
- At the and of your turn you need to actively skip your turn:
```bash
Type: /playturn for an Action; /chat to send message;
/playturn
What action you want to do? 1-action 2-place Leader Card 3-activate Leader Card 4-exchange Leader Card 5-skip
5
You skipped your turn!
```
- If you don't Pass in time, the timer expires and you will be automatically kicked out of the game:

- Use command /chat to send a message in Chat in any case, with the following pattern:
```bash
/chat messagetosend
```
- When the game has finished, it will be displayed the name of the winner and pressing any key will take you to the login phase, where you can log in again to start another game.
### Graphic User Interface
#### Login/Lobby phase
- A Login Window will open right after you chose the connection type: here you can log in or register, filling the right text spot and pressing the Login/Registration button;
- After a successful login, a Waiting Room will be opened, where you can press the Logout button in order to return to the Login Window;
- If 2 or more players are in the Waiting Room, the timer will start and at the end of it, the Game Window will be opened;
- If the Waiting Room reaches 5 players waiting for the game to start, then the game will start immediately;
#### Game Phase
- The Game windows is composed of 6 areas:
  * Game board is situated at your left, showing all the Family Member that were used by all the players;
  * Your player board is situated at your bottom-right, showing your cards, resources, family member not used and points;
  * At your top-right you can see:
    + The Request window: here are displayed Turn Number and what player is in turn and there will be asked questions about your actions during the game. Here you can also Pass your turn or Place/Activate/Exchange a leader card;
    + The Chat window: here are displayed in red the error messages and you can send messages to the other players and read other players' messages;
    + The LeaderCard window: here are display your leader cards and moving your mouse on them, you can see their status (In hand, Placed, Activated);
    + The Player window: here you can click on a player's username in order to change the player board you want to see, but keep in mind, you can move only your Family Member;
  * In order to Activate/Place/Exchange a leader card, press on the arrow near Pass button and choose your card.
- If you do not complete your action clicking on Pass button, timer will expires and it will be notified on your screen. Clicking on your screen will take you to the Login Window;
- If the game finishes, the name of the winner will be displayed on the screen and clicking on it will take you to the Login Window.



