

public class SimulationExecutive implements SimulationObject{

    private Double _simulationTime;
    private EventSet _eventSet;
    public ConditionalEventSet _condEventSet;

    private class Event{
        public EventAction _ea;
        public Double _time;
        public String _eventName;
        public Event _nextEvent;

        Event(EventAction ea, Double t, String eventName){
            _ea = ea;
            _time = t;
            _eventName = eventName;
            _nextEvent = null;
        }
    }

    private class EventSet{
        private Event _head;
        private Integer _numberOfEvents;


        EventSet(){InitializeES();}

        public void InitializeES(){
            _head = null;
            _condEventSet = new ConditionalEventSet();
            _numberOfEvents = 0;
        }

        public void AddEvent(Event e){
            if(_head == null){
                _head = e;
                _numberOfEvents++;
                if (e._eventName.equals("Passenger Arrival Event"))
                    _numberOfEvents --;
            }
            else if (_head._time > e._time){
                Event temp = _head;
                e._nextEvent = temp;
                _head = e;
                _numberOfEvents++;
               if (e._eventName.equals("Passenger Arrival Event"))
                    _numberOfEvents --;
            }
            else{
                Event currentEvent = _head;
                while (currentEvent._nextEvent != null && currentEvent._time < e._time){
                    if(currentEvent._nextEvent._time > e._time){
                        e._nextEvent = currentEvent._nextEvent;
                        currentEvent._nextEvent = e;
                        _numberOfEvents++;
                        if (e._eventName.equals("Passenger Arrival Event"))
                            _numberOfEvents --;
                        return;
                    }
                    else {
                        currentEvent = currentEvent._nextEvent;
                    }
                }
                if (currentEvent._nextEvent == null) {
                    currentEvent._nextEvent = e;
                    _numberOfEvents++;
                    if (e._eventName.equals("Passenger Arrival Event"))
                        _numberOfEvents --;
                }
                else{
                    e._nextEvent = currentEvent._nextEvent;
                    currentEvent._nextEvent = e;
                    _numberOfEvents++;
                    if (e._eventName.equals("Passenger Arrival Event"))
                        _numberOfEvents --;
                }
            }
        }

        public EventAction GetEventAction(){
            _numberOfEvents--;
            Event temp = _head;
            _head = _head._nextEvent;
            _simulationTime = temp._time;
            System.out.println("Event is executing at " + _simulationTime);
            return temp._ea;
        }

        public int GetNumberOfEventsLeftInEventSet(){
            return _numberOfEvents;
        }
    }

    public void InitializeSimulation(){
        _simulationTime = 0.0;
        _eventSet = new EventSet();
    }

    public void RunSimulation(){
        while (_eventSet.GetNumberOfEventsLeftInEventSet() != 0)
            _eventSet.GetEventAction().Execute();
        System.out.println("The Simulation terminated at " + _simulationTime);
        System.out.println(""); System.out.println("");
        for (int i = 0; i < SimExecutor._destinationsMap.size() - 1; ++i){
            System.out.println(SimExecutor._destinationsMap.get(SimExecutor._destinationsKeyList.get(i)).GetName() + " had "
                    + SimExecutor._destinationsMap.get(SimExecutor._destinationsKeyList.get(i)).AirportFlightTracker()
            + " flights arrive to this location");
        }

    }

    @Override
    public void ScheduleEventIn(Double t, EventAction ea, String eventName) {
        Event eventToAdd = new Event(ea,_simulationTime + t, eventName);
        _eventSet.AddEvent(eventToAdd);
    }

    @Override
    public void ScheduleEventAt(Double t, EventAction ea, String eventName) {
        Event eventToAdd = new Event(ea, t, eventName);
        _eventSet.AddEvent(eventToAdd);
    }

    @Override
    public Double GetSimulationTime() {
        return _simulationTime;
    }
}
