
import java.io.*;
import java.rmi.*;
import java.rmi.server.*;
import java.util.*;


/**
 * This class implements the remote interface RMIInterface.
 */
public class RMIImplementation extends UnicastRemoteObject implements RMIInterface {

    List<File> f = new ArrayList<>();
    File[] files = new File[50];

    public RMIImplementation() throws RemoteException {
        super();
    }

    @Override
    public byte[] downloadFile(String title) throws RemoteException {
        File folder = new File("Storage-Server");
        String path = "Storage-Server";
        File[] listOfFiles = folder.listFiles();
        File objective;
        path = searchFile(listOfFiles,path,title);
        objective = new File(path);
        byte buffer[]=new byte[(int)objective.length()];
        
        try{
            BufferedInputStream input = new BufferedInputStream(new FileInputStream(path));
            input.read(buffer,0,buffer.length);
            input.close();
            return(buffer);
        } catch (IOException e) {
            System.out.println("FileServer exception:"+e.getMessage());
            return null;
        }
    }
    
    public String searchFile(File[] listOfFiles, String path,String title){
        String found = null;
        for (File e : listOfFiles) {
            if (e.isFile()) {
                if (e.getName().equals(title)) {
                    return path+"/"+title;
                }
            } else if (e.isDirectory()) {
                File folder = new File(path+"/"+e.getName());
                found = searchFile(folder.listFiles(), path + "/" + folder.getName(),title);
                if (found != null){
                    return found;
                }
            }
        }
        return found;
    }

    @Override
    public void saveFile(byte[] buffer, String title) throws RemoteException {
        String uniqueID = UUID.randomUUID().toString();
        File dir = new File("Storage-Server/" + uniqueID);
        dir.mkdir();
        String path = "Storage-Server/" + uniqueID+ "/" + title;
        
        try{
                FileOutputStream FOS = new FileOutputStream(path);
                BufferedOutputStream Output = new BufferedOutputStream(FOS);
                Output.write(buffer,0,buffer.length);
                Output.flush();
                Output.close();
        }
        catch(IOException e){
            System.out.println("FileServer exception:"+e.getMessage());
        }
    }
}
