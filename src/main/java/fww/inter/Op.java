package fww.inter;

public class Op extends Expr{
    public Op(fww.lexer.Token tok, fww.symbols.Type p) {
        super(tok, p);
    }

    public Expr reduce() {
        Expr x = gen();
        Temp t = new Temp(type);
        emit(t.toString() + " = " + x.toString());
        return t;
    }
}
