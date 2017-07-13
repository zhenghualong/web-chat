import umontreal.iro.lecuyer.simevents.Sim;

import java.util.ArrayList;

/**
 * Created by zhenghualong on 11/7/2017.
 */
public class Agent{
    int ID;
    int levelID;

    ServerPool serverPool;

    ArrayList<Customer> agentServeCustomer = new ArrayList<>();


    public Agent(int ID, int levelID, ServerPool serverPool){
        this.ID = ID;
        this.levelID = levelID;
        this.serverPool = serverPool;
        agentServeCustomer.clear();
    }


    public void addCus(Customer cust){
        agentInfoUpdate(cust);
        agentServeCustomer.add(cust);
    }

    public void removeServedCus(){
        levelID--;
        agentServeCustomer.remove(getRemoveServedCustomerIndex());

    }

    public void removeRenegeCus(){
        levelID--;
        agentServeCustomer.remove(getRemoveRenegeCustomerIndex());

    }


    public void rescheduleServiceDown(){
        for (int i=0; i < agentServeCustomer.size(); i++){
            agentServeCustomer.get(i).rescheduleServiceDown(this);
        }
    }

    public void rescheduleServiceUp(){
        levelID++;
        for (int i=0; i < agentServeCustomer.size(); i++){
            agentServeCustomer.get(i).rescheduleServiceUp(this);
            int temp = serverPool.getServiceLevel(0).size;
        }
    }

    public void agentInfoUpdate(Customer cust){
        cust.levelID = levelID;
        cust.agentID = ID;
    }

    public int getRemoveServedCustomerIndex(){
        int temp = 0;
        for (int i=0; i < agentServeCustomer.size(); i++){
            if(agentServeCustomer.get(i).isServed()){
                temp = i;
                break;
            }
        }
        return temp;
    }

    public int getRemoveRenegeCustomerIndex(){
        int temp = 0;
        for (int i=0; i < agentServeCustomer.size(); i++){
            if(agentServeCustomer.get(i).isRenege()){
                temp = i;
                break;
            }
        }
        return temp;
    }
}
