
import java.io.File;
import java.rmi.*;
import java.util.Scanner;

public class RMIClient {

    public static void main(String args[]) {
        try {
            String registryURL
                    = "rmi://localhost:" + 1234 + "/some";
            RMIInterface inter
                    = (RMIInterface) Naming.lookup(registryURL);
            // invoke the remote method(s)
            String[] title = new String[50];
            String[] content = new String[50];
            File[] file = new File[50];
            int j = 0, i = 0;
            Scanner reader = new Scanner(System.in);
            while (true) {
                System.out.println("What should I do?(upload file, download file or stop");
                String input = reader.nextLine();

                if ("upload file".equals(input)) {
                    System.out.println("Entre title name");
                    input = reader.nextLine();
                    title[i] = input;
                    System.out.println("Enter content");
                    input = reader.nextLine();
                    content[i] = input;
                    inter.saveFile(title[i], content[i]);
                    i++;
                } else if ("download file".equals(input)) {
                    System.out.println("Insert the content of the file you want");
                    input = reader.nextLine();
                    file[j] = inter.downloadFile(input);
                    if (file[j] == null) {
                        System.out.println("The file hasn't been found");
                        break;
                    }
                    j++;
                } else if ("stop".equals(input)) {
                    break;
                } else {
                    System.out.println("Unrecognized order");
                }
            }
        } // end try
        catch (Exception e) {
            System.out.println("Exception in RMIClient: " + e);
        }
    }
}
