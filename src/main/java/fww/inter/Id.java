package fww.inter;

public class Id extends Expr {
    public int offset; // relative address
    public Id(fww.lexer.Word id, fww.symbols.Type p, int b) {
        super(id, p);
        offset = b;
    }
}
