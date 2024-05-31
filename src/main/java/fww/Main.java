package fww;

import fww.lexer.Lexer;
import fww.parser.Parser;

import java.io.FileNotFoundException;

//TIP 要<b>运行</b>代码，请按 <shortcut actionId="Run"/> 或
// 点击装订区域中的 <icon src="AllIcons.Actions.Execute"/> 图标。
public class Main {
    public static void main(String[] args) {
        Lexer lexer = null;
        try {
            lexer = new Lexer("/home/fww/IdeaProjects/wcc/src/main/resources/test");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            Parser parser = new Parser(lexer);
            parser.program();
            System.out.write('\n');
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}