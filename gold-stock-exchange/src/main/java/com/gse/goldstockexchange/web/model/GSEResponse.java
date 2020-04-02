package com.gse.goldstockexchange.web.model;

import com.gse.goldstockexchange.model.GSEPerson;
import lombok.*;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter


public class GSEResponse {
    List<GSEPerson> buyerList;
    List<GSEPerson> sellerList;
    String message;

}
