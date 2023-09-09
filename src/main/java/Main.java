public class Main {
    public static void main(String[] args) {
        someOtherFunction();
    }

    public static void someFunction() throws Exception {
        throw new Exception("A checked exception");
    }

    public static void someOtherFunction() {
        try {
            someFunction();
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
}
