
import java.io.*;
import java.rmi.*;
import java.util.Scanner;

public class RMIClient {

    public static void main(String args[]) {
        try {
            File dir = new File("Storage-Client");
            dir.mkdir();
            
            
            String registryURL
                    = "rmi://localhost:" + 1234 + "/some";
            RMIInterface inter
                    = (RMIInterface) Naming.lookup(registryURL);
            // invoke the remote method(s)
            String title = new String();
            String content = new String();
            Scanner reader = new Scanner(System.in);
            
            while (true) {
                System.out.println("What should I do?(upload file, download file or stop");
                String input = reader.nextLine();

                if ("upload file".equals(input)) {
                    System.out.println("Entre title name");
                    input = reader.nextLine();
                    title = input;
                    System.out.println("Enter content");
                    input = reader.nextLine();
                    content = input;
                    inter.saveFile(title, content);
                }
                else if ("download file".equals(input)) {
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
                            BufferedOutputStream Output = new BufferedOutputStream(new FileOutputStream(path));
                            Output.write(file,0,file.length);
                            Output.flush();
                            Output.close();
                        }
                        catch(Exception e){
                        System.out.println("FileServer exception:"+e.getMessage());
                        }
                    }
                }
                else if ("stop".equals(input)) {
                    break;
                } 
                else {
                    System.out.println("Unrecognized order");
                }
            }
        } // end try
        catch (Exception e) {
            System.out.println("Exception in RMIClient: " + e);
        }
    }
}
