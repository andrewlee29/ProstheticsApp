package JsonReader;

import java.util.List;

public class Historydetaildata {
    private String temp;
    private String humid;
    private List<Emgdata> emgdata1;;
    private List<Integer> section;

    public List<Integer> getSection() {
        return section;
    }

    public String gethumid() {
        return humid;
    }

    public void sethumid(String humid) {
        this.humid = humid;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public List<Emgdata> getemgdata1() {
        return emgdata1;
    }
}
