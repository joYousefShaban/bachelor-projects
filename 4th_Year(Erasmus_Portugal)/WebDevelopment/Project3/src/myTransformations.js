"use strict";
exports.__esModule = true;
exports.changeEmail = exports.deleteGeo = exports.renameShallowKey = void 0;
function renameShallowKey(_json, from, to) {
    _json.forEach(function (obj) {
        obj[to] = obj[from];
        delete obj[from];
    });
}
exports.renameShallowKey = renameShallowKey;
function deleteGeo(_json) {
    _json.forEach(function (obj) {
        delete obj['address']['geo'];
    });
}
exports.deleteGeo = deleteGeo;
function changeEmail(_json) {
    _json.forEach(function (obj) {
        obj['email'] = obj['email'].replace(/@.*$/, '@ualg.pt');
    });
}
exports.changeEmail = changeEmail;
