package com.procurement.clarification.controller;

import com.procurement.clarification.exception.ValidationException;
import com.procurement.clarification.model.dto.CreateEnquiryDto;
import com.procurement.clarification.model.dto.CreateEnquiryParams;
import com.procurement.clarification.model.dto.UpdateEnquiryDto;
import com.procurement.clarification.model.dto.UpdateEnquiryParams;
import com.procurement.clarification.model.dto.bpe.ResponseDto;
import com.procurement.clarification.service.EnquiryService;
import java.time.LocalDateTime;
import javax.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/enquiry")
public class EnquiryController {

    private EnquiryService enquiryService;

    public EnquiryController(final EnquiryService enquiryService) {
        this.enquiryService = enquiryService;
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createEnquiry(@RequestParam(value = "cpid") final String cpid,
                                                     @RequestParam(value = "stage") final String stage,
                                                     @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                     @RequestParam(value = "date") final LocalDateTime date,
                                                     @RequestParam(value = "owner") final String owner,
                                                     @Valid @RequestBody final CreateEnquiryDto dataDto,
                                                     final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }
        final ResponseDto responseDto = enquiryService.saveEnquiry(
                new CreateEnquiryParams(cpid, stage, date, owner, dataDto)
        );
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @PostMapping("/update")
    public ResponseEntity<ResponseDto> updateEnquiry(@RequestParam(value = "cpid") final String cpid,
                                                     @RequestParam(value = "token") final String token,
                                                     @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                     @RequestParam(value = "date") final LocalDateTime date,
                                                     @RequestParam(value = "owner") final String owner,
                                                     @Valid @RequestBody final UpdateEnquiryDto dataDto,
                                                     final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        final ResponseDto responseDto = enquiryService.updateEnquiry(
                new UpdateEnquiryParams(token, cpid, date, owner, dataDto)
        );

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @GetMapping("/checkEnquiries")
    public ResponseEntity<ResponseDto> checkEnquiries(@RequestParam(value = "cpid") final String cpid,
                                                      @RequestParam(value = "stage") final String stage) {

        return new ResponseEntity<>(enquiryService.checkEnquiries(cpid, stage), HttpStatus.OK);
    }
}
