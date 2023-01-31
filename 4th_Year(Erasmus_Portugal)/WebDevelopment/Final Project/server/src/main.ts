import express, { Express, NextFunction, Request, Response } from 'express';
import { SendMailOptions } from 'nodemailer';
import * as SMTP from './SMTP';
import * as Students from './Students';
import { IStudent } from './Students';
import { serverInfo } from './serverInfo';

async function main() {
  const app: Express = express();

  var cors = require('cors');
  app.use(cors());
  app.use(express.json());

  //middleware which appends nessesary response headers for the endpoints to function
  app.use(function (_, inResponse: Response,
    inNext: NextFunction) {
    inResponse.header('Access-Control-Allow-Origin', '*');
    inResponse.header(
      'Access-Control-Allow-Methods',
      'GET,POST,DELETE,OPTIONS',
    );
    inResponse.header('Access-Control-Allow-Headers',
      'Origin,X-Requested-With,Content-Type,Accept'
    );
    inNext();
  });

  //hitting an https request on localhost:8080/student/avg will end up calling this block, in which it will call a method to check average of the registered students in the database
  app.get('/student/avg', async (_, inResponse: Response) => {
    try {
      const studentsWorker: Students.Student = new Students.Student();
      const studentsavg = await studentsWorker.averageMarks();
      inResponse.json(studentsavg); // serialize object into JSON
    } catch (inError) {
      inResponse.send(inError);
    }
  });

  app.get('/student/prate', async (_, inResponse: Response) => {
    try {
      const studentsWorker: Students.Student = new Students.Student();
      const studentsPassedRate = await studentsWorker.onFindPassedRate();
      inResponse.json(studentsPassedRate); // serialize object into JSON
    } catch (inError) {
      inResponse.send(inError);
    }
  });
  

  //hitting an https request on localhost:8080/student will end up calling this block, in which it will call a method to return all students that are registered on the database
  app.get('/student', async (_, inResponse: Response) => {
    try {
      const studentsWorker: Students.Student = new Students.Student();
      const students: IStudent[] = await studentsWorker.listStudents();
      inResponse.json(students); // serialize object into JSON
    } catch (inError) {
      inResponse.send(inError);
    }
  });

  //hitting an https request on localhost:8080/student/:id will end up calling this block, in which we will pass the id value which will participate in returning the student with that ID
  app.get('/student/:id', async (inRequest: Request, inResponse: Response) => {
    try {
      const studentsWorker: Students.Student = new Students.Student();
      const student: IStudent = await studentsWorker.getStudent(inRequest.params.id);
      inResponse.json(student); // serialize object into JSON
    } catch (inError) {
      inResponse.send(inError);
    }
  });
  
  //hitting an https request on localhost:8080/student as POST will end up calling this block, in which we will request the user to pass values to fulfill the record in the database
  //notice that each error will be handled during the request.
  //we will be sending a message from the registered email in serverinfo.json to the student(knowing that it won't be seny unless the student registeres successfully)
  app.post('/student', async (inRequest: Request, inResponse: Response) => {
    try {
      const studentsWorker: Students.Student = new Students.Student();
      const student: IStudent = await studentsWorker.addStudent(inRequest.body);

      const smtpWorker: SMTP.SMTP = new SMTP.SMTP(serverInfo);
      await smtpWorker.sendMessage({
        //no need to place a put attribute, since the sending email is already defined in serverinfo.json
        'to': (inRequest.body as IStudent).email,
        'subject': 'Node Mailer',
        'text': 'Dear student, Your mark has been submited by your professor'
      } as SendMailOptions);

      inResponse.json(student); // for client acknowledgment and future use ( includes ID)
    } catch (inError) {
      inResponse.statusCode = 500;
      inResponse.send(inError);
    }
  });

  //hitting an https request on localhost:8080/student/:id as DELETE will end up calling this block, in which we will pass the id value which will participate in deleting the student with that ID
  app.delete('/student/:id', async (inRequest: Request, inResponse: Response) => {
    try {
      const studentsWorker: Students.Student = new Students.Student();
      await studentsWorker.deleteStudent(inRequest.params.id);

      inResponse.send('ok');
    } catch (inError) {
      inResponse.send(inError);
    }
  });

  //hitting an https request on localhost:8080/student/:id as PUT will end up calling this block, in which we will request the user to pass values to edit the record in the database
  //notice that each error will be handled during the request.
  //we will be sending a message from the registered email in serverinfo.json to the student(knowing that it won't be sent unless the student registeres his new information successfully)
  app.put('/student/:id', async (inRequest: Request, inResponse: Response) => {
    try {
      const studentsWorker: Students.Student = new Students.Student();
      const student: IStudent = await studentsWorker.updateStudent(inRequest.params.id, inRequest.body);

      const smtpWorker: SMTP.SMTP = new SMTP.SMTP(serverInfo);
      await smtpWorker.sendMessage({
        //no need to place "from" attribute, since the sending email the is already defined in serverinfo.json
        'to': (inRequest.body as IStudent).email,
        'subject': 'Node Mailer',
        'text': 'Dear student, The information on your account has been modified by your professor'
      } as SendMailOptions);

      inResponse.json(student); // serialize object into JSON
    } catch (inError) {
      inResponse.statusCode = 500;
      inResponse.send(inError);
    }
  });

  //this is the port, in which we will be sending requests to. (will be used later on for axios clientside)
  app.listen(8080);
}

main();