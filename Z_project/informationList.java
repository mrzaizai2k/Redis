import java.util.*;
import java.io.*;

public class informationList implements Serializable {
    private List obj = new LinkedList();
    //private static informationList info;

    public informationList(List list) {
        this.obj = list;
    }

    public Iterator getList(){
        return obj.iterator();
    }
}
