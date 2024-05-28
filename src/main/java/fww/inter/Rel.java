package fww.inter;

public class Rel extends Logical{
    public Rel(fww.lexer.Token tok, Expr x1, Expr x2) {
        super(tok, x1, x2);
    }

    public fww.symbols.Type check(fww.symbols.Type p1, fww.symbols.Type p2) {
        if (p1 instanceof fww.symbols.Array || p2 instanceof fww.symbols.Array) {
            return null;
        } else if (p1 == p2) {
            return fww.symbols.Type.Bool;
        } else {
            return null;
        }
    }

    public void jumping(int t, int f) {
        Expr a = expr1.reduce();
        Expr b = expr2.reduce();
        String test = a.toString() + " " + op.toString() + " " + b.toString();
        emitjumps(test, t, f);
    }
}
