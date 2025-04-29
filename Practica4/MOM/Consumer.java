package MOM;

public class Consumer {
    private String name;

    Consumer() {
        this.name = "Consumer";
    }

    public void notify(Message message) {
        // Logic to handle the message
        System.out.println("[" + this.name + "] receives: " + message.getContent());
    }
}
