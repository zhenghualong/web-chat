/**
 * Created by zhenghualong on 11/7/2017.
 */
import umontreal.iro.lecuyer.simevents.*;
class RenegeQEvent extends Event {
    WebChat webchat;
    Customer cust;
    public RenegeQEvent(WebChat webchat,Customer cust) {
        this.webchat = webchat;
        this.cust = cust;
    }
    public void actions() {
        // if that is really an abandonment
        if (cust.isInQueue()){
            cust.renegeQ();
            try {
                new DataUpdate(webchat,cust).RenEventQUpdate();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

}