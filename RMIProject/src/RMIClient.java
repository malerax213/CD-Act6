
import java.io.*;
import java.net.MalformedURLException;
import java.rmi.*;
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
            
            while (true) {
                System.out.println("What should I do?(upload file, download file or stop");
                input = reader.nextLine();

                if ("upload file".equals(input)) {
                    uploadFileAction(inter);
                }
                else if ("download file".equals(input)) {
                    downloadFileAction(inter);
                }
                else if ("stop".equals(input)) {
                    break;
                } 
                else {
                    System.out.println("Unrecognized order");
                }
            }
        } // end try
        catch (MalformedURLException | NotBoundException | RemoteException e) {
            System.out.println("Exception in RMIClient: " + e);
        }
    }
    
    public static void uploadFileAction(RMIInterface inter) throws RemoteException{      
        String title;
        Scanner reader = new Scanner(System.in);
        
        System.out.println("Entre title name");
        title = reader.nextLine();
        
        File folder = new File("Storage-Client");
        String path = "Storage-Client/";
        File[] listOfFiles = folder.listFiles();
        File objective;
        path = searchFile(listOfFiles,path,title);
        
        if(path!=null){
            objective = new File(path);
            byte buffer[]=new byte[(int)objective.length()];

            try{
                FileInputStream FIS = new FileInputStream(path);
                BufferedInputStream input = new BufferedInputStream(FIS);
                input.read(buffer,0,buffer.length);
                input.close();
                inter.saveFile(buffer,title);
            } catch (IOException e) {
                System.out.println("FileServer exception:"+e.getMessage());
            }     
        }
        else{
            System.out.println("File: " + title + ",not found");
        }
    }

    
    public static String searchFile(File[] listOfFiles, String path,String title){
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
    
    public static void downloadFileAction(RMIInterface inter) throws RemoteException{
        String input;
        Scanner reader = new Scanner(System.in);
        
        System.out.println("Insert the content of the file you want");
        input = reader.nextLine();
        String path = "Storage-Client/" + input;
        File save = new File(path);
        byte[] file = inter.downloadFile(input);

        if (file == null) {
            System.out.println("The file hasn't been found");
        }
        else{

            try{
                FileOutputStream FOS = new FileOutputStream(path);
                BufferedOutputStream Output = new BufferedOutputStream(FOS);
                Output.write(file,0,file.length);
                Output.flush();
                Output.close();
            }
            catch(IOException e){
            System.out.println("FileServer exception:"+e.getMessage());
            }
        }
    }
}
