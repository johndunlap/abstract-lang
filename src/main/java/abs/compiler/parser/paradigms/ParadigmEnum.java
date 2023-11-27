package abs.compiler.parser.paradigms;

public enum ParadigmEnum {
    OOP("oop");

    private final String name;

    ParadigmEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static ParadigmEnum findByName(String name) {
        for (ParadigmEnum paradigm : values()) {
            if (paradigm.getName().equals(name)) {
                return paradigm;
            }
        }
        return null;
    }
}
