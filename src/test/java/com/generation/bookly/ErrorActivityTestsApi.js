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
                // no body expected per API / 404 test
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

const BASE = "http://localhost:8080/bookly/api/activities";

let tests = [];

// 1. Update status with invalid id (404)
tests.push(new Test(
    "Update status of non-existent activity returns 404",
    `${BASE}/999/status?status=APPROVED`,
    "PATCH",
    null,
    404,
    null
));

// 2. Update status with invalid status (assume it causes 400)
tests.push(new Test(
    "Update status with invalid status returns 400",
    `${BASE}/1/status?status=INVALID`,
    "PATCH",
    null,
    400,
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
            setTimeout(runNext, 500);
        }
        runNext();
    });
}

if (typeof require !== 'undefined' && require.main === module) {
    runAll();
}

module.exports = { runAll };