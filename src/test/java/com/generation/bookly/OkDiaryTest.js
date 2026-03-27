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
const slotDate = new Date(Date.now() + 86400000).toISOString().slice(0, 10);
let createdDiaryId = null;

let tests = [];

// 1. Create a new diary slot
tests.push(new Test(
    "Create new diary slot",
    BASE,
    "POST",
    () => ({
        date: slotDate,
        startHour: "09:00:00",
        endHour: "10:00:00",
        activityId: 1,
        available: "AVAILABLE"
    }),
    201,
    json => {
        if (!(json && json.id)) {
            return false;
        }
        createdDiaryId = json.id;
        return json.activityId === 1;
    }
));

// 2. Get available slots by activity and date
tests.push(new Test(
    "Get available slots by activity 1 and date",
    () => `${BASE}/available/1?date=${slotDate}`,
    "GET",
    null,
    200,
    json => Array.isArray(json) && json.some(slot => slot.id === createdDiaryId)
));

// 3. Update created diary slot
tests.push(new Test(
    "Update created diary slot",
    () => `${BASE}/${createdDiaryId}`,
    "PATCH",
    {
        startHour: "10:00:00",
        endHour: "11:00:00",
        available: "AVAILABLE"
    },
    200,
    json => json && json.id === createdDiaryId
));

// 4. Delete created diary slot
tests.push(new Test(
    "Delete created diary slot",
    () => `${BASE}/${createdDiaryId}`,
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
