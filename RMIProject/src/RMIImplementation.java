
import java.io.*;
import java.rmi.*;
import java.rmi.server.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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
        path =searchFile(listOfFiles,path,title);
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
    public void saveFile(String title, String content) throws RemoteException {
        try {
            manageFile(title, content);
        } catch (IOException ex) {
            Logger.getLogger(RMIImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void manageFile(String title, String content) throws IOException {
        try {
            File file = new File("Storage-Server/"+title);
            if (!file.exists()) {
                file.createNewFile();
            }
            this.f.add(file);
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content);
            bw.close();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

}
