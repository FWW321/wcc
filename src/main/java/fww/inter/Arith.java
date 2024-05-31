package fww.inter;

public class Arith extends Op{
    public Expr expr1, expr2;
    public Arith(fww.lexer.Token tok, Expr x1, Expr x2) {
        super(tok, null); // null type to start
        expr1 = x1;
        expr2 = x2;
        type = fww.symbols.Type.max(expr1.type, expr2.type);
        if (type == null) error("type error");
    }

    public void error(String booleanRequiredInDo) {
        super.error(booleanRequiredInDo);
    }

    public Expr gen() {
        return new Arith(op, expr1.reduce(), expr2.reduce());
    }

    public String toString() {
        return expr1.toString() + " " + op.toString() + " " + expr2.toString();
    }
}
