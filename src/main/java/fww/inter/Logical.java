package fww.inter;

public class Logical extends Expr{
    public Expr expr1, expr2;

    Logical(fww.lexer.Token tok, Expr x1, Expr x2) {
        super(tok, null);
        expr1 = x1;
        expr2 = x2;
        type = check(expr1.type, expr2.type);
        if (type == null) {
            error("type error");
        }
    }

    public fww.symbols.Type check(fww.symbols.Type p1, fww.symbols.Type p2) {
        if (p1 == fww.symbols.Type.Bool && p2 == fww.symbols.Type.Bool) {
            return fww.symbols.Type.Bool;
        } else {
            return null;
        }
    }

    public Expr gen() {
        int f = newlabel();
        int a = newlabel();
        Temp temp = new Temp(type);
        this.jumping(0, f);
        emit(temp.toString() + " = true");
        emit("goto L" + a);
        emitlabel(f);
        emit(temp.toString() + " = false");
        emitlabel(a);
        return temp;
    }

    public String toString() {
        return expr1.toString() + " " + op.toString() + " " + expr2.toString();
    }
}
