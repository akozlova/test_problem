import static javax.swing.SwingUtilities.invokeLater;

public class TestJava {

    public static void main(java.lang.String[] args) {
        invokeLater(() -> {});
    }

    public static void invokeLater(java.lang.Runnable r) {}

}