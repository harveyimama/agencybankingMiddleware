package com.boat.bp.middleware.data;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.boat.bp.middleware.helper.Settings;
import com.boat.grpc.server.grpcserver.AddBankRequest;
import com.boat.grpc.server.grpcserver.AddBillerRequest;
import com.boat.grpc.server.grpcserver.OnboardingRequest;

public class Client  {
	
	private String clientId;
	private String officeId;
	private String firstname;
	private String lastname;
	private String fullname;
	private String externalId;
	private String dateFormat;
	private String locale;
	private boolean  active;
	private String submittedOnDate;
	private String activationDate;
	private String savingsProductId;
	private String savingsProductName;
	private List<SavingsProduct> products;
	private List<Datatable> datatables;
	private String mobileNo ;
	private String clientTypeId;
	private String street;
	private String addressLine1;
	private String addressLine2;
	private String addressLine3;
	private String city;
	private String stateProvinceId;
	private String countryId;
	private String postalCode;
	private String addressType;
	private String role;
	
	
	public Client(OnboardingRequest request) {
		
		this.firstname = request.getFirstName();
		this.lastname = request.getMiddleName().concat(" ").concat(request.getLastName());
		this.active = request.getActivated();	
		this.externalId =  request.getEmail();
		this.mobileNo = request.getPhone();
		this.setLocale(Settings.LOCALE);
		this.active = true;	
		this.dateFormat = Settings.DATE_FORMAT;
		this.officeId = Settings.officeId;
		Date date = Calendar.getInstance().getTime();  
		DateFormat dateFormat = new SimpleDateFormat(Settings.DATE_FORMAT);  
		this.submittedOnDate =  dateFormat.format(date); 
		this.clientTypeId = request.getRole().equals("Agent")?Settings.AGENT_CLIENT_TYPE:Settings.CUSTOMER_CLIENT_TYPE;
		this.activationDate = dateFormat.format(date);
		this.savingsProductId = Settings.WALLET_PRODUCT;
		
		
	}
	public Client(AddBankRequest request, boolean isMain) {
		
			this.fullname = request.getBankName()+(isMain?"":" Charge Account");
			this.savingsProductId = isMain?Settings.BANK_PRODUCT:Settings.BANK_CHARGE_PRODUCT;
			this.externalId =  request.getBankCode()+(isMain?"":"-CH");
			this.active = true;	
			this.setLocale(Settings.LOCALE);
			this.dateFormat = Settings.DATE_FORMAT;
			this.officeId = Settings.officeId;
			Date date = Calendar.getInstance().getTime();  
			DateFormat dateFormat = new SimpleDateFormat(Settings.DATE_FORMAT);  
			this.submittedOnDate =  dateFormat.format(date); 
			this.clientTypeId = Settings.BANK_CLIENT_TYPE;	
			this.activationDate = dateFormat.format(date);
		
	}
	public Client(AddBillerRequest request, boolean isMain) {
		this.fullname = request.getBillerName()+(isMain?"":" Charge Account");
		this.savingsProductId = isMain?Settings.BILLER_PRODUCT:Settings.BILLER_CHARGE_PRODUCT;  
		this.externalId =  request.getBillerCode()+(isMain?"":"-CH");
		this.active = true;	
		this.setLocale(Settings.LOCALE);
		this.dateFormat = Settings.DATE_FORMAT;
		this.officeId = Settings.officeId;
		Date date = Calendar.getInstance().getTime();  
		DateFormat dateFormat = new SimpleDateFormat(Settings.DATE_FORMAT);  
		this.submittedOnDate =  dateFormat.format(date); 
		this.clientTypeId = Settings.BILLER_CLIENT_TYPE;	
		this.activationDate = dateFormat.format(date);
	}
	public String getAddressType() {
		return addressType;
	}
	public void setAddressType(String addressType) {
		this.addressType = addressType;
	}
	public String getOfficeId() {
		return officeId;
	}
	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public String getExternalId() {
		return externalId;
	}
	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}
	public String getDateFormat() {
		return dateFormat;
	}
	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}
	public String getLocale() {
		return locale;
	}
	public void setLocale(String locale) {
		this.locale = locale;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public String getSubmittedOnDate() {
		return submittedOnDate;
	}
	public void setSubmittedOnDate(String submittedOnDate) {
		this.submittedOnDate = submittedOnDate;
	}
	public String getActivationDate() {
		return activationDate;
	}
	public void setActivationDate(String activationDate) {
		this.activationDate = activationDate;
	}
	public String getSavingsProductId() {
		return savingsProductId==null?"":savingsProductId;
	}
	public void setSavingsProductId(String savingsProductId) {
		this.savingsProductId = savingsProductId;
	}
	public String getSavingsProductName() {
		return savingsProductName;
	}
	public void setSavingsProductName(String savingsProductName) {
		this.savingsProductName = savingsProductName;
	}
	public List<SavingsProduct> getProducts() {
		return products;
	}
	public void setProducts(List<SavingsProduct> products) {
		this.products = products;
	}
	public List<Datatable> getDatatables() {
		return datatables;
	}
	public void setDatatables(List<Datatable> datatables) {
		this.datatables = datatables;
	}
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	public String getClientTypeId() {
		return clientTypeId;
	}
	public void setClientTypeId(String clientTypeId) {
		this.clientTypeId = clientTypeId;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getAddressLine1() {
		return addressLine1;
	}
	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}
	public String getAddressLine2() {
		return addressLine2;
	}
	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}
	public String getAddressLine3() {
		return addressLine3;
	}
	public void setAddressLine3(String addressLine3) {
		this.addressLine3 = addressLine3;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getStateProvinceId() {
		return stateProvinceId;
	}
	public void setStateProvinceId(String stateProvinceId) {
		this.stateProvinceId = stateProvinceId;
	}
	public String getCountryId() {
		return countryId;
	}
	public void setCountryId(String countryId) {
		this.countryId = countryId;
	}
	public String getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getFullname() {
		return fullname;
	}
	public void setFullname(String fullname) {
		this.fullname = fullname;
	}
	

	
	

}
