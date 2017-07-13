/**
 * Created by zhenghualong on 11/7/2017.
 */

import java.util.Random;

import umontreal.iro.lecuyer.simevents.*;

class ArrivalEvent extends Event {
    WebChat webchat;

    public ArrivalEvent(WebChat webchat){
        this.webchat = webchat;
    }
    public void actions() {

        if(Sim.time()< webchat.endTime){
            double aTime = Sim.time(); //arrival time
            double pTimeInQ = webchat.genFq.nextDouble(); //patience time for waiting
            double sTime = webchat.genG.nextDouble(); //service time
            double pTimeInS = webchat.genF.nextDouble(); //patience time for service

            Customer cust = new Customer(aTime, sTime, pTimeInQ, pTimeInS, webchat);

            new DataUpdate(webchat,cust).ArrivalUpdate();
            double nextDouble= webchat.genArr.nextDouble();
            webchat.nextArrival.schedule(nextDouble);


            if (webchat.serverpool.isBusy()) {
                cust.joinQueue(webchat.buffer);
                try {
                    new DataUpdate(webchat,cust).EnterQueue();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                cust.scheduleRenegeQ();

            } else{
                int levelID;
                levelID = webchat.serverpool.priorityLevel();
                cust.getService(levelID);
                cust.scheduleCompleteService();
                cust.scheduleRenegeS();
            }

        }
        else{
            webchat.nextArrival.cancel();
        }
    }
}
