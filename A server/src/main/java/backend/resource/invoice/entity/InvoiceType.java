package backend.resource.invoice.entity;

public enum  InvoiceType {
    PURCHASE("구매"),
    SALE("판매");

    private final String description;

    InvoiceType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
