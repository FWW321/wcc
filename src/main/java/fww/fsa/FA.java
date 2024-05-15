package fww.fsa;

public abstract class FA {
    protected Status startStatus;

    protected Status finalStatus;

    protected FA() {
    }

    public FA(String regex) {
        FA nfa = builder(regex);
        this.startStatus = nfa.startStatus;
        this.finalStatus = nfa.finalStatus;
    }

    protected FA(Status startStatus, Status finalStatus){
        this.startStatus = startStatus;
        this.finalStatus = finalStatus;
    }

    protected FA(FA n){
        if(n == null){
            startStatus = null;
            finalStatus = null;
        }else{
            startStatus = n.startStatus;
            finalStatus = n.finalStatus;
        }
    }

    public Status getStartStatus() {
        return startStatus;
    }

    public Status getFinalStatus(){
        return finalStatus;
    }

    protected abstract FA builder(String regex);

    protected abstract boolean match(String s);
}
