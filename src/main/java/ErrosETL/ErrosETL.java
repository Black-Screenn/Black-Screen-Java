package ErrosETL;

public class ErrosETL extends RuntimeException {
    public ErrosETL(){}
    public ErrosETL(String message) {
        super(message);
    }
}
