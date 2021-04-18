import java.util.*;
import java.util.stream.Stream;

public class Airplane {
    private int _passengerCount;
    private int _maxNumberOfPassengers;
    private int _numberOfFlights;
    private int _numberOfTotalFlights;
    private Cargo _flightCargo;
    private String _parentCompany;
    private String _airplaneType;
    private List<String> _listOfDestinations;
    private List<String> _executedDestinations;
    private String _flightOrigin;
    public static List<String> _airplaneParameters;
    private int _copyCount;
    private int _airplaneOfTypeCount;
    private int _firstClassCount;
    private int _businessClassCount;
    private int _economyCount;
    private int _coachCount;
    private Integer _planeFuelCapacity;
    private Integer _planeFuelLevel;
    private Map<String, List<Person>> _airplaneSeatClass;
    private Map<String, List<Person>> _finalListClass;
    private Map<String, Integer> _seatMap;

    public int get_passengerCount() {
        return _passengerCount;
    }

    public Stream<String> get_listOfDestinations() {
        return _listOfDestinations.stream();
    }

    public Stream<String> get_executedDestinations() {
        return _executedDestinations.stream();
    }

    public List<String> get_ListOfExecutedDestinations(){
        return _executedDestinations;
    }

    public String get_airplaneType(){ return _airplaneType; }

    public void UpdateListOfExecutedDestinations(Airport airport) { _executedDestinations.add(airport.GetName()); }

    public void SetFlightOrigin(Airport airport){
        _flightOrigin = airport.GetName();
    }

    public void DebarkPassengers(){
        _passengerCount = 0;
    }

//    public void OnboardPassengerCargo(int amountOfCargoToAdd){
//        _flightCargo.AddCargo(amountOfCargoToAdd);
//    }

//    public void RemovePassengerCargo(){
//        _flightCargo.RemoveCargo();
//    }

//    public int GetCargoCapacity(){
//        return _flightCargo.GetCargoQuantityOnboard();
//    }

//    public void EmbarkPassengers(int numOfPassengersInAirport){
//        if (numOfPassengersInAirport < _maxNumberOfPassengers){
//            _passengerCount = numOfPassengersInAirport;
//        }
//        else
//            _passengerCount = _maxNumberOfPassengers;
//    }

    public static void AirplaneParams(){
        _airplaneParameters = new ArrayList<>();
        _airplaneParameters.add(0, "name");
        _airplaneParameters.add(1, "passengerCount");
        _airplaneParameters.add(2, "cargoAmount");
        _airplaneParameters.add(3, "totalFlights");
        _airplaneParameters.add(4, "fuelCapacity");
    }

    private void InstantiateSeatStructure(){
        _airplaneSeatClass.put("firstClass", new ArrayList<>());
        _airplaneSeatClass.put("businessClass", new ArrayList<>());
        _airplaneSeatClass.put("economy", new ArrayList<>());
        _airplaneSeatClass.put("coach", new ArrayList<>());
    }

    public static String GetKey(){
        return "airplane";
    }

    public static List<String> GetAirplaneParams(){
        return _airplaneParameters;
    }

    private void PopulateSeatStructureNumbers(){
        int totalNumber = _maxNumberOfPassengers;
        _firstClassCount = totalNumber / 5;
        totalNumber -= _firstClassCount;
        _businessClassCount = totalNumber / 3;
        totalNumber -= _businessClassCount;
        _economyCount = totalNumber / 2;
        totalNumber -= _economyCount;
        _coachCount = totalNumber;
    }

    private void TrackSeatCountsMap(){
        _seatMap.put("fistClass", _firstClassCount);
        _seatMap.put("businessClass", _businessClassCount);
        _seatMap.put("economy", _economyCount);
        _seatMap.put("coach", _coachCount);
    }

