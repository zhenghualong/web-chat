/**
 * Created by zhenghualong on 11/7/2017.
 */
import java.util.ArrayList;
public class ServerPoolLevel {
    ServerPool serverpool;
    WebChat webchat;

    protected int levelID;
    protected int size; //number of agents

    ArrayList<Agent> servicelevel = new ArrayList<>();

    public ServerPoolLevel(int levelID, ServerPool serverpool, WebChat webchat){
        this.levelID = levelID;
        this.serverpool = serverpool;
        this.webchat = webchat;
    }


    public void admit(Agent agent){
        servicelevel.add(agent);
        size++;
        serverpool.ZSizeUpdate(levelID,size);
    }


    public void startServe(Customer cust){
        size--;
        serverpool.ZSizeUpdate(levelID,size);

        System.out.printf("startService" + levelID+"\n");

        Agent agent = servicelevel.remove(0);
        serverpool.getServiceLevel(levelID+1).admit(agent);
        agent.rescheduleServiceUp();
        agent.addCus(cust);
    }


    public void departServe(Customer cust){  //renege from service or service completion
        size--;
        serverpool.ZSizeUpdate(levelID,size);

        Agent agent = servicelevel.get(getAgentIndexInLevel(cust.getLevelID()));
        servicelevel.remove(agent);
        System.out.printf("long server pool level" + levelID+"\n");
        if(levelID > 0) {
            serverpool.getServiceLevel(levelID - 1).admit(agent);
        }
        agent.removeCus(cust);
        agent.rescheduleServiceDown();
    }



    public int getAgentIndexInLevel(int ID){
        int temp=0;
        for(int i=0;i<size;i++){
            if(servicelevel.get(i).ID == ID){
                temp=i;
                break;
            }
        }
        return temp;
    }



}
