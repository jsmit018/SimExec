import java.util.*;
import java.util.stream.Collectors;

public class Airport extends SimToolTask{
    private String _name;
    private Integer _capacityOfAirport;
    private Integer _numberOfTravelers;
    private Resource _terminalGates;
    private int _numberOfAirplanesToCreate;
    private Map<String, SimToolQueue<Person>> _waitingToBoardQueue;
    public static List<String> _airportConstructionParams;
    private int _flightTrackerCounter;
    private SimToolQueue<Person> _baggageCheckQueue;
    private SimToolQueue<Person> _tsaPreCheck;
    private SimToolQueue<Person> _terminalLobby;
    private boolean _originalPassengers = true;

    //Static Functions
    ///___________________________________________
    public static void PopulateAirportParams(){
        _airportConstructionParams = new ArrayList<>();
        _airportConstructionParams.add(0, "airportName");
        _airportConstructionParams.add(1, "capacity");
        _airportConstructionParams.add(2, "gates");
        _airportConstructionParams.add(3, "numberOfTravelers");
    }

    public static List<String> GetAirportParams(){
        return _airportConstructionParams;
    }

    public static String GetKey(){
        return "airport";
    }
    ///___________________________________________

    private class ArriveEventAction implements EventAction{
        private Airport _airport;
        private Airplane _airplane;

        ArriveEventAction(
                Airport airport,
                Airplane airplane
        ){
            _airport = airport;
            _airplane = airplane;
        }

        @Override
        public void Execute() {
            _airport.ArriveEventMethod(_airplane);
        }
    }

    private class DebarkEventAction implements EventAction{
        private Airport _airport;
        private Airplane _airplane;

        DebarkEventAction(
                Airport airport,
                Airplane airplane
        ){
            _airport = airport;
            _airplane = airplane;
        }

        @Override
        public void Execute() {
            _airport.DebarkEventMethod(_airplane);
        }
    }

    private class CleanAndRefuelEventAction implements EventAction{
        private Airport _airport;
        private Airplane _airplane;

        CleanAndRefuelEventAction(
                Airport airport,
                Airplane airplane
        ){
            _airport = airport;
            _airplane = airplane;
        }

        @Override
        public void Execute() {
            _airport.CleanAndRefuelEM(_airplane);
        }
    }

    private class EmbarkPassengersEventAction implements  EventAction{
        private Airport _airport;
        private Airplane _airplane;

        EmbarkPassengersEventAction(
                Airport airport,
                Airplane airplane
        ){
            _airport = airport;
            _airplane = airplane;
        }

        @Override
        public void Execute() {
            _airport.EmbarkPassengersEM(_airplane);
        }
    }

    private class TakeOffEventAction implements EventAction{
        private Airport _airport;
        private Airplane _airplane;

        TakeOffEventAction(
                Airport airport,
                Airplane airplane
        ){
            _airport = airport;
            _airplane = airplane;
        }

        @Override
        public void Execute() {
            _airport.TakeOffEM(_airplane);
        }
    }

    private class ScheduleTravelArrival implements EventAction{
        Airport _airport;

        ScheduleTravelArrival(Airport airport){
            _airport = airport;
        }

        @Override
        public void Execute() {
            _airport.ScheduleTravelerArrivalToAirport();
        }
    }

    private class PassengerEntityArrivalEventAction implements EventAction{
        private Airport _airport;
        private SimToolQueue _stq;

        PassengerEntityArrivalEventAction(Airport airport, SimToolQueue stq){
            _airport = airport;
            _stq = stq;
        }

        public void Execute(){ _airport.PassengerEntityArrivalEM(_stq); }
    }

    private class TSACheckInEventAction implements EventAction{
        private Airport _airport;
        private SimToolQueue _stq;
        private SimToolQueue _previousQueue;

        TSACheckInEventAction(Airport airport, SimToolQueue stq, SimToolQueue pq){
            _airport = airport;
            _stq = stq;
            _previousQueue = pq;
        }

        public void Execute(){ _airport.TSACheckInEM(_stq, _previousQueue); }
    }

    private class PreBoardingTerminalEventAction implements EventAction{
        private Airport _airport;
        private SimToolQueue _stq;

        PreBoardingTerminalEventAction(Airport airport, SimToolQueue stq){
            _airport = airport;
            _stq = stq;
        }

        public void Execute(){_airport.PreBoardingTerminalEM(_stq); }
    }

    /*
    Class Functions will be listed below this line
     */

