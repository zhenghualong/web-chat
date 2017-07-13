import umontreal.iro.lecuyer.simevents.*;
import umontreal.iro.lecuyer.stat.*;
import java.io.IOException;

/**
 * Created by zhenghualong on 10/7/2017.
 */
public class WebChat {
    //simulation time
    double startTime, stopTime, endTime, duringTime;
    double maxArrivals = 2E5;

    //parameters
    int I = 6; //service levels

    double lambda; //arrival rate
    int N; //number of agents
    double[] mu; //service rate of each level;

    int typeFq;    //0-exponential, 1-log normal
    double meanFq; //mean of patience time for waiting
    double varFq; //variance of patience time for waiting

    int typeG;
    double meanG; //mean of service time
    double varG; //variance of service time

    int typeF;
    double meanF; //mean of patience time for service
    double varF; //variance of patience time for service

    int[] priority = new int[I]; //priority order

    //performance measures
    int nArrivals, nServed, nAbandon;

    Accumulate QSize;
    Accumulate[] ZSize;

    Tally Wait;
    Tally WaitS;
    Tally WaitAb;



    Tally EQ = new Tally ("Expected Queue Length");
    Tally EW = new Tally ("Expected Time in System");
    Tally EW_S = new Tally ("Expected Time in System for Those Served");
    Tally EW_A = new Tally ("Expected Time in System for Those Abandoning");
    Tally PAb = new Tally ("Probability of Abandonment");
    Tally AbRate = new Tally ("Abandon Rate");

    Tally[] EZ = new Tally[I+1];

    //random events
    GenRandomVariable genArr, genFq, genG, genF;
    ArrivalEvent nextArrival;

    //system components
    ServerPool serverpool;
    Buffer buffer;

    public WebChat (String fileName) throws IOException {
        new ReadData(fileName, this);
        endTime = maxArrivals / lambda;
        startTime = 0.1 * endTime;
        stopTime = 0.9 * endTime;
        duringTime = stopTime - startTime;

        genArr = new GenRandomVariable(1. / lambda, 1. / (lambda * lambda), 0);
        genFq = new GenRandomVariable(meanFq, varFq, typeFq);
        genG  = new GenRandomVariable(meanG,  varG,  typeG);
        genF  = new GenRandomVariable(meanF,  varF,  typeF);

        for(int i = 0; i< EZ.length; i++){
            String temp = "Expected Number of Agents in Level " + Integer.toString(i);
            EZ[i] = new Tally (temp);
        }

        nextArrival = new ArrivalEvent(this);

        serverpool = new ServerPool(this);
        buffer = new Buffer();
    }


    public void simulateOneRun () {
        Sim.init();

        new InitEvent(this).schedule(0);
        nextArrival.schedule(genArr.nextDouble());

        Sim.start();

        EQ.add(QSize.average());
        EW.add( Wait.sum() / nArrivals);
        EW_S.add(WaitS.sum() / nServed);
        EW_A.add(WaitAb.sum() / nAbandon);
        PAb.add((double)nAbandon / nArrivals);
        AbRate.add(nAbandon / duringTime);


        for(int i = 0; i < EZ.length; i++){
            EZ[i].add(ZSize[i].average());
        }

    }

    static public void main (String[] args) throws IOException {
        WebChat webchat = new WebChat ("WebChat.dat");
        for (int i = 0; i < 10; i++)  webchat.simulateOneRun();

        for (int i = 0; i < webchat.EZ.length; i++) {
            webchat.EZ[i].setConfidenceIntervalStudent();
            webchat.EZ[i].setConfidenceLevel (0.95);
        }
        System.out.println (Tally.report ("WebChat Server Pool:", webchat.EZ));
        webchat.EQ.setConfidenceIntervalStudent();
        webchat.EW_S.setConfidenceIntervalStudent();
        webchat.EW_A.setConfidenceIntervalStudent();
        webchat.EW.setConfidenceIntervalStudent();
        webchat.PAb.setConfidenceIntervalStudent();
        webchat.AbRate.setConfidenceIntervalStudent();
        int digit = 5;
        double confidentLevel = 0.95;
        System.out.println (webchat.AbRate.report (confidentLevel, digit));

        System.out.println (webchat.EW_S.report (confidentLevel, digit));
        System.out.println (webchat.EW_A.report (confidentLevel, digit));
        System.out.println (webchat.EW.report (confidentLevel, digit));
        System.out.println (webchat.PAb.report (confidentLevel, digit));

        System.out.println (webchat.EQ.report (confidentLevel, digit));

    }

}
