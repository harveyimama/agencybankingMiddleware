package com.boat.bp.middleware.data;

import java.util.ArrayList;
import java.util.List;

import com.boat.bp.middleware.helper.Settings;
import com.boat.grpc.server.grpcserver.AddBankRequest;
import com.boat.grpc.server.grpcserver.AddBillerRequest;
import com.boat.grpc.server.grpcserver.OnboardingRequest;
import com.fasterxml.jackson.annotation.JsonSetter;

public class User {

    private String username;
    private String password;
    private String firstname;
    private String lastname;
    private String email;
    private String officeId;
    private List<Integer> roles;
    private boolean sendPasswordToEmail;
    private boolean isSelfServiceUser;
    private boolean passwordNeverExpires;
    private List<Integer> clients;
    private String repeatPassword;

    public User(OnboardingRequest r) {

        this.username = r.getPhone();
        this.password = r.getPhone();
        this.repeatPassword = this.password;
        this.firstname = r.getFirstName();
        this.lastname = r.getLastName();
        this.email = r.getEmail();
        this.officeId = Settings.officeId;
        this.roles = new ArrayList<Integer>();
        this.roles.add(Settings.CUSTOMER_ROLE);
        this.sendPasswordToEmail = false;
        this.isSelfServiceUser = true;
        this.passwordNeverExpires = true;
        this.clients = new ArrayList<Integer>();
        this.clients.add(Integer.parseInt(r.getClientId())) ;
    }

    public User(final AddBillerRequest r,final String mainClient,final String commissionClient) {

        this.username = r.getBillerCode();
        this.password = r.getBillerCode();
        this.repeatPassword = this.password;
        this.firstname = r.getBillerName();
        this.lastname = r.getBillerCode();
        this.email = r.getBillerCode().concat("@boat.com");
        this.officeId = Settings.officeId;
        this.roles = new ArrayList<Integer>();
        this.roles.add(Settings.CUSTOMER_ROLE);
        this.sendPasswordToEmail = false;
        this.isSelfServiceUser = true;
        this.passwordNeverExpires = true;
        this.clients = new ArrayList<Integer>();
        this.clients.add(Integer.parseInt(mainClient)) ;
        this.clients.add(Integer.parseInt(commissionClient)) ;
    }

    

    public User(AddBankRequest r, String clientId, String chargeClientId) {

        this.username = r.getBankCode();
        this.password = r.getBankCode();
        this.repeatPassword = this.password;
        this.firstname = r.getBankName();
        this.lastname = r.getBankCode();
        this.email = r.getBankCode().concat("@boat.com");
        this.officeId = Settings.officeId;
        this.roles = new ArrayList<Integer>();
        this.roles.add(Settings.CUSTOMER_ROLE);
        this.sendPasswordToEmail = false;
        this.isSelfServiceUser = true;
        this.passwordNeverExpires = true;
        this.clients = new ArrayList<Integer>();
        this.clients.add(Integer.parseInt(clientId)) ;
        this.clients.add(Integer.parseInt(chargeClientId)) ;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    public List<Integer> getRoles() {
        return roles;
    }

    public void setRoles(List<Integer> roles) {
        this.roles = roles;
    }

    public boolean isSendPasswordToEmail() {
        return sendPasswordToEmail;
    }

    public void setSendPasswordToEmail(boolean sendPasswordToEmail) {
        this.sendPasswordToEmail = sendPasswordToEmail;
    }

    public boolean isSelfServiceUser() {
        return isSelfServiceUser;
    }
    @JsonSetter("isSelfServiceUser")
    public void setSelfServiceUser(boolean isSelfServiceUser) {
        this.isSelfServiceUser = isSelfServiceUser;
    }

    public boolean isPasswordNeverExpires() {
        return passwordNeverExpires;
    }

    public void setPasswordNeverExpires(boolean passwordNeverExpires) {
        this.passwordNeverExpires = passwordNeverExpires;
    }

    public List<Integer> getClients() {
        return clients;
    }

    public void setClients(List<Integer> clients) {
        this.clients = clients;
    }

    public String getRepeatPassword() {
        return repeatPassword;
    }

    public void setRepeatPassword(String repeatPassword) {
        this.repeatPassword = repeatPassword;
    } 

    
    
}
