package com.laborlink.renting.utils;

import com.laborlink.renting.enums.LaborLinkTopics;
import com.laborlink.renting.enums.ToolStatus;
import com.laborlink.renting.exceptions.UnkownGlobalException;

public class EnumConverter {

    public static ToolStatus convertToToolStatus(String toolId, Object status) {

        if (status == null)
            throw new IllegalArgumentException(
                    String.format("You are trying to update tool with id %s with null status", toolId));

        switch (status.toString()) {
            case "GOOD":
                return ToolStatus.GOOD;
            case "0":
                return ToolStatus.GOOD;
            case "TO_BE_REPLACED":
                return ToolStatus.TO_BE_REPLACED;
            case "1":
                return ToolStatus.TO_BE_REPLACED;
            case "BROKEN":
                return ToolStatus.BROKEN;
            case "2":
                return ToolStatus.BROKEN;
            case "NOT_AVAILABLE":
                return ToolStatus.NOT_AVAILABLE;
            case "3":
                return ToolStatus.NOT_AVAILABLE;
            case "NOT_SET":
                return ToolStatus.NOT_SET;
            case "4":
                return ToolStatus.NOT_SET;
            default:
                throw new IllegalArgumentException(
                        String.format("You are trying to update tool with id %s with unvalid status status", toolId));
        }

    }

    public static String getTopicName(LaborLinkTopics topic) {

        if (topic == LaborLinkTopics.ActivityRecordEvents)
            return "activity.record.events";

        if (topic == LaborLinkTopics.ToolRentalEvents)
            return "tool.rental.events";

        if (topic == LaborLinkTopics.InvoiceIssuedEvents) /* NOT NEEDED HERE IN THIS SERVICE ANY WAY ITS DEFINED */
            return "invoice.issued.events";

        throw new UnkownGlobalException();

    }
}
