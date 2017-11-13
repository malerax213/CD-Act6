// file: SomeInterface.java
// to be implemented by a Java RMI server class.

import java.rmi.*;
import java.util.List;

public interface RMIInterface extends Remote {

    public byte[] downloadFile(String content)
            throws java.rmi.RemoteException;
    
    public void saveFile(byte[] file, String title, String user, String tags)
            throws java.rmi.RemoteException;
    
    public List searchFiles(String tags)
            throws java.rmi.RemoteException;
    
    public Boolean deleteFile(String file, String user)
        throws java.rmi.RemoteException;

}
