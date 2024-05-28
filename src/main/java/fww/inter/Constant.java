package fww.inter;

public class Constant extends Expr{
    public Constant(fww.lexer.Token tok, fww.symbols.Type p) {
        super(tok, p);
    }

    public Constant(int i) {
        super(new fww.lexer.Num(i), fww.symbols.Type.Int);
    }

    public static final Constant True = new Constant(fww.lexer.Word.True, fww.symbols.Type.Bool);
    public static final Constant False = new Constant(fww.lexer.Word.False, fww.symbols.Type.Bool);

    public void jumping(int t, int f) {
        if (this == True && t != 0) {
            emit("goto L" + t);
        } else if (this == False && f != 0) {
            emit("goto L" + f);
        }
    }
}
