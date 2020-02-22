package Scope;

public class EntryInfo {

    private String kind;
    private String type;

    public EntryInfo(String kind, String type) {
        this.kind = kind;
        this.type = type;
    }

    public EntryInfo(String kind) {
        this.kind = kind;
        this.type = "";
    }


    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return this.getKind() + " - " + this.getType();
    }
}
