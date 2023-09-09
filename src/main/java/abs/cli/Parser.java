package abs.cli;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Parser {
    public Arguments parse(String[] args) {
        Map<String, String> values = new HashMap<>();
        Map<String, Boolean> booleans = new HashMap<>();
        boolean previousWasFlag = false;
        String previous = null;

        for (String arg : args) {
            if (arg.startsWith("--")) {
                // The previous argument was a boolean flag if both it and the current argument are flags
                if (previousWasFlag) {
                    booleans.put(previous, true);
                }

                // Remember that this argument was a flag for the next iteration
                previousWasFlag = true;
            } else if (arg.startsWith("-")) {
                if (arg.length() > 2) {

                } else {

                }

                // Remember that this argument was a flag for the next iteration
                previousWasFlag = true;
            } else {
                if (previousWasFlag == true) {

                }

                // Remember that this argument was NOT a flag for the next iteration
                previousWasFlag = false;
            }

            // Remember this argument in the next iteration
            previous = arg;
        }

        return null;
    }
}
