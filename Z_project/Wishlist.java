import java.util.*;
import java.io.*;
import java.util.Collections;
public class Wishlist implements Serializable {
    private List wishes = new LinkedList();
    private informationList copyList = new informationList(wishes);
    private static Wishlist wishlist;

    //copyList to avoid invoking remove while iterator is processing


    public static Wishlist instance() {
        if (wishlist == null) {
            return (wishlist = new Wishlist());
        } else {
            return wishlist;
        }
    }

    public boolean addToWishlist(Record item) {
        wishes.add(item);
        return true;
    }

    public boolean isEmpty(){
        return wishes.isEmpty();
    }

    public boolean removeFromWishlist(Record wish) {
        //System.out.println("Items before removal: " + wishes);
        //System.out.println("Item that is trying to be removed: " + wish);
        boolean removed = wishes.remove(wish);
        //System.out.println("Item removed: " + removed);
        return removed;
    }

    public Iterator getWishlist() {
        return copyList.getList();
    }
}
