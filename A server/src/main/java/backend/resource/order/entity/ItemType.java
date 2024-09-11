package backend.resource.order.entity;

public enum ItemType {
    GOLD_999("99.9% 금"),
    GOLD_9999("99.99% 금");

    private final String description;

    ItemType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
