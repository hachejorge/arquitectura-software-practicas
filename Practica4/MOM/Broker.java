package MOM;

import java.util.List;

public class Broker {
    private List<Queue> queues;

    private int getQueueIndex(String name) {
        for (int i = 0; i < queues.size(); i++) {
            if (queues.get(i).getName().equals(name)) {
                return i;
            }
        }
        return -1;
    }

    public void declare_queue(String name) {
        int index = getQueueIndex(name);
        if (index == -1) {
            Queue newQueue = new Queue(name);
            queues.add(newQueue);
        }
    }

    public void publish(String queueName, Message message) {
        int index = getQueueIndex(queueName);
        if (index != -1) {
            Queue queue = queues.get(index);
            queue.addMessage(message);

            // Notify consumers

        } else {
            System.out.println(queueName + " not found");
        }
    }

    public Message consume(String queueName) {
        int index = getQueueIndex(queueName);
        if (index != -1) {
            Queue queue = queues.get(index);
            return queue.getNextMessage();
        }
        return null;
    }
}
