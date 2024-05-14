package fww.fsa;

import java.util.*;

public class NFA {
    private final Status startStatus;

    private final Status finalStatus;


    public NFA(String regex) {
        NFA nfa = builder(regex);
        this.startStatus = nfa.startStatus;
        this.finalStatus = nfa.finalStatus;
    }

    private NFA(NFA n){
        if(n == null){
            startStatus = new Status();
            finalStatus = new Status();
            startStatus.addTranslation(new Translation(' ', finalStatus));
            finalStatus.setFinal();
        }else{
            startStatus = n.startStatus;
            finalStatus = n.finalStatus;
        }
    }

    private NFA(){
        startStatus = new Status();
        finalStatus = new Status();
        finalStatus.setFinal();
    }

    public Status getStartStatus() {
        return startStatus;
    }

    public Status getFinalStatus(){
        return finalStatus;
    }

//    private NFA builder(String regex) {
//        List<NFA> concat = new ArrayList<>();
//        char[] chars = regex.toCharArray();
//        Stack<Integer> stack = new Stack<>();
//        for (int i = 0; i < chars.length; i++) {
//            NFA nfa = null;
//            char c = chars[i];
//            if (c == '(') {
//                stack.push(i);
//            } else if (c == ')') {
//                int start = stack.pop();
//                nfa = new NFA(regex.substring(start + 1, i));
//            } else if (c == '|') {
//                NFA s = concat(concat);
//                NFA t = new NFA(regex.substring(i + 1));
//                nfa = orRule(s, t);
//            } else if (c == '*') {
//                NFA n = concat.remove(concat.size() - 1);
//                nfa = starRule(n);
//            } else if (c == '+') {
//                NFA n = concat.remove(concat.size() - 1);
//                nfa = plusRule(n);
//            } else if (c == '?') {
//                NFA n = concat.remove(concat.size() - 1);
//                nfa = questionRule(n);
//            } else {
//                nfa = baseRule(c);
//            }
//            concat.add(nfa);
//        }
//        return concat(concat);
//    }

    private NFA builder(String regex){
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
                i = j - 1;
            } else if (c == '|') {
                NFA s = concat(concat);
                NFA t = new NFA(regex.substring(i + 1));
                nfa = orRule(s, t);
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
            } else {
                nfa = baseRule(c);
            }
            concat.add(nfa);
        }
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
        NFA nfa = new NFA();
        s = new NFA(s);
        t = new NFA(t);
        nfa.getStartStatus().addTranslation(new Translation(' ', s.getStartStatus()));
        s.getFinalStatus().addTranslation(new Translation(' ', t.getStartStatus()));
        t.getFinalStatus().addTranslation(new Translation(' ', nfa.getFinalStatus()));
        s.getFinalStatus().notFinal();
        t.getFinalStatus().notFinal();
        return nfa;
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
        for(int i = 0; i < concat.size() - 1; i++){
            concat.set(i, concatRule(concat.get(i), concat.get(i + 1)));
            concat.remove(i + 1);
        }
        NFA nfa = concat.getFirst();
        concat.clear();
        return nfa;
    }

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
        Set<Status> result = status.getNextStatusN(' ');
        result.add(status);
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

//    @Override
//    public String toString() {
//        return "NFA{" +
//                "startStatus=" + startStatus +
//                ", finalStatus=" + finalStatus +
//                '}';
//    }

    public static void main(String[] args) {
        NFA nfa = new NFA("a(b|c)");
        System.out.println(nfa.match("ac"));
    }
}
