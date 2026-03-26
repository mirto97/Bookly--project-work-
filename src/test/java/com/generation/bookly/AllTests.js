// Import dei vari file di test
const { runAll: runOkService } = require('./OkServiceTestsApi');
const { runAll: runErrorService } = require('./ErrorServiceTestsApi');
const { runAll: runOkActivity } = require('./OkActivityTestsApi');
const { runAll: runErrorActivity } = require('./ErrorActivityTestsApi');
const { runAll: runOkUser } = require('./OkUserTestsApi');
const { runAll: runErrorUser } = require('./ErrorUserTestsApi');
const { runAll: runOkBooking } = require('./OkBookingTestsApi');
const { runAll: runErrorBooking } = require('./ErrorBookingTestsApi');
const { runAll: runOkReview } = require('./OkReviewTestsApi');
const { runAll: runErrorReview } = require('./ErrorReviewTestsApi');
const { runAll: runOkReporting } = require('./OkReportingTestsApi');
const { runAll: runErrorReporting } = require('./ErrorReportingTestsApi');

async function runAllTests() {
    console.log('==== Run OK Service tests ====>');
    await runOkService();

    console.log('==== Run Error Service tests ====>');
    await runErrorService();

    console.log('==== Run OK Activity tests ====>');
    await runOkActivity();

    console.log('==== Run Error Activity tests ====>');
    await runErrorActivity();

    console.log('==== Run OK User tests ====>');
    await runOkUser();

    console.log('==== Run Error User tests ====>');
    await runErrorUser();

    console.log('==== Run OK Booking tests ====>');
    await runOkBooking();

    console.log('==== Run Error Booking tests ====>');
    await runErrorBooking();

    console.log('==== Run OK Review tests ====>');
    await runOkReview();

    console.log('==== Run Error Review tests ====>');
    await runErrorReview();

    console.log('==== Run OK Reporting tests ====>');
    await runOkReporting();

    console.log('==== Run Error Reporting tests ====>');
    await runErrorReporting();

    console.log('==== All tests completed ====>');
}

if (typeof require !== 'undefined' && require.main === module) {
    runAllTests().catch(err => {
        console.error('All tests runner failed', err);
        process.exit(1);
    });
}

module.exports = { runAllTests };
