/**
 * Created by zhenghualong on 10/7/2017.
 */
import umontreal.iro.lecuyer.randvar.*;
import umontreal.iro.lecuyer.rng.*;

public class GenRandomVariable {
    RandomVariateGen genRan;
    double LogNormMu, LogNormSigma;


    public  GenRandomVariable(double mean, double var, int ranType){
        if(ranType == 0){
            genRan = new ExponentialGen (new MRG32k3a(), 1./mean);
        }else if(ranType == 1){
            LognormalConvert(mean, var);
            genRan = new LognormalGen(new MRG32k3a(),LogNormMu,LogNormSigma);
        }
    }

    public double nextDouble(){
        return  genRan.nextDouble();
    }

    private void LognormalConvert(double mean, double var) {
        double temp1 = Math.log(1.+var/(mean*mean));
        double temp2 = Math.log(var+mean*mean);
        LogNormMu = 2*Math.log(mean)-.5*temp2;
        LogNormSigma = Math.sqrt(temp1);
    }

}
