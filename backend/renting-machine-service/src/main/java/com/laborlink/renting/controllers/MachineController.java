package com.laborlink.renting.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.laborlink.renting.dtos.ResponseShape;
import com.laborlink.renting.dtos.SingleRentedItemDTO;
import com.laborlink.renting.services.ActivityService;

import lombok.AllArgsConstructor;

@CrossOrigin(origins = { "http://localhost:3001" })
@RestController
@RequestMapping("/activity")
@AllArgsConstructor
public class MachineController {

    private ActivityService activityService;

    @PostMapping("/rent")
    public ResponseEntity<ResponseShape> rentTools(@RequestHeader(value = "jwt-card", required = true) String jwt,
            @RequestBody List<SingleRentedItemDTO> items) {
        System.out.println(jwt);
        // JWT Should be Decoded by client secret here in this service but because of timelimitation we don't hv enough time to do it
        // So its decoded in Machine Renting Monitor
        activityService.startRentingToolsActivity(items,jwt);
        return new ResponseEntity<>(new ResponseShape(items), HttpStatus.OK);
    }

    @PostMapping("/return")
    public ResponseEntity<ResponseShape> returnTools(@RequestHeader(value = "jwt-card", required = true) String jwt,
            @RequestBody List<SingleRentedItemDTO> items) {
        
        activityService.startReturnToolsActivity(items,jwt);
        return new ResponseEntity<>(new ResponseShape(items), HttpStatus.OK);
    }

}
