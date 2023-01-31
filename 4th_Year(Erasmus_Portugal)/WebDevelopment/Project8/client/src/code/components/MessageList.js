"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
exports.__esModule = true;
// React imports.
var react_1 = __importDefault(require("react"));
var Table_1 = __importDefault(require("@material-ui/core/Table"));
var TableBody_1 = __importDefault(require("@material-ui/core/TableBody"));
var TableCell_1 = __importDefault(require("@material-ui/core/TableCell"));
var TableHead_1 = __importDefault(require("@material-ui/core/TableHead"));
var TableRow_1 = __importDefault(require("@material-ui/core/TableRow"));
/**
 * MessageList.
 */
var MessageList = function (_a) {
    var state = _a.state;
    return (react_1["default"].createElement(Table_1["default"], { stickyHeader: true, padding: "none" },
        react_1["default"].createElement(TableHead_1["default"], null,
            react_1["default"].createElement(TableRow_1["default"], null,
                react_1["default"].createElement(TableCell_1["default"], { style: { width: 120 } }, "Date"),
                react_1["default"].createElement(TableCell_1["default"], { style: { width: 300 } }, "From"),
                react_1["default"].createElement(TableCell_1["default"], null, "Subject"))),
        react_1["default"].createElement(TableBody_1["default"], null, state.messages.map(function (message) { return (react_1["default"].createElement(TableRow_1["default"], { key: message.id, onClick: function () { return state.showMessage(message); } },
            react_1["default"].createElement(TableCell_1["default"], null, new Date(message.date).toLocaleDateString()),
            react_1["default"].createElement(TableCell_1["default"], null, message.from),
            react_1["default"].createElement(TableCell_1["default"], null, message.subject))); }))));
}; /* Mailboxes. */
exports["default"] = MessageList;