    Airport(String name, int capacity, int gates, int numberOfTravelers, int numberOfAirplanesToCreate){
        super(name);
        _name = name;
        _capacityOfAirport = capacity;
        _terminalGates = new Resource("Gates", gates);
        _numberOfTravelers = numberOfTravelers;
        _numberOfAirplanesToCreate = numberOfAirplanesToCreate;
        _flightTrackerCounter = 0;
        _baggageCheckQueue = new SimToolQueue<>();
        _tsaPreCheck = new SimToolQueue<>();
        _terminalLobby = new SimToolQueue<>();
        _waitingToBoardQueue = new HashMap<>();
        //GenerateAirportFootTraffic();
        //SimExecutor.simExec.ScheduleEventIn(0.0, new ScheduleTravelArrival(this), "Passenger Arrival Event");
    }

    public void ScheduleFlights(){
        for (int i = 0; i < _numberOfAirplanesToCreate; ++i){
            Airplane airplaneToSchedule = new Airplane(SimExecutor._masterAirplaneList.get(RandomNumber.randomInt(0, SimExecutor._masterAirplaneList.size() - 1)));
            airplaneToSchedule.MapToList(SimExecutor._destinationsMap.keySet());
            _flightTrackerCounter++;
            SimExecutor.simExec.ScheduleEventIn(Distributions.triangularDistribution(0, 15,30), new EmbarkPassengersEventAction(this, airplaneToSchedule), "Embark Event Action");
        }
    }

    public void PopulateHashMap(Set keySet){
        List<String> tempList = new ArrayList<>();
        tempList.addAll(keySet);
        for (String s : tempList){
            _waitingToBoardQueue.put(s, new SimToolQueue<>());
        }
    }

    protected void SendFlightToNextAirport(Airplane outgoingPlane){
//        List destinations = outgoingPlane.get_listOfDestinations().map(lod -> lod.toString()).collect(Collectors.toList());
//        int randomDest = RandomNumber.randomInt(0, SimExecutor._destinationsMap.size() - 1);
//        if (SimExecutor._destinationsMap.get(destinations.get(randomDest))._name.equals(this._name)){
//            int newRandDest = 0;
//            do {
//                newRandDest = RandomNumber.randomInt(0, SimExecutor._destinationsMap.size() - 1);
//            } while (SimExecutor._destinationsMap.get(destinations.get(newRandDest))._name.equals(this._name));
//            this.SetNextTask(SimExecutor._destinationsMap.get(destinations.get(randomDest)));
//        } else {
//            this.SetNextTask(SimExecutor._destinationsMap.get(destinations.get(randomDest)));
//        }
        SendFlight(outgoingPlane);
    }

    public void InsertPassengerIntoBaggageCheck(Person p){
        _baggageCheckQueue.AddToQueue(p);
        if (_originalPassengers != true)
            this._numberOfTravelers++;
    }

    public void StartPassengerProcessing(){
        SimExecutor.simExec.ScheduleEventIn(0.0, new TSACheckInEventAction(this, this._tsaPreCheck, this._baggageCheckQueue), "TSA " +
                "Check In");
    }

    private void GenerateAirportFootTraffic(){
        //int _numberOfPeopleToGenerate = RandomNumber.randomInt(25,_capacityOfAirport);
        for (int i = 0; i < _numberOfTravelers; ++i){
            Person temp = new Person();
            this.InsertPassengerIntoBaggageCheck(temp);
        }
    }

    public void TriggerFootTrafficGeneration(){
        GenerateAirportFootTraffic();
        _originalPassengers = false;
    }

    @Override
    protected void Execute(Airplane airplane){
        SimExecutor.simExec.ScheduleEventIn(0.0, new ArriveEventAction(this, airplane), "Airport Arrival Event");
    }

    /**
     * Going to list all of the associated event methods below all of the actual class functions
     */

    private void ArriveEventMethod(
            Airplane airplane
    ) {
        _flightTrackerCounter++;
        airplane.UpdateListOfExecutedDestinations(this);
        if (_terminalGates.AcquireResource(1) == true)
        {
            System.out.println("Acquired a gate, docking airplane");
            SimExecutor.simExec.ScheduleEventIn(Distributions.triangularDistribution(5, 10, 25),new DebarkEventAction(this, airplane), "Debark Passengers Event Action");
        }
        else
        {
            SimExecutor.simExec._condEventSet.AddEvent(new ConditionalEventSet.ConditionalEvent(new ArriveEventAction(this, airplane), "ArriveEventAction", _terminalGates, 1));
        }
    }

