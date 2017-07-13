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


    Agent agent;

    public Customer(double aTime, double sTime, double pTimeInQ, double pTimeInS, WebChat webchat) {
        this.webchat = webchat;
        this.arrivalTime = aTime;  // sim clock
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
//        System.out.printf("long!!!!" + isInService()+"\n");
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
//        System.out.printf("arrivalTime" + arrivalTime + "\n");
//        System.out.printf("service complete " + serviceCompleteEvent.time() + "\n");
//        System.out.printf("previousRescheduletime" + previousRescheduleTime + "\n");
//        System.out.printf("received Service Time" + receivedServiceTime + "\n");
//        System.out.printf("Service Time" + sTime + "\n");
//        System.out.printf("service level " + levelID + "\n");

//        System.out.printf("remaining Service Time " + receivedServiceTime +"\n");
//        System.out.printf("level ID " + levelID +"\n");
//        System.out.printf("mu" + webchat.mu[levelID -1] +"\n");
//        System.out.printf("isRenege()" + isRenege() +"\n");
//        System.out.printf("isInService()" + isInService() +"\n");
//        System.out.printf("Sime.time" + Sim.time() +"\n");
        serviceCompleteEvent.reschedule(remainingServiceTime / webchat.mu[levelID - 1]);

//        System.out.printf("service complete " + serviceCompleteEvent.time() + "\n");

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
//        System.out.printf("service levle schedule" + levelID + "\n");
        serviceCompleteEvent.schedule(sTime / webchat.mu[levelID -1]);
//        System.out.printf("service complete schedule " + serviceCompleteEvent.time() + "\n");
    }



}



