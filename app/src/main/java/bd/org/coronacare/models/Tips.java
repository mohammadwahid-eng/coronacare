package bd.org.coronacare.models;

public class Tips {
    private String label;
    private int graphic;

    public Tips() {

    }

    public Tips(String label, int graphic) {
        this.label = label;
        this.graphic = graphic;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getGraphic() {
        return graphic;
    }

    public void setGraphic(int graphic) {
        this.graphic = graphic;
    }
}