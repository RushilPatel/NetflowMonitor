package manager;

import subscriber.ISubscriber;

import java.util.LinkedList;
import java.util.List;

public class SubscriptionManager {

    private List<ISubscriber> subscribers;

    public SubscriptionManager(){
        subscribers = new LinkedList<ISubscriber>();
    }

    public void addSubscriber(ISubscriber subscriber){
        this.subscribers.add(subscriber);

    }

    public void removeSubscriber(ISubscriber subscriber){
        this.subscribers.remove(subscriber);
    }

    public List<ISubscriber> getSubscribers(){
        return this.subscribers;
    }
}
