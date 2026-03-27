// Test di tutte le api che devono restituire errori (status 400, 500)

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

const BASE = "http://localhost:8080/bookly/api/diary";
const setupDate = new Date(Date.now() + 172800000).toISOString().slice(0, 10);
let setupDiaryId = null;

function createSetupDiarySlot() {
    const body = {
        date: setupDate,
        startHour: "15:00:00",
        endHour: "16:00:00",
        activityId: 1,
        available: "AVAILABLE"
    };

    return fetch(BASE, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(body)
    })
    .then(response => {
        if (response.status !== 201) {
            throw new Error("Setup failed: expected 201 got " + response.status);
        }
        return response.json();
    })
    .then(json => {
        setupDiaryId = json.id;
    });
}

function cleanupSetupDiarySlot() {
    if (!setupDiaryId) {
        return Promise.resolve();
    }

    return fetch(`${BASE}/${setupDiaryId}`, {
        method: "DELETE",
        headers: { "Content-Type": "application/json" }
    })
    .catch(() => Promise.resolve());
}

let tests = [];

// 1. Patch with invalid available enum
tests.push(new Test(
    "Patch diary slot with invalid available returns 400",
    () => `${BASE}/${setupDiaryId}`,
    "PATCH",
    { available: "INVALID_STATUS" },
    400,
    null
));

// 2. Delete non-existent diary slot
tests.push(new Test(
    "Delete non-existent diary slot returns 500",
    `${BASE}/99999999`,
    "DELETE",
    null,
    500,
    null
));

function runAll() {
    return createSetupDiarySlot()
        .then(() => tests.reduce(
            (promise, test) => promise.then(() => test.run()),
            Promise.resolve()
        ))
        .finally(() => cleanupSetupDiarySlot());
}

if (typeof require !== 'undefined' && require.main === module) {
    runAll();
}

module.exports = { runAll };
