var tessel = require('tessel');
var ble = require('ble-ble113a').use(tessel.port['B']);

var interval;

// Connect to BLE
ble.on('ready', function(err) {
  if (err) return console.log(err);
  console.log('start advertising');
  ble.startAdvertising();
});

// Once a master connects
ble.on('connect', function() {
  console.log('master connected');
  var value = 0;
  interval = setInterval(function() {
    ble.writeLocalValue(0, new Buffer('interval #' + value++));
  }, 100);
});

// Once a master disconnects
ble.on('disconnect', function() {
  clearInterval(interval);
  console.log('master disconnect, start advertising');
  ble.startAdvertising();
});