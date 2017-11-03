// file: SomeInterface.java
// to be implemented by a Java RMI server class.

import java.io.File;
import java.rmi.*;

public interface RMIInterface extends Remote {

    public byte[] downloadFile(String content)
            throws java.rmi.RemoteException;
    
    public void saveFile(String title, String content)
            throws java.rmi.RemoteException;

}
