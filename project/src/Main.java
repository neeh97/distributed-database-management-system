import Login.*;

import java.util.Scanner;

public class Main {

    private static String LOGIN_FAILED = "LOGIN_FAILED";

    public static void main(String [] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to Group 18's DBMS Project. Please select an option");
        System.out.println("1. Login" +"\n"
                +"2. Register");
        Integer option = scanner.nextInt();
        if (option == 1) {
            Login login = new Login();
            String loginResult = login.login();
            if (!loginResult.equals(LOGIN_FAILED)) {
                System.out.println("Login successful"); //Main flow to app goes here
                try {
                    Parser parser = new Parser();
                    parser.userInput(loginResult);
                } catch (Exception e) {
                    System.out.println("Exception in application : "+e.getMessage());
                }
            } else {
                System.out.println("Login failed. Please try again.");
            }
        }
        else if (option == 2) {
            Registration registration = new Registration();
            if (registration.register()) {
                System.out.println("User successfully registered!");
            } else {
                System.out.println("Registration failed. Please try again.");
            }
        }
    }
}
