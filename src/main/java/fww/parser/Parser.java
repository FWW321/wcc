package fww.parser;

import fww.lexer.Lexer;
import fww.lexer.Token;
import fww.symbols.Env;

import java.io.IOException;
import java.util.Stack;

public class Parser {
    private Lexer lex;
    private Token look;
    Env top = null;
    int used = 0;

    public Parser(Lexer l) throws IOException {
        lex = l;
        move();
    }

    void move() throws IOException {
        look = lex.scan();
    }

    public static void error(String s) {
        throw new Error("near line " + Lexer.line + ": " + s);
    }
    
    void match(int t) throws IOException {
        if (look.tag == t) {
            move();
        } else {
            error("syntax error");
        }
    }
}
