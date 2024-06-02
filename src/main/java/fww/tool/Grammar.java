package fww.tool;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Grammar {
    List<Rule> rules;
    Set<String> nonTerminals;
    Set<String> terminals;

    Grammar() {
        this.rules = new ArrayList<>();
        this.nonTerminals = new HashSet<>();
        this.terminals = new HashSet<>();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Rule rule : rules) {
            sb.append(rule).append("\n");
        }
        return sb.toString();
    }
}
