package commons.utils;

public enum JokerType {
    DOUBLE_POINTS("Double points"),
    HALF_TIME("Half Time"),
    REMOVE_ANSWER("Remove Answer");

    private final String name;

    JokerType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
