package gov.cipam.gi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Deepak on 11/18/2017.
 */

public class Product implements Serializable {

    private String name,dpurl,detail,category,state;
    @JsonIgnore
    private ArrayList<Seller> seller;

    public Product(){

    }

    public void  GI(String name, String dpurl, String detail, String category, String state) {
        this.name = name;
        this.dpurl = dpurl;
        this.detail = detail;
        this.category = category;
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDpurl() {
        return dpurl;
    }

    public void setDpurl(String dpurl) {
        this.dpurl = dpurl;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public ArrayList<Seller> getSeller() {
        return seller;
    }

    public void setSeller(ArrayList<Seller> seller) {
        this.seller = seller;
    }
}
