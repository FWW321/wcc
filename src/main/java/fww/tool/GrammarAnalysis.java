package fww.tool;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class GrammarAnalysis {
    Grammar grammar = new Grammar();
    Map<String, Set<String>> first = new HashMap<>();
    Map<String, Set<String>> follow = new HashMap<>();
    String startSymbol = "Program";


    public static void main(String[] args) {
        GrammarAnalysis grammarAnalysis = new GrammarAnalysis();
        grammarAnalysis.init();
        grammarAnalysis.generatePredictiveAnalysisTable();
        grammarAnalysis.writeGrammar();
    }

    public void init(){
        addRule("Program", List.of("Block"));
        addRule("Block", Arrays.asList("{", "Decls", "Stmts", "}"));
        addRule("Decls",Arrays.asList("Decl", "Decls"));
        addRule("Decls", List.of("ε"));
        addRule("Decl", Arrays.asList("Type", "id", ";"));
        addRule("Type", Arrays.asList("basic", "Dims"));
        addRule("Dims", Arrays.asList("[", "num", "]", "Dims"));
        addRule("Dims", List.of("ε"));
        addRule("Stmts", List.of("Seq"));
        addRule("Seq", Arrays.asList("Stmt", "seq"));
        addRule("Seq", List.of("ε"));
        addRule("Stmt", Arrays.asList("Loc", "=", "Bool", ";"));
        addRule("Stmt", Arrays.asList("if", "(", "Bool", ")", "Stmt"));
        addRule("Stmt", Arrays.asList("if", "(", "Bool", ")", "Stmt", "else", "Stmt"));
        addRule("Stmt", Arrays.asList("while", "(", "Bool", ")", "Stmt"));
        addRule("Stmt", Arrays.asList("do", "Stmt", "while", "(", "Bool", ")", ";"));
        addRule("Stmt", Arrays.asList("break", ";"));
        addRule("Stmt", List.of("Block"));
        addRule("Loc", Arrays.asList("id", "Loc'"));
        addRule("Loc'", Arrays.asList("[", "Bool", "]", "Loc'"));
        addRule("Loc'", List.of("ε"));
        addRule("Bool", Arrays.asList("Join", "Bool'"));
        addRule("Bool'", Arrays.asList("||", "Join", "Bool'"));
        addRule("Bool'", List.of("ε"));
        addRule("Join", Arrays.asList("Equality", "Join'"));
        addRule("Join'", Arrays.asList("&&", "Equality", "Join'"));
        addRule("Join'", List.of("ε"));
        addRule("Equality", Arrays.asList("Rel", "Equality'"));
        addRule("Equality'", Arrays.asList("==", "Rel", "Equality'"));
        addRule("Equality'", Arrays.asList("!=", "Rel", "Equality'"));
        addRule("Equality'", List.of("ε"));
        addRule("Rel", Arrays.asList("Expr", "<", "Expr"));
        addRule("Rel", Arrays.asList("Expr", "<=", "Expr"));
        addRule("Rel", Arrays.asList("Expr", ">", "Expr"));
        addRule("Rel", Arrays.asList("Expr", ">=", "Expr"));
        addRule("Rel", List.of("Expr"));
        addRule("Expr", Arrays.asList("Term", "Expr'"));
        addRule("Expr'", Arrays.asList("+", "Term", "Expr'"));
        addRule("Expr'", Arrays.asList("-", "Term", "Expr'"));
        addRule("Expr'", List.of("ε"));
        addRule("Term", Arrays.asList("Unary", "Term'"));
        addRule("Term'", Arrays.asList("*", "Unary", "Term'"));
        addRule("Term'", Arrays.asList("/", "Unary", "Term'"));
        addRule("Term'", List.of("ε"));
        addRule("Unary", Arrays.asList("!", "Unary"));
        addRule("Unary", Arrays.asList("-", "Unary"));
        addRule("Unary", List.of("Factor"));
        addRule("Factor", Arrays.asList("(", "Bool", ")"));
        addRule("Factor", List.of("Loc"));
        addRule("Factor", List.of("num"));
        addRule("Factor", List.of("real"));
        addRule("Factor", List.of("true"));
        addRule("Factor", List.of("false"));

        // 初始化 FIRST 和 FOLLOW 集合
        initializeFirstAndFollow();

        // 计算 FIRST 集合
        computeFirstSets();

        // 计算 FOLLOW 集合
        computeFollowSets();

        writeFirstAndFollowToFile();
    }

    private void writeFirstAndFollowToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("src/main/resources/first_and_follow"))) {
            // 写入 FIRST 集合
            writer.println("FIRST 集合:");
            for (String nonTerminal : grammar.nonTerminals) {
                writer.println("FIRST(" + nonTerminal + ") = " + first.get(nonTerminal));
            }

            // 写入 FOLLOW 集合
            writer.println("\nFOLLOW 集合:");
            for (String nonTerminal : grammar.nonTerminals) {
                writer.println("FOLLOW(" + nonTerminal + ") = " + follow.get(nonTerminal));
            }

            System.out.println("FIRST 和 FOLLOW 集合已成功写入到文件: " + "src/main/resources/first_and_follow");
        } catch (IOException e) {
            System.err.println("写入 FIRST 和 FOLLOW 集合时出现错误: " + e.getMessage());
        }
    }

    private void addRule(String lhs, List<String> rhs) {
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

    private void initializeFirstAndFollow() {
        for (String nonTerminal : grammar.nonTerminals) {
            first.put(nonTerminal, new HashSet<>());
            follow.put(nonTerminal, new HashSet<>());
        }
        follow.get(startSymbol).add(""+(char)65535); // 起始符号的FOLLOW集包含$
    }

    private void computeFirstSets() {
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

    private Set<String> computeFirst(List<String> symbols) {
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

    private void computeFollowSets() {
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

//    public void generatePredictiveAnalysisTable() {
//        Map<String, Map<String, Rule>> predictiveAnalysisTable = new HashMap<>();
//        for (Rule rule : grammar.rules) {
//            String lhs = rule.lhs;
//            Set<String> firstOfRhs = computeFirst(rule.rhs);
//            for (String symbol : firstOfRhs) {
//                if (!symbol.equals("ε")) {
//                    if (!predictiveAnalysisTable.containsKey(lhs)) {
//                        predictiveAnalysisTable.put(lhs, new HashMap<>());
//                    }
//                    predictiveAnalysisTable.get(lhs).put(symbol, rule);
//                }
//            }
//            if (firstOfRhs.contains("ε")) {
//                for (String symbol : follow.get(lhs)) {
//                    if (!predictiveAnalysisTable.containsKey(lhs)) {
//                        predictiveAnalysisTable.put(lhs, new HashMap<>());
//                    }
//                    predictiveAnalysisTable.get(lhs).put(symbol, rule);
//                }
//            }
//        }
//        System.out.println("\n预测分析表:");
//        for (String nonTerminal : predictiveAnalysisTable.keySet()) {
//            for (String terminal : predictiveAnalysisTable.get(nonTerminal).keySet()) {
//                System.out.println("M[" + nonTerminal + ", " + terminal + "] = " + predictiveAnalysisTable.get(nonTerminal).get(terminal).lhs + " -> " + predictiveAnalysisTable.get(nonTerminal).get(terminal).rhs);
//            }
//        }
//    }

    public void generatePredictiveAnalysisTable() {
        Map<String, Map<String, Rule>> predictiveAnalysisTable = new HashMap<>();
        for (Rule rule : grammar.rules) {
            String lhs = rule.lhs;
            Set<String> firstOfRhs = computeFirst(rule.rhs);
            for (String symbol : firstOfRhs) {
                if (!symbol.equals("ε")) {
                    if (!predictiveAnalysisTable.containsKey(lhs)) {
                        predictiveAnalysisTable.put(lhs, new HashMap<>());
                    }
                    predictiveAnalysisTable.get(lhs).put(symbol, rule);
                }
            }
            if (firstOfRhs.contains("ε")) {
                for (String symbol : follow.get(lhs)) {
                    if (!predictiveAnalysisTable.containsKey(lhs)) {
                        predictiveAnalysisTable.put(lhs, new HashMap<>());
                    }
                    predictiveAnalysisTable.get(lhs).put(symbol, rule);
                }
            }
        }

        String filePath = "src/main/resources/table";

        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            writer.println("预测分析表:");
            for (String nonTerminal : predictiveAnalysisTable.keySet()) {
                for (String terminal : predictiveAnalysisTable.get(nonTerminal).keySet()) {
                    writer.println("M[" + nonTerminal + ", " + terminal + "] = " + predictiveAnalysisTable.get(nonTerminal).get(terminal).lhs + " -> " + predictiveAnalysisTable.get(nonTerminal).get(terminal).rhs);
                }
            }
            System.out.println("预测分析表已成功写入到文件: " + filePath);
        } catch (IOException e) {
            System.err.println("写入预测分析表时出现错误: " + e.getMessage());
        }
    }

    public String getRules() {
        StringBuilder sb = new StringBuilder();
        for (Rule rule : grammar.rules) {
            sb.append(rule).append("\n");
        }
        return sb.toString();
    }

    //将文法写入src/main/resources/grammar文件
    public void writeGrammar() {
        String filePath = "src/main/resources/grammar";
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            writer.println(getRules());
            System.out.println("文法已成功写入到文件: " + filePath);
        } catch (IOException e) {
            System.err.println("写入文法时出现错误: " + e.getMessage());
        }
    }
}

