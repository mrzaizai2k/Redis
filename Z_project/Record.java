import java.io.Serializable;
import java.util.Objects;

public class Record implements Serializable {
    private Product product;
    private Member member;
    private int quantity;
    private float price;

    public Record(Product product, Member member, int quantity, float price) {
        this.product = product;
        this.member = member;
        this.quantity = quantity;
        this.price = price;
    }

    public Product getProduct() {
        return product;
    }

    public Member getMember() {
        return member;
    }
    public float getPrice(){
        return price;
    }

    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int newQuantity) {
        this.quantity = newQuantity;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null | getClass() != obj.getClass()) return false;
        Record other = (Record) obj;
        return Objects.equals(this.product, other.product) &&
                Objects.equals(this.member, other.member) &&
                Objects.equals(this.quantity, other.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(product, member, quantity);
    }

    public String toString() {
        return "Product Name: " + product.getProductName() + " Member: " + member.getName() + " Quantity: " + quantity + " Price: $" + price;
    }
}