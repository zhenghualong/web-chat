/**
 * Created by zhenghualong on 11/7/2017.
 */
import java.util.LinkedList;


public class Buffer {

    protected int size;

    LinkedList<Customer> buffer = new LinkedList<> ();

    public Buffer() {
    }

    public void init(){
        buffer.clear();
        size = 0;
    }

    public void admit(Customer cust){
        buffer.addLast(cust);
        size++;
    }

    public boolean isNonEmpty() {
        while( (!buffer.isEmpty()) && (buffer.getFirst().reneged == 1)){
            buffer.removeFirst();
        }

        if (buffer.isEmpty())
            return false;
        else
            return true;
    }

    public Customer nextInQueue() { // must be used right after isNonEmpty()
        size--;
        return buffer.removeFirst();
    }

    public void renege() {
        size--;
    }

    public int getSize() {
        return size;
    }


}
