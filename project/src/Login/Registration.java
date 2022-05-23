package Login;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Scanner;

public class Registration{

    public Boolean register() throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please choose a Username");
        String username = scanner.next();
        System.out.println("Please choose a password");
        String password = scanner.next();
        try {
            File users = new File("Data/Users.txt");
            if (users.createNewFile()) {
                FileWriter fileWriter = new FileWriter(users, true);
                String outputline = username+";";
                outputline += Base64.getEncoder().encodeToString(password.getBytes(StandardCharsets.UTF_8));
                fileWriter.append(outputline);
                fileWriter.append("\n");
                fileWriter.close();
            }
            else {
                FileReader fileReader = new FileReader(users);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                String user = bufferedReader.readLine();
                while (user != null)
                {
                    String [] userDetails = user.split(";");
                    if (username.equals(userDetails[0]))
                    {
                        System.out.println("Username already exists! Please choose a new username");
                        return false;
                    }
                    user = bufferedReader.readLine();
                }
                FileWriter fileWriter = new FileWriter(users, true);
                String outputline = username+";";
                outputline += Base64.getEncoder().encodeToString(password.getBytes(StandardCharsets.UTF_8));
                fileWriter.append(outputline);
                fileWriter.append("\n");
                fileWriter.close();
            }
        } catch (Exception e)
        {
            System.out.println("Exception in application : "+e.getMessage());
            return false;
        }
        return true;
    }
}
