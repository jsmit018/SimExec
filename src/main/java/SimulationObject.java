import java.sql.Time;

public interface SimulationObject {

    public void ScheduleEventIn(Double t, EventAction ea, String eventName);

    public void ScheduleEventAt(Double t, EventAction ea, String eventName);

    public Double GetSimulationTime();

}