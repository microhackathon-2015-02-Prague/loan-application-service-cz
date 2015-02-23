package com.ofg.twitter.place.extractor

import com.codahale.metrics.Meter
import com.codahale.metrics.MetricRegistry
import com.ofg.infrastructure.web.resttemplate.fluent.ServiceRestClient
import com.ofg.loan.application.frauddetection.FraudDetectionClient
import com.ofg.loan.application.frauddetection.FraudDetectionJsonBuilder
import com.ofg.loan.application.frauddetection.LoanApplicationPropagatingWorker
import com.ofg.loan.application.frauddetection.PropagationWorker
import com.ofg.twitter.place.extractor.metrics.ExtractorMetricsConfiguration
import com.ofg.twitter.place.extractor.metrics.MatchProbabilityMetrics
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import(ExtractorMetricsConfiguration)
class PlaceExtractorConfiguration {

    @Bean
    CityFinder cityFinder(ServiceRestClient serviceRestClient, @Value('${city.finding.service.url:http://api.openweathermap.org/data/2.5/weather}') String cityFindingServiceUrl) {
        return new CityFinder(new WeatherClient(serviceRestClient, cityFindingServiceUrl))
    }
    
    @Bean
    PlacesExtractor placesExtractor(CityFinder cityFinder, MatchProbabilityMetrics matchProbabilityMetrics, MetricRegistry metricRegistry) {
        Meter analyzedTweetsMeter = metricRegistry.meter('twitter.places.analyzed.tweets')
        List<PlaceExtractor> placeExtractors = [ new PlaceSectionExtractor(matchProbabilityMetrics),
                                                 new CoordinatesPlaceExtractor(cityFinder, matchProbabilityMetrics) ]
        return new PlacesExtractor(placeExtractors, analyzedTweetsMeter)
    }
    
    @Bean
    FraudDetectionJsonBuilder placesJsonBuilder() {
        return new FraudDetectionJsonBuilder()
    }
    
    @Bean
    PropagationWorker propagationWorker(PlacesExtractor placesExtractor,
                                        FraudDetectionJsonBuilder placesJsonBuilder,
                                        FraudDetectionClient colleratorClient) {
        return new LoanApplicationPropagatingWorker(placesExtractor, placesJsonBuilder, colleratorClient)
    }

    @Bean
    FraudDetectionClient colleratorClient(ServiceRestClient serviceRestClient) {
        return new FraudDetectionClient(serviceRestClient)
    }

}
