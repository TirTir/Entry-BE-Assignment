package backend.resource.common.pagination;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaginationLinks {
    private String first;
    private String last;
    private String next;
    private String prev;
}
