package com.gettec.fsnip.fsn.model.business;


import java.util.HashSet;
import java.util.Set;

/**
 * Created by Administrator on 2016/9/22.
 */
public class NavigationAddress {

    private long navigationId;

    private int addressId;

    public long getNavigationId() {
        return navigationId;
    }

    public void setNavigationId(long navigationId) {
        this.navigationId = navigationId;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }
}

