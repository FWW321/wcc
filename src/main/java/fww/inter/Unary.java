package fww.inter;

public class Unary extends Op {
    public Expr expr;

    public Unary(fww.lexer.Token tok, Expr x) {
        super(tok, null);
        expr = x;
        type = fww.symbols.Type.max(fww.symbols.Type.Int, expr.type);
        if (type == null) {
            error("type error");
        }
    }

    public Expr gen() {
        return new fww.inter.Unary(op, expr.reduce());
    }

    public String toString() {
        return op.toString() + " " + expr.toString();
    }
}
