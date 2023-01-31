"use strict";
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
        while (g && (g = 0, op[0] && (_ = 0)), _) try {
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
exports.createState = void 0;
// App imports.
var Contacts = __importStar(require("./Contacts"));
var config_1 = require("./config");
var IMAP = __importStar(require("./IMAP"));
var SMTP = __importStar(require("./SMTP"));
/**
 * This function must be called once and only once from BaseLayout.
 */
function createState(inParentComponent) {
    return {
        // Flag: Is the please wait dialog visible?
        pleaseWaitVisible: false,
        // List of contacts.
        contacts: [],
        // List of mailboxes.
        mailboxes: [],
        // List of messages in the current mailbox.
        messages: [],
        // The view that is currently showing ("welcome", "message", "compose", "contact" or "contactAdd").
        currentView: "welcome",
        // The currently selected mailbox, if any.
        currentMailbox: null,
        // The details of the message currently being viewed or composed, if any.
        messageID: null,
        messageDate: null,
        messageFrom: null,
        messageTo: null,
        messageSubject: null,
        messageBody: null,
        // The details of the contact currently being viewed or added, if any.
        contactID: null,
        contactName: null,
        contactEmail: null,
        // ------------------------------------------------------------------------------------------------
        // ------------------------------------ View Switch functions -------------------------------------
        // ------------------------------------------------------------------------------------------------
        /**
         * Shows or hides the please wait dialog during server calls.
         *
         * @param inVisible True to show the dialog, false to hide it.
         */
        showHidePleaseWait: function (inVisible) {
            this.setState({ pleaseWaitVisible: inVisible });
        }.bind(inParentComponent),
        /**
         * Show ContactView in view mode.
         *
         * @param inID    The ID of the contact to show.
         * @param inName  The name of the contact to show.
         * @oaram inEmail The Email address of the contact to show.
         */
        showContact: function (inID, inName, inEmail) {
            console.log("state.showContact()", inID, inName, inEmail);
            this.setState({ currentView: "contact", contactID: inID, contactName: inName, contactEmail: inEmail });
        }.bind(inParentComponent),
        /**
         * Show ContactView in add mode.
         */
        showAddContact: function () {
            console.log("state.showAddContact()");
            this.setState({ currentView: "contactAdd", contactID: null, contactName: "", contactEmail: "" });
        }.bind(inParentComponent),
        /**
         * Show MessageView in view mode.
         *
         * @param inMessage The message object that was clicked.
         */
        showMessage: function (inMessage) {
            return __awaiter(this, void 0, void 0, function () {
                var imapWorker, mb;
                return __generator(this, function (_a) {
                    switch (_a.label) {
                        case 0:
                            console.log("state.showMessage()", inMessage);
                            // Get the message's body.
                            this.state.showHidePleaseWait(true);
                            imapWorker = new IMAP.Worker();
                            return [4 /*yield*/, imapWorker.getMessageBody(inMessage.id, this.state.currentMailbox)];
                        case 1:
                            mb = _a.sent();
                            this.state.showHidePleaseWait(false);
                            // Update state.
                            this.setState({ currentView: "message",
                                messageID: inMessage.id, messageDate: inMessage.date, messageFrom: inMessage.from,
                                messageTo: "", messageSubject: inMessage.subject, messageBody: mb
                            });
                            return [2 /*return*/];
                    }
                });
            });
        }.bind(inParentComponent),
        /**
         * Show MessageView in compose mode.
         *
         * @param inType Pass "new" if this is a new message, "reply" if it's a reply to the message currently being
         *                    viewed, and "contact" if it's a message to the contact currently being viewed.
         */
        showComposeMessage: function (inType) {
            console.log("state.showComposeMessage()");
            switch (inType) {
                case "new":
                    this.setState({ currentView: "compose",
                        messageTo: "", messageSubject: "", messageBody: "",
                        messageFrom: config_1.config.userEmail
                    });
                    break;
                case "reply":
                    this.setState({ currentView: "compose",
                        messageTo: this.state.messageFrom, messageSubject: "Re: ".concat(this.state.messageSubject),
                        messageBody: "\n\n---- Original Message ----\n\n".concat(this.state.messageBody), messageFrom: config_1.config.userEmail });
                    break;
                case "contact":
                    this.setState({ currentView: "compose",
                        messageTo: this.state.contactEmail, messageSubject: "", messageBody: "",
                        messageFrom: config_1.config.userEmail
                    });
                    break;
            }
        }.bind(inParentComponent),
        // ------------------------------------------------------------------------------------------------
        // ---------------------------------------- List functions ----------------------------------------
        // ------------------------------------------------------------------------------------------------
        /**
         * Add a mailbox to the list of mailboxes.
         *
         * @param inMailbox A mailbox descriptor object.
         */
        addMailboxToList: function (inMailbox) {
            console.log("state.addMailboxToList()", inMailbox);
            // Copy list.
            var cl = this.state.mailboxes.slice(0);
            // Add new element.
            cl.push(inMailbox);
            // Update list in state.
            this.setState({ mailboxes: cl });
        }.bind(inParentComponent),
        /**
         * Add a contact to the list of contacts.
         *
         * @param inContact A contact descriptor object.
         */
        addContactToList: function (inContact) {
            console.log("state.addContactToList()", inContact);
            // Copy list.
            var cl = this.state.contacts.slice(0);
            // Add new element.
            cl.push({ _id: inContact._id, name: inContact.name, email: inContact.email });
            // Update list in state.
            this.setState({ contacts: cl });
        }.bind(inParentComponent),
        /**
         * Add a message to the list of messages in the current mailbox.
         *
         * @param inMessage A message descriptor object.
         */
        addMessageToList: function (inMessage) {
            console.log("state.addMessageToList()", inMessage);
            // Copy list.
            var cl = this.state.messages.slice(0);
            // Add new element.
            cl.push({ id: inMessage.id, date: inMessage.date, from: inMessage.from, subject: inMessage.subject });
            // Update list in state.
            this.setState({ messages: cl });
        }.bind(inParentComponent),
        /**
         * Clear the list of messages currently displayed.
         */
        clearMessages: function () {
            console.log("state.clearMessages()");
            this.setState({ messages: [] });
        }.bind(inParentComponent),
        // ------------------------------------------------------------------------------------------------
        // ------------------------------------ Event Handler functions -----------------------------------
        // ------------------------------------------------------------------------------------------------
        /**
         * Set the current mailbox.
         *
         * @param inPath The path of the current mailbox.
         */
        setCurrentMailbox: function (inPath) {
            console.log("state.setCurrentMailbox()", inPath);
            // Update state.
            this.setState({ currentView: "welcome", currentMailbox: inPath });
            // Now go get the list of messages for the mailbox.
            this.state.getMessages(inPath);
        }.bind(inParentComponent),
        /**
         * Get a list of messages in the currently selected mailbox, if any.
         *
         * @param inPath The path to the mailbox to get messages for.  Note that because this method is called when the
         *               current mailbox is set, we can't count on state having been updated by the time this is called,
         *               hence why the mailbox is passed in.  This avoids the problem with setState() being asynchronous.
         */
        getMessages: function (inPath) {
            return __awaiter(this, void 0, void 0, function () {
                var imapWorker, messages;
                var _this = this;
                return __generator(this, function (_a) {
                    switch (_a.label) {
                        case 0:
                            console.log("state.getMessages()");
                            this.state.showHidePleaseWait(true);
                            imapWorker = new IMAP.Worker();
                            return [4 /*yield*/, imapWorker.listMessages(inPath)];
                        case 1:
                            messages = _a.sent();
                            this.state.showHidePleaseWait(false);
                            this.state.clearMessages();
                            messages.forEach(function (inMessage) {
                                _this.state.addMessageToList(inMessage);
                            });
                            return [2 /*return*/];
                    }
                });
            });
        }.bind(inParentComponent),
        /**
         * Fires any time the user types in an editable field.
         *
         * @param inEvent The event object generated by the keypress.
         */
        fieldChangeHandler: function (inEvent) {
            var _a;
            console.log("state.fieldChangeHandler()", inEvent.target.id, inEvent.target.value);
            // Enforce max length for contact name.
            if (inEvent.target.id === "contactName" && inEvent.target.value.length > 16) {
                return;
            }
            this.setState((_a = {}, _a[inEvent.target.id] = inEvent.target.value, _a));
        }.bind(inParentComponent),
        /**
         * Save contact.
         */
        saveContact: function () {
            return __awaiter(this, void 0, void 0, function () {
                var cl, contactsWorker, contact;
                return __generator(this, function (_a) {
                    switch (_a.label) {
                        case 0:
                            console.log("state.saveContact()", this.state.contactID, this.state.contactName, this.state.contactEmail);
                            cl = this.state.contacts.slice(0);
                            // Save to server.
                            this.state.showHidePleaseWait(true);
                            contactsWorker = new Contacts.Worker();
                            return [4 /*yield*/, contactsWorker.addContact({ name: this.state.contactName, email: this.state.contactEmail })];
                        case 1:
                            contact = _a.sent();
                            this.state.showHidePleaseWait(false);
                            // Add to list.
                            cl.push(contact);
                            // Update state.
                            this.setState({ contacts: cl, contactID: null, contactName: "", contactEmail: "" });
                            return [2 /*return*/];
                    }
                });
            });
        }.bind(inParentComponent),
        /**
         * Delete the currently viewed contact.
         */
        deleteContact: function () {
            return __awaiter(this, void 0, void 0, function () {
                var contactsWorker, cl;
                var _this = this;
                return __generator(this, function (_a) {
                    switch (_a.label) {
                        case 0:
                            console.log("state.deleteContact()", this.state.contactID);
                            // Delete from server.
                            this.state.showHidePleaseWait(true);
                            contactsWorker = new Contacts.Worker();
                            return [4 /*yield*/, contactsWorker.deleteContact(this.state.contactID)];
                        case 1:
                            _a.sent();
                            this.state.showHidePleaseWait(false);
                            cl = this.state.contacts.filter(function (inElement) { return inElement._id != _this.state.contactID; });
                            // Update state.
                            this.setState({ contacts: cl, contactID: null, contactName: "", contactEmail: "" });
                            return [2 /*return*/];
                    }
                });
            });
        }.bind(inParentComponent),
        /**
         * Delete the currently viewed message.
         */
        deleteMessage: function () {
            return __awaiter(this, void 0, void 0, function () {
                var imapWorker, cl;
                var _this = this;
                return __generator(this, function (_a) {
                    switch (_a.label) {
                        case 0:
                            console.log("state.deleteMessage()", this.state.messageID);
                            // Delete from server.
                            this.state.showHidePleaseWait(true);
                            imapWorker = new IMAP.Worker();
                            return [4 /*yield*/, imapWorker.deleteMessage(this.state.messageID, this.state.currentMailbox)];
                        case 1:
                            _a.sent();
                            this.state.showHidePleaseWait(false);
                            cl = this.state.messages.filter(function (inElement) { return inElement.id != _this.state.messageID; });
                            // Update state.
                            this.setState({ messages: cl, currentView: "welcome" });
                            return [2 /*return*/];
                    }
                });
            });
        }.bind(inParentComponent),
        /**
         * Delete a message (from the server and the contact list).
         */
        sendMessage: function () {
            return __awaiter(this, void 0, void 0, function () {
                var smtpWorker;
                return __generator(this, function (_a) {
                    switch (_a.label) {
                        case 0:
                            console.log("state.sendMessage()", this.state.messageTo, this.state.messageFrom, this.state.messageSubject, this.state.messageBody);
                            // Send the message.
                            this.state.showHidePleaseWait(true);
                            smtpWorker = new SMTP.Worker();
                            return [4 /*yield*/, smtpWorker.sendMessage(this.state.messageTo, this.state.messageFrom, this.state.messageSubject, this.state.messageBody)];
                        case 1:
                            _a.sent();
                            this.state.showHidePleaseWait(false);
                            // Update state.
                            this.setState({ currentView: "welcome" });
                            return [2 /*return*/];
                    }
                });
            });
        }.bind(inParentComponent) /* End sendMessage(). */
    };
} /* End createState(). */
exports.createState = createState;
