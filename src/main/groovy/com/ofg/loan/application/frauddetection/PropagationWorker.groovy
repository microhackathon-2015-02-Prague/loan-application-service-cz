package com.ofg.loan.application.frauddetection

interface PropagationWorker {
    void createApplicationAndPropagate(Long clientId, BigDecimal amount)
}