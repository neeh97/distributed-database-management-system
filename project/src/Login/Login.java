package Login;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Scanner;

public class Login {

    private static String LOGIN_FAILED = "LOGIN_FAILED";

    public String login() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your username");
        String username = scanner.next();
        System.out.println("Enter your password");
        String password = scanner.next();
        try {
            File users = new File("Data/Users.txt");
            FileReader fileReader = new FileReader(users);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String user = bufferedReader.readLine();
            while (user != null) {
                String [] userDetails = user.split(";");
                if (username.equals(userDetails[0])) {
                    byte[] decodedBytes = Base64.getDecoder().decode(userDetails[1]);
                    String decodedPassword = new String(decodedBytes, StandardCharsets.UTF_8);
                    if (password.equals(decodedPassword)) {
                        return username;
                    }
                    else {
                        System.out.println("Wrong password. Please try again");
                        return LOGIN_FAILED;
                    }
                }
                user = bufferedReader.readLine();
            }
            System.out.println("User not found!");
        } catch (Exception e) {
            System.out.println("Exception in application : "+e.getMessage());
            return LOGIN_FAILED;
        }
        return LOGIN_FAILED;
    }
}
