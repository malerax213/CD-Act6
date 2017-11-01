
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
public class RMIImplementation extends UnicastRemoteObject
        implements RMIInterface {

    List<File> f = new ArrayList<File>();
    File[] files = new File[50];

    public RMIImplementation() throws RemoteException {
        super();
    }

    public File downloadFile(String content) throws RemoteException {
        Boolean found = false;
        int i = 0;
        while (found != true) {
            BufferedReader br = null;
            try {
                br = new BufferedReader(new FileReader(f.get(i)));
            } catch (FileNotFoundException ex) {
                Logger.getLogger(RMIImplementation.class.getName()).log(Level.SEVERE, null, ex);
            }
            String line = null;
            try {
                line = br.readLine();
            } catch (IOException ex) {
                Logger.getLogger(RMIImplementation.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println(line);
            System.out.println(content);
            if (line == content) {
                found = true;
                return f.get(i);
            } else {
                i++;
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
            File file = new File(title);
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
