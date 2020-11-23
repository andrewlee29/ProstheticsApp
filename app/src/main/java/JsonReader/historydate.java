package JsonReader;

public class historydate {
    private String date;
    private String temp;
    private String humid;

    public historydate(String date, String temp, String humid){
        this.date =date;
        this.temp =temp;
        this.humid =humid;
    }

    public String getDate() {
        return date;
    }

    public String getTemp() {
        return temp;
    }

    public String getHumid() {
        return humid;
    }
}

