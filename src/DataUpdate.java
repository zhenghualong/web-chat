/**
 * Created by zhenghualong on 11/7/2017.
 */

import umontreal.iro.lecuyer.simevents.Sim;


public class DataUpdate {
    WebChat webchat;
    Customer cust;

    public DataUpdate(WebChat webchat,Customer cust) {
        this.webchat = webchat;
        this.cust = cust;
    }


    public void RenEventQUpdate() throws Exception{
        if(recordTime()){
            webchat.nAbandon++;
            webchat.WaitAb.add(cust.waitTime);
            webchat.Wait.add(cust.waitTime);
            webchat.QSize.update(webchat.buffer.getSize());
        }
    }

    public void RenEventSUpdate() throws Exception{
        if(recordTime()){
            webchat.nAbandon++;
            webchat.Wait.add(cust.waitTime);
            webchat.WaitAb.add(cust.waitTime);
        }
    }

    public void ServiceCompleteUpdate(){
        if(recordTime()){
            webchat.nServed++;
            webchat.Wait.add(cust.waitTime);
            webchat.WaitS.add(cust.waitTime);
        }
    }

    public void EnterQueue() throws Exception{
        if(recordTime()){
            webchat.QSize.update(webchat.buffer.getSize());
        }
    }

    public void ArrivalUpdate(){
        if(recordTime()){
            webchat.nArrivals++;
        }
    }

    boolean recordTime(){
        if(webchat.startTime<=Sim.time() && Sim.time()<= webchat.stopTime){
            return true;
        }else{
            return false;
        }
    }

}
