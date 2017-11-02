
import java.io.*;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIServer {

    public static void main(String args[]) {
        File dir = new File("Storage-Server");
        dir.mkdir();
        
        try {
            // code for port number value to be supplied
            RMIImplementation exportedObj = new RMIImplementation();
            startRegistry(1234);
            // register the object under the name “some”
            String registryURL = "rmi://localhost:" + 1234 + "/some";
            Naming.rebind(registryURL, exportedObj);
            System.out.println("Server started");
        } catch (Exception ex) {
            System.out.println("An error has been found\n" + ex);
        }
    }

    // This method starts a RMI registry on the local host, if it
    // does not already exists at the specified port number.
    private static void startRegistry(int RMIPortNum)
            throws RemoteException {
        try {
            Registry registry = LocateRegistry.getRegistry(RMIPortNum); // No valid registry at that port.
            registry.list();

            // The above call will throw an exception
            // if the registry does not already exist
        } catch (RemoteException ex) {
            // No valid registry at that port.
            System.out.println(
                    "RMI registry cannot be located at port " + RMIPortNum);
            Registry registry = LocateRegistry.createRegistry(1234);
            System.out.println(
                    "RMI registry created at port " + RMIPortNum);
        }
    }
}
