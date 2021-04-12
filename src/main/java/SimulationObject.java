import java.sql.Time;

public interface SimulationObject {

    //public double simulationTime = 0.0;

    public void ScheduleEventIn(Double t, EventAction ea, String eventName);

    public void ScheduleEventAt(Double t, EventAction ea, String eventName);

    public Double GetSimulationTime();

}