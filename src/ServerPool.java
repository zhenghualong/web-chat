/**
 * Created by zhenghualong on 11/7/2017.
 */
import java.util.ArrayList;
public class ServerPool {
    WebChat webchat;

    ArrayList<ServerPoolLevel> pool = new ArrayList<>();

    public ServerPool(WebChat webchat){
        this.webchat = webchat;
    }

    public void init(){
        pool.clear();

        for(int i = 0; i < webchat.I+1; i++){
            pool.add(new ServerPoolLevel(i, this));
        }
        for(int i = 0; i< webchat.N; i++){
            pool.get(0).admit(new Agent(i, 0, this));
        }

    }

    public boolean isBusy(){
        if(pool.get(webchat.I).size == webchat.N){
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
        for (int i = 0; i < webchat.I; i++){
            if(getServiceLevel(webchat.priority[i]).size > 0){
                temp = i;
                break;
            }
        }
        return temp;
    }


    public void ZSizeUpdate(int levelID, int size){
            webchat.ZSize[levelID].update(size);
    }

}



