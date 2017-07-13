import umontreal.iro.lecuyer.simevents.Sim;

/**
 * Created by zhenghualong on 11/7/2017.
 */
public class Customer {

     double arrivalTime, // time when arrive, via Sim.time(), single use
            sTime, // service time, via random gen
            pTimeInQ,	// patient time, via random gen
            pTimeInS;

     int reneged, // if reneged 1, else 0
            served, // if service finished 1, else 0
            inQueue,
            inService;

    int agentID;
    int levelID;


    double receivedServiceTime = 0;
    double previousRescheduleTime;
    double remainingServiceTime =0;
    double waitTime = 0;
    double completeServiceTime = 0;

    WebChat webchat;

    RenegeQEvent renegeQevent;
    RenegeSEvent renegeSevent;
    ServiceCompleteEvent serviceCompleteEvent;


    public Customer(double aTime, double sTime, double pTimeInQ, double pTimeInS, WebChat webchat) {
        this.webchat = webchat;
        this.arrivalTime = aTime;
        this.sTime = sTime;
        this.pTimeInQ = pTimeInQ;
        this.pTimeInS = pTimeInS;

        reneged = 0;
        served = 0;
        inQueue = 0;
        inService = 0;

        previousRescheduleTime = aTime;
    }


    public void joinQueue(Buffer buffer){
        reneged = 0;
        served = 0;
        inQueue = 1;
        inService = 0;
        buffer.admit(this);
    }


    public void getService(int levelID){
        reneged = 0;
        served = 0;
        inQueue = 0;
        inService = 1;
        webchat.serverpool.getServiceLevel(levelID).startServe(this);
    }

    public void getInitiativeService(){
        reneged = 0;
        served = 0;
        inQueue = 0;
        inService = 1;
        webchat.serverpool.getServiceLevel(webchat.I-1).startServe(this);
    }


    public void renegeQ(){
        waitTime = Sim.time() - arrivalTime;
        webchat.buffer.renege();
        reneged = 1;
        served = 0;
        inQueue = 0;
        inService = 0;
    }

    public void renegeS(){
        reneged = 1;
        served = 0;
        inQueue = 0;
        inService = 0;
        waitTime = Sim.time() - arrivalTime;
        webchat.serverpool.getServiceLevel(levelID).renegeServe(this);
    }


    public void completeService() {
        reneged = 0;
        served = 1;
        inQueue = 0;
        inService = 0;
        waitTime = Sim.time() - arrivalTime;
        webchat.serverpool.getServiceLevel(levelID).completeServe(this);
    }

    public void rescheduleServiceDown(Agent agent){
        levelID = agent.levelID;
        receivedServiceTime = receivedServiceTime + (Sim.time() - previousRescheduleTime)  * webchat.mu[levelID];
        remainingServiceTime = sTime - receivedServiceTime;
        serviceCompleteEvent.reschedule(remainingServiceTime / webchat.mu[levelID - 1]);
        previousRescheduleTime = Sim.time();
    }

    public void rescheduleServiceUp(Agent agent){
        levelID = agent.levelID;
        receivedServiceTime = receivedServiceTime + (Sim.time() - previousRescheduleTime) * webchat.mu[levelID - 2];
        remainingServiceTime = sTime - receivedServiceTime;
        serviceCompleteEvent.reschedule(remainingServiceTime / webchat.mu[levelID -1]);
        previousRescheduleTime = Sim.time();
    }


    // for checking customer's status
    boolean isInQueue() {
        if (inQueue == 1)
            return true;
        else
            return false;
    }

    boolean isInService() {
        if (inService == 1)
            return true;
        else
            return false;
    }

    boolean isServed() {
        if (served == 1)
            return true;
        else
            return false;
    }

    boolean isRenege() {
        if (reneged == 1)
            return true;
        else
            return false;
    }

    public void scheduleRenegeQ(){
        renegeQevent = new RenegeQEvent(webchat, this);
        renegeQevent.schedule(pTimeInQ);
    }

    public void scheduleRenegeS(){
        renegeSevent = new RenegeSEvent(webchat, this);
        renegeSevent.schedule(pTimeInS);
    }

    public void scheduleCompleteService(){
        serviceCompleteEvent = new ServiceCompleteEvent(webchat, this);
        serviceCompleteEvent.schedule(sTime / webchat.mu[levelID -1]);
    }



}



