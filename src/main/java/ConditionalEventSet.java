public class ConditionalEventSet {
       ConditionalEvent _head;

    static class ConditionalEvent{
        public EventAction _ea;
        //public Double _time;
        public String _eventName;
        public ConditionalEvent _nextEvent;
        //public int _resNeeded;
        Resource _resNeeded;
        int _neededResAmount;
        //public Airport _requestingAirport;

        ConditionalEvent(
                EventAction ea,
                String eventName,
                Resource neededRes,
                int amountOfNeededRes
                //Airport airport
        ){
            _ea = ea;
            //_time = t;
            _eventName = eventName;
            _nextEvent = null;
            _neededResAmount = amountOfNeededRes;
            _resNeeded = neededRes;
            //_requestingAirport = airport;
        }
    }

        ConditionalEventSet(){InitializeES();}

        public void InitializeES(){
            _head = null;
        }

        public boolean ConditionalQueueTrigger(Resource checkThisResource){
            ConditionalEvent eventChecker = _head;
            ConditionalEvent previousEvent = _head;
            while (_head != null) {
                if (!checkThisResource.equals(eventChecker._resNeeded)){
                    if (eventChecker.equals(_head))
                        eventChecker = eventChecker._nextEvent;
                    else{
                        eventChecker = eventChecker._nextEvent;
                        previousEvent._nextEvent = eventChecker._nextEvent;
                    }
                }
                else if (eventChecker._neededResAmount <= checkThisResource.get_resourceCount()) {
                    eventChecker._ea.Execute();
                    if (eventChecker.equals(_head))
                        eventChecker = eventChecker._nextEvent;
                    else{
                        eventChecker = eventChecker._nextEvent;
                        previousEvent._nextEvent = eventChecker._nextEvent;
                    }
                    return true;
                }
                else{
                    if(!eventChecker.equals(_head))
                    {
                        previousEvent = previousEvent._nextEvent;
                    }
                    eventChecker = eventChecker._nextEvent;
                }
            }
            return false;
        }

        public void AddEvent(ConditionalEvent e){
            if(_head == null){
                _head = e;
            }
            /*else if (_head._time > e._time){
                ConditionalEvent temp = _head;
                e._nextEvent = _head;
                _head = e;
            }*/
            else{
                /*ConditionalEvent currentEvent = _head;
                while (currentEvent._nextEvent != null && currentEvent._time > e._time){
                    currentEvent = currentEvent._nextEvent;
                }
                if (currentEvent._nextEvent == null)
                    currentEvent._nextEvent = e;
                else{
                    e._nextEvent = currentEvent._nextEvent;
                    currentEvent._nextEvent = e;
                }*/
                ConditionalEvent listItr = _head;
                while (listItr._nextEvent != null)
                    listItr = listItr._nextEvent;
                listItr._nextEvent = e;
            }
        }
}
