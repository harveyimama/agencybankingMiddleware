package com.boat.bp.middleware.responses;

import com.boat.bp.middleware.data.ClientResource;

public class PagedResponse {

    private int totalFilteredRecords ;
    private ClientResource[] pageItems; 

    public ClientResource[] getPageItems() {
		return pageItems;
	}

	public void setPageItems(ClientResource[] pageItems) {
		this.pageItems = pageItems;
	}

    public int getTotalFilteredRecords() {
        return totalFilteredRecords;
    }

    public void setTotalFilteredRecords(int totalFilteredRecords) {
        this.totalFilteredRecords = totalFilteredRecords;
    }


    
}
