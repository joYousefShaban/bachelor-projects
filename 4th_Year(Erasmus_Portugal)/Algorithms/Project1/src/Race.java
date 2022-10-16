public class Race {
    private final String circuit;
    private final String racer;
    private final int year;
    private final int position;
    private final boolean finished;

    public Race(String circuit, String racer, int year, int position, boolean finished) {
        this.circuit = circuit;
        this.racer = racer;
        this.year = year;
        this.finished = finished;
        if (finished)
            this.position = position;
        else
            this.position = 0;
    }

    public String getCircuit() {
        return this.circuit;
    }

    public String getRacer() {
        return this.racer;
    }

    public int getYear() {
        return this.year;
    }

    public int getPosition() {
        return this.position;
    }

    public boolean getFinished() {
        return this.finished;
    }

    @Override
    public String toString() {
        if(getFinished())
        return this.circuit + "-" +
                this.year + "-" +
                this.racer +
                ": " + this.position;
        return this.circuit + "-" +
                this.year + "-" +
                this.racer + ": ";
    }
}