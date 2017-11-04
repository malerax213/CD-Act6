
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
        String path = "Storage-Server/";
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
        for (File listOfFile : listOfFiles) {
            if (listOfFile.isFile()) {
                if (listOfFile.getName().equals(title)) {
                    return path+"/"+title;
                }
            } else if (listOfFile.isDirectory()) {
                File folder = new File(listOfFile.getName());
                found = searchFile(folder.listFiles(), path + "/" + listOfFile.getName(),title);
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
        File file = new File(path);
        
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
