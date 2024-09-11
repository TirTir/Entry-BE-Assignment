package backend.keumbang.common.constants;

public enum UserRole {
    ADMIN("판매자"),
    USER("구매자");

    private final String description;

    UserRole(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
