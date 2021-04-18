import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataCollection {

    private static Map<String, Map<String, List<Integer>>> _airplaneSeatUtilization;
    private static Map<String, List<Integer>> _averagePassengerCount;
    private static Map<String, List<Integer>> _AverageTravellerCountOfAirport;

    public static void AirplaneSeatUtilization(String seatClass, Airplane airplane){
        boolean flag = false;
        for (String s : _airplaneSeatUtilization.keySet()){
            if (s.equals(airplane.get_airplaneType())){
                flag = true;
            }
        }
        if (flag == false){
            _airplaneSeatUtilization.put(airplane.get_airplaneType(), new HashMap<>());
            _airplaneSeatUtilization.get(airplane.get_airplaneType()).put(seatClass, new ArrayList<>());
            _airplaneSeatUtilization.get(airplane.get_airplaneType()).get(seatClass).add(1);
        }
        else if (flag == true){
            _airplaneSeatUtilization.get(airplane.get_airplaneType()).get(seatClass).add(1);
        }
    }

    public static void AveragePassengerCountOfAirplane(int passengerCount, Airplane airplane){
        boolean flag = false;
        if (_averagePassengerCount.get(airplane.get_airplaneType()) != null)
            flag = true;

        if (flag == false){
            _averagePassengerCount.put(airplane.get_airplaneType(), new ArrayList<>());
            _averagePassengerCount.get(airplane.get_airplaneType()).add(passengerCount);
        }
        else if (flag == true){
            _averagePassengerCount.get(airplane.get_airplaneType()).add(passengerCount);
        }
    }

    public static void AverageTravellerCountOfAirport(int travellerCount, Airplane airplane){
        boolean flag = false;
        if (_AverageTravellerCountOfAirport.get(airplane.get_airplaneType()) != null)
            flag = true;

        if (flag == false){
            _AverageTravellerCountOfAirport.put(airplane.get_airplaneType(), new ArrayList<>());
            _AverageTravellerCountOfAirport.get(airplane.get_airplaneType()).add(travellerCount);
        }
        else if (flag == true){
            _averagePassengerCount.get(airplane.get_airplaneType()).add(travellerCount);
        }
    }
}
