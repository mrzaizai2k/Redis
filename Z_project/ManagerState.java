import java.util.*;
import java.text.*;
import java.io.*;
public class ManagerState extends WarehouseState {
    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private static Warehouse warehouse;
    private WarehouseContext context;
    private static ManagerState instance;
    private static final int EXIT = 0;
    private static final int MODIFY_PRODUCT_PRICE = 1;
    private static final int RECEIVE_SHIPMENT = 2;
    private static final int FREEZE_UNFREEZE_CLIENT = 3;
    private static final int BECOME_SALESCLERK = 11;
    private static final int LOGOUT = 12;
    private static final int HELP = 13;
    private ManagerState() {
        super();
        warehouse = Warehouse.instance();
        //context = LibContext.instance();
    }

    public static ManagerState instance() {
        if (instance == null) {
            instance = new ManagerState();
        }
        return instance;
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
    public int getNumber(String prompt) {
        do {
            try {
                String item = getToken(prompt);
                Integer num = Integer.valueOf(item);
                return num.intValue();
            } catch (NumberFormatException nfe) {
                System.out.println("Please input a number ");
            }
        } while (true);
    }
    public Calendar getDate(String prompt) {
        do {
            try {
                Calendar date = new GregorianCalendar();
                String item = getToken(prompt);
                DateFormat df = SimpleDateFormat.getDateInstance(DateFormat.SHORT);
                date.setTime(df.parse(item));
                return date;
            } catch (Exception fe) {
                System.out.println("Please input a date as mm/dd/yy");
            }
        } while (true);
    }
    public int getCommand() {
        int command;
        do {
            try {
                int value = Integer.parseInt(getToken("Enter command:" + HELP + " for help"));
                if (value >= EXIT && value <= HELP) {
                    return value;
                }
            } catch (NumberFormatException nfe) {
                System.out.println("Enter a number");
            }
        } while (true);
    }

    public void help() {
        System.out.println("Enter a number between 0 and 12 as explained below:");
        System.out.println(EXIT + " to Exit\n");
        System.out.println(MODIFY_PRODUCT_PRICE + " to modify product price");
        System.out.println(RECEIVE_SHIPMENT + " to receive a shipment");
        System.out.println(FREEZE_UNFREEZE_CLIENT + " to freeze/unfreeze client account ");
        System.out.println(BECOME_SALESCLERK + " to  switch to the sales clerk menu");
        System.out.println(LOGOUT + " to logout");
        System.out.println(HELP + " for help");
    }

    public void modifyProductPrice(){
        System.out.println("Modify product price");
    }
    public void receiveShipment(){
        System.out.println("receive shipment");
    }
    public void freezeUnfreezeClient(){
        System.out.println("freeze/unfreeze client");
    }
    public void becomeSalesclerk(){
        String userID = getToken("Please input the manager password: ");
        if (userID.equals("manager")){
            (WarehouseContext.instance()).setUser(userID);
            (WarehouseContext.instance()).changeState(1);
        }
        else{
            System.out.println("Invalid user id.");
        }
    }






    public void Clerkmenu()
    {
        String userID = getToken("Please input the user id: ");
        if (Warehouse.instance().searchMembership(userID) != null){
            (WarehouseContext.instance()).setUser(userID);
            (WarehouseContext.instance()).changeState(1);
        }
        else
            System.out.println("Invalid user id.");
    }

    public void logout()
    {
        (WarehouseContext.instance()).changeState(0); // exit with a code 0
    }


    public void process() {
        int command;
        help();
        while ((command = getCommand()) != EXIT) {
            switch (command) {
                case MODIFY_PRODUCT_PRICE:        modifyProductPrice();
                    break;
                case RECEIVE_SHIPMENT:         receiveShipment();
                    break;
                case FREEZE_UNFREEZE_CLIENT:     freezeUnfreezeClient();
                    break;
                case BECOME_SALESCLERK:      becomeSalesclerk();
                    break;
                case LOGOUT:                 logout();
                    break;
                case HELP:              help();
                    break;
            }
        }
        logout();
    }
    public void run() {
        process();
    }
}
