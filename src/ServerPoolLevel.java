/**
 * Created by zhenghualong on 11/7/2017.
 */
import java.util.ArrayList;
public class ServerPoolLevel {
    ServerPool serverpool;

    protected int levelID; //0,1,...,I
    protected int size; //number of agents

    ArrayList<Agent> servicelevel = new ArrayList<>();

    public ServerPoolLevel(int levelID, ServerPool serverpool){
        this.levelID = levelID;
        this.serverpool = serverpool;
    }


    public void admit(Agent agent){
        servicelevel.add(agent);
        size++;
        serverpool.ZSizeUpdate(levelID,size);
    }



    public void startServe(Customer cust){
        size--;
        serverpool.ZSizeUpdate(levelID,size);

        Agent agent = servicelevel.remove(0);
        serverpool.getServiceLevel(levelID+1).admit(agent);
        agent.rescheduleServiceUp();
        agent.addCus(cust);
    }


    public void completeServe(Customer cust){  //renege from service or service completion
        size--;
        serverpool.ZSizeUpdate(levelID,size); //update for the server pool level size
        Agent agent = servicelevel.remove(getAgentIndexInLevel(cust.agentID));
        serverpool.getServiceLevel(levelID - 1).admit(agent);
        agent.removeServedCus(); //remove the served customer
        agent.rescheduleServiceDown(); //reschedule other customers

    }


    public int getAgentIndexInLevel(int ID){
        int temp=100;
        for(int i=0;i<servicelevel.size();i++){
            if(servicelevel.get(i).ID == ID){
                temp=i;
                break;
            }
        }
        return temp;
    }


    public void renegeServe(Customer cust){  //renege from service or service completion
        size--;
        serverpool.ZSizeUpdate(levelID,size); //update for the server pool level size

        Agent agent = servicelevel.remove(getAgentIndexInLevel(cust.agentID));
        agent.removeRenegeCus();
        agent.rescheduleServiceDown();
        serverpool.getServiceLevel(levelID - 1).admit(agent);


    }






}
