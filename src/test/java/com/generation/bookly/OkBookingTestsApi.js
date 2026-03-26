// Test di tutte le api che devono funzionare (status 200)

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

        const resolvedApiUrl = (typeof this.apiurl === "function") ? this.apiurl() : this.apiurl;
        const resolvedBody = (typeof this.body === "function") ? this.body() : this.body;

        let options = {
            method: this.verb,
            headers: { 'Content-Type': 'application/json'}
        };

        // le request get non hanno body
        if (resolvedBody && this.verb !== 'GET') {
            options.body = JSON.stringify(resolvedBody);
        }

        fetch(resolvedApiUrl, options)
        .then(response=>
        {
            if(response.status!==this.status)
                throw new Error("Status mismatch, expected "+this.status+" got "+response.status+" ("+resolvedApiUrl+")");

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

const BASE = "http://localhost:8080/bookly/api/bookings";

let tests = [];

// 1. Get all bookings
tests.push(new Test(
    "Get all bookings",
    BASE,
    "GET",
    null,
    200,
    json => Array.isArray(json)
));

// 2. Get bookings by user id
tests.push(new Test(
    "Get bookings by user id 1",
    `${BASE}/user/1`,
    "GET",
    null,
    200,
    json => Array.isArray(json)
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
