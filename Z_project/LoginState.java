import java.util.*;
import java.text.*;
import java.io.*;
import java.util.Scanner;
public class LoginState extends WarehouseState{
    private static final int CLERK_LOGIN = 1;
    private static final int MANAGER_LOGIN = 0;
    private static final int USER_LOGIN = 2;
    private static final int EXIT = 3;
    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private WarehouseContext context;
    private static LoginState instance;
    private LoginState() {
        super();
        // context = LibContext.instance();
    }

    public static LoginState instance() {
        if (instance == null) {
            instance = new LoginState();
        }
        return instance;
    }

    public int getCommand() {
        do {
            try {
                int value = Integer.parseInt(getToken("Enter command:" ));
                if (value <= EXIT && value >= MANAGER_LOGIN) {
                    return value;
                }
            } catch (NumberFormatException nfe) {
                System.out.println("Enter a number");
            }
        } while (true);
    }

    public String getToken(String prompt) {
        do {
            try {
                System.out.println(prompt);
                String line = reader.readLine();
                StringTokenizer tokenizer = new StringTokenizer(line,"\n\r\f");
                if (tokenizer.hasMoreTokens()) {
                    return tokenizer.nextToken();
                }
            } catch (IOException ioe) {
                System.exit(0);
            }
        } while (true);
    }

    private boolean yesOrNo(String prompt) {
        String more = getToken(prompt + " (Y|y)[es] or anything else for no");
        if (more.charAt(0) != 'y' && more.charAt(0) != 'Y') {
            return false;
        }
        return true;
    }
    private boolean verifyPassword(String user, String pass, String role){
        if("clerk".equals(role)) {
            if ("salesclerk".equals(user) && "salesclerk".equals(pass)) {
                return true;
            } else {
                //System.out.println(user + pass);
                return false;
            }
        }
        if("manager".equals(role)) {
            if ("manager".equals(user) && "manager".equals(pass)) {
                return true;
            } else {
                //System.out.println(user + pass);
                return false;
            }
        }
        else{
            return false;
        }
    }

    private void clerk(){
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter UserName: ");
        String userName = scanner.nextLine();
        System.out.println("Enter Password: ");
        String password = scanner.nextLine();
        if (verifyPassword(userName, password, "clerk") = true) {
            (WarehouseContext.instance()).setLogin(WarehouseContext.IsClerk);
            (WarehouseContext.instance()).changeState(1);
        }
        else{
            System.out.println("Invalid Username or password");
        }
    }

    private void manager(){
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter UserName: ");
        String userName = scanner.nextLine();
        System.out.println("Enter Password: ");
        String password = scanner.nextLine();
        if(verifyPassword(userName, password, "manager") == true) {
            (WarehouseContext.instance()).setLogin(WarehouseContext.IsManager);
            (WarehouseContext.instance()).changeState(0);
        }
        else{
            System.out.println("Invalid Username or password");
        }
    }

    private void user(){
        String userID = getToken("Please input the user id: ");
        if (Warehouse.instance().searchMembership(userID) != null){
            (WarehouseContext.instance()).setLogin(WarehouseContext.IsClient);
            (WarehouseContext.instance()).setUser(userID);
            (WarehouseContext.instance()).changeState(2);
        }
        else
            System.out.println("Invalid user id.");
    }

    public void process() {
        int command;
        System.out.println("Please input 0 to login as Manager\n"+
                "input 1 to login as Clerk\n" +
                "input 2 to login as Client\n" +
                "Input 3 to exit the system\n");
        while ((command = getCommand()) != EXIT) {
                System.out.println("Command entered: " + command);
                System.out.println("Manager_Login integer: " + MANAGER_LOGIN);
            switch (command) {
                case CLERK_LOGIN:       clerk();
                    break;
                case USER_LOGIN:        user();
                    break;
                case MANAGER_LOGIN:     manager();
                    break;

                default:                System.out.println("Invalid choice");

            }
            System.out.println("Please input 0 to login as Manager\n"+
                    "input 1 to login as Clerk\n" +
                    "input 2 to login as Client\n" +
                    "Input 3 to exit the system\n");
        }
        (WarehouseContext.instance()).changeState(3);
    }

    public void run() {
        process();
    }
}
