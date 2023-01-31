"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
exports.__esModule = true;
// React imports.
var react_1 = __importDefault(require("react"));
// Material-UI imports.
var List_1 = __importDefault(require("@material-ui/core/List"));
var ListItem_1 = __importDefault(require("@material-ui/core/ListItem"));
var ListItemAvatar_1 = __importDefault(require("@material-ui/core/ListItemAvatar"));
var Avatar_1 = __importDefault(require("@material-ui/core/Avatar"));
var Person_1 = __importDefault(require("@material-ui/icons/Person"));
var ListItemText_1 = __importDefault(require("@material-ui/core/ListItemText"));
/**
 * Contacts.
 */
var ContactList = function (_a) {
    var state = _a.state;
    return (react_1["default"].createElement(List_1["default"], null, state.contacts.map(function (value) {
        return (react_1["default"].createElement(ListItem_1["default"], { key: value, button: true, onClick: function () { return state.showContact(value._id, value.name, value.email); } },
            react_1["default"].createElement(ListItemAvatar_1["default"], null,
                react_1["default"].createElement(Avatar_1["default"], null,
                    react_1["default"].createElement(Person_1["default"], null))),
            react_1["default"].createElement(ListItemText_1["default"], { primary: "".concat(value.name) })));
    })));
}; /* End Contacts. */
exports["default"] = ContactList;
