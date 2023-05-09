package com.aravindcz.bankrestapi.exceptions;

import com.aravindcz.bankrestapi.models.dtos.ResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


/**
 * CentralizedExceptionHandler - used to handle all the exceptions that are thrown in the spring boot application , all the methods
 * return response entity following the uniform response format
 * @author Aravind C
 */

@ControllerAdvice
@Slf4j
public class CentralizedExceptionHandler {


    @ExceptionHandler({EmailAlreadyRegisteredException.class})
    public ResponseEntity handleEmailAlreadyRegisteredException(){

        log.error("Email already registered exception occured for user");

        ResponseDTO responseDTO = new ResponseDTO(false,409,"Email address provided is already registered with an account",null);

        ResponseEntity responseEntity = new ResponseEntity(responseDTO, HttpStatus.CONFLICT);

        return responseEntity;
    }


    @ExceptionHandler({CustomerDetailsAlreadyAddedException.class})
    public ResponseEntity handleCustomerDetailsAlreadyAddedException(){

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.error("Customer details already added exception occured for user"+userDetails.getUsername());

        ResponseDTO responseDTO = new ResponseDTO(false,409,"Customer details for this id is already added",null);

        ResponseEntity responseEntity = new ResponseEntity(responseDTO, HttpStatus.CONFLICT);

        return responseEntity;
    }

    @ExceptionHandler({UnauthorizedCustomerException.class})
    public ResponseEntity handleUnauthorizedCustomerException(){

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.error("Unauthorized customer  exception occured for user"+userDetails.getUsername());

        ResponseDTO responseDTO = new ResponseDTO(false,403,"Customer is not authorized to perform this action on the resource",null);

        ResponseEntity responseEntity = new ResponseEntity(responseDTO, HttpStatus.FORBIDDEN);

        return responseEntity;
    }

    @ExceptionHandler({CustomerNotFoundException.class})
    public ResponseEntity handleCustomerNotFoundException(){

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.error("Customer not  found exception occured for user"+userDetails.getUsername());

        ResponseDTO responseDTO = new ResponseDTO(false,404,"There are no customers in the database with this id",null);

        ResponseEntity responseEntity = new ResponseEntity(responseDTO, HttpStatus.NOT_FOUND);

        return responseEntity;
    }

    @ExceptionHandler({OfferingDetailsAlreadyAddedException.class})
    public ResponseEntity handleOfferingDetailsAlreadyAddedException(){

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.error("Offering details already added exception occured for user"+userDetails.getUsername());

        ResponseDTO responseDTO = new ResponseDTO(false,409,"Offering details for this id is already added",null);

        ResponseEntity responseEntity = new ResponseEntity(responseDTO, HttpStatus.CONFLICT);

        return responseEntity;
    }

    @ExceptionHandler({OfferingNotFoundException.class})
    public ResponseEntity handleOfferingNotFoundException(){

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.error("Offering details not found exception occured for user"+userDetails.getUsername());

        ResponseDTO responseDTO = new ResponseDTO(false,404,"There are no offerings in the database for this customer",null);

        ResponseEntity responseEntity = new ResponseEntity(responseDTO, HttpStatus.NOT_FOUND);

        return responseEntity;
    }

    @ExceptionHandler({LockerAlreadyPresentException.class})
    public ResponseEntity handleLockerAlreadyPresentException(){

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.error("Locker already present exception occured for user"+userDetails.getUsername());

        ResponseDTO responseDTO = new ResponseDTO(false,409,"Locker with same number already present . Please choose another number",null);

        ResponseEntity responseEntity = new ResponseEntity(responseDTO, HttpStatus.CONFLICT);

        return responseEntity;
    }

    @ExceptionHandler({LockerNotFoundException.class})
    public ResponseEntity handleLockerNotFoundException(){

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.error("Locker not found exception occured for user"+userDetails.getUsername());

        ResponseDTO responseDTO = new ResponseDTO(false,404,"There are no lockers in the database for this customer with this nunmber",null);

        ResponseEntity responseEntity = new ResponseEntity(responseDTO, HttpStatus.NOT_FOUND);

        return responseEntity;
    }



