io.coderate.accurest.dsl.GroovyDsl.make {
    request {
        method 'POST'
        url $(client(regex('/api/[0-9]{2}')), server('/api/loanApplication/12')) 
        headers {
            header 'Content-Type': 'application/vnd.com.ofg.com.ofg.loan-application-service-cz.v1+json'
        }
        body '''\
    [{
        "amount": "150.00"
    }]
'''
    }
    response {
        status 200
    }
}