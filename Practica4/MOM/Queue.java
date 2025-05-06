package MOM;

import java.util.ArrayList;
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
        this.messages = new ArrayList<>();
        this.consumers = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void addMessage(Message message) {
        messages.add(message);
        notifyConsumers();
    }

    public void registerConsumer(Consumer callback) {
        consumers.add(callback);
        notifyConsumers();
    }

    // Notify the round-robin consumer
    private void notifyConsumers() {
        if (!consumers.isEmpty() && lastMessageProcessed < messages.size()) {

            Consumer consumer = consumers.get(indexRoundRobin);

            // Get the next message to process
            Message message = getNextMessage();
            try {
                consumer.procesarMensaje(message.getContent());
            } catch (Exception e) {

            }

            // Move to the next consumer in round-robin fashion
            indexRoundRobin = indexRoundRobin + 1 % consumers.size();
        }
    }

    private Message getNextMessage() {
        if (lastMessageProcessed + 1 < messages.size()) {
            lastMessageProcessed++;
            return messages.get(lastMessageProcessed);
        }
        return null;
    }
}
