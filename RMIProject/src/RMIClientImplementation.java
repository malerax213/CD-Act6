import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;


public class RMIClientImplementation extends UnicastRemoteObject implements RMIClientInterface{
    
    public RMIClientImplementation() throws RemoteException{
        super();
    }

    @Override
    public void sendMessage(String msg) throws RemoteException{
        System.out.println(msg);
    }


}
