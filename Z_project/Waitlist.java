import java.util.*;
import java.io.*;
public class Waitlist implements Serializable {

    //private List<Item> items;
    private List items = new LinkedList();
    private informationList copyList = new informationList(items);

    private static Waitlist waitList;



    public static Waitlist instance(){
        if (waitList = null){
            return (waitList = new Waitlist());
        } else{
            return waitList;
        }
    }

    public boolean addToWaitlist(Item item) {
        items.add(item);
        return true;
    }



    public boolean removeFromWaitlist(Item item) {
        System.out.println("Items before removal: " + items);
        System.out.println("Item that is trying to be removed: " +  item);
        boolean removed = items.remove(item);
        System.out.println("Item removed: " + removed);
        return removed;
    }

    public Iterator getItems(){
        return copyList.getList();
    }

    public boolean isProductOnWaitlist(Item i) {
        return items.contains(i);
    }

    public void clearWaitlist() {
        items.clear();
    }
    public boolean isEmpty(){
        return items.isEmpty();
    }




}
