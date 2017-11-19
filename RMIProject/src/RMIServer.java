
import java.io.*;
import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class RMIServer {
    static String Name;
            
    public static void main(String args[]) throws IOException {
        File dir = new File("Storage-Server");
        dir.mkdir();
        
        dir = new File("Storage-Server/config");
        dir.mkdir();
        
        File config = new File("Storage-Server/config/library");
        config.createNewFile();
        
        try {
            RMIServerImplementation self = confServer();
            joinDS(self);
        } catch (Exception ex) {
            System.out.println("An error has been found\n" + ex);
        }
    }

    public static RMIServerImplementation confServer() throws RemoteException, NotBoundException, MalformedURLException {
        // Will look for a server
        String portNum, IP;
        Scanner reader = new Scanner(System.in);
        
        System.out.println("Enter the name of this Server");
        Name = reader.nextLine();
        
        System.out.println("Enter IP address: (you can write localhost to use the default one)");
        IP = reader.nextLine();

        System.out.println("Enter the port of the server:");
        portNum = reader.nextLine();

        
        RMIServerImplementation exportedObj = new RMIServerImplementation();
        startRegistry(Integer.parseInt(portNum));
            
        // Register the object under the name “some”
        String registryURL = "rmi://" + IP + ":" + portNum + "/some";
        Naming.rebind(registryURL, exportedObj);
        System.out.println("Server ready.\n");
        exportedObj.Name = Name;
        return exportedObj;
    }
    
    // This method starts a RMI registry on the local host, if it does not already exists at the specified port number.
    private static void startRegistry(int RMIPortNum)
            throws RemoteException {
        try {
            Registry registry = LocateRegistry.getRegistry(RMIPortNum); // No valid registry at that port.
            registry.list();

            // The above call will throw an exception if the registry does not already exist
        } catch (RemoteException ex) {
            // No valid registry at that port.
            System.out.println(
                    "RMI registry cannot be located at port " + RMIPortNum);
            LocateRegistry.createRegistry(RMIPortNum);
            System.out.println(
                    "RMI registry created at port " + RMIPortNum);
        }
    }
    
    public static void joinDS(RMIServerImplementation self) throws RemoteException, NotBoundException, MalformedURLException {
        // Will look for a server
        String portNum;
        String IP;
        Scanner reader = new Scanner(System.in);

        System.out.println("Do you want to connect with an already active system?");
        String s = reader.nextLine();
        if("y".equals(s) || "yes".equals(s)){
            System.out.println("Enter the IP of the server:");
            IP = reader.nextLine();
            
            System.out.println("Enter the port of the server:");
            portNum = reader.nextLine();
            
            String registryURL = "rmi://" + IP + ":" + portNum + "/some";
            RMIServerInterface inter = (RMIServerInterface) Naming.lookup(registryURL);
            inter.registerServer(self, Name);
        }
    }
}