    Airplane(String planeType, int passCount, Integer totalCargoCount, int totalFlights, String parentCompany,
             int fuelCapacity){
        _airplaneType = planeType;
        _passengerCount = 0;
        _maxNumberOfPassengers = passCount;
        _flightCargo = new Cargo(totalCargoCount);
        _parentCompany = parentCompany;
        _flightOrigin = null;
        _numberOfFlights = 0;
        _numberOfTotalFlights = totalFlights;
        _executedDestinations = new ArrayList<>();
        _planeFuelCapacity = 0;
        _copyCount = 0;
        _airplaneOfTypeCount = 0;
        _planeFuelLevel = 0;
        _airplaneSeatClass = new HashMap<>();
        _finalListClass = new HashMap<>();
        _seatMap = new HashMap<>();
        InstantiateSeatStructure();
        _finalListClass = _airplaneSeatClass;
        PopulateSeatStructureNumbers();
        TrackSeatCountsMap();
    }

    Airplane(Airplane copyPlane){
        _airplaneType = copyPlane._airplaneType;
        _passengerCount = copyPlane._passengerCount;
        _maxNumberOfPassengers = copyPlane._maxNumberOfPassengers;
        _flightCargo = copyPlane._flightCargo;
        _parentCompany = copyPlane._parentCompany;
        _flightOrigin = null;
        _numberOfFlights = 0;
        _numberOfTotalFlights = copyPlane._numberOfTotalFlights;
        _executedDestinations = new ArrayList<>();
        _planeFuelCapacity = copyPlane._planeFuelCapacity;
        _airplaneOfTypeCount = copyPlane.GetCopyCount();
        _airplaneSeatClass = new HashMap<>();
        copyPlane.UpdateCopyCount();
        _planeFuelLevel = 0;
        _airplaneSeatClass = copyPlane._finalListClass;
    }

    private class Cargo{
        public int _quantity;
        public int _sizeUtilized;
        public int _capacity;

        Cargo(int capacity)
        {
            _quantity = 0;
            _sizeUtilized = 0;
            _capacity = capacity;
        }

        public void AddCargo(int amountOfCargoToAdd){ _sizeUtilized += amountOfCargoToAdd; }

//        public void RemoveCargo(int amountOfCargoToRemove){  _sizeUtilized -= amountOfCargoToRemove; }

        public void RemoveCargo() { _sizeUtilized = 0; }

        public int GetCargoQuantityOnboard(){ return _capacity; }
    }

    public void UpdateListOfDestinations(List<String> listToUpdate){
        _listOfDestinations = listToUpdate;
    }

    public boolean RetireAirplane(){
        _numberOfFlights++;
        if (_numberOfFlights == _numberOfTotalFlights) {
            System.out.println("This Airplane is now retiring");
            for (String s : _executedDestinations){
                System.out.println(this._airplaneType + " " +
                        this._airplaneOfTypeCount + " has visited, " + s);
            }
            return true;
        }
        else
            return false;
    }

    public void UpdateCopyCount(){
        _copyCount++;
    }

    public void FuelPlane(int fuelToAdd){
        _planeFuelLevel += fuelToAdd;
    }

    public Integer CheckFuelLevel(){
        return _planeFuelLevel;
    }

    public Integer MaxFuelLevel(){
        return _planeFuelCapacity;
    }

    public int GetCopyCount(){ return _copyCount; }

    public void MapToList(Set keySet){
        List createdDestList = new ArrayList<>();
        createdDestList.addAll(keySet);
        UpdateListOfDestinations(createdDestList);
    }

    public Person BoardPlane(String className, Person personToAdd){
        Person tempPersonData = personToAdd;
        if (_airplaneSeatClass.get(className).size() < _seatMap.get(className)) {
            _airplaneSeatClass.get(className).add(personToAdd);
            tempPersonData = null;
            _passengerCount++;
        }
        return tempPersonData;
    }

    public List<Person> DebarkPlane(String className){
        return _airplaneSeatClass.get(className);
    }

    public Iterator SeatClassIterator(){
        return _airplaneSeatClass.entrySet().iterator();
    }

    public Integer GetSeatSize(){
        return _airplaneSeatClass.size();
    }

}
