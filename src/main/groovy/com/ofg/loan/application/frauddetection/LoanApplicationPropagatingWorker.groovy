package com.ofg.loan.application.frauddetection

import com.ofg.twitter.place.extractor.PlacesExtractor
import groovy.transform.TypeChecked
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired

@TypeChecked
@Slf4j
class LoanApplicationPropagatingWorker implements PropagationWorker {
    
    private final PlacesExtractor placesExtractor
    private final FraudDetectionJsonBuilder fraudDetectionJsonBuilder
    private final FraudDetectionClient fraudDetectionClient

    @Autowired
    LoanApplicationPropagatingWorker(PlacesExtractor placesExtractor, 
                           FraudDetectionJsonBuilder fraudDetectionJsonBuilder,
                           FraudDetectionClient fraudDetectionClient) {
        this.placesExtractor = placesExtractor
        this.fraudDetectionJsonBuilder = fraudDetectionJsonBuilder
        this.fraudDetectionClient = fraudDetectionClient
    }

    @Override
    void createApplicationAndPropagate(Long clientId, BigDecimal amount) {
//        Map<String, Optional<Place>> extractedPlaces = placesExtractor.extractPlacesFrom(tweets)
        Long applicationId = 1L
        String jsonToPropagate = fraudDetectionJsonBuilder.buildFraudDetectionJson(clientId, amount)
        fraudDetectionClient.process(applicationId, jsonToPropagate)
        log.debug("Sent json [$jsonToPropagate] to collerator")
    }
}
