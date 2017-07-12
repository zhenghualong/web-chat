import java.io.*;
import java.util.*;

/**
 * Created by zhenghualong on 10/7/2017.
 */
public class ReadData {


    public  ReadData (String fileName, WebChat webchat) throws IOException {
        Locale loc = Locale.getDefault();
        Locale.setDefault(Locale.US); // to read reals as 8.3 instead of 8,3
        BufferedReader input = new BufferedReader (new FileReader(fileName));
        Scanner scan = new Scanner(input);
        webchat.lambda = scan.nextDouble();          scan.nextLine();
        webchat.N = scan.nextInt();      scan.nextLine();

        webchat.mu = new double[webchat.I];
        for (int i = 0; i < webchat.I; i++) {
            webchat.mu[i] = scan.nextDouble();
            scan.nextLine();
        }

        webchat.typeFq = scan.nextInt();      scan.nextLine();
        webchat.meanFq = scan.nextDouble();   scan.nextLine();
        webchat.varFq = scan.nextDouble();    scan.nextLine();

        webchat.typeG = scan.nextInt();       scan.nextLine();
        webchat.meanG = scan.nextDouble();    scan.nextLine();
        webchat.varG = scan.nextDouble();     scan.nextLine();

        webchat.typeF = scan.nextInt();       scan.nextLine();
        webchat.meanF = scan.nextDouble();    scan.nextLine();
        webchat.varF = scan.nextDouble();     scan.nextLine();

        for(int i =0; i <webchat.I; i++){
            webchat.priority[i] = scan.nextInt();
            scan.nextLine();
        }


        scan.close();
        Locale.setDefault(loc);
    }
}
