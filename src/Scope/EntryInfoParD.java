package Scope;

public class EntryInfoParD extends EntryInfo {
    private String id;

    public EntryInfoParD(String id, String kind, String type) {
        super(kind, type);
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public EntryInfo getEntryInfo() {
        return new EntryInfo(this.getKind(), this.getType());
    }

    public String toString() {
        return "id: " + this.id + "\n" + super.toString();
    }
}
