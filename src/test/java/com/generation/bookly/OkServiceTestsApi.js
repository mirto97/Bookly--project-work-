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

        return fetch(resolvedApiUrl, options)
        .then(response=>
        {
            if(response.status!==this.status)
                throw new Error("Status mismatch, expected "+this.status+" got "+response.status+" ("+resolvedApiUrl+")");

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

const BASE = "http://localhost:8080/bookly/api/services";
let createdServiceId = null;

let tests = [];
// 1. Create a new service (requires activity payload, adjust activity.id se necessario)
const newServiceBody = {
    activity: { id: 1 },
    name: "Test Service API",
    description: "Servizio creato da test automatico",
    duration: 60,
    price: 100,
    notes: "Nessuna"
};

tests.push(new Test(
    "Create new service",
    BASE,
    "POST",
    newServiceBody,
    200,
    json => {
        if (!(json && json.id && json.name === "Test Service API")) {
            return false;
        }
        createdServiceId = json.id;
        return true;
    }
));
// 2. Get all services
tests.push(new Test(
    "Get all services",
    BASE,
    "GET",
    null,
    200,
    json => Array.isArray(json)
));

// 3. Get services by activity id (assume activity 1 exists)
tests.push(new Test(
    "Get services by activity 1",
    `${BASE}/activity/1`,
    "GET",
    null,
    200,
    json => Array.isArray(json)
));

// 4. Get created service by id
tests.push(new Test(
    "Get created service by id",
    () => `${BASE}/${createdServiceId}`,
    "GET",
    null,
    200,
    json => json && json.id === createdServiceId
));



// 5. Delete created service by id
tests.push(new Test(
    "Delete created service by id",
    () => `${BASE}/${createdServiceId}`,
    "DELETE",
    null,
    204,
    null
));

function runAll() {
    return tests.reduce(
        (promise, test) => promise.then(() => test.run()),
        Promise.resolve()
    );
}

if (typeof require !== 'undefined' && require.main === module) {
    runAll();
}

module.exports = { runAll };
