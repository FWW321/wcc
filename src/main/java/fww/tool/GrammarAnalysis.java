package fww.tool;

import java.util.*;

public class GrammarAnalysis {
    static class Grammar {
        List<Rule> rules;
        Set<String> nonTerminals;
        Set<String> terminals;

        Grammar() {
            this.rules = new ArrayList<>();
            this.nonTerminals = new HashSet<>();
            this.terminals = new HashSet<>();
        }
    }

    static class Rule {
        String lhs;
        List<String> rhs;

        Rule(String lhs, List<String> rhs) {
            this.lhs = lhs;
            this.rhs = rhs;
        }
    }

    static Grammar grammar = new Grammar();
    static Map<String, Set<String>> first = new HashMap<>();
    static Map<String, Set<String>> follow = new HashMap<>();
    static String startSymbol = "E";  // 假设 E 是起始符号

    public static void main(String[] args) {
        // 定义文法规则
        addRule("E", Arrays.asList("T", "E'"));
        addRule("E'", Arrays.asList("+", "T", "E'"));
        addRule("E'", Arrays.asList("ε"));
        addRule("T", Arrays.asList("F", "T'"));
        addRule("T'", Arrays.asList("*", "F", "T'"));
        addRule("T'", Arrays.asList("ε"));
        addRule("F", Arrays.asList("(", "E", ")"));
        addRule("F", Arrays.asList("id"));

        // 初始化 FIRST 和 FOLLOW 集合
        initializeFirstAndFollow();

        // 计算 FIRST 集合
        computeFirstSets();

        // 计算 FOLLOW 集合
        computeFollowSets();

        // 输出 FIRST 集合
        System.out.println("FIRST 集合:");
        for (String nonTerminal : grammar.nonTerminals) {
            System.out.println("FIRST(" + nonTerminal + ") = " + first.get(nonTerminal));
        }

        // 输出 FOLLOW 集合
        System.out.println("\nFOLLOW 集合:");
        for (String nonTerminal : grammar.nonTerminals) {
            System.out.println("FOLLOW(" + nonTerminal + ") = " + follow.get(nonTerminal));
        }
    }

    private static void addRule(String lhs, List<String> rhs) {
        grammar.rules.add(new Rule(lhs, rhs));
        grammar.nonTerminals.add(lhs);
        for (String symbol : rhs) {
            if (Character.isUpperCase(symbol.charAt(0))) {
                grammar.nonTerminals.add(symbol);
            } else {
                grammar.terminals.add(symbol);
            }
        }
    }

    private static void initializeFirstAndFollow() {
        for (String nonTerminal : grammar.nonTerminals) {
            first.put(nonTerminal, new HashSet<>());
            follow.put(nonTerminal, new HashSet<>());
        }
        follow.get(startSymbol).add("$"); // 起始符号的FOLLOW集包含$
    }

    private static void computeFirstSets() {
        boolean changed;
        do {
            changed = false;
            for (Rule rule : grammar.rules) {
                String lhs = rule.lhs;
                List<String> rhs = rule.rhs;

                int oldSize = first.get(lhs).size();
                first.get(lhs).addAll(computeFirst(rhs));
                if (first.get(lhs).size() > oldSize) {
                    changed = true;
                }
            }
        } while (changed);
    }

    private static Set<String> computeFirst(List<String> symbols) {
        Set<String> result = new HashSet<>();
        for (String symbol : symbols) {
            if (grammar.terminals.contains(symbol)) {
                result.add(symbol);
                break;
            } else {
                result.addAll(first.get(symbol));
                if (!first.get(symbol).contains("ε")) {
                    break;
                }
            }
        }
        return result;
    }

    private static void computeFollowSets() {
        boolean changed;
        do {
            changed = false;
            for (Rule rule : grammar.rules) {
                String lhs = rule.lhs;
                List<String> rhs = rule.rhs;

                for (int i = 0; i < rhs.size(); i++) {
                    String symbol = rhs.get(i);
                    if (grammar.nonTerminals.contains(symbol)) {
                        Set<String> followSet = follow.get(symbol);
                        int oldSize = followSet.size();

                        if (i < rhs.size() - 1) {
                            List<String> beta = rhs.subList(i + 1, rhs.size());
                            Set<String> firstOfBeta = computeFirst(beta);
                            followSet.addAll(firstOfBeta);
                            followSet.remove("ε");
                        }

                        if (i == rhs.size() - 1 || computeFirst(rhs.subList(i + 1, rhs.size())).contains("ε")) {
                            followSet.addAll(follow.get(lhs));
                        }

                        if (followSet.size() > oldSize) {
                            changed = true;
                        }
                    }
                }
            }
        } while (changed);
    }
}

