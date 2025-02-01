package com.laborlink.renting.enums;

public enum LaborLinkTopics {
    /*
     * IMPORTANT NOTE tool.rental.events
     * This Topic follow kafka Log Compact Rentention Policy
     * This Topic contains two Related Actions (Rent Tools, and Return Tools)
     */
    ToolRentalEvents, // tool.rental.events
    ActivityRecordEvents, // activity.record.events
    InvoiceIssuedEvents, // invoice.issued.events /* NOT NEEDED HERE IN THIS SERVICE ANY WAY ITS DEFINED
                         // */
}