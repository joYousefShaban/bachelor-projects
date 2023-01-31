"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
exports.__esModule = true;
// React imports.
var react_1 = __importDefault(require("react"));
// Material-UI imports.
var Button_1 = __importDefault(require("@material-ui/core/Button"));
var ContactMail_1 = __importDefault(require("@material-ui/icons/ContactMail"));
var Email_1 = __importDefault(require("@material-ui/icons/Email"));
/**
 * Toolbar.
 */
var Toolbar = function (_a) {
    var state = _a.state;
    return (react_1["default"].createElement("div", null,
        react_1["default"].createElement(Button_1["default"], { variant: "contained", color: "primary", size: "small", style: { marginRight: 10 }, onClick: function () { return state.showComposeMessage("new"); } },
            react_1["default"].createElement(Email_1["default"], { style: { marginRight: 10 } }),
            "New Message"),
        react_1["default"].createElement(Button_1["default"], { variant: "contained", color: "primary", size: "small", style: { marginRight: 10 }, onClick: state.showAddContact },
            react_1["default"].createElement(ContactMail_1["default"], { style: { marginRight: 10 } }),
            "New Contact")));
};
exports["default"] = Toolbar;
