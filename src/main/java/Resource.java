import java.util.Queue;

public class Resource {
    private String _resourceName;
    private Integer _resourceCount;
    private SimToolQueue<Airport> _resourceQueue;

    Resource(String resName, int resCount){
        _resourceName = resName;
        _resourceCount = resCount;
        _resourceQueue = new SimToolQueue();
    }

    public String get_resourceName(){
        return _resourceName;
    }

    public Integer get_resourceCount(){
        return _resourceCount;
    }

    public boolean AcquireResource(int amountToAcquire){
        if (_resourceCount < amountToAcquire){
            System.out.println("Requested amount of resources is too large, adding request to queue until ready");
            //_resourceQueue.AddToQueue(airport);
            return false;
        }
        else{
            _resourceCount -= amountToAcquire;
            return true;
        }
    }

    public void ReleaseResource(
            int amountToRelease
    ) {
        _resourceCount += amountToRelease;
        System.out.println("Resources have been released. Checking Conditional event list");
        SimExecutor.simExec._condEventSet.ConditionalQueueTrigger(this);
    }

}
