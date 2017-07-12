import umontreal.iro.lecuyer.simevents.Sim;

/**
 * Created by zhenghualong on 11/7/2017.
 */
public class Customer {

    protected double arrivalTime, // time when arrive, via Sim.time(), single use
            sTime, // service time, via random gen
            pTimeInQ,	// patient time, via random gen
            pTimeInS;

    protected int reneged, // if reneged 1, else 0
            served, // if service finished 1, else 0
            inQueue,
            inService;

    double receivedServiceTime;
    double previousRescheduleTime;
    double remainingServiceTime;
    double waitTime;

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
    }


    public void joinQueue(Buffer buffer){
        reneged = 0;
        served = 0;
        inQueue = 1;
        inService = 0;
        buffer.admit(this);
    }


    public void getService(int levelID){
        webchat.serverpool.getServiceLevel(levelID).startServe(this);
        reneged = 0;
        served = 0;
        inQueue = 0;
        inService = 1;
    }

    public void getInitiativeService(){
        webchat.serverpool.getServiceLevel(webchat.I-1).startServe(this);
        reneged = 0;
        inQueue = 0;
        inService = 1;
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
        waitTime = Sim.time() - arrivalTime;
        webchat.serverpool.getServiceLevel(getLevelID()).departServe(this);
        reneged = 1;
        served = 0;
        inQueue = 0;
        inService = 0;
    }


    public void completeService() {
        waitTime = Sim.time() - arrivalTime;
        webchat.serverpool.getServiceLevel(getLevelID()).departServe(this);
        reneged = 0;
        served = 1;
        inQueue = 0;
        inService = 0;
    }


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



    public void agent(Agent agent){
        this.agent = agent;
    }

    int getAgentID(){
        return agent.ID;
    }

    int getLevelID(){
        return agent.levelID;
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
        serviceCompleteEvent.schedule(sTime/webchat.mu[getLevelID()-1]);
    }



    public void rescheduleServiceDown(){
        receivedServiceTime = receivedServiceTime + (Sim.time()-previousRescheduleTime)*webchat.mu[getLevelID()];
        remainingServiceTime = sTime - receivedServiceTime;
        if(getLevelID()>0) {
            serviceCompleteEvent.reschedule(remainingServiceTime / webchat.mu[getLevelID() - 1]);
        }
        previousRescheduleTime = Sim.time();
    }

    public void rescheduleServiceUp(){
        receivedServiceTime = receivedServiceTime + (Sim.time()-previousRescheduleTime)*webchat.mu[getLevelID()-1];
        remainingServiceTime = sTime - receivedServiceTime;
        serviceCompleteEvent.reschedule(remainingServiceTime / webchat.mu[getLevelID()]);
        previousRescheduleTime = Sim.time();
    }
}



