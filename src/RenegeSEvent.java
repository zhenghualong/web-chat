/**
 * Created by zhenghualong on 12/7/2017.
 */
import umontreal.iro.lecuyer.simevents.*;
class RenegeSEvent extends Event {
    WebChat webchat;
    Customer cust;

    public RenegeSEvent(WebChat webchat,Customer cust) {
        this.webchat = webchat;
        this.cust = cust;
    }
    public void actions() {
        // if that is really an abandonment
        if (cust.isInService()){
            int levelID = cust.getLevelID();
            cust.renegeS();
            try {
                new DataUpdate(webchat,cust).RenEventSUpdate();
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