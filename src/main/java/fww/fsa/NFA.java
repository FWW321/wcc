package fww.fsa;

import java.util.HashSet;
import java.util.Set;

public class NFA {
    private Status startStatus;

    private Status finalStatus;

    private Set<Status> statuses = new HashSet<>();

    public NFA(String regex) {
        Status startStatus = new Status();
        startStatus.setInitial();
        statuses.add(startStatus);

    }

    public Set<Status> getStartStatuses() {
        return statuses;
    }

    public void addStatus(Status status){
        statuses.add(status);
    }

    public Status getStatus(String id){
        for(Status status : statuses){
            if(status.getId().equals(id)){
                return status;
            }
        }
        return null;
    }

    public Status getStartStatus(){
        for(Status status : statuses){
            if(status.isInitial()){
                return status;
            }
        }
        return null;
    }

    public Set<Status> getFinalStatuses(){
        Set<Status> finalStatuses = new HashSet<>();
        for(Status status : statuses){
            if(status.isFinal()){
                finalStatuses.add(status);
            }
        }
        return finalStatuses;
    }

    private void builder(String regex){

    }

    private Status baseRule(char c){
        Status startStatus = new Status();
        startStatus.setInitial();
        Status finalStatus = new Status();
        finalStatus.setFinal();
        startStatus.addTranslation(new Translation(c, finalStatus));
        return finalStatus;
    }

    private Status orRule(char c){
        Status startStatus = new Status();
        startStatus.setInitial();
        Status finalStatus = new Status();
        finalStatus.setFinal();
        startStatus.addTranslation(new Translation(c, finalStatus));
        return finalStatus;
    }
}
