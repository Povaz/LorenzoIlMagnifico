package it.polimi.ingsw.pc34.SocketRMICongiunction;

/**
 * Created by Povaz on 04/07/2017.
 */
public class ClientInfo {
    private final ConnectionType connectionType;
    private final ClientType clientType;

    public ClientInfo (ConnectionType connectionType, ClientType clientType) {
        this.connectionType = connectionType;
        this.clientType = clientType;
    }
}
