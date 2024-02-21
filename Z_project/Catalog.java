import java.util.*;
import java.lang.*;
import java.io.*;

public class Catalog implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<Product> products = new LinkedList();
    private static Catalog catalog;
    private Catalog(){
    }
    public static Catalog instance(){
        if(catalog == null){
            return (catalog = new Catalog());
        } else {
            return catalog;
        }
    }

    public boolean insertProduct(Product product){
        //System.out.println("This is the catalog");
        //System.out.println("This is the catalog list: " + products);
        products.add(product);
        return true;
    }

    public boolean removeFromCatalog(Product product){
        //System.out.println("Products before removal: [" + products + "]");
        //System.out.println("product that is trying to be removed: [" +  product + "]");

        boolean removed = products.remove(product);
        System.out.println("Product removed: " + removed);
        return removed;
    }

    public boolean changeProductQuantity(String productName, int newQuantity) {
        Iterator<Product> productIterator = products.iterator();
        while (productIterator.hasNext()) {
            Product product = productIterator.next();
            if (product.getProductName().equalsIgnoreCase(productName)) {
                product.setQuantity(newQuantity);
                return true;
            }
        }
        return false; // Product not found
    }

    public Iterator<Product> getProducts(){ return products.iterator();}

    private void writeObject(java.io.ObjectOutputStream output){
        try {
            output.defaultWriteObject();
            output.writeObject(catalog);
        } catch(IOException ioe){
            System.out.println(ioe);
        }
    }
    private void readObject(java.io.ObjectInputStream input) {
        try {
            if (catalog != null) {
                return;
            } else {
                input.defaultReadObject();
                if (catalog = null) {
                    catalog = (Catalog) input.readObject();
                } else {
                    input.readObject();
                }
            }
        } catch(IOException ioe) {
            System.out.println("in Catalog readObject \n" + ioe);
        } catch(ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        }
    }
    public String toString() {
        return products.toString();
    }
}
