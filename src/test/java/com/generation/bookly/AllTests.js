// Import dei vari file di test
const { runAll: runOkService } = require('./OkServiceTestsApi');
const { runAll: runErrorService } = require('./ErrorServiceTestsApi');
const { runAll: runOkActivity } = require('./OkActivityTestsApi');
const { runAll: runErrorActivity } = require('./ErrorActivityTestsApi');

async function runAllTests() {
    console.log('==== Run OK Service tests ====>');
    await runOkService();

    console.log('==== Run Error Service tests ====>');
    await runErrorService();

    console.log('==== Run OK Activity tests ====>');
    await runOkActivity();

    console.log('==== Run Error Activity tests ====>');
    await runErrorActivity();

    console.log('==== All tests completed ====>');
}

if (typeof require !== 'undefined' && require.main === module) {
    runAllTests().catch(err => {
        console.error('All tests runner failed', err);
        process.exit(1);
    });
}

module.exports = { runAllTests };
