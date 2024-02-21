import java.util.*;
import java.io.*;

public class InvoiceList implements Serializable {
    private List<Record> invoices = new LinkedList();
    private static InvoiceList invoiceList;

    public static InvoiceList instance() {
        if (invoiceList == null) {
            return (invoiceList = new InvoiceList());
        } else {
            return invoiceList;
        }
    }

    public boolean addToInvoices(Record rec) {
        invoices.add(rec);
        return true;
    }

    public boolean removeFromInvoices(Record rec) {
        boolean removed = invoices.remove(rec);
        return removed;
    }

    public Iterator getInvoices() {
        return invoices.iterator();
    }
}
