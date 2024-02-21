import java.util.*;
import java.io.*;


public class Warehouse implements Serializable{
    private static final long serialVersionUID = 1L;
    public static final int PRODUCT_NOT_FOUND = 1;
    public static final int PRODUCT_NOT_PURCHASED = 2;
    public static final int PRODUCT_ON_WAITLIST = 3;
    public static final int PRODUCT_PURCHASED = 4;
    public static final int PRODUCT_PLACED_ON_WAITLIST = 5;
    public static final int PRODUCT_NOT_ON_WAITLIST = 6;
    public static final int OPERATION_COMPLETED = 7;
    public static final int OPERATION_FAILED = 8;
    public static final int NO_SUCH_MEMBER = 9;
    private Catalog catalog;
    private MemberList memberList;
    //private Waitlist waitList;
    //private Wishlist wishlist;
    //private InvoiceList invoiceList;

    private static Warehouse warehouse;
    private Warehouse(){
        catalog = Catalog.instance();
        memberList = MemberList.instance();
        //waitList = Waitlist.instance();
        //wishlist = Wishlist.instance();
        //invoiceList = InvoiceList.instance();
    }
    public static Warehouse instance() {
        if (warehouse == null) {
            MemberIdServer.instance();
            return (warehouse = new Warehouse());
        } else {
            return warehouse;
        }
    }

    public Product findProductByName(String productName) {
        Iterator productIterator = catalog.getProducts();

        while (productIterator.hasNext()) {
            Product product = (Product) productIterator.next();
            if (product.getProductName().equalsIgnoreCase(productName)) {
                return product;
            }
        }
        return null; // Product not found
    }

    public boolean checkIfProductExists(String productName){
        Iterator productIterator = catalog.getProducts();

        while (productIterator.hasNext()){
            Product product = (Product) productIterator.next();
            System.out.println(product.getProductName());
            if (product.getProductName().equalsIgnoreCase(productName)) {
                return true;
            }
        }
        return false; // Product not found
    }

    public Member searchMembership(String memberId) {
        return memberList.search(memberId);
    }

    public Member findMemberByName(String memberName) {
        return memberList.findMember(memberName);
    }



    public Product addProduct(String ProductName, int Quantity, float Price) {
        Product product = new Product(ProductName, Quantity, Price);

        if (catalog.insertProduct(product)) {
            return (product);
        }
        return null;
    }

    /*public boolean removeProduct(Product product){
        return catalog.removeFromCatalog(product);
    }*/

    public boolean removeProduct(Product product) {
        Product existing_product = findProductByName(product.getProductName());

        if (existing_product != null && existing_product.getId().equals(product.getId())) {
            return catalog.removeFromCatalog(existing_product);
        }

        return false;
    }

    public Member addMember(String name, String address, String phone) {
        Member member = new Member(name, address, phone);
        if (memberList.insertMember(member)) {
            return (member);
        }
        return null;
    }

    public boolean changeProductQuantity(String productName, int newQuantity) {
        return catalog.changeProductQuantity(productName, newQuantity);
    }

    public Item addWaitItem(Product product, Member member, int quantity, float price){
        Item item = new Item(product, member, quantity, price);
        if (product != null ) {
            if (product.addWait(item)) {
                return (item);
            }
        }
        return null;
    }

    public boolean removeWaitItem(Item item, Product product){
        return product.removeWait(item);
    }

    public boolean addToWishlist(Member member, Record record) {
        return member.addwish(record);
    }

    public boolean removeFromWishlist(Member member, Record record) {
        return member.removeWish(record);
    }

    public Iterator getWishlist(Member member) {
        return member.getWish();
    }

    public boolean wishListIsempty(Member member){
        return member.isEmpty();
    }

    public int addToInvoices(Record wish, Member member) {
        Product p = wish.getProduct();
        if(p.getQuantity() >= wish.getQuantity()){
            int quantity = p.getQuantity() - wish.getQuantity();
            p.setQuantity(quantity);
            member.addtoInvoice(wish);
            return 0;
        }
        else{
            int quantity = wish.getQuantity() - p.getQuantity();
            p.setQuantity(0);
            Item wait = new Item(p, member, quantity, wish.getPrice());
            //member.addtoInvoice(wish);
            p.addWait(wait);
            return 1;
        }
    }

//    public boolean removeFromInvoices(Item item, Member member) {
//        return member.removeFromInvoice(item);
//    }

    public Iterator getInvoices(Member member) {
        return member.getInvoice();
    }

    public Iterator getProducts() { return catalog.getProducts();}

    //public String getMemberInfo() { return member.}

    public Iterator getMembers() {
        return memberList.getMembers();
    }
    public Iterator getWaitlist(Product product){
        return product.getWaitlist();
    }
    public Iterator processShipment(Shipment shipment){
        Product product = shipment.getProduct();
        Iterator waitlist = product.getWaitlist();
        return waitlist;
    }

    public boolean allocate(Shipment shipment, int quantity){
        if (shipment.allocate(quantity)){
            Product product = shipment.getProduct();
            product.addQuantity(quantity);
            return true;
        }
        return false;
    }

    public static Warehouse retrieve() {
        try {
//            Scanner keyboard = new Scanner(System.in);
//            System.out.println("filename");
//            String filename = keyboard.next();
            FileInputStream file = new FileInputStream("WarehouseData");
            ObjectInputStream input = new ObjectInputStream(file);
            warehouse = (Warehouse) input.readObject();
            MemberIdServer.retrieve(input);
            return warehouse;
            //input.readObject();
            //MemberIdServer.retrieve(input);
            //return warehouse;
        } catch(IOException ioe) {
            ioe.printStackTrace();
            return null;
        } catch(ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
            return null;
        }
    }
    public static  boolean save() {
        try {
            FileOutputStream file = new FileOutputStream("WarehouseData");
            ObjectOutputStream output = new ObjectOutputStream(file);
            output.writeObject(warehouse);
            output.writeObject(MemberIdServer.instance());
            return true;
        } catch(IOException ioe) {
            ioe.printStackTrace();
            return false;
        }
    }
    private void writeObject(java.io.ObjectOutputStream output) {
        try {
            output.defaultWriteObject();
            output.writeObject(warehouse);
        } catch(IOException ioe) {
            System.out.println(ioe);
        }
    }
    private void readObject(java.io.ObjectInputStream input) {
        try {
            input.defaultReadObject();
            if (warehouse == null) {
                warehouse = (Warehouse) input.readObject();
            } else {
                input.readObject();
            }
        } catch(IOException ioe) {
            ioe.printStackTrace();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    public String toString() {
        return catalog + "\n" + memberList;
    }
}
