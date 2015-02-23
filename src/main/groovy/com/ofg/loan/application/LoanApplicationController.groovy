package com.ofg.loan.application

import com.ofg.loan.application.frauddetection.PropagationWorker
import com.wordnik.swagger.annotations.Api
import com.wordnik.swagger.annotations.ApiOperation
import groovy.transform.TypeChecked
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

import javax.validation.constraints.NotNull
import java.util.concurrent.Callable

import static com.ofg.config.Versions.LOAN_APPLICATION_JSON_VERSION_1
import static org.springframework.web.bind.annotation.RequestMethod.PUT

@Slf4j
@RestController
@RequestMapping('/api/loanApplication')
@TypeChecked
@Api(value = "clientId", description = "Generates application for given client and amount and process")
class LoanApplicationController {

    private final PropagationWorker propagationWorker

    @Autowired
    LoanApplicationController(PropagationWorker propagationWorker) {
        this.propagationWorker = propagationWorker
    }

    @RequestMapping(
            value = '{clientId}',
            method = PUT,
            consumes = LOAN_APPLICATION_JSON_VERSION_1,
            produces = LOAN_APPLICATION_JSON_VERSION_1)
    @ApiOperation(value = "Async generation of loan application and propagating of further process of decision making",
            notes = "This will asynchronously call creating of loan application and her propagation to fraud detection and decision making")
    Callable<Void> createApplication(@PathVariable @NotNull long clientId, @RequestBody @NotNull BigDecimal amount) {
        return {
            propagationWorker.createApplicationAndPropagate(clientId, amount)
        }
    }

}
