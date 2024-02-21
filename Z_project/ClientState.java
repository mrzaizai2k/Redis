import java.util.*;
import java.text.*;
import java.io.*;
public class ClientState extends WarehouseState {
    private static ClientState clientState;
    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private static Warehouse warehouse;
    private static final int EXIT = 0;
    private static final int VIEW_ACCOUNT = 3;
    private static final int CHECK_PRICE = 5;
    private static final int PROCESS_WISHLIST = 7;
    private static final int MODIFY_WISHLIST = 8;
    private static final int LOGOUT = 10;
    private static final int VIEW_INVOICES = 12;
    private static final int HELP = 13;
    private ClientState() {

        warehouse = Warehouse.instance();
    }

    public static ClientState instance() {
        if (clientState == null) {
            return clientState = new ClientState();
        } else {
            return clientState;
        }
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

    public void viewAccount(){
        String name = getToken("Enter Member Name: ");
        if(warehouse.findMemberByName(name) != null) {
            Member member = warehouse.findMemberByName(name);
            System.out.println(member.toString());
        }
        else{
            System.out.println("Member does not exist");
        }
    }
    public void checkPrice(){
        String name = getToken("Enter Product Name: ");
        if(warehouse.findProductByName(name) != null) {
            Product product = warehouse.findProductByName(name);
            String price = "Price: $" + product.getPrice();
            System.out.println(price);
        }
        else{
            System.out.println("Product does not exist");
        }
    }
    public void processWishlist(){
        //System.out.println("process Wishlist");
        String Name = getToken("Enter Client Name: ");
        Member member = warehouse.findMemberByName(Name);
        Iterator wishIter = warehouse.getWishlist(member);

        System.out.println("Your processing Options:");
        System.out.println("1. Add item to Invoice");
        System.out.println("2. Remove item from wishlist");
        System.out.println("3. Change item quantity and add to Invoice");

        while (wishIter.hasNext()) {
            Record wish = (Record) (wishIter.next());
            System.out.println(wish.toString());
            int choice = getNumber("Enter your choice:");
            switch (choice) {
                case 1:
                    int success = warehouse.addToInvoices(wish, member);
                    if (success == 0) {
                        System.out.println("Item Added");
                    } else if (success == 1) {
                        System.out.println("Item Added to Waitlist");
                    } else {
                        System.out.println("Unexpected Input");
                    }

                case 2:
                    if (member != null) {
                        Record record = new Record(wish.getProduct(), member, wish.getQuantity(), wish.getPrice());
                        boolean removed = warehouse.removeFromWishlist(member, record);
                        if (removed) {
                            System.out.println("Item removed from the wishlist.");
                        } else {
                            System.out.println("Item was not found on the wishlist.");
                        }
                    } else {
                        System.out.println("Product or member not found.");
                    }
                    break;

                case 3:
                    if (member != null) {
                        int newQuantity = getNumber("Enter the new quantity:");
                        wish.setQuantity(newQuantity);
                        int done = warehouse.addToInvoices(wish, member);
                        if (done == 0) {
                            System.out.println("Item Added");
                        } else if (done == 1) {
                            System.out.println("Item Added to Waitlist");
                        } else {
                            System.out.println("Unexpected Input");
                        }
                        break;
                    }
            }
        }
        warehouse.getInvoices(member);
    }
    public void modifyWishlist(){
        //System.out.println("modify Wishlist");
        String Name = getToken("Enter Client Name: ");
        Member member = warehouse.findMemberByName(Name);
        Iterator wishIter = warehouse.getWishlist(member);

        System.out.println("Your processing Options:");
        System.out.println("1. Add item to wishlist");
        System.out.println("2. Remove item from wishlist");
        System.out.println("3. Change item quantity on wishlist");
        System.out.println("4. Display Wishlist");

        while (wishIter.hasNext()) {
            Record wish = (Record) (wishIter.next());
            System.out.println(wish.toString());
            int choice = getNumber("Enter your choice:");
            switch (choice) {
                case 1:
                    String productName = getToken("Enter Product Name");
                    //String memberName = getToken("Enter Member Name");
                    int quantity = getNumber("Enter Quantity");

                    Product product = warehouse.findProductByName(productName);
                    //Member member = warehouse.findMemberByName(memberName);
                    float price = product.getPrice();
                    if (product != null && member != null) {
                        Record record = new Record(product, member, quantity, price);
                        boolean added = warehouse.addToWishlist(member, record);

                        if (added) {
                            System.out.println("Item added to the wishlist.");
                        } else {
                            System.out.println("Item could not be added to the wishlist.");
                        }
                    } else {
                        System.out.println("Product or member not found.");
                    }

                case 2:
                    if (member != null) {
                        Record record = new Record(wish.getProduct(), member, wish.getQuantity(), wish.getPrice());
                        boolean removed = warehouse.removeFromWishlist(member, record);
                        if (removed) {
                            System.out.println("Item removed from the wishlist.");
                        } else {
                            System.out.println("Item was not found on the wishlist.");
                        }
                    } else {
                        System.out.println("Product or member not found.");
                    }
                    break;

                case 3:
                    if (member != null) {
                        int newQuantity = getNumber("Enter the new quantity:");
                        wish.setQuantity(newQuantity);
                        System.out.println("New Quantity for " + wish.getProduct() + " is " + wish.getQuantity());
                        //int done = warehouse.addToInvoices(wish, member);
                        //if (done == 0) {
                            //System.out.println("Item Added");
                        //} else if (done == 1) {
                           //System.out.println("Item Added to Waitlist");
                        //} else {
                            //System.out.println("Unexpected Input");
                        }
                        break;
                    }
            }
        }

    public void printInvoices() {
        System.out.println("printInvoices");
    }

    public void help() {
        System.out.println("Enter a number between 0 and 12 as explained below:");
        System.out.println(EXIT + " to Exit\n");
        System.out.println(VIEW_ACCOUNT + " to view your account details");
        System.out.println(CHECK_PRICE + " to check price of a product ");
        System.out.println(PROCESS_WISHLIST + " to process your wishlist");
        System.out.println(MODIFY_WISHLIST + " to modify your wishlist");
        System.out.println(VIEW_INVOICES + " to print your invoices");
        System.out.println(LOGOUT + " to logout");
        System.out.println(HELP + " for help");
    }



    public void process() {
        int command;
        help();
        while ((command = getCommand()) != EXIT) {
            switch (command) {

                case VIEW_ACCOUNT:       viewAccount();
                    break;
                case CHECK_PRICE:       checkPrice();
                    break;
                case PROCESS_WISHLIST:        processWishlist();
                    break;
                case MODIFY_WISHLIST:       modifyWishlist();
                    break;
                case VIEW_INVOICES:  printInvoices();
                    break;
                case LOGOUT:  logout();
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

    public void logout()
    {
        if ((WarehouseContext.instance()).getLogin() == WarehouseContext.IsClerk)
        { //stem.out.println(" going to clerk \n ");
            (WarehouseContext.instance()).changeState(1); // exit with a code 1
        }
        else if (WarehouseContext.instance().getLogin() == WarehouseContext.IsClient)
        {  //stem.out.println(" going to login \n");
            (WarehouseContext.instance()).changeState(0); // exit with a code 2
        }
        else
            (WarehouseContext.instance()).changeState(2); // exit code 2, indicates error
    }

}
