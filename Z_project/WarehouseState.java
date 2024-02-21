public abstract class WarehouseState {
    protected static WarehouseContext context;
    protected WarehouseState() {
        //context = LibContext.instance();
    }
    public abstract void run();
}

