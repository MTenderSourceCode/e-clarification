package com.procurement.clarification.service;

import com.procurement.clarification.model.dto.EnquiryPeriodDto;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;

@Service
public interface EnquiryPeriodService {

    EnquiryPeriodDto calculateAndSaveEnquiryPeriod(String cpId,
                                                   String country,
                                                   String pmd,
                                                   LocalDateTime startDate,
                                                   LocalDateTime endDate,
                                                   String owner);
}
