package com.ofg.loan.application.frauddetection

import groovy.text.SimpleTemplateEngine
import groovy.transform.PackageScope
import groovy.transform.TypeChecked

@TypeChecked
@PackageScope
class FraudDetectionJsonBuilder {

    String buildFraudDetectionJson(Long clientId, BigDecimal amount) {
        return new SimpleTemplateEngine().createTemplate(JSON_RESPONSE_TEMPLATE)
                .make([clientId     : clientId,
                       amount: amount]
                        ).toString()
    }


    private static final String JSON_RESPONSE_TEMPLATE = '''
                {
                    "client_id" : $clientId,
                    "amount" : "$amount"
                }
                '''

}
