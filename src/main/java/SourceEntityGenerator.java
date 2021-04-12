import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SourceEntityGenerator {
    private int _numberOfPeopleToGenerate;
    private boolean _scheduleFlag = true;

    private class GeneratePeopleEventAction implements EventAction{
        private SourceEntityGenerator _seg;

        GeneratePeopleEventAction(SourceEntityGenerator seg){
            _seg = seg;
        }

        public void Execute(){
            _seg.GeneratePeopleEM();
        }

    }

    SourceEntityGenerator(){
    }

    private void GeneratePeopleEM(){
        _numberOfPeopleToGenerate = RandomNumber.randomInt(25,70);
        for (int i = 0; i < _numberOfPeopleToGenerate; ++i){
            Person temp = new Person();
            SimExecutor._destinationsMap.get(temp.GetTravelerOrigin()).InsertPassengerIntoBaggageCheck(temp);
        }
        if (_scheduleFlag == true) {
            Iterator itr = SimExecutor._destinationsMap.keySet().iterator();
            while (itr.hasNext()) {
                SimExecutor._destinationsMap.get(itr.next()).StartPassengerProcessing();
            }
            _scheduleFlag = false;
        }
        SimExecutor.simExec.ScheduleEventIn(Distributions.triangularDistribution(2, 4, 8),
                new GeneratePeopleEventAction(this), "Passenger Arrival Event");
    }

    public void InitializeSourceObject(){
        SimExecutor.simExec.ScheduleEventIn(0.0,
                new GeneratePeopleEventAction(this), "Passenger Arrival Event");
    }


}
