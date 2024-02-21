import java.util.*;
import java.text.*;
import java.util.*;
import java.io.*;
import java.util.Scanner;

public class UserInterface {
    private static UserInterface userInterface;
    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private static Warehouse warehouse;
    private static final int EXIT = 0;
    private static final int ADD_MEMBER = 1;
    private static final int ADD_PRODUCTS = 2;
    private static final int PURCHASE_PRODUCTS = 5;
    //private static final int CHANGE_PRODUCT_QUANTITY = 4;
    //private static final int REMOVE_PRODUCT = 4;
    //private static final int PLACE_ON_WAITLIST = 6;
    //private static final int REMOVE_FROM_WAITLIST = 7;
    //private static final int PROCESS_WAITLIST = 8;
    private static final int ADD_TO_WISHLIST = 3;
    private static final int REMOVE_FROM_WISHLIST = 4;
    private static final int PROCESS_SHIPMENT = 6;
    private static final int SHOW_MEMBERS = 7;
    private static final int SHOW_PRODUCTS = 8;
    private static final int SHOW_ITEMS = 9;
    private static final int SHOW_WISHLIST = 10;
    private static final int SHOW_INVOICES = 11;
    private static final int SAVE = 12;
    private static final int RETRIEVE = 13;
    private static final int HELP = 14;

    private UserInterface() {
        if (yesOrNo("Look for saved data and  use it?")) {
            retrieve();
        } else {
            warehouse = Warehouse.instance();
        }
    }

    public static UserInterface instance() {
        if (userInterface == null) {
            return userInterface = new UserInterface();
        } else {
            return userInterface;
        }
    }

