import java.util.*;
import java.lang.*;
import java.io.*;
import java.io.Serializable.*;

public class Product implements Serializable {

    public String ProductName;
    private String id;
    private static final String PRODUCT_STRING = "P";
    public int Quantity;
    public float Price;
    private Waitlist waitlist;


    public Product(String ProductName, int Quantity, float Price) {
        this.ProductName = ProductName;
        this.Quantity = Quantity;

        this.Price = Price;
        id = PRODUCT_STRING + (ProductIdServer.instance()).getId();
        this.waitlist = new Waitlist();
    }

    public int getQuantity() {
        return Quantity;
    }

    public String getProductName() {
        return ProductName;
    }

    public String getId() {
        return id;
    }

    public float getPrice(){
        return Price;
    }

    public void setPrice(float newPrice){
        Price = newPrice;
    }

    public void setQuantity(int newQuantity){
        Quantity = newQuantity;
    }
    public void addQuantity(int newQuantity){
        Quantity = Quantity + newQuantity;
    }
    public void setProductName(String newProductName){
        ProductName = newProductName;
    }
    public void setId(String newId){
        id = newId;
    }
    public boolean addWait(Item item){
        return waitlist.addToWaitlist(item);
    }
    public boolean removeWait(Item item){
        return waitlist.removeFromWaitlist(item);
    }
    public Iterator getWaitlist(){
        return waitlist.getItems();
    }
    public boolean isEmpty(){
        return waitlist.isEmpty();
    }

    public String toString() {
        return "Product Name: " + ProductName + " Id: " + id + " Quantity " + Quantity + " Price: $" + Price;
    }

}
