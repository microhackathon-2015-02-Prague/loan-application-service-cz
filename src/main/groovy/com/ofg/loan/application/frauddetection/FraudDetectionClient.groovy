package com.ofg.loan.application.frauddetection

import com.ofg.infrastructure.web.resttemplate.fluent.ServiceRestClient
import com.ofg.config.Collaborators
import groovy.transform.CompileStatic

@CompileStatic
class FraudDetectionClient {

    private final ServiceRestClient serviceRestClient

    FraudDetectionClient(ServiceRestClient serviceRestClient) {
        this.serviceRestClient = serviceRestClient
    }

    void process(Long applicationId, String jsonToPropagate) {
        serviceRestClient.forService(Collaborators.FRAUD_DETECTION_SERVICE_NAME)
                .post()
                .onUrlFromTemplate("/{applicationId}").withVariables(applicationId)
                .body(jsonToPropagate)
                .anObject()
                .ofType(String)
    }

}
