package it.polimi.ingsw.pcXX.RMI;

import it.polimi.ingsw.pcXX.JSONUtility;
import org.json.JSONException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Povaz on 24/05/2017.
 **/
public class ServerLoginImpl extends UnicastRemoteObject implements ServerLogin {
    private static ArrayList<UserLogin> usersLogged;
    private Timer timer;

    private ServerLoginImpl () throws RemoteException {
        usersLogged = new ArrayList<>();
    }

    private boolean searchUserLogged (UserLogin userLogin) throws RemoteException {
        for (UserLogin user : usersLogged) {
            if ( (userLogin.getUsername().equals(user.getUsername())) && (userLogin.getPassword().equals(user.getPassword()))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean loginServer (UserLogin userLogin) throws JSONException, IOException {
        if (!searchUserLogged(userLogin)) {
            if (JSONUtility.checkLogin(userLogin.getUsername(), userLogin.getPassword())) {
                usersLogged.add(userLogin);
                userLogin.sendMessage("Login Successful");

                if (usersLogged.size() == 2) {
                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            // Passo usersLogged a chi gestirà la partita;
                            System.out.println("Start Game with: " + usersLogged.size() + "players");
                            System.exit(0);
                        }
                    }, 10000);
                }

                if (usersLogged.size() == 5) {
                    timer.cancel();
                    //Passo usersLogged a chi gestirà la partita
                    System.out.println("Start Game");
                }

                return true;
            }
                userLogin.sendMessage("Incorrect Username or password");
                return false;
        }
        userLogin.sendMessage("User already logged");
        return false;
    }

    @Override
    public void registrationServer (UserLogin userLogin) throws JSONException, IOException {
        if (JSONUtility.checkRegister(userLogin.getUsername(), userLogin.getPassword())) {
            userLogin.sendMessage("Registration Successful");
        }
        else {
            userLogin.sendMessage("Registration Failed: Username already taken");
        }
    }

    @Override
    public boolean logoutServer (UserLogin userLogin) throws RemoteException {
        for (UserLogin user : usersLogged) {
            if ((userLogin.getUsername().equals(user.getUsername())) && (userLogin.getPassword().equals(user.getPassword()))) {
                usersLogged.remove(user);
                userLogin.sendMessage("Logout successful");

                if (usersLogged.size() == 1) {
                    System.out.println(userLogin.getUsername() + "left the room");
                    timer.cancel();
                }

                return false;
            }
        }
        userLogin.sendMessage("Logout Failed");
        return true;
    }

    @Override
    public void printLoggedUsers () throws RemoteException{
        for (UserLogin user: usersLogged) {
            System.out.println(user.getUsername());
            System.out.println(user.getPassword());
        }
    }

    public static void main (String[] args) throws RemoteException, AlreadyBoundException, MalformedURLException {
        System.out.println("Constructing server implementation...");
        ServerLoginImpl serverLoginImpl = new ServerLoginImpl();

        System.out.println("Binding Server implementation to registry...");
        Registry registry = LocateRegistry.createRegistry(8000);
        registry.bind("serverLogin", serverLoginImpl);

        System.out.println("Waiting for invocations from clients...");
    }
}
