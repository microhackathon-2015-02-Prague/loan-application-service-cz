package com.ofg.base

import com.jayway.restassured.module.mockmvc.RestAssuredMockMvc
import com.ofg.loan.application.LoanApplicationController
import com.ofg.loan.application.frauddetection.PropagationWorker
import spock.lang.Specification

abstract class BaseMockMvcSpec extends Specification {

    def setup() {
        RestAssuredMockMvc.standaloneSetup(new LoanApplicationController(createAndStubPropagationWorker()))
    }

    private PropagationWorker createAndStubPropagationWorker() {
        return Mock(PropagationWorker)
    }
}