    public String getToken(String prompt) {
        do {
            try {
                System.out.println(prompt);
                String line = reader.readLine();
                StringTokenizer tokenizer = new StringTokenizer(line, "\n\r\f");
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
        System.out.println("Main options:");
        System.out.println(ADD_MEMBER + " to add member");
        System.out.println(ADD_PRODUCTS + " to add products to catalog");
        //System.out.println(CHANGE_PRODUCT_QUANTITY + " to  change products quantity ");
        //System.out.println(REMOVE_PRODUCT + " to remove products from catalog");
        //System.out.println(PLACE_ON_WAITLIST + " to place products on waitlist");
        //System.out.println(REMOVE_FROM_WAITLIST + " to remove products from waitlist");
        System.out.println(ADD_TO_WISHLIST + " to add products to wishlist");
        System.out.println(REMOVE_FROM_WISHLIST + " to remove products from wishlist");
        System.out.println(PURCHASE_PRODUCTS + " to Process Client Order");
        //System.out.println(PROCESS_WAITLIST + " to process waitlist");
        System.out.println(PROCESS_SHIPMENT + " to process a shipment");
        System.out.println("");
        System.out.println("Display options:");
        System.out.println(SHOW_MEMBERS + " to print members");
        System.out.println(SHOW_PRODUCTS + " to print catalog");
        System.out.println(SHOW_ITEMS + " to print waitlist items");
        System.out.println(SHOW_WISHLIST + " to print wishlist items");
        System.out.println(SHOW_INVOICES + " to print invoices");
        System.out.println("");
        System.out.println("File options:");
        System.out.println(SAVE + " to save data");
        System.out.println(RETRIEVE + " to retrieve");
        System.out.println(HELP + " for help");
    }


    public void addMember() {
        String name = getToken("Enter member name");
        String address = getToken("Enter address");
        String phone = getToken("Enter phone");

        Member result;
        result = warehouse.addMember(name, address, phone);
        if (result == null) {
            System.out.println("Could not add member");
        }
        System.out.println(result);
    }

    public void addProducts() {
        Product result;
        Scanner myObj = new Scanner(System.in);
        do {
            String ProductName = getToken("Enter  ProductName");
            System.out.println("Enter Quantity");

            int Quantity = myObj.nextInt();

            System.out.println("Enter Price");
            float Price = myObj.nextFloat();
            //String id = getToken("Enter id");
            result = warehouse.addProduct(ProductName, Quantity, Price);
            if (result != null) {
                System.out.println(result);
            } else {
                System.out.println("Product could not be added");
            }
            if (!yesOrNo("Add more products?")) {
                break;
            }
        } while (true);
    }

    public void placeOnWaitlist() {
        Item result;
        Scanner myObj = new Scanner(System.in);
        do {
            String ProductName = getToken("Enter  ProductName: ");
            System.out.println("Enter Quantity: ");

            int Quantity = myObj.nextInt();
            String name = getToken("Enter Member name: ");
            Product product = warehouse.findProductByName(ProductName);
            Member member = warehouse.findMemberByName(name);
            float price = product.getPrice();
            result = warehouse.addWaitItem(product, member, Quantity, price);
            if (result != null) {
                System.out.println(result);
            } else {
                System.out.println("Item could not be added");
            }
            if (!yesOrNo("Add more items?")) {
                break;
            }
        } while (true);
    }

    public void changeProductQuantity() {
        String productName = getToken("Enter product name: ");
        int newQuantity = getNumber("Enter new quantity: ");

        boolean success = warehouse.changeProductQuantity(productName, newQuantity);

        if (success) {
            System.out.println("Product quantity changed successfully.");
        } else {
            System.out.println("Product not found. Quantity change failed.");
        }
    }

    public void processOrder() {
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


    /*public void editWishlist(Wishlist wishlist) {
        boolean continueEditing = true;

        while (continueEditing) {
            System.out.println("Wishlist Options:");
            System.out.println("1. Add item to wishlist");
            System.out.println("2. Remove item from wishlist");
            System.out.println("3. Change item quantity");
            System.out.println("4. Exit wishlist editing");

            int choice = getNumber("Enter your choice:");

            String productName = getToken("Enter Product Name");
            String memberName = getToken("Enter Member Name");
            int quantity = getNumber("Enter Quantity");

            Product product = warehouse.findProductByName(productName);
            Member member = warehouse.findMemberByName(memberName);


            switch (choice) {
                case 1:
                    Record newItem = new Record(product, member, quantity);
                    warehouse.addToWishlist(member, newItem);
                    System.out.println("Item added to wishlist.");
                    break;

                case 2:
                    if (product != null && member != null) {
                        Record record = new Record(product, member, quantity);
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

                    if (product != null && member != null) {
                        int newQuantity = getNumber("Enter the new quantity:");
                        Record record = new Record(product, member, quantity);
                        record.setQuantity(newQuantity);
                        System.out.println("Item quantity changed.");
                    } else {
                        System.out.println("Item not found in the wishlist.");
                    }
                    break;

                case 4:
                    continueEditing = false;
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }*/

    public void showWaitlist() {
        String name = getToken("Enter Product Name: ");
        Product product = warehouse.findProductByName(name);
        if (product != null) {
            if (product.isEmpty()) {
                System.out.println("Product does not have a waitlist");
            }
            Iterator allItems = warehouse.getWaitlist(product);
            while (allItems.hasNext()) {
                Item item = (Item) (allItems.next());
                System.out.println(item.toString());
            }
        }
        if (product == null) {
            System.out.println("Product entered does not exist in catalog");
        }
    }

    public void showProducts() {
        Iterator allProducts = warehouse.getProducts();
        while (allProducts.hasNext()) {
            Product product = (Product) (allProducts.next());
            System.out.println(product.toString());
        }
    }

    public void showMembers() {
        Iterator allMembers = warehouse.getMembers();
        while (allMembers.hasNext()) {
            Member member = (Member) (allMembers.next());
            System.out.println(member.toString());
        }
    }

    public void addToWishlist() {
        String productName = getToken("Enter Product Name");
        String memberName = getToken("Enter Member Name");
        int quantity = getNumber("Enter Quantity");

        Product product = warehouse.findProductByName(productName);
        Member member = warehouse.findMemberByName(memberName);
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
    }

    public void removeFromWishlist() {
        String productName = getToken("Enter Product Name");
        String memberName = getToken("Enter Member Name");
        int quantity = getNumber("Enter Quantity");

        Product product = warehouse.findProductByName(productName);
        Member member = warehouse.findMemberByName(memberName);
        float price = product.getPrice();
        if (product != null && member != null) {
            Record record = new Record(product, member, quantity, price);
            boolean removed = warehouse.removeFromWishlist(member, record);

            if (removed) {
                System.out.println("Item removed from the wishlist.");
            } else {
                System.out.println("Item was not found on the wishlist.");
            }
        } else {
            System.out.println("Product or member not found.");
        }
    }

    public void showWishlist() {
        String name = getToken("Enter Member Name: ");
        Member member = warehouse.findMemberByName(name);
        Iterator allItems = warehouse.getWishlist(member);
        if (member.isEmpty()) {
            System.out.println("WishList is Empty");
        }
        while (allItems.hasNext()) {
            Record record = (Record) allItems.next();
            System.out.println(record.toString());
        }
    }

    public void removeProduct() {
        Scanner scanner = new Scanner(System.in);

        // Ask the user for the product name


        System.out.print("Enter the product name: ");
        String productName = scanner.nextLine();
        if (warehouse.checkIfProductExists(productName) != true) {
            System.out.println("Product does not exist");
        } else {


            // Ask the user for the member name
            System.out.print("Enter the product id: ");
            String id = scanner.nextLine();

            System.out.println("Enter Quantity");
            int quantity = scanner.nextInt();

            System.out.println("Enter Price");
            float price = scanner.nextFloat();


            Product product = new Product(productName, quantity, price);
            System.out.println(product.getProductName());

            if (product != null) {
                //Item item = new Item(product, member, 0); // Quantity is not needed for removal

                boolean removed = warehouse.removeProduct(product);

                if (removed) {
                    System.out.println("Product removed from the waitlist.");
                } else {
                    System.out.println("Product was not found on the waitlist.");
                }

            }

        }
    }


    public void removeItemFromWaitlist() {
        //String productName = getToken("Enter product name");
        //String memberName = getToken("Enter member name");

        Scanner scanner = new Scanner(System.in);

        // Ask the user for the product name
        System.out.print("Enter the product name: ");
        String productName = scanner.nextLine();

        // Ask the user for the member name
        System.out.print("Enter the member name: ");
        String memberName = scanner.nextLine();

        System.out.println("Enter Quantity");
        int quantity = scanner.nextInt();

        Product product = warehouse.findProductByName(productName);
        Member member = warehouse.findMemberByName(memberName);
        float price = product.getPrice();
        System.out.println(product.getProductName());
        System.out.println(member.getName());
        if (product != null && member != null) {
            //Item item = new Item(product, member, 0); // Quantity is not needed for removal
            Item item = new Item(product, member, quantity, price);
            boolean removed = warehouse.removeWaitItem(item, product);
            if (removed) {
                System.out.println("Item removed from the waitlist.");
            } else {
                System.out.println("Item was not found on the waitlist.");
            }
        } else {
            System.out.println("Product or member not found.");
        }
    }


    public void showInvoices() {
        String name = getToken("Enter Member Name: ");
        Member member = warehouse.findMemberByName(name);
        Iterator invoices = warehouse.getInvoices(member);
        while (invoices.hasNext()) {
            Record invoice = (Record) invoices.next();
            System.out.println(invoice.toString());
        }
    }


    public void processWaitlistHold() {
        System.out.println("Dummy Action");
    }

    public void processShipment() {
        String name = getToken("Enter Shipment Product Name: ");
        Product product = warehouse.findProductByName(name);
        int quantity = getNumber("Enter quantity of Product: ");
        if (product != null && !product.isEmpty()) {
            Shipment ship = new Shipment(product, quantity);
            System.out.println(ship.toString());
            Iterator waitIter = warehouse.getWaitlist(product);

            System.out.println("Your processing Options: ");
            System.out.println("1. Add Product to Member Invoice: ");
            System.out.println("2. Remove item from Waitlist: ");
            System.out.println("3. Change item quantity and add to Member Invoice: ");

            while (waitIter.hasNext()) {
                Item wait = (Item) (waitIter.next());
                Member member = wait.getMember();
                int addQuantity = wait.getQuantity();
                float price = wait.getPrice();
                Record record = new Record(product, member, addQuantity, price);
                System.out.println(wait.toString());
                int choice = getNumber("Enter your choice:");
                switch (choice) {
                    case 1:
                        boolean success = warehouse.allocate(ship, addQuantity);
                        if (success) {
                            warehouse.addToInvoices(record, member);
                        } else {
                            System.out.println("Unexpected Input");
                        }
                        break;

                    case 2:
                        if (member != null) {
                            Record record1 = new Record(wait.getProduct(), member, wait.getQuantity(), wait.getPrice());
                            boolean removed = warehouse.removeFromWishlist(member, record1);
                            if (removed = true) {
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
                            Record record2 = new Record(product, member, newQuantity, wait.getPrice());
                            wait.setQuantity(newQuantity);
                            boolean success2 = warehouse.allocate(ship, newQuantity);
                            if (success2) {
                                warehouse.addToInvoices(record2, member);
                                System.out.println("Added to invoice with new quantity");
                            } else {
                                System.out.println("Unexpected Input");
                            }
                            break;
                        }
                }
            }
        } else if (product.isEmpty() && product != null) {
            Shipment Ship = new Shipment(product, quantity);
            System.out.println("Waitlist is Currently Empty");
            System.out.println(Ship.toString() + " has been added to catalog");
            warehouse.allocate(Ship, quantity);

        } else {
            System.out.println("Waitlist is Currently Empty");
        }
    }

    public void getTransactions() {
        System.out.println("Dummy Action");
    }

    private void save() {
        if (warehouse.save()) {
            System.out.println(" The Warehouse has been successfully saved in the file WarehouseData \n");
        } else {
            System.out.println(" There has been an error in saving \n");
        }
    }

    private void retrieve() {
        try {
            Warehouse tempWarehouse = Warehouse.retrieve();
            if (tempWarehouse != null) {
                System.out.println(" The Warehouse has been successfully retrieved from the file WarehouseData \n");
                warehouse = tempWarehouse;
            } else {
                System.out.println("File doesnt exist; creating new Warehouse");
                warehouse = Warehouse.instance();
            }
        } catch (Exception cnfe) {
            cnfe.printStackTrace();
        }
    }

    public void process() {
        int command;
        help();
        while ((command = getCommand()) != EXIT) {
            switch (command) {
                case ADD_MEMBER:
                    addMember();
                    break;
                case ADD_PRODUCTS:
                    addProducts();
                    break;
                case PURCHASE_PRODUCTS:
                    processOrder();
                    break;
                //case CHANGE_PRODUCT_QUANTITY:      changeProductQuantity(); //This should be a function that happens when a shipment comes in. or for when a customer wants to change their item quantity
                //break;
                //case REMOVE_PRODUCT:      removeProduct();
                //break;
                //case PLACE_ON_WAITLIST:       placeOnWaitlist(); //This should happen automatically if quantity ordered is greater than quantity in stock
                //break;
                //case REMOVE_FROM_WAITLIST:        removeItemFromWaitlist(); //This should happen automatically when a shipment comes in and the waitlist is processed
                //break;
                //case PROCESS_WAITLIST:       processWaitlistHold(); //Either keep manual, or change to happen automatically when a shipment comes in.
                //break;
                case PROCESS_SHIPMENT:
                    processShipment();
                    break;
                case SAVE:
                    save();
                    break;
                case RETRIEVE:
                    retrieve();
                    break;
                case SHOW_MEMBERS:
                    showMembers();
                    break;
                case SHOW_PRODUCTS:
                    showProducts();
                    break;
                case SHOW_ITEMS:
                    showWaitlist();
                    break;
                case ADD_TO_WISHLIST:
                    addToWishlist();
                    break;
                case REMOVE_FROM_WISHLIST:
                    removeFromWishlist();
                    break;
                case SHOW_INVOICES:
                    showInvoices();
                    break;
                case SHOW_WISHLIST:
                    showWishlist();
                    break;
                case HELP:
                    help();
                    break;
            }
        }
    }

    public static void main(String[] s) {
        /*Scanner scanner = new Scanner(System.in);
        System.out.println("1. Login");
        System.out.println("2. Create new User");
        int choice = scanner.nextInt();
        switch (choice) {
            case 1:
                System.out.println("Enter Username: ");
                String name = scanner.nextLine();
                System.out.println("Enter password: ");
                String password = scanner.nextLine();


                break;

            case 2:
                System.out.println("Enter Username: ");

*/
        {
            UserInterface.instance().process();
        }
    }

}


//Things to do -Fix ShowwaitList(). Works fine if there is a waitlist for the item. but if waitlist is null it has an error message.