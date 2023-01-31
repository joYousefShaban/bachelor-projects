import * as nodemailer from 'nodemailer';
import { SendMailOptions, SentMessageInfo } from 'nodemailer';
import Mail from 'nodemailer/lib/mailer';
import { IServerInfo } from './serverInfo';


//imports serverinfo using dependency injection, while authorising sending email messages without exposing the email cridentials used to send
export class SMTP {
    private static serverInfo: IServerInfo;
    constructor(inServerInfo: IServerInfo) {
        SMTP.serverInfo = inServerInfo;
    }

    //the next block will be sending a message to the registered email and sending back a promise the the method that called it, which will handle both situations of succsses and failer
    public sendMessage(inOptions: SendMailOptions): Promise<void> {
        return new Promise((inResolve, inReject) => {
            const transport: Mail = nodemailer.createTransport(SMTP.serverInfo.smtp);
            transport.sendMail(inOptions,
                (inError: Error | null, inInfo: SentMessageInfo) => {
                    if (inError) {
                        inReject(inError);
                    } else {
                        inResolve(inInfo);
                    }
                }
            );
        });
    }
}    