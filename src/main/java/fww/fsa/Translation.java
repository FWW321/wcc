package fww.fsa;

import java.util.Objects;

public class Translation {
    private char c;

    private Status nextStatus;

    public Translation(char c, Status nextStatus) {
        this.c = c;
        this.nextStatus = nextStatus;
    }

    public char getC() {
        return c;
    }

    public Status getNextStatus() {
        return nextStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(c, nextStatus);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Translation that = (Translation) obj;
        return c == that.c && Objects.equals(nextStatus, that.nextStatus);
    }

//    @Override
//    public String toString() {
//        return "Translation{" +
//                "c=" + c +
//                ", nextStatus=" + nextStatus +
//                '}';
//    }
}
