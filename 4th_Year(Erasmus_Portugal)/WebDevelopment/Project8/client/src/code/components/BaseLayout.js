"use strict";
var __extends = (this && this.__extends) || (function () {
    var extendStatics = function (d, b) {
        extendStatics = Object.setPrototypeOf ||
            ({ __proto__: [] } instanceof Array && function (d, b) { d.__proto__ = b; }) ||
            function (d, b) { for (var p in b) if (Object.prototype.hasOwnProperty.call(b, p)) d[p] = b[p]; };
        return extendStatics(d, b);
    };
    return function (d, b) {
        if (typeof b !== "function" && b !== null)
            throw new TypeError("Class extends value " + String(b) + " is not a constructor or null");
        extendStatics(d, b);
        function __() { this.constructor = d; }
        d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
    };
})();
var __createBinding = (this && this.__createBinding) || (Object.create ? (function(o, m, k, k2) {
    if (k2 === undefined) k2 = k;
    var desc = Object.getOwnPropertyDescriptor(m, k);
    if (!desc || ("get" in desc ? !m.__esModule : desc.writable || desc.configurable)) {
      desc = { enumerable: true, get: function() { return m[k]; } };
    }
    Object.defineProperty(o, k2, desc);
}) : (function(o, m, k, k2) {
    if (k2 === undefined) k2 = k;
    o[k2] = m[k];
}));
var __setModuleDefault = (this && this.__setModuleDefault) || (Object.create ? (function(o, v) {
    Object.defineProperty(o, "default", { enumerable: true, value: v });
}) : function(o, v) {
    o["default"] = v;
});
var __importStar = (this && this.__importStar) || function (mod) {
    if (mod && mod.__esModule) return mod;
    var result = {};
    if (mod != null) for (var k in mod) if (k !== "default" && Object.prototype.hasOwnProperty.call(mod, k)) __createBinding(result, mod, k);
    __setModuleDefault(result, mod);
    return result;
};
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
exports.__esModule = true;
// React imports.
var react_1 = __importStar(require("react"));
// Library imports.
var Dialog_1 = __importDefault(require("@material-ui/core/Dialog"));
var DialogContent_1 = __importDefault(require("@material-ui/core/DialogContent"));
var DialogContentText_1 = __importDefault(require("@material-ui/core/DialogContentText"));
var DialogTitle_1 = __importDefault(require("@material-ui/core/DialogTitle"));
// App imports.
var Toolbar_1 = __importDefault(require("./Toolbar"));
var MailboxList_1 = __importDefault(require("./MailboxList"));
var MessageList_1 = __importDefault(require("./MessageList"));
var ContactList_1 = __importDefault(require("./ContactList"));
var WelcomeView_1 = __importDefault(require("./WelcomeView"));
var ContactView_1 = __importDefault(require("./ContactView"));
var MessageView_1 = __importDefault(require("./MessageView"));
var state_1 = require("../state");
/**
 * BaseLayout.
 */
var BaseLayout = /** @class */ (function (_super) {
    __extends(BaseLayout, _super);
    function BaseLayout() {
        var _this = _super !== null && _super.apply(this, arguments) || this;
        /**
         * State data for the app.  This also includes all mutator functions for manipulating state.  That way, we only
         * ever have to pass this entire object down through props (not necessarily the best design in terms of data
         * encapsulation, but it does have the benefit of being quite a bit simpler).
         */
        _this.state = (0, state_1.createState)(_this);
        return _this;
    }
    /**
     * Render().
     */
    BaseLayout.prototype.render = function () {
        return (react_1["default"].createElement("div", { className: "appContainer" },
            react_1["default"].createElement(Dialog_1["default"], { open: this.state.pleaseWaitVisible, disableBackdropClick: true, disableEscapeKeyDown: true, transitionDuration: 0 },
                react_1["default"].createElement(DialogTitle_1["default"], { style: { textAlign: "center" } }, "Please Wait"),
                react_1["default"].createElement(DialogContent_1["default"], null,
                    react_1["default"].createElement(DialogContentText_1["default"], null, "...Contacting server..."))),
            react_1["default"].createElement("div", { className: "toolbar" },
                react_1["default"].createElement(Toolbar_1["default"], { state: this.state })),
            react_1["default"].createElement("div", { className: "mailboxList" },
                react_1["default"].createElement(MailboxList_1["default"], { state: this.state })),
            react_1["default"].createElement("div", { className: "centerArea" },
                react_1["default"].createElement("div", { className: "messageList" },
                    react_1["default"].createElement(MessageList_1["default"], { state: this.state })),
                react_1["default"].createElement("div", { className: "centerViews" },
                    this.state.currentView === "welcome" && react_1["default"].createElement(WelcomeView_1["default"], null),
                    (this.state.currentView === "message" || this.state.currentView === "compose") &&
                        react_1["default"].createElement(MessageView_1["default"], { state: this.state }),
                    (this.state.currentView === "contact" || this.state.currentView === "contactAdd") &&
                        react_1["default"].createElement(ContactView_1["default"], { state: this.state }))),
            react_1["default"].createElement("div", { className: "contactList" },
                react_1["default"].createElement(ContactList_1["default"], { state: this.state }))));
    }; /* End render(). */
    return BaseLayout;
}(react_1.Component)); /* End class. */
exports["default"] = BaseLayout;
