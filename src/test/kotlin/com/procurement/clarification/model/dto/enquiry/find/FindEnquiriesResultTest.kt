package com.procurement.clarification.model.dto.enquiry.find

import com.procurement.clarification.infrastructure.handler.v2.model.response.FindEnquiriesResult
import com.procurement.clarification.model.dto.AbstractDTOTestBase
import org.junit.Test

class FindEnquiriesResultTest : AbstractDTOTestBase<FindEnquiriesResult>(
    FindEnquiriesResult::class.java) {

    @Test
    fun fully() {
        testBindingAndMapping("json/dto/enquiry/find/find_enquiries_result_full.json")
    }

    @Test
    fun fully1() {
        testBindingAndMapping("json/dto/enquiry/find/find_enquiries_result_1.json")
    }

    @Test
    fun fully2() {
        testBindingAndMapping("json/dto/enquiry/find/find_enquiries_result_2.json")
    }
}
