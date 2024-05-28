package fww.inter;

import fww.lexer.Token;
import fww.symbols.Type;

public class Expr {
    public Token op;
    public Type type;

    Expr(Token tok, Type p) {
        op = tok;
        type = p;
    }

    public Expr gen() {
        return this;
    }

    public Expr reduce() {
        return this;
    }

    public void jumping(int t, int f) {
        emitjumps(toString(), t, f);
    }

    public void emitjumps(String test, int t, int f) {
        if (t != 0 && f != 0) {
            emit("if " + test + " goto L" + t);
            emit("goto L" + f);
        } else if (t != 0) {
            emit("if " + test + " goto L" + t);
        } else if (f != 0) {
            emit("iffalse " + test + " goto L" + f);
        } else {
            // nothing since both t and f fall through
        }
    }

    public String toString() {
        return op.toString();
    }

    public void emit(String s) {
        System.out.println("\t" + s);
    }

    public void error(String booleanRequiredInDo) {
        throw new Error(booleanRequiredInDo);
    }
}
