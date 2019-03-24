package com.vicctorb.wgtesttask36.controller;

import com.vicctorb.wgtesttask36.repository.CatsRepository;
import com.vicctorb.wgtesttask36.validation.NewCatRequest;
import com.vicctorb.wgtesttask36.validation.OffsetBasedPageRequest;
import com.vicctorb.wgtesttask36.validation.SortingAndPagingRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
public class CatsController {

    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        return new MethodValidationPostProcessor();
    }

    @Autowired
    CatsRepository catsRepository;

    //this controls the request count per minute (setted to 600 requests per minute) for Task 6
    @Autowired
    RequestsCountController requestsCountController;


    @RequestMapping(value = "/ping", method = RequestMethod.GET)
    public ResponseEntity ping() {
        requestsCountController.increase();
        if (RequestsCountController.currentRequestsCount > 600) {
            return new ResponseEntity(HttpStatus.TOO_MANY_REQUESTS);
        }
        return ResponseEntity.ok().body("Cats Service. Version 0.1");
    }

    @RequestMapping(value = "/cats", method = RequestMethod.GET)
    public ResponseEntity getCats(@Valid @ModelAttribute SortingAndPagingRequest modelRequest, BindingResult result) {

        requestsCountController.increase();
        if (RequestsCountController.currentRequestsCount > 600) {
            return new ResponseEntity(HttpStatus.TOO_MANY_REQUESTS);
        }

        //validating request params
        //and if there was an errors - than return bad request and the reason
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getFieldError().getDefaultMessage());
        }

        //optional request params
        Optional<String> attribute = Optional.ofNullable(modelRequest.getAttribute());
        Optional<String> order = Optional.ofNullable(modelRequest.getOrder());
        Optional<Integer> offset = Optional.ofNullable(modelRequest.getOffset());
        Optional<Integer> limit = Optional.ofNullable(modelRequest.getLimit());

        OffsetBasedPageRequest pageable = new OffsetBasedPageRequest();
        if (attribute.isPresent()) {
            if (order.isPresent()) {
                switch (order.get()) {
                    case "asc": {
                        pageable.setSort(new Sort(Sort.Direction.ASC, attribute.get()));
                        break;
                    }
                    case "desc": {
                        pageable.setSort(new Sort(Sort.Direction.DESC, attribute.get()));
                        break;
                    }
                }

            } else {
                // if there is no order param but attribute is setted
                pageable.setSort(new Sort(Sort.Direction.ASC, attribute.get()));
            }

        }
        offset.ifPresent(pageable::setOffset); //if offset is present -> setting it
        limit.ifPresent(pageable::setLimit);   //if limit is present -> setting it
        return ResponseEntity.ok().body(catsRepository.findAll(pageable).getContent());
    }

    @PostMapping(value = "/cat", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addCat(@Valid @RequestBody NewCatRequest newCatRequest, BindingResult bindingResult) {
        int rowsAffected;
        requestsCountController.increase();
        if (RequestsCountController.currentRequestsCount > 600) {
            return new ResponseEntity(HttpStatus.TOO_MANY_REQUESTS);
        }


        //if there were errors
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getFieldError().getDefaultMessage()); //than returns bad request status and the reason
        }

        try{ //trying to save cat to repository
            rowsAffected = catsRepository.saveCat(newCatRequest.getName(), newCatRequest.getColor(), newCatRequest.getTail_length(), newCatRequest.getWhiskers_length());
        }catch (DataIntegrityViolationException e){
            return ResponseEntity.badRequest().body("There was an unexpected error while trying to create cat for you because: " + e.getMessage());
        }


        if (rowsAffected == 1) { //if succesfully added
            return ResponseEntity.ok().body(newCatRequest);
        } else {
            return ResponseEntity.badRequest().body("There was an unexpected error while trying to create cat for you :(");
        }
    }

}
