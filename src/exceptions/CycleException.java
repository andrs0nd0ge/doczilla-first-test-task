package exceptions;

import java.util.List;

public class CycleException extends RuntimeException {
    private final List<String> cycle;

    public CycleException(List<String> cycle) {
        this.cycle = cycle;
    }

    public String getCycleString() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < cycle.size(); i++) {
            sb.append(cycle.get(i));
            if (i + 1 < cycle.size()) {
                sb.append(" -> ");
            }
        }

        return sb.toString();
    }
}
