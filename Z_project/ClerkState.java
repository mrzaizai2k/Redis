import java.util.*;
import java.text.*;
import java.io.*;

public class ClerkState extends WarehouseState {
    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private static Warehouse warehouse;
    private WarehouseContext context;
    private static ClerkState instance;
    private static final int EXIT = 0;
    private static final int PRINT_CATALOG = 1;

    private static final int ADD_MEMBER = 2;
    private static final int ADD_PRODUCT= 3;
    private static final int SAVE_DATABASE = 4;
    private static final int LOGOUT = 5;
    private static final int CLIENT_MENU = 11;
    private static final int HELP = 13;
    private ClerkState() {
        super();
        warehouse = Warehouse.instance();
        //context = LibContext.instance();
    }

    public static ClerkState instance() {
        if (instance == null) {
            instance = new ClerkState();
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
        if (more.charAt(0) != 'y'&more.charAt(0) != 'Y') {
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
        System.out.println(ADD_MEMBER + " to add a member");
        System.out.println(ADD_PRODUCT + " to  add products to catalog");
        System.out.println(PRINT_CATALOG + " to display products in catalog ");
        System.out.println(SAVE_DATABASE + " to save the database");
        System.out.println(LOGOUT + " to Logout");
        System.out.println(CLIENT_MENU+ " to become a client");
        System.out.println(HELP + " for help");
    }

    public void addMember() {
        String name = getToken("Enter the member's name:");
        String address = getToken("Enter the member's address:");
        String phone = getToken("Enter the member's phone number");

        Member newMember = new Member(name, address, phone);
        if (MemberList.instance().insertMember(newMember)) {
            System.out.println("Member added successfully.");
        } else {
            System.out.println("Failed to add the member. Member may already exist.");
        }
    }

    public void addProduct() {
        String name = getToken("Enter the product name:");
        float price = Float.parseFloat(getToken("Enter the product price:"));
        int quantity = Integer.parseInt(getToken("Enter the product quantity:"));

        Product newProduct = new Product(name, quantity, price);
        if (Catalog.instance().insertProduct(newProduct)) {
            System.out.println("Product added to the catalog successfully.");
        } else {
            System.out.println("Failed to add the product. Product may already exist.");
        }

    }

    public void printCatalog() {
        System.out.println("Product Catalog:");
        Iterator<Product> productIterator = Catalog.instance().getProducts();
        while (productIterator.hasNext()) {
            Product product = productIterator.next();
            System.out.println("Product ID: " + product.getId());
            System.out.println("Name: " + product.getProductName());
            System.out.println("Price: $" + product.getPrice());
            System.out.println("Quantity: " + product.getQuantity());
            System.out.println("----------------------------");
        }
    }
    public void SaveDatabase(){
          try {
        // Serialize MemberList
        FileOutputStream memberListFile = new FileOutputStream("MemberList.ser");
        ObjectOutputStream memberListOut = new ObjectOutputStream(memberListFile);
        memberListOut.writeObject(MemberList.instance());
        memberListOut.close();
        memberListFile.close();

        // Serialize Catalog
        FileOutputStream catalogFile = new FileOutputStream("Catalog.ser");
        ObjectOutputStream catalogOut = new ObjectOutputStream(catalogFile);
        catalogOut.writeObject(Catalog.instance());
        catalogOut.close();
        catalogFile.close();

        System.out.println("Database saved successfully.");
    } catch (IOException e) {
        System.out.println("Error saving the database: " + e.getMessage());
    }
    }




    public void clientMenu()
    {
        String userID = getToken("Please input the user id: ");
        if (Warehouse.instance().searchMembership(userID) != null){
            (WarehouseContext.instance()).setUser(userID);
            (WarehouseContext.instance()).changeState(2);
        }
        else
            System.out.println("Invalid user id.");
    }

    public void logout()
    {
        (WarehouseContext.instance()).changeState(3); // exit with a code 0
    }


    public void process() {
        int command;
        help();
        while ((command = getCommand()) != EXIT) {
            switch (command) {
                case ADD_MEMBER:        addMember();
                    break;
                case ADD_PRODUCT:         addProduct();
                    break;
                case PRINT_CATALOG:      printCatalog();
                    break;
                case SAVE_DATABASE:      SaveDatabase();
                    break;
                case LOGOUT:      logout();
                    break;
                case CLIENT_MENU:          clientMenu();
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
