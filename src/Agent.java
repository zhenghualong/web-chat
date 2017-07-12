import java.util.ArrayList;

/**
 * Created by zhenghualong on 11/7/2017.
 */
public class Agent{
    int ID;
    int levelID;

    ArrayList<Customer> agentServeCustomer = new ArrayList<>();


    public Agent(int ID, int levelID){
        this.ID = ID;
        this.levelID = levelID;
        agentServeCustomer.clear();
    }

    public void served(Customer cust){
        levelID--;
        agentServeCustomer.remove(cust);
    }

    public void addCus(Customer cust){
        levelID++;
        agentServeCustomer.add(cust);
        cust.agent(this);
    }

    public void removeCus(Customer cust){
        levelID--;
        agentServeCustomer.remove(cust);
    }


    public void rescheduleServiceDown(){
        for (int i=0; i < agentServeCustomer.size(); i++){
            agentServeCustomer.get(i).rescheduleServiceDown();
        }
    }

    public void rescheduleServiceUp(){
        for (int i=0; i < agentServeCustomer.size(); i++){
            agentServeCustomer.get(i).rescheduleServiceUp();
        }
    }
}
