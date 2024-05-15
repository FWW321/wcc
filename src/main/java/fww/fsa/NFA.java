package fww.fsa;

import java.util.*;

public class NFA extends FA{

    public NFA(String regex) {
        super(regex);
    }

    private NFA(Status startStatus, Status finalStatus){
        super(startStatus, finalStatus);
    }

    private NFA(NFA n){
        super(n);
        if(n == null){
            startStatus = new Status();
            finalStatus = new Status();
            startStatus.addTranslation(new Translation(' ', finalStatus));
            finalStatus.setFinal();
        }
    }

    private NFA(){
        startStatus = new Status();
        finalStatus = new Status();
        finalStatus.setFinal();
    }

    @Override
    protected NFA builder(String regex){
        List<NFA> concat = new ArrayList<>();
        char[] chars = regex.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            NFA nfa = null;
            char c = chars[i];
            if (c == '(') {
                int j = i + 1;
                int count = 1;
                while (count != 0) {
                    if (chars[j] == '(') {
                        count++;
                    } else if (chars[j] == ')') {
                        count--;
                    }
                    j++;
                }
                nfa = new NFA(regex.substring(i + 1, j - 1));
                System.out.println(nfa);
                System.out.println(regex.substring(i + 1, j - 1));
                i = j - 1;
            } else if (c == '|') {
                NFA s = concat(concat);
                NFA t = new NFA(regex.substring(i + 1));
                nfa = orRule(s, t);
                i = chars.length - 1;
            } else if (c == '*') {
                NFA n = concat.getLast();
                concat.removeLast();
                nfa = starRule(n);
            } else if (c == '+') {
                NFA n = concat.getLast();
                concat.removeLast();
                nfa = plusRule(n);
            } else if (c == '?') {
                NFA n = concat.getLast();
                concat.removeLast();
                nfa = questionRule(n);
            } else if (c == '\\') {
                if(i < chars.length - 1){
                    nfa = baseRule(chars[i + 1]);
                }
                i = i + 1;
            } else {
                nfa = baseRule(c);
                System.out.println(nfa);
            }
            System.out.println(nfa);
            concat.add(nfa);
        }
        System.out.println(concat);
        return concat(concat);
    }

    private NFA baseRule(char c){
        NFA nfa = new NFA();
        nfa.getStartStatus().addTranslation(new Translation(c, nfa.getFinalStatus()));
        return nfa;
    }

    private NFA orRule(NFA s, NFA t){
        NFA nfa = new NFA();
        s = new NFA(s);
        t = new NFA(t);
        nfa.getStartStatus().addTranslation(new Translation(' ', s.getStartStatus()));
        nfa.getStartStatus().addTranslation(new Translation(' ', t.getStartStatus()));
        s.getFinalStatus().addTranslation(new Translation(' ', nfa.getFinalStatus()));
        t.getFinalStatus().addTranslation(new Translation(' ', nfa.getFinalStatus()));
        s.getFinalStatus().notFinal();
        t.getFinalStatus().notFinal();
        return nfa;
    }

    private NFA concatRule(NFA s, NFA t){
        s = new NFA(s);
        t = new NFA(t);
        s.getFinalStatus().addTranslation(new Translation(' ', t.getStartStatus()));
        s.getFinalStatus().notFinal();
        return new NFA(s.getStartStatus(), t.getFinalStatus());
    }

    private NFA starRule(NFA n){
        NFA nfa = new NFA();
        n = new NFA(n);
        nfa.getStartStatus().addTranslation(new Translation(' ', n.getStartStatus()));
        nfa.getStartStatus().addTranslation(new Translation(' ', nfa.getFinalStatus()));
        n.getFinalStatus().addTranslation(new Translation(' ', n.getStartStatus()));
        n.getFinalStatus().addTranslation(new Translation(' ', nfa.getFinalStatus()));
        n.getFinalStatus().notFinal();
        return nfa;
    }

    private NFA plusRule(NFA n){
        NFA nfa = new NFA();
        n = new NFA(n);
        nfa.getStartStatus().addTranslation(new Translation(' ', n.getStartStatus()));
        n.getFinalStatus().addTranslation(new Translation(' ', nfa.getFinalStatus()));
        n.getFinalStatus().addTranslation(new Translation(' ', n.getStartStatus()));
        n.getFinalStatus().notFinal();
        return nfa;
    }

    private NFA questionRule(NFA n){
        NFA nfa = new NFA();
        n = new NFA(n);
        nfa.getStartStatus().addTranslation(new Translation(' ', n.getStartStatus()));
        nfa.getStartStatus().addTranslation(new Translation(' ', nfa.getFinalStatus()));
        n.getFinalStatus().addTranslation(new Translation(' ', nfa.getFinalStatus()));
        n.getFinalStatus().notFinal();
        return nfa;
    }

    private NFA concat(List<NFA> concat){
        NFA s = concat.getFirst();
        for (int i = 1; i < concat.size(); i++) {
            NFA t = concat.get(i);
            s = concatRule(s, t);
        }
        concat.clear();
        return s;
    }

    protected void lookahead(){

    }

    @Override
    public boolean match(String s){
        Set<Status> status = getNILTranslation(startStatus);
        if(isFinal(status)){
            return true;
        }
        for (char c : s.toCharArray()) {
            status = getNILTranslation(move(status, c));
            if(isFinal(status)){
                return true;
            }
        }
        return false;
    }

    public Set<Status> getNILTranslation(Status status){
        Set<Status> result = new HashSet<>();
        Queue<Status> queue = new LinkedList<>();
        queue.add(status);

        while(!queue.isEmpty()){
            Status current = queue.poll();
            result.add(current);
            Set<Status> nextStatus = current.getNextStatusN(' ');
            for(Status next : nextStatus){
                if(!result.contains(next)){
                    queue.add(next);
                }
            }
        }

        return result;
    }

    public Set<Status> getNILTranslation(Set<Status> status){
        for (Status s : status) {
            status.addAll(getNILTranslation(s));
        }
        return status;
    }

    public Set<Status> move(Status status, char c){
        return status.getNextStatusN(c);
    }

    public Set<Status> move(Set<Status> status, char c){
        Set<Status> result = new HashSet<>();
        for (Status s : status) {
            result.addAll(move(s, c));
        }
        return result;
    }

    public boolean isFinal(Set<Status> status){
        return status.stream().anyMatch(Status::isFinal);
    }

    public static void main(String[] args) {
        FA nfa = new NFA("a\\");
        System.out.println(nfa.match("a"));
    }
}
