package abs.compiler.archive.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum Paradigm {
    OOP(true),
    PROCEDURAL(false),
    FUNCTIONAL(false);

    // Caches for performance
    private static Map<String, Paradigm> supportedParadigmMap;
    private static List<Paradigm> supportedParadigmList;
    private static String legalParadigms;

    private boolean supported;

    Paradigm(boolean supported) {
        this.supported = supported;

        updateStaticMembers();
    }

    // This method should only be called from the constructor
    private void updateStaticMembers() {
        if (supportedParadigmList == null) {
            supportedParadigmList = new ArrayList<>();
        } if (supportedParadigmMap == null) {
            supportedParadigmMap = new HashMap<>();
        }

        if (this.supported) {
            supportedParadigmList.add(this);
            supportedParadigmMap.put(this.name().toLowerCase(), this);
        }
    }

    public boolean isSupported() {
        return supported;
    }

    public static boolean isSupported(String p) {
        return supportedParadigmMap.get(p) != null;
    }

    public static String getLegalParadigmsAsString() {
        // Return a cached string if possible, for performance reasons
        if (legalParadigms != null) {
            return legalParadigms;
        }

        StringBuilder sb = new StringBuilder();

        // Build a string list of supported paradigms which an be used in error messages
        boolean first = true;
        for (Paradigm p : supportedParadigmList) {
            if (!first) {
                sb.append(',');
            } else {
                sb.append(p.name().toLowerCase());
            }
            first = false;
        }

        legalParadigms = sb.toString();
        return legalParadigms;
    }
}
