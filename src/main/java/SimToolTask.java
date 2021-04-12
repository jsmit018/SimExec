public class SimToolTask {
    protected SimToolTask _nextTask;
    private String _taskName;
    private Integer _taskID;

    protected SimToolTask(String name){
        _taskName = name;
        _nextTask = null;
    }

    public void Arrive(Airplane airplane){
        Execute(airplane);
    }

    public void SetNextTask(SimToolTask nextTask){
        _nextTask = nextTask;
    }

    public String GetNextTask(){ return _nextTask.toString(); }

    public String GetName(){
        return _taskName;
    }

    public Integer GetTaskID(){
        return _taskID;
    }

    protected void SendFlight(Airplane airplane){
        _nextTask.Arrive(airplane);
    }

    protected void Execute(Airplane airplane){
    }

}
