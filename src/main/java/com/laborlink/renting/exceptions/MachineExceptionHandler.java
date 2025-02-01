package com.laborlink.renting.exceptions;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.laborlink.renting.dtos.ErrorDTO;
import com.laborlink.renting.dtos.ResponseShape;

@ControllerAdvice
public class MachineExceptionHandler {

        @ExceptionHandler(IllegalArgumentException.class)
        public ResponseEntity<ResponseShape> catchIllegalArgumentException(IllegalArgumentException e) {
                return new ResponseEntity<>(
                                new ResponseShape(new ErrorDTO("#4", e.getMessage())),
                                HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler(DataIntegrityViolationException.class)
        public ResponseEntity<ResponseShape> catchDataIntegrityViolationException(DataIntegrityViolationException e) {
                return new ResponseEntity<>(
                                new ResponseShape(new ErrorDTO("#5", e.getMessage())),
                                HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler(InvalidRentingToolIdException.class)
        public ResponseEntity<ResponseShape> catchInvalidRentingToolIdException(InvalidRentingToolIdException e) {
                return new ResponseEntity<>(
                                new ResponseShape(new ErrorDTO("#6", e.getMessage())),
                                HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler(InvalidRentingToolQuantityException.class)
        public ResponseEntity<ResponseShape> catchInvalidRentingToolQuantityException(InvalidRentingToolQuantityException e) {
                return new ResponseEntity<>(
                                new ResponseShape(new ErrorDTO("#7", e.getMessage())),
                                HttpStatus.BAD_REQUEST);
        }


        @ExceptionHandler(UnkownGlobalException.class)
        public ResponseEntity<ResponseShape> catchUnkownGlobalException(UnkownGlobalException e) {
                return new ResponseEntity<>(
                                new ResponseShape(new ErrorDTO("#8", e.getMessage())),
                                HttpStatus.INTERNAL_SERVER_ERROR);
        }

        @ExceptionHandler(UserHasAnExistedContract.class)
        public ResponseEntity<ResponseShape> catchUserHasAnExistedContract(UserHasAnExistedContract e) {
                return new ResponseEntity<>(
                                new ResponseShape(new ErrorDTO("#9", e.getMessage())),
                                HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler(MissedContractException.class)
        public ResponseEntity<ResponseShape> catchMissedContractException(MissedContractException e) {
                return new ResponseEntity<>(
                                new ResponseShape(new ErrorDTO("#10", e.getMessage())),
                                HttpStatus.BAD_REQUEST);
        }

}
