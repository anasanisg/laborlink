package com.laborlink.tool.exceptions;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.laborlink.tool.dtos.ErrorDTO;
import com.laborlink.tool.dtos.ResponseShape;

@ControllerAdvice
public class ToolExceptionsHandler {

        @ExceptionHandler(ToolNameIsExistedException.class)
        public ResponseEntity<ResponseShape> catchToolNameIsExistedException(ToolNameIsExistedException e) {
                return new ResponseEntity<>(
                                new ResponseShape(new ErrorDTO("#1", e.getMessage())),
                                HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler(NotFoundToolIdException.class)
        public ResponseEntity<ResponseShape> catchNotFoundToolIdException(NotFoundToolIdException e) {
                return new ResponseEntity<>(
                                new ResponseShape(new ErrorDTO("#2", e.getMessage())),
                                HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler(ToolInvalidUpdateException.class)
        public ResponseEntity<ResponseShape> catchToolInvalidUpdateException(ToolInvalidUpdateException e) {
                return new ResponseEntity<>(
                                new ResponseShape(new ErrorDTO("#3", e.getMessage())),
                                HttpStatus.BAD_REQUEST);
        }

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



        @ExceptionHandler(UnkownGlobalException.class)
        public ResponseEntity<ResponseShape> catchUnkownGlobalException(UnkownGlobalException e) {
                return new ResponseEntity<>(
                                new ResponseShape(new ErrorDTO("#8", e.getMessage())),
                                HttpStatus.INTERNAL_SERVER_ERROR);
        }

}
