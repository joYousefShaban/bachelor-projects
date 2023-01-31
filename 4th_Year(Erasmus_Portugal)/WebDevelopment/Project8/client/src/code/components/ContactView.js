"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
exports.__esModule = true;
// React imports.
var react_1 = __importDefault(require("react"));
// Material-UI imports.
var Button_1 = __importDefault(require("@material-ui/core/Button"));
var core_1 = require("@material-ui/core");
/**
 * ContactView.
 */
var ContactView = function (_a) {
    var state = _a.state;
    return (react_1["default"].createElement("form", null,
        react_1["default"].createElement(core_1.TextField, { margin: "dense", id: "contactName", label: "Name", value: state.contactName, variant: "outlined", InputProps: { style: { color: "#000000" } }, disabled: state.currentView === "contact", style: { width: 260 }, onChange: state.fieldChangeHandler }),
        react_1["default"].createElement("br", null),
        react_1["default"].createElement(core_1.TextField, { margin: "dense", id: "contactEmail", label: "Email", value: state.contactEmail, variant: "outlined", InputProps: { style: { color: "#000000" } }, disabled: state.currentView === "contact", style: { width: 520 }, onChange: state.fieldChangeHandler }),
        react_1["default"].createElement("br", null),
        state.currentView === "contactAdd" &&
            react_1["default"].createElement(Button_1["default"], { variant: "contained", color: "primary", size: "small", style: { marginTop: 10 }, onClick: state.saveContact }, "Save"),
        state.currentView === "contact" &&
            react_1["default"].createElement(Button_1["default"], { variant: "contained", color: "primary", size: "small", style: { marginTop: 10, marginRight: 10 }, onClick: state.deleteContact }, "Delete"),
        state.currentView === "contact" &&
            react_1["default"].createElement(Button_1["default"], { variant: "contained", color: "primary", size: "small", style: { marginTop: 10 }, onClick: function () { return state.showComposeMessage("contact"); } }, "Send Email")));
}; /* ContactView. */
exports["default"] = ContactView;
