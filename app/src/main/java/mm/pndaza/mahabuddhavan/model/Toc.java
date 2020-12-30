package mm.pndaza.mahabuddhavan.model;

public class Toc {
    String name;
    int level;
    int page;

    public Toc(String name, int level, int page) {
        this.name = name;
        this.level = level;
        this.page = page;
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public int getPage() {
        return page;
    }
}
