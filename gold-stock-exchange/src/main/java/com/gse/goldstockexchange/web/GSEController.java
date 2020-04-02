package com.gse.goldstockexchange.web;

import com.gse.goldstockexchange.service.GSEService;
import com.gse.goldstockexchange.web.model.GSEResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping (
        path = "/gse",
        consumes = MediaType.ALL_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
  )
public class GSEController {

  @Autowired
  private GSEService gseService;

  @GetMapping("/buyGold/{buyerName}/{requiredQuantity}/{buyerRate}")
  public GSEResponse buyGoldForBuyer(@PathVariable String buyerName , @PathVariable int requiredQuantity, @PathVariable int buyerRate){
    return gseService.getAllPossibleSellers(buyerName,requiredQuantity,buyerRate);
  }

  @GetMapping("/welcome")
  public String welcome(){
        return "Welcome to Gold Stock Exchange";
  }

}
