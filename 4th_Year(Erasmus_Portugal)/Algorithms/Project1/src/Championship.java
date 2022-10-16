public class Championship {
    private final int year;
    private final String racer;
    private final String category;
    private final String bike;
    private final int position;
    private final int points;
    private final Race[] race = new Race[21];

    public Championship(int year, String racer, String category, String bike, int position, int points) {
        this.year = year;
        this.racer = racer;
        this.category = category;
        this.bike = bike;
        this.position = position;
        this.points = points;
    }

    public int getYear() {
        return year;
    }

    public String getRacer() {
        return racer;
    }

    public String getCategory() {
        return category;
    }

    public String getBike() {
        return bike;
    }

    public int getPosition() {
        return position;
    }

    public int getPoints() {
        return points;
    }

    public void addRace(int raceNumber, Race r) {
        if (this.getYear() != r.getYear() || !this.getRacer().equals(r.getRacer()))
            throw new IllegalArgumentException();
        if (raceNumber < 1 || raceNumber > 21)
            throw new IndexOutOfBoundsException();
        race[raceNumber - 1] = r;
    }

    public Race getRace(int raceNumber) {
        if (raceNumber < 1 || raceNumber > 21)
            throw new IndexOutOfBoundsException();
        return race[raceNumber - 1];
    }

    @Override
    public String toString() {
        StringBuilder stb = new StringBuilder();
        for (int i = 0; i < race.length; i++) {
            if (race[i] == null)
                stb.append(" /");
            else if (race[i].getFinished())
                stb.append(race[i].getCircuit() + " " + race[i].getPosition() + "/");
            else
                stb.append(race[i].getCircuit() + " /");
        }
        return racer +
                "/" + year +
                "/" + category +
                "/" + bike +
                "\n" + stb;
    }

    public int compareTo(Championship c2) {
        int raceComparator = this.getRacer().compareTo(c2.getRacer());
        if (raceComparator == 0) {
            return c2.getYear() - this.getYear();
        } else {
            return raceComparator;
        }
    }
}