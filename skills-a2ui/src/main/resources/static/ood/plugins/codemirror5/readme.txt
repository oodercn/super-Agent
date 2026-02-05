codemirror current version: 5.37.0

1) modify: lib\codemirror.js <=> lib\o-codemirror.js 
2) modify: mode\javascript\javascript.js <=> mode\javascript\o-javascript.js
3) add: addon\lint\csslint.js ; addon\lint\jslint.js ; addon\lint\jsonlint.js ; addon\lint\htmlhint.js
4) merge: codemirror.css => default.css

5) sublime
var mac = keyMap['default'] == keyMap.macDefault;