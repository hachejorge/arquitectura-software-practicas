package MOM;

import java.util.List;

public class Queue {
    private String name;
    private List<Message> messages;
    private List<Consumer> consumers;

    private int lastMessageProcessed;
    private int indexRoundRobin;

    public Queue(String name) {
        this.name = name;
        this.lastMessageProcessed = -1;
        this.indexRoundRobin = 0;
    }

    public String getName() {
        return name;
    }

    public void addMessage(Message message) {
        messages.add(message);
    }

    public Message getNextMessage() {
        if (lastMessageProcessed + 1 < messages.size()) {
            lastMessageProcessed++;
            return messages.get(lastMessageProcessed);
        }
        return null;
    }

    // Notify the round-robin consumer
    private void notifyConsumers() {
        if (indexRoundRobin < consumers.size()) {

            Consumer consumer = consumers.get(indexRoundRobin);

            consumer.notify(messages.get(lastMessageProcessed));

            // Move to the next consumer in round-robin fashion
            indexRoundRobin = indexRoundRobin + 1 % consumers.size();
        }
    }
}
