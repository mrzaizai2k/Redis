import java.io.Serializable;

public class Shipment implements Serializable {
    private Product product;
    private int quantity;

    public Shipment(Product product, int quantity){
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public boolean allocate(float subtract){
        if (quantity >= subtract){
            quantity = quantity - subtract;
            return true;
        }
        return false;
    }

    public String toString() {
        return "Product Name: " + product.getProductName() + " Quantity: " + getQuantity();
    }
}
