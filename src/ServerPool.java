/**
 * Created by zhenghualong on 11/7/2017.
 */
import java.util.ArrayList;
public class ServerPool {
    int NAgents;
    int ILevel;
    WebChat webchat;

    ArrayList<ServerPoolLevel> pool = new ArrayList<>();

    public ServerPool(int N, int ILevel, WebChat webchat){
        NAgents = N;
        this.ILevel = ILevel;
        this.webchat = webchat;
    }

    public void init(){
        pool.clear();

        for(int i = 0; i < ILevel+1; i++){
            pool.add(new ServerPoolLevel(i, this, webchat));
        }
        for(int i = 0; i< NAgents; i++){
            pool.get(0).admit(new Agent(i, 0));
        }

    }

    public boolean isBusy(){
        if(pool.get(ILevel).size == NAgents){
            return true;
        }
        else{
            return false;
        }
    }

    public ServerPoolLevel getServiceLevel(int levelID){
        return pool.get(levelID);
    }


    public int priorityLevel(){
        int temp = 0;
        for (int i = 0; i < ILevel; i++){
            if(getServiceLevel(webchat.priority[i]).size > 0){
                temp = i;
                break;
            }
        }
        return temp;
    }

    private int getLevelSize(int levelID){
        return getServiceLevel(levelID).size;
    }

    public void ZSizeUpdate(int levelID, int size){
        if(levelID > 0) {
            webchat.ZSize[levelID-1].update(size);
        }
    }

}



