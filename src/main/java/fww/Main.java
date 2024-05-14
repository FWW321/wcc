package fww;

import fww.fsa.NFA;

//TIP 要<b>运行</b>代码，请按 <shortcut actionId="Run"/> 或
// 点击装订区域中的 <icon src="AllIcons.Actions.Execute"/> 图标。
public class Main {
    public static void main(String[] args) {
        NFA nfa = new NFA("a(b|c)*d");
        System.out.println(nfa.match("abcccd"));
    }
}