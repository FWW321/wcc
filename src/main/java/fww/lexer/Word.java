package fww.lexer;

public class Word extends Token {
    public String lexeme = "";

    public Word(int tag, String s) {
        super(tag);
        lexeme = s;
    }

    public String toString() {
        return lexeme;
    }

    public static final Word
        and = new Word(Tag.AND, "&&"),
        or = new Word(Tag.OR, "||"),
        eq = new Word(Tag.EQ, "=="),
        ne = new Word(Tag.NE, "!="),
        le = new Word(Tag.LE, "<="),
        ge = new Word(Tag.GE, ">="),
        minus = new Word(Tag.MINUS, "minus"),
        True = new Word(Tag.TRUE, "true"),
        False = new Word(Tag.FALSE, "false"),
        temp = new Word(Tag.TEMP, "t"),
        If = new Word(Tag.IF, "if"),
        Else = new Word(Tag.ELSE, "else"),
        While = new Word(Tag.WHILE, "while"),
        Do = new Word(Tag.DO, "do"),
        Break = new Word(Tag.BREAK, "break");
}
