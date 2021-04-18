import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class Person {
    private String _name;
    private List<String> _travelList;
    private String _travelerOrigin;
    private String _nextDestination;
    private boolean _passport;
    private int _amountOfLuggage;
    private String _ticketClass;
    private String[] _ticketCategory = new String[]{
        "firstClass", "businessClass", "economy", "coach"};

    Person(){
         _name = UUID.randomUUID().toString();
         if (RandomNumber.randomInt(0, 2) > 1)
             _passport = true;
         else
             _passport = false;
         _amountOfLuggage = RandomNumber.randomInt(0, 10);
         _ticketClass = _ticketCategory[RandomNumber.randomInt(0, _ticketCategory.length - 1)];
         _travelList = new ArrayList<>();
         GeneratePersonalTravelList(SimExecutor._destinationsMap.keySet());
         _travelerOrigin = _travelList.get(0);
         _nextDestination = null;
    }

    public String GetTicketClass(){
        return _ticketClass;
    }

    public boolean HasPassport(){ return _passport; }

    private void GeneratePersonalTravelList(Set destinationKeySet){
        List<String> destinations = new ArrayList();
        destinations.addAll(destinationKeySet);
        int randomNumberOfDests = RandomNumber.randomInt(1, 5);
        for (int i = 0; i < randomNumberOfDests; ++i){
            if (i == 0)
                _travelList.add(destinations.get(RandomNumber.randomInt(0, destinations.size() - 1)));
            else{
                String destination = destinations.get(RandomNumber.randomInt(0, destinations.size() - 1));
                while (destination.equals(_travelList.get(i -1))){
                    destination = destinations.get(RandomNumber.randomInt(0, destinations.size() - 1));
                    if (destination.equals("London Heathrow Airport(LHR)")){
                        if (HasPassport() == false)
                            destination = destinations.get(RandomNumber.randomInt(0, destinations.size() - 1));
                    }
                }
                _travelList.add(destination);
            }
        }
    }

    public String GetTravelerOrigin(){
        return _travelerOrigin;
    }

    public String GetTravelersNextDestination(){
        return _travelList.get(0 + 1);
    }

    public void UpdateTravelList(){ _travelList.remove(0); }

}
