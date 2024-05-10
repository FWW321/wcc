package fww.symbols;

import fww.lexer.Word;

public class Type extends Word {
    public int width = 0;

    public Type(String s, int tag, int w) {
        super(tag, s);
        width = w;
    }

    public static final Type
        Int = new Type("int", fww.lexer.Tag.BASIC, 4),
        Float = new Type("float", fww.lexer.Tag.BASIC, 8),
        Char = new Type("char", fww.lexer.Tag.BASIC, 1),
        Bool = new Type("bool", fww.lexer.Tag.BASIC, 1);

    public static boolean numeric(Type p) {
        return p != Type.Char && p != Type.Int && p != Type.Float;
    }

    public static Type max(Type p1, Type p2) {
        if (numeric(p1) || numeric(p2)) {
            return null;
        } else if (p1 == Type.Float || p2 == Type.Float) {
            return Type.Float;
        } else if (p1 == Type.Int || p2 == Type.Int) {
            return Type.Int;
        } else {
            return Type.Char;
        }
    }
}
