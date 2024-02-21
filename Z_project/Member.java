import java.util.*;
import java.io.*;

public class Member implements Serializable{

    private String id;
    private String name;
    private String address;
    private static final String MEMBER_STRING = "M";
    private String phone;
    private Wishlist wishlist;
    private InvoiceList invoice;
    private String status;


    public Member (String name, String address, String phone){
        //this.id =id;

        this.name = name;
        this.address = address;
        this.phone = phone;
        id = MEMBER_STRING + (MemberIdServer.instance()).getId();
        this.wishlist = new Wishlist();
        this.invoice = new InvoiceList();

    }

    public String getName() {
        return name;
    }
    public String getPhone() {
        return phone;
    }
    public String getAddress() {
        return address;
    }
    public String getId() {
        return id;
    }

    public boolean addwish(Record rec){
        return wishlist.addToWishlist(rec);
    }
    public Iterator getInvoice(){
        return invoice.getInvoices();
    }
    public boolean removeFromInvoice(Record rec){
        return invoice.removeFromInvoices(rec);
    }
    public boolean addtoInvoice(Record rec){
        return invoice.addToInvoices(rec);
    }
    public boolean removeWish(Record record){
        return wishlist.removeFromWishlist(record);
    }

    public Iterator getWish(){
        return wishlist.getWishlist();
    }

    public boolean isEmpty(){
        return wishlist.isEmpty();
    }

    public void setName(String newName) {
        name = newName;
    }
    public void setAddress(String newAddress) {
        address = newAddress;
    }
    public void setPhone(String newPhone) {
        phone = newPhone;
    }
    public boolean equals(String id) {
        return this.id.equals(id);
    }


    public String toString(){
        String string = "Member Name: " + name + " Id: " + id + " Address: " + address + " Phone: " + phone;
        return string;
    }


}