    private void DebarkEventMethod(
            Airplane airplane
    ) {
        System.out.println("Docked, offloading passengers");
//        int temp = _numberOfTravelers;
//        this._numberOfTravelers += airplane.get_passengerCount();
//        System.out.println("Previous traveler count: " + temp + " Number of passengers in airport after offload: " + _numberOfTravelers);
//        airplane.DebarkPassengers();
//        airplane.RemovePassengerCargo();
        for (int i = 0; i < airplane.GetSeatSize(); ++i){
            Iterator itr = airplane.SeatClassIterator();
            List temp = airplane.DebarkPlane((String) itr.next());
            for (int j = 0; j < temp.size(); ++j){
                Person p = (Person) temp.get(j);
                this._numberOfTravelers++;
                p.UpdateTravelList();
                if (p.GetTravelersNextDestination() != null){
                    _waitingToBoardQueue.get(p.GetTravelersNextDestination()).AddToQueue(p);
                }
                else{
                    _numberOfTravelers--;
                }
            }
        }
        if (airplane.RetireAirplane() == true)
            return;
        else
            SimExecutor.simExec.ScheduleEventIn(Distributions.triangularDistribution(5, 10, 15), new CleanAndRefuelEventAction(this, airplane), "Clean and Refuel");
    }

    private void CleanAndRefuelEM(
            Airplane airplane
    ){
        int fuelToAdd = airplane.MaxFuelLevel() - airplane.CheckFuelLevel();
        airplane.FuelPlane(fuelToAdd);
        SimExecutor.simExec.ScheduleEventIn(10.0, new EmbarkPassengersEventAction(this, airplane), "Passenger Boarding");
    }

    private void EmbarkPassengersEM(
            Airplane airplane
    ){
//        airplane.EmbarkPassengers(this._numberOfTravelers);
//        this._numberOfTravelers -= airplane.get_passengerCount();
//        airplane.OnboardPassengerCargo(RandomNumber.randomInt(0, airplane.GetCargoCapacity()));
        List destinations = airplane.get_listOfDestinations().map(lod -> lod.toString()).collect(Collectors.toList());
        int randomDest = RandomNumber.randomInt(0, SimExecutor._destinationsMap.size() - 1);
        if (SimExecutor._destinationsMap.get(destinations.get(randomDest))._name.equals(this._name)){
            int newRandDest = 0;
            do {
                newRandDest = RandomNumber.randomInt(0, SimExecutor._destinationsMap.size() - 1);
            } while (SimExecutor._destinationsMap.get(destinations.get(newRandDest))._name.equals(this._name));
            this.SetNextTask(SimExecutor._destinationsMap.get(destinations.get(randomDest)));
        } else {
            this.SetNextTask(SimExecutor._destinationsMap.get(destinations.get(randomDest)));
        }
        int queueSize = _waitingToBoardQueue.get(this.GetNextTask()).GetQueueSize();
        for (int i = 0; i < queueSize; ++i){
            //String s = this.GetNextTask();
            Person p = _waitingToBoardQueue.get(this.GetNextTask()).GetHeadOfQueue();
            p = airplane.BoardPlane(p.GetTicketClass(), p);
            this._numberOfTravelers--;
            if (p == null)
                continue;
            else
                _waitingToBoardQueue.get(this.GetNextTask()).AddToQueue(p);
        }
        SimExecutor.simExec.ScheduleEventIn(Distributions.triangularDistribution(10, 25, 40), new TakeOffEventAction(this, airplane), "Preparing Airplane Departure");
    }

    private void TakeOffEM(
            Airplane airplane
    ){
        airplane.SetFlightOrigin(this);
        SendFlightToNextAirport(airplane);
        _terminalGates.ReleaseResource(1);
    }

    private void TSACheckInEM(SimToolQueue stq, SimToolQueue pq){
        for (int i = 0; i < RandomNumber.randomInt(1, pq.GetQueueSize()); ++i)
            stq.AddToQueue(pq.GetHeadOfQueue());
        SimExecutor.simExec.ScheduleEventIn(Distributions.triangularDistribution(5, 25, 35),
                new PreBoardingTerminalEventAction(this, stq), "Post TSA, Terminal Arrival Event");
        SimExecutor.simExec.ScheduleEventIn(Distributions.triangularDistribution(3, 5, 10), new TSACheckInEventAction(this,
                this._tsaPreCheck, this._baggageCheckQueue), "TSA Check In");
    }

    private void PreBoardingTerminalEM(SimToolQueue<Person> stq){
        int size = stq.GetQueueSize();
        for (int i = 0; i < size; ++i){
            Person tempPerson = stq.GetHeadOfQueue();
            if (tempPerson.GetTravelersNextDestination() == null){
                continue;
            }
            else {
                _waitingToBoardQueue.get(tempPerson.GetTravelersNextDestination()).AddToQueue(tempPerson);
            }
        }
    }

    private void PassengerEntityArrivalEM(SimToolQueue stq){

    }

    // Total Airports visit tracker
    public int AirportFlightTracker(){
        return _flightTrackerCounter;
    }

    public void ScheduleTravelerArrivalToAirport(){
        _numberOfTravelers += RandomNumber.randomInt(50, 100);
        SimExecutor.simExec.ScheduleEventIn(Distributions.triangularDistribution(25, 50, 75),
                new ScheduleTravelArrival(this), "Passenger Arrival Event");
    }

}