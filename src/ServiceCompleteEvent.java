/**
 * Created by zhenghualong on 12/7/2017.
 */
import umontreal.iro.lecuyer.simevents.*;
import java.util.Random;
class ServiceCompleteEvent extends Event {
    WebChat webchat;
    Customer cust;

    public ServiceCompleteEvent(WebChat webchat, Customer cust) {
        this.webchat = webchat;
        this.cust = cust;
    }

    public void actions() {
        if (cust.isInService()){
            int levelID = cust.getLevelID();
            cust.completeService();
            try {
                new DataUpdate(webchat,cust).ServiceCompleteUpdate();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if(levelID == webchat.I && webchat.buffer.isNonEmpty()){
                Customer nextCust = webchat.buffer.nextInQueue();
                nextCust.getInitiativeService();

                nextCust.scheduleRenegeS();
            }
        }


    }
}
