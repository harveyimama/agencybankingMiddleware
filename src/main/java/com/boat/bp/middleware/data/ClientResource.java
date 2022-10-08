package com.boat.bp.middleware.data;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClientResource {
   private  String mobileNo;
   private String externalId;
   private int savingsAccountId;
   private int id;
 
   
   public String getMobileNo() {
      return mobileNo;
   }
   public void setMobileNo(String mobileNo) {
      this.mobileNo = mobileNo;
   }
   public String getExternalId() {
      return externalId;
   }
   public void setExternalId(String externalId) {
      this.externalId = externalId;
   }
   public int getSavingsAccountId() {
      return savingsAccountId;
   }
   public void setSavingsAccountId(int savingsAccountId) {
      this.savingsAccountId = savingsAccountId;
   }
   public int getId() {
      return id;
   }
   public void setId(int id) {
      this.id = id;
   }
  
   

   
    
}
