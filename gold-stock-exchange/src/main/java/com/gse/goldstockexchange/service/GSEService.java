package com.gse.goldstockexchange.service;

import com.gse.goldstockexchange.model.GSEPerson;
import com.gse.goldstockexchange.web.model.GSEResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class GSEService {

    List<GSEPerson> personSellerList ;
    List<GSEPerson> personBuyerList;

    GSEService(){
      this.personSellerList=populateSellerList();
      this.personBuyerList=populateBuyerList();
    }

    public GSEResponse getAllPossibleSellers(String buyerName, int requiredQuantity, int buyerRate){
       log.info("Filter sellers based on buyer's rate");
       List<GSEPerson> sellerList = personSellerList.stream().filter(person-> person.getRate() <= buyerRate).collect(Collectors.toList());
       log.info("Prospective sellers are..{}",sellerList);
       if(sellerList!=null && sellerList.isEmpty()){
           log.info("Buyers order is in pending state as no suitable seller found");
           return GSEResponse.builder().message("Order submitted in pending state").buyerList(personBuyerList).sellerList(personSellerList).build();

       }

       else{
           Comparator<GSEPerson> compareFirstByRate = Comparator.comparing( GSEPerson::getRate );
           Comparator<GSEPerson> compareNextByTime = compareFirstByRate.thenComparing( GSEPerson::getTimestamp );
           log.info("Sorting the sellers on rate and then on timestamp");
           Collections.sort(sellerList,compareNextByTime);
           log.info("After sorting the sellers on rate and then on timestamp..{}",sellerList);

           //remove all the sellers who have quantity as zero
           sellerList = sellerList.stream().filter(person->person.getQuantity() >0).collect(Collectors.toList());
           int newQuantity = requiredQuantity;
           for (GSEPerson person : sellerList) {
               int itemQuantity= person.getQuantity();
               log.info("Current Item's quantity:{}",itemQuantity);
               if(newQuantity >= itemQuantity){
                   updateSellers(person,0);
                   newQuantity=newQuantity-itemQuantity;
               } else {
                   updateSellers(person,itemQuantity-newQuantity);
                   newQuantity=0;
               }
               log.info("Quantity left to be complete:{}",newQuantity);
               if (newQuantity ==0){
                   log.info("Quantity need complete");
                   log.info("Current BuyerList : {} ",personBuyerList);
                   log.info("Current SellerList : {} ",personSellerList);
                   return GSEResponse.builder().buyerList(personBuyerList).sellerList(personSellerList).build();

               }
               if(sellerList.size()-1==sellerList.indexOf(person) && newQuantity>0){
                   return GSEResponse.builder().message("Currently no sellers have enough quantity to serve remaining gold request: "+ " "+ newQuantity).buyerList(personBuyerList).sellerList(personSellerList).build();
               }

           }

       }
        return null;
    }

    private void updateSellers(GSEPerson p, int requiredQuantity){
        log.info("ENTRY:Inside updateSellers:");
        Optional<GSEPerson> seller= personSellerList.stream().filter(person->person.getName().equals(p.getName())).findAny();
        if(seller.isPresent()){
            log.info("Update sellers new quantity");
            GSEPerson newP = seller.get();
            newP.setQuantity(requiredQuantity);
            personSellerList.remove(seller.get());
            personSellerList.add(newP);
        }else {
            log.info("Seller not found");
        }
        log.info("EXIT:updateSellers:");

    }


    private List<GSEPerson> populateSellerList(){
        personSellerList= new ArrayList<>();
        GSEPerson person = GSEPerson.builder().name("Seller-1").timestamp(LocalTime.parse("08:01:03", DateTimeFormatter.ISO_LOCAL_TIME)).rate(500).quantity(5).build();
        personSellerList.add(person);
        person = GSEPerson.builder().name("Seller-2").timestamp(LocalTime.parse("08:03:02", DateTimeFormatter.ISO_LOCAL_TIME)).rate(700).quantity(2).build();
        personSellerList.add(person);
        person = GSEPerson.builder().name("Seller-3").timestamp(LocalTime.parse("08:04:04", DateTimeFormatter.ISO_LOCAL_TIME)).rate(800).quantity(6).build();
        personSellerList.add(person);
        person = GSEPerson.builder().name("Seller-4").timestamp(LocalTime.parse("08:04:05", DateTimeFormatter.ISO_LOCAL_TIME)).rate(600).quantity(10).build();
        personSellerList.add(person);
        return personSellerList;

    }

    private List<GSEPerson> populateBuyerList(){
        personBuyerList = new ArrayList<>();
        GSEPerson person = GSEPerson.builder().name("Buyer-1").timestamp(LocalTime.parse("09:01", DateTimeFormatter.ISO_LOCAL_TIME)).rate(500).quantity(8).build();
        personBuyerList.add(person);
        return personBuyerList;

    }


}
