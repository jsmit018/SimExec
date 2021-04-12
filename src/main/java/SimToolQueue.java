import java.util.ArrayList;
import java.util.List;

public class SimToolQueue<T> {
    //private T _queue;
    private List<T> _firstInFirstOutQueue;

    SimToolQueue(){
        _firstInFirstOutQueue = new ArrayList<>();
    }

    public void AddToQueue(T objectToInsert){
        int indexToAdd = (_firstInFirstOutQueue.size() - 1 < 0) ? 0 : _firstInFirstOutQueue.size();
        _firstInFirstOutQueue.add(indexToAdd, objectToInsert);
    }

    public T GetHeadOfQueue(){
        T head = _firstInFirstOutQueue.get(0);
        _firstInFirstOutQueue.remove(0);
        return head;
    }

    public int GetQueueSize(){
        return _firstInFirstOutQueue.size();
    }
}
