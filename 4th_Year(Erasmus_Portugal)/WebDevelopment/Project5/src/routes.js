"use strict";
var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
var __generator = (this && this.__generator) || function (thisArg, body) {
    var _ = { label: 0, sent: function() { if (t[0] & 1) throw t[1]; return t[1]; }, trys: [], ops: [] }, f, y, t, g;
    return g = { next: verb(0), "throw": verb(1), "return": verb(2) }, typeof Symbol === "function" && (g[Symbol.iterator] = function() { return this; }), g;
    function verb(n) { return function (v) { return step([n, v]); }; }
    function step(op) {
        if (f) throw new TypeError("Generator is already executing.");
        while (_) try {
            if (f = 1, y && (t = op[0] & 2 ? y["return"] : op[0] ? y["throw"] || ((t = y["return"]) && t.call(y), 0) : y.next) && !(t = t.call(y, op[1])).done) return t;
            if (y = 0, t) op = [op[0] & 2, t.value];
            switch (op[0]) {
                case 0: case 1: t = op; break;
                case 4: _.label++; return { value: op[1], done: false };
                case 5: _.label++; y = op[1]; op = [0]; continue;
                case 7: op = _.ops.pop(); _.trys.pop(); continue;
                default:
                    if (!(t = _.trys, t = t.length > 0 && t[t.length - 1]) && (op[0] === 6 || op[0] === 2)) { _ = 0; continue; }
                    if (op[0] === 3 && (!t || (op[1] > t[0] && op[1] < t[3]))) { _.label = op[1]; break; }
                    if (op[0] === 6 && _.label < t[1]) { _.label = t[1]; t = op; break; }
                    if (t && _.label < t[2]) { _.label = t[2]; _.ops.push(op); break; }
                    if (t[2]) _.ops.pop();
                    _.trys.pop(); continue;
            }
            op = body.call(thisArg, _);
        } catch (e) { op = [6, e]; y = 0; } finally { f = t = 0; }
        if (op[0] & 5) throw op[1]; return { value: op[0] ? op[1] : void 0, done: true };
    }
};
exports.__esModule = true;
exports.initializeRoutes = exports.initializeMiddlewares = void 0;
var usersService_1 = require("./externalAPI/usersService");
var express = require("express");
var node_cache = require("node-cache");
var compression = require("compression");
function initializeMiddlewares(app, shouldCache, shouldCompress) {
    if (shouldCache === void 0) { shouldCache = true; }
    if (shouldCompress === void 0) { shouldCompress = true; }
    return __awaiter(this, void 0, void 0, function () {
        function cachePayload(req, res, next) {
            try {
                var key_1 = '__express__' + req.originalUrl || req.url;
                if (cryptoCache.has(key_1) && shouldCache) {
                    return res.send(cryptoCache.get(key_1)).status(200);
                }
                res.sendResponse = res.send;
                res.send = function (body) {
                    cryptoCache.set(key_1, body);
                    res.sendResponse(body);
                };
                next();
            }
            catch (err) {
                console.log(err);
                throw err;
            }
        }
        var cryptoCache;
        return __generator(this, function (_a) {
            cryptoCache = new node_cache({ stdTTL: 60 * 5 });
            app.use(cachePayload);
            if (shouldCompress && app.request.headers['Accept-Encoding']) {
                app.use(compression());
            }
            return [2 /*return*/];
        });
    });
}
exports.initializeMiddlewares = initializeMiddlewares;
;
function initializeRoutes(app) {
    return __awaiter(this, void 0, void 0, function () {
        var router;
        var _this = this;
        return __generator(this, function (_a) {
            router = express.Router();
            router.get('/', function (_request, response) { return __awaiter(_this, void 0, void 0, function () {
                var responseBody;
                return __generator(this, function (_a) {
                    switch (_a.label) {
                        case 0: return [4 /*yield*/, (0, usersService_1.getAllUsers)()];
                        case 1:
                            responseBody = _a.sent();
                            response.send(responseBody);
                            return [2 /*return*/];
                    }
                });
            }); });
            router.get('/city/:nameCity', function (request, response) { return __awaiter(_this, void 0, void 0, function () {
                var cityName, responseBody;
                return __generator(this, function (_a) {
                    switch (_a.label) {
                        case 0:
                            cityName = request.params.nameCity;
                            return [4 /*yield*/, (0, usersService_1.getSomeUsers)(cityName)];
                        case 1:
                            responseBody = _a.sent();
                            response.send(responseBody);
                            return [2 /*return*/];
                    }
                });
            }); });
            app.use(router);
            return [2 /*return*/];
        });
    });
}
exports.initializeRoutes = initializeRoutes;
;
