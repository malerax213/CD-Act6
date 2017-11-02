
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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

    List<File> f = new ArrayList<File>();
    File[] files = new File[50];

    public RMIImplementation() throws RemoteException {
        super();
    }

    public File downloadFile(String title) throws RemoteException {
        File folder = new File("Storage-Server");
        File[] listOfFiles = folder.listFiles();

        return searchFile(listOfFiles,title);
    }
    
    public File searchFile(File[] listOfFiles, String title){
        for (File listOfFile : listOfFiles) {
            if (listOfFile.isFile()) {
                if (listOfFile.getName().equals(title)) {
                    return new File(title);
                }
            } else if (listOfFile.isDirectory()) {
                File folder = new File(listOfFile.getName());
                return searchFile(folder.listFiles(), listOfFile.getName() + "/" + title);
            }
        }
        return null;
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
