"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
exports.__esModule = true;
// React imports.
var react_1 = __importDefault(require("react"));
// Material-UI imports.
var Chip_1 = __importDefault(require("@material-ui/core/Chip"));
var List_1 = __importDefault(require("@material-ui/core/List"));
/**
 * Mailboxes.
 */
var MailboxList = function (_a) {
    var state = _a.state;
    return (react_1["default"].createElement(List_1["default"], null, state.mailboxes.map(function (value) {
        return (react_1["default"].createElement(Chip_1["default"], { label: "".concat(value.name), onClick: function () { return state.setCurrentMailbox(value.path); }, style: { width: 128, marginBottom: 10 }, color: state.currentMailbox === value.path ? "secondary" : "primary" }));
    })));
}; /* Mailboxes. */
exports["default"] = MailboxList;