    @ExceptionHandler({UnauthorizedEmployeeException.class})
    public ResponseEntity handleUnauthorizedEmployeeException(){

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.error("Unauthorized employee exception occured for user"+userDetails.getUsername());

        ResponseDTO responseDTO = new ResponseDTO(false,403,"Employee is not authorized to perform this action on the resource",null);

        ResponseEntity responseEntity = new ResponseEntity(responseDTO, HttpStatus.UNAUTHORIZED);

        return responseEntity;
    }

    @ExceptionHandler({EmployeeDetailsAlreadyAddedException.class})
    public ResponseEntity handleEmployeeDetailsAlreadyAddedException(){

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.error("Employee details already added exception occured for user"+userDetails.getUsername());

        ResponseDTO responseDTO = new ResponseDTO(false,409,"Employee details for this id is already added",null);

        ResponseEntity responseEntity = new ResponseEntity(responseDTO, HttpStatus.CONFLICT);

        return responseEntity;
    }

    @ExceptionHandler({EmployeeNotFoundException.class})
    public ResponseEntity handleEmployeeNotFoundException(){

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.error("Employee not found  exception occured for user"+userDetails.getUsername());

        ResponseDTO responseDTO = new ResponseDTO(false,404,"There are no employees in the database with this id",null);

        ResponseEntity responseEntity = new ResponseEntity(responseDTO, HttpStatus.NOT_FOUND);

        return responseEntity;
    }



    @ExceptionHandler({LoanAlreadyPresentException.class})
    public ResponseEntity handleLoanAlreadyPresentException(){

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.error("Loan already present exception occured for user"+userDetails.getUsername());

        ResponseDTO responseDTO = new ResponseDTO(false,409,"Loan with same number already present . Please choose another number",null);

        ResponseEntity responseEntity = new ResponseEntity(responseDTO, HttpStatus.CONFLICT);

        return responseEntity;
    }


    @ExceptionHandler({LoanNotFoundException.class})
    public ResponseEntity handleLoanNotFoundException(){

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.error("Loan not found exception occured for user"+userDetails.getUsername());

        ResponseDTO responseDTO = new ResponseDTO(false,404,"There are no loans in the database for this customer with this nunmber",null);

        ResponseEntity responseEntity = new ResponseEntity(responseDTO, HttpStatus.NOT_FOUND);

        return responseEntity;
    }

    @ExceptionHandler({InconsistentDetailsException.class})
    public ResponseEntity handleInconsistentDetailsException(){

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.error("Inconsistent details exception occured for user"+userDetails.getUsername());

        ResponseDTO responseDTO = new ResponseDTO(false,400,"Inconsistent details found in the request for the given request",null);

        ResponseEntity responseEntity = new ResponseEntity(responseDTO, HttpStatus.BAD_REQUEST);

        return responseEntity;
    }

    @ExceptionHandler({InvalidEmailAddressException.class})
    public ResponseEntity handleInvalidEmailAddressException(){


        log.error("Invalid email address exception occured for user");

        ResponseDTO responseDTO = new ResponseDTO(false,400,"Email address is in invalid format",null);

        ResponseEntity responseEntity = new ResponseEntity(responseDTO, HttpStatus.BAD_REQUEST);

        return responseEntity;
    }


    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity handleMethodArgumentNotValid(){


        log.error("Method Argument Not Valid ");

        ResponseDTO responseDTO = new ResponseDTO(false,400,"Method arguments are not valid",null);

        ResponseEntity responseEntity = new ResponseEntity(responseDTO, HttpStatus.BAD_REQUEST);

        return responseEntity;
    }


    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity handleAccessDeniedException(){



        log.error("Access denied exception occured for user");

        ResponseDTO responseDTO = new ResponseDTO(false,403,"User is not authorized to make this request",null);

        ResponseEntity responseEntity = new ResponseEntity(responseDTO, HttpStatus.FORBIDDEN);

        return responseEntity;
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity handleException(Exception e){

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.error("Some kind of exception occured for user"+userDetails.getUsername());
        log.error(e.getMessage());

        ResponseDTO responseDTO = new ResponseDTO(false,500,"Server side error",null);

        ResponseEntity responseEntity = new ResponseEntity(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);

        return responseEntity;
    }
}

