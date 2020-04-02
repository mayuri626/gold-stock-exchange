package com.gse.goldstockexchange.service;

import com.gse.goldstockexchange.web.model.GSEResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@Slf4j
public class GSEServiceTest {

    private GSEService gseService=new GSEService();

    @Test
    public void getAllPossibleSellers() {

        GSEResponse response= gseService.getAllPossibleSellers("Buyer-1",8,650);
        long count=response.getSellerList().stream().filter(person->person.getName().equals("Seller-1") || person.getName().equals("Seller-4")).count();
        log.info("Actual Count{}",count);
        Assert.assertEquals(2,count);
    }

    @Test
    public void getAllPossibleSellersWithLeastBuyerRate() {

        String expectedResponse="Order submitted in pending state";
        GSEResponse response= gseService.getAllPossibleSellers("Buyer-1",8,100);
        boolean isMatch=response.getMessage().equals(expectedResponse);
        log.info("Actual Match{}",isMatch);
        Assert.assertEquals(true,isMatch);
    }


}