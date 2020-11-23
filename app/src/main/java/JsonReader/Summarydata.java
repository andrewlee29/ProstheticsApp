package JsonReader;

public class Summarydata {
    private String date;
    private String environmentStatus;
    private String muscleStatus;

    public Summarydata(String date, String environmentStatus, String muscleStatus){
        this.date =date;
        this.environmentStatus =environmentStatus;
        this.muscleStatus =muscleStatus;
    }

    public String getDate() {
        return date;
    }

    public String getEnvironmentStatus() {
        return environmentStatus;
    }

    public String getMuscleStatus() {
        return muscleStatus;
    }
}
