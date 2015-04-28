package channel;


import manager.SubscriptionManager;
import subscriber.ISubscriber;

public abstract class SubscriptionChannel {

    private SubscriptionManager subscriptionManager;
    public SubscriptionChannel(SubscriptionManager subscriptionManager){
        this.subscriptionManager = subscriptionManager;
    }

    public void publish(String data){
        for(ISubscriber subscriber : subscriptionManager.getSubscribers()){
            subscriber.send(data);
        }
    }



    public abstract void start();

    public abstract void stop();


}
