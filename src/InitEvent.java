/**
 * Created by zhenghualong on 12/7/2017.
 */
import umontreal.iro.lecuyer.simevents.Accumulate;
import umontreal.iro.lecuyer.simevents.Event;
import umontreal.iro.lecuyer.stat.Tally;
class InitEvent extends Event {
    WebChat webchat;
    public InitEvent(WebChat webchat) {
        this.webchat = webchat;
    }
    public void actions() {
        webchat.QSize      = new Accumulate ("QSize");
        webchat.ZSize = new Accumulate[webchat.EZ.length];
        for(int i = 0; i< webchat.EZ.length; i++){
            webchat.ZSize[i] = new Accumulate ("ZSize of Agents in Level i+1");
            webchat.ZSize[i].init();
        }
        webchat.Wait = new Tally ("Wait");
        webchat.WaitS = new Tally ("WaitS");
        webchat.WaitAb = new Tally ("WaitAb");

        webchat.nArrivals = 0;
        webchat.nServed = 0;
        webchat.nAbandon = 0;

        webchat.serverpool.init();
        webchat.buffer.init();

    }

}