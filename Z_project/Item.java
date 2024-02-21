import java.io.Serializable;
import java.util.Objects;

public class Item implements Serializable {
    private Product product;
    private Member member;
    private int quantity;
    private float price;

    public Item(Product product, Member member, int quantity, float price) {
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

    public int getQuantity() {
        return quantity;
    }

    public float getPrice(){ return price;}
    public void setQuantity(int x){
        quantity = x;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null | getClass() != obj.getClass()) return false;
        Item other = (Item) obj;
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
