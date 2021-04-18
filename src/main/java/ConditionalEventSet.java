public class ConditionalEventSet {
       ConditionalEvent _head;

    static class ConditionalEvent{
        public EventAction _ea;
        public String _eventName;
        public ConditionalEvent _nextEvent;
        Resource _resNeeded;
        int _neededResAmount;

        ConditionalEvent(
                EventAction ea,
                String eventName,
                Resource neededRes,
                int amountOfNeededRes
        ){
            _ea = ea;
            _eventName = eventName;
            _nextEvent = null;
            _neededResAmount = amountOfNeededRes;
            _resNeeded = neededRes;
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
            else{
                ConditionalEvent listItr = _head;
                while (listItr._nextEvent != null)
                    listItr = listItr._nextEvent;
                listItr._nextEvent = e;
            }
        }
}
