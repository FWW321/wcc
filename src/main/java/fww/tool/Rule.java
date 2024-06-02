package fww.tool;

import java.util.List;

public class Rule {
    String lhs;
    List<String> rhs;

    Rule(String lhs, List<String> rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

    public String toString() {
        return lhs + " -> " + rhs;
    }
}
