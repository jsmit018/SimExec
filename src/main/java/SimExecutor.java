import org.json.simple.JSONArray;
//import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class SimExecutor {
    public static Map<String, Airport> _destinationsMap = new HashMap<>();
    public static SimulationExecutive simExec = new SimulationExecutive();
    public static List<Airplane> _masterAirplaneList = new ArrayList<>();
    private static List<String> _parentCompanyList = new ArrayList<>();
    public static List<String> _destinationsKeyList = new ArrayList<>();
    private static boolean flag = false;
    private static int counter = 0;
    private static int size = 0;

    @SuppressWarnings("unchecked")
    public static void ReadJSONInput(){
        JSONParser jsonParser = new JSONParser();
        JSONParser jsonParserAirplane = new JSONParser();

        try (FileReader fileReader = new FileReader("C:\\Users\\jmsm1\\Documents\\Vanderbilt - Intro to Software\\Final Project - Fixed\\SimExec\\src\\main\\resources\\airplanes.json")){
            Object airplaneObjs = jsonParserAirplane.parse(fileReader);
            JSONArray airportList = (JSONArray) airplaneObjs;
            JSONObject tempCompanyObj = (JSONObject) airportList.get(airportList.size() - 1);
            JSONArray tempCompanyArray = (JSONArray) tempCompanyObj.get("parentCompany");
            for(int i = 0; i < tempCompanyArray.size(); ++i) {
                _parentCompanyList.add((String) tempCompanyArray.get(i));
            }
            size = airportList.size();
            Airplane.AirplaneParams();
            airportList.forEach(airplane -> AppendAirplanes((JSONObject) airplane));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }

        try (FileReader fileReader = new FileReader("C:\\Users\\jmsm1\\Documents\\Vanderbilt - Intro to Software\\Final Project - Fixed\\SimExec\\src\\main\\resources\\data.json")){
            Object airportObj = jsonParser.parse(fileReader);
            JSONArray airportList = (JSONArray) airportObj;
            Airport.PopulateAirportParams();
            airportList.forEach(airport -> InstantiateAirports((JSONObject) airport));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }
    }

    private static void InstantiateAirports(JSONObject airport){
        JSONObject destinationAirport = (JSONObject) airport.get(Airport.GetKey());
        String airportName = (String) destinationAirport.get(Airport.GetAirportParams().get(0));
        JSONArray airportCapacity = (JSONArray) destinationAirport.get(Airport.GetAirportParams().get(1));
        JSONArray airportGates = (JSONArray) destinationAirport.get(Airport.GetAirportParams().get(2));
        JSONArray airportTravelers = (JSONArray) destinationAirport.get(Airport.GetAirportParams().get(3));

        _destinationsKeyList.add(airportName);
        _destinationsMap.put(airportName, new Airport(airportName, RandomNumber.randomInt(Integer.parseInt((String) airportCapacity.get(0)),
                Integer.parseInt((String) airportCapacity.get(1))), RandomNumber.randomInt(Integer.parseInt((String) airportGates.get(0)),
                Integer.parseInt((String) airportGates.get(1))), RandomNumber.randomInt(Integer.parseInt((String) airportTravelers.get(0)),
                Integer.parseInt((String) airportTravelers.get(1))), 2));
    }

    private static void AppendAirplanes(JSONObject airplane){
        if (counter == size - 1){
            counter = 0;
        }
        else {
            JSONObject airplaneToAppend = (JSONObject) airplane.get(Airplane.GetKey());
            String airplaneName = (String) airplaneToAppend.get(Airplane.GetAirplaneParams().get(0));
            JSONArray airplanePassengerCount = (JSONArray) airplaneToAppend.get(Airplane.GetAirplaneParams().get(1));
            JSONArray airplaneCargo = (JSONArray) airplaneToAppend.get(Airplane.GetAirplaneParams().get(2));
            JSONArray airplaneTotalFlights = (JSONArray) airplaneToAppend.get(Airplane.GetAirplaneParams().get(3));
            String fuelCapacity = (String) airplaneToAppend.get(Airplane.GetAirplaneParams().get(4));

            int pc = RandomNumber.randomInt(Integer.parseInt((String) airplanePassengerCount.get(0)),
                    Integer.parseInt((String) airplanePassengerCount.get(1)));
            int cargo = RandomNumber.randomInt(Integer.parseInt((String) airplaneCargo.get(0)),
                    Integer.parseInt((String) airplaneCargo.get(1)));
            int flights = RandomNumber.randomInt(Integer.parseInt((String) airplaneTotalFlights.get(0)),
                    Integer.parseInt((String) airplaneTotalFlights.get(1)));


            int randomCompany = RandomNumber.randomInt(0, _parentCompanyList.size() - 1);
            _masterAirplaneList.add(new Airplane(airplaneName, pc, cargo, flights, _parentCompanyList.get(randomCompany), Integer.parseInt(fuelCapacity)));
            counter++;
        }
    }

    public static class TestClass{
        private int _numEventsToCreate;
        private int _eventcounter = 0;
        public SimulationExecutive _simExec;

        public class TestEventEA implements EventAction{
            private TestClass _object;

            TestEventEA(TestClass object){
                _object = object;
            }

            @Override
            public void Execute(){
                _object.TestEventEM(new Random().nextDouble() * 10);
            }
        }

        public void TestEventEM(Double t){
            if (_eventcounter == _numEventsToCreate)
                return;
            System.out.println("Current Simulation Time is " + _simExec.GetSimulationTime());
            System.out.println("Time Event is supposed to occur " + (_simExec.GetSimulationTime() + t));
            _eventcounter++;
            _simExec.ScheduleEventIn(t, new TestEventEA(this), "Testing Event " + _eventcounter);

        }

        TestClass(int numEventsToCreate, SimulationExecutive simExec){
            _numEventsToCreate = numEventsToCreate;
            _simExec = simExec;
            _simExec.ScheduleEventIn(new Random().nextDouble() * 10, new TestEventEA(this), "Testing Event " + _eventcounter);
            _eventcounter++;
            _simExec.ScheduleEventIn(new Random().nextDouble() * 10, new TestEventEA(this), "Testing Event " + _eventcounter);
            _eventcounter++;
        }
    }

    public static void main(String[] args) {
        simExec.InitializeSimulation();
        //TestClass t = new TestClass(10, simExec);
        ReadJSONInput();
        Iterator destinationsItr = _destinationsMap.keySet().iterator();
        for (int i = 0; i < _destinationsMap.size(); ++i){
            String s = (String) destinationsItr.next();
            _destinationsMap.get(s).ScheduleFlights();
            _destinationsMap.get(s).PopulateHashMap(_destinationsMap.keySet());
            _destinationsMap.get(s).TriggerFootTrafficGeneration();
        }
        SourceEntityGenerator temp = new SourceEntityGenerator();
        temp.InitializeSourceObject();
        simExec.RunSimulation();
    }
}
