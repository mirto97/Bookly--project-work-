// Test di tutte le api che devono restituire errori (status 400, 404, 500)

class Test {
    /*
    *       REQUEST
    *       api url             => url della api da testare
    *       verb                => verbo della api
    *       body della request  => corpo della request
    *
    *       RESPONSE
    *       status              => codice previsto
    *       verify              => funzione di verifica da applicare al corpo della response
    *                              callback di verifica
    *
    */
    constructor(testname, apiurl, verb, body, status, verify){
        this.testname = testname;
        this.apiurl = apiurl;
        this.verb = verb;
        this.body = body;
        this.status = status;
        this.verify = verify;
    }

    run(){
        console.log("Running "+this.testname);

        let options = {
            method: this.verb,
            headers: { 'Content-Type': 'application/json'}
        };

        // le request get non hanno body
        if (this.body && this.verb !== 'GET') {
            options.body = JSON.stringify(this.body);
        }

        fetch(this.apiurl, options)
        .then(response=>
        {
            if(response.status!==this.status)
                throw new Error("Status mismatch, expected "+this.status+" got "+response.status+" ("+this.apiurl+")");

            if (response.status === 204 || response.status === 404) {
                return Promise.resolve(null);
            }

            const contentType = response.headers.get("content-type") || "";
            if (!contentType.includes("application/json")) {
                return Promise.resolve(null);
            }

            return response.json();
        })
        .then(json=>
        {
            if(!this.verify || this.verify(json))
                console.log("PASSED - "+this.testname);
            else
                console.log("NOT PASSED - "+this.testname, json);
        })
        .catch(err=>
        {
            console.log("NOT PASSED - "+this.testname, err);
        });
    }
}

const BASE = "http://localhost:8080/bookly/api/reports";

let tests = [];

// 1. Invalid report status filter
tests.push(new Test(
    "Get reports with invalid status returns 500",
    `${BASE}?status=INVALID_STATUS`,
    "GET",
    null,
    500,
    null
));

// 2. Get missing report by id
tests.push(new Test(
    "Get missing report by id returns 500",
    `${BASE}/999999`,
    "GET",
    null,
    500,
    null
));

function runAll() {
    return new Promise((resolve) => {
        let index = 0;
        function runNext() {
            if (index >= tests.length) {
                resolve();
                return;
            }
            tests[index].run();
            index++;
            setTimeout(runNext, 600);
        }
        runNext();
    });
}

if (typeof require !== 'undefined' && require.main === module) {
    runAll();
}

module.exports = { runAll };
