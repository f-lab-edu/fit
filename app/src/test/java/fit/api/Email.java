package fit.api;

public class Email {

    private final String email;

    public Email(String localPort) {
        email = localPort + "@fit.com";
    }

    public String toString() {
        return email;
    }
}
