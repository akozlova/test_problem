public class TestJava {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {});
    }

}

public class SwingUtilities {

    public static void invokeLater(Runnable r) {}

}