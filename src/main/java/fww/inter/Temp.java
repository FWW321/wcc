package fww.inter;

public class Temp extends Expr {
    static int count = 0;
    int number = 0;

    public Temp(fww.symbols.Type p) {
        super(fww.lexer.Word.temp, p);
        number = ++count;
    }

    public String toString() {
        return "t" + number;
    }
}
