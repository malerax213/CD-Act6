
import java.io.*;
import java.net.MalformedURLException;
import java.rmi.*;
import java.util.List;
import java.util.Scanner;

public class RMIClient {

    public static void main(String args[]) {
        try {
            File dir = new File("Storage-Client");
            dir.mkdir();

            String registryURL = "rmi://localhost:" + 1234 + "/some";
            RMIInterface inter = (RMIInterface) Naming.lookup(registryURL);// invoke the remote method(s)

            String input;
            Scanner reader = new Scanner(System.in);

            System.out.println("Enter your user Name");
            String user = reader.nextLine();

            while (true) {
                System.out.println("What should I do?(upload file, download file, search files, delete file or stop");
                input = reader.nextLine();

                if ("upload file".equals(input)) {
                    uploadFileAction(inter, user);
                } else if ("download file".equals(input)) {
                    downloadFileAction(inter);
                } else if ("search files".equals(input)) {
                    searchFilesAction(inter);
                } else if ("delete file".equals(input)) {
                    deleteFileAction(inter,user);
                } else if ("stop".equals(input)) {
                    break;
                } else {
                    System.out.println("Unrecognized order");
                }
            }
        } // end try
        catch (MalformedURLException | NotBoundException | RemoteException e) {
            System.out.println("Exception in RMIClient: " + e);
        }
    }

    public static void uploadFileAction(RMIInterface inter, String user) throws RemoteException {
        // Uploads the file with a title and tags to the server
        String title, tags;
        Scanner reader = new Scanner(System.in);

        System.out.println("Enter title name");
        title = reader.nextLine();
        System.out.println("Enter some tags");
        tags = reader.nextLine();

        File folder = new File("Storage-Client");
        String path = "Storage-Client/";
        File[] listOfFiles = folder.listFiles();
        File objective;
        path = searchFile(listOfFiles, path, title);

        if (path != null) {
            objective = new File(path);
            byte buffer[] = new byte[(int) objective.length()];

            try {
                FileInputStream FIS = new FileInputStream(path);
                BufferedInputStream input = new BufferedInputStream(FIS);
                input.read(buffer, 0, buffer.length);
                input.close();

                inter.saveFile(buffer, title, user, tags);
                System.out.println("File " + title + " has been uploaded to the server");
            } catch (IOException e) {
                System.out.println("FileServer exception:" + e.getMessage());
            }
        } else {
            System.out.println("File: " + title + ",not found");
        }
    }

    public static String searchFile(File[] listOfFiles, String path, String title) {
        // Looks if there is a file with the name "title"
        String found = null;
        for (File e : listOfFiles) {
            if (e.isFile()) {
                if (e.getName().equals(title)) {
                    return path + "/" + title;
                }
            } else if (e.isDirectory()) {
                File folder = new File(path + "/" + e.getName());
                found = searchFile(folder.listFiles(), path + "/" + folder.getName(), title);
                if (found != null) {
                    return found;
                }
            }
        }
        return found;
    }

    public static void downloadFileAction(RMIInterface inter) throws RemoteException {
        String input;
        Scanner reader = new Scanner(System.in);

        System.out.println("Insert the title of the file you want");
        input = reader.nextLine();
        String path = "Storage-Client/" + input;
        byte[] file = inter.downloadFile(input);

        if (file == null) {
            System.out.println("The file hasn't been found");
        } else {
            try {
                FileOutputStream FOS = new FileOutputStream(path);
                BufferedOutputStream Output = new BufferedOutputStream(FOS);
                Output.write(file, 0, file.length);
                Output.flush();
                Output.close();
                System.out.println("File " + input + " has been downloaded from the server");
            } catch (IOException e) {
                System.out.println("FileServer exception:" + e.getMessage());
            }
        }
    }

    public static void searchFilesAction(RMIInterface inter) throws RemoteException {
        // Prints the titles of the files with the tag "textualDescrption"
        System.out.println("Insert the tags you want to look for");

        Scanner reader = new Scanner(System.in);
        String tags = reader.nextLine();
        List<String> result = inter.searchFiles(tags);

        System.out.println("List of contents related to the entered tag:");
        if (!result.isEmpty()) {
            for (int i = 0; i < result.size(); i++) {
                System.out.println("Title number "+(i+1)+": "+result.get(i));
            }
        }else{
            System.out.println("Nothing has been found!");
        }
    }
    
        public static void deleteFileAction(RMIInterface inter, String user) throws RemoteException {
        // Prints the titles of the files with the tag "textualDescrption"
        System.out.println("Insert the file you want to erase");
        Scanner reader = new Scanner(System.in);
        String file = reader.nextLine();

        Boolean result;
        result = inter.deleteFile(file,user);

        if (result) {
            System.out.println("File erased");
        }else{
            System.out.println("Nothing has been found!");
        }
    }
}
