package fww.fsa;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class Status {

    private final String id;

    private Set<Translation> translations = new HashSet<>();

    boolean isFinal = false;

    public Status() {
        this.id = UUID.randomUUID().toString();
    }

    public void addTranslation(Translation translation) {
        translations.add(translation);
    }

    public void removeTranslation(Translation translation) {
        translations.remove(translation);
    }

    public void removeTranslation(char c) {
        translations.removeIf(translation -> translation.getC() == c);
    }

    public void removeTranslation(Status nextStatus) {
        translations.removeIf(translation -> translation.getNextStatus().equals(nextStatus));
    }

    public void removeTranslation(char c, Status nextStatus) {
        translations.removeIf(translation -> translation.getC() == c && translation.getNextStatus().equals(nextStatus));
    }

    public void clearTranslations() {
        translations.clear();
    }

    public boolean hasTranslation(char c) {
        return translations.stream().anyMatch(translation -> translation.getC() == c);
    }

    public boolean hasTranslation(Status nextStatus) {
        return translations.stream().anyMatch(translation -> translation.getNextStatus().equals(nextStatus));
    }

    public boolean hasTranslation(char c, Status nextStatus) {
        return translations.stream().anyMatch(translation -> translation.getC() == c && translation.getNextStatus().equals(nextStatus));
    }

    public boolean hasTranslation() {
        return !translations.isEmpty();
    }

    public Translation getTranslation(char c) {
        return translations.stream().filter(translation -> translation.getC() == c).findFirst().orElse(null);
    }

    public Translation getTranslation(Status nextStatus) {
        return translations.stream().filter(translation -> translation.getNextStatus().equals(nextStatus)).findFirst().orElse(null);
    }

    public Translation getTranslation(char c, Status nextStatus) {
        return translations.stream().filter(translation -> translation.getC() == c && translation.getNextStatus().equals(nextStatus)).findFirst().orElse(null);
    }

    public boolean isFinal() {
        return isFinal;
    }

    public void notFinal() {
        isFinal = false;
    }

    public void setFinal() {
        isFinal = true;
    }

    public Set<Status> getNextStatusN(char c) {
        Set<Status> nextStatus = new HashSet<>();
        translations.stream().filter(translation -> translation.getC() == c).forEach(translation -> nextStatus.add(translation.getNextStatus()));
        return nextStatus;
    }

    public Status getNextStatusD(char c) {
        return translations.stream().filter(translation -> translation.getC() == c).findFirst().map(Translation::getNextStatus).orElse(null);
    }

    public String getId() {
        return id;
    }

    public Set<Translation> getTranslations() {
        return translations;
    }

    public void setTranslations(Set<Translation> translations) {
        this.translations = translations;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Status that = (Status) obj;
        return Objects.equals(id, that.id);
    }

//    @Override
//    public String toString() {
//        return "Status{" +
//                "id='" + id + '\'' +
//                ", translations=" + translations +
//                ", isFinal=" + isFinal +
//                '}';
//    }
}
