class TestJava {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {});
    }

}

class SwingUtilities {

    public static void invokeLater(Runnable r) {}

}