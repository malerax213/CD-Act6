
import java.io.*;
import java.rmi.*;
import java.rmi.server.*;
import java.util.*;
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
        String path = "Storage-Server";
        File[] listOfFiles = folder.listFiles();
        path = searchFile(listOfFiles, path, title);
        File objective = new File(path);
        byte buffer[] = new byte[(int) objective.length()];

        try {
            BufferedInputStream input = new BufferedInputStream(new FileInputStream(path));
            input.read(buffer, 0, buffer.length);
            input.close();
            return (buffer);
        } catch (IOException e) {
            System.out.println("FileServer exception:" + e.getMessage());
            return null;
        }
    }

    public String searchFile(File[] listOfFiles, String path, String title) {
        String found = null;
        for (File e : listOfFiles) {
            if (e.isFile()) {
                if (e.getName().equals(title)) {
                    return path + "/" + title;
                }
            } else if (e.isDirectory() && !"config".equals(e.getName())) {
                File folder = new File(path + "/" + e.getName());
                found = searchFile(folder.listFiles(), path + "/" + folder.getName(), title);
                if (found != null) {
                    return found;
                }
            }
        }
        return found;
    }

    @Override
    public void saveFile(byte[] buffer, String title, String user, String tags) throws RemoteException {
        String uniqueID = UUID.randomUUID().toString();
        File dir = new File("Storage-Server/" + uniqueID);
        dir.mkdir();
        String path = "Storage-Server/" + uniqueID + "/" + title;

        try {
            addToLibrary(title, path, user, tags);
        } catch (IOException ex) {
            Logger.getLogger(RMIImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            FileOutputStream FOS = new FileOutputStream(path);
            BufferedOutputStream Output = new BufferedOutputStream(FOS);
            Output.write(buffer, 0, buffer.length);
            Output.flush();
            Output.close();
        } catch (IOException e) {
            System.out.println("FileServer exception:" + e.getMessage());
        }
    }

    public void addToLibrary(String title, String path, String user, String tags) throws IOException {
        FileWriter fw = new FileWriter("Storage-Server/config/library", true);
        BufferedWriter bw = new BufferedWriter(fw);

        bw.write(title);
        bw.newLine();
        bw.write(user);
        bw.newLine();
        bw.write(path);
        bw.newLine();
        bw.write(tags);
        bw.newLine();

        bw.close();
    }

    public Map readLibrary() throws FileNotFoundException, IOException {
        // Reads the file library (where all the information about uploads is saved)
        FileReader fr = new FileReader("Storage-Server/config/library");
        BufferedReader br = new BufferedReader(fr);

        String line = br.readLine();
        Map<String, ArrayList> library = new HashMap<String, ArrayList>();
        String title = null;
        while (line != null) {
            ArrayList<Object> info = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                if (i == 0) {
                    title = line;
                }
                info.add(line);
                System.out.println("Agrego: "+line);
                line = br.readLine();
            }
            library.put(title, info);
        }
        br.close();
        fr.close();
        return library; // Returns a Map with the title and the content of every upload made by a client
    }

    @Override
    public List searchFiles(String tags) throws RemoteException {
        Map<String, ArrayList> library = new HashMap<String, ArrayList>();
        List<String> result = new ArrayList();
        String[] tagslist = tags.split("[ ,]");
        
        try {
            library = readLibrary();
            for (Map.Entry<String, ArrayList> entry : library.entrySet()) {
                String[] tagsfile = String.valueOf(entry.getValue().get(3)).split("[ ,]");
                Boolean found = true;

                for(String tag : tagslist){
                    if (!Arrays.asList(tagsfile).contains(tag)) { // The 3rd position of the value will be the TAG field
                        found = false;
                    }
                }
                if(found){
                    result.add(String.valueOf(entry.getValue().get(0)));
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(RMIImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
}
