package fww.inter;

public class Id extends Expr {
    public int offset; //相对地址
    public Id(fww.lexer.Word id, fww.symbols.Type p, int b) {
        super(id, p);
        offset = b;
    }
}
