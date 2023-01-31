import * as path from 'path';
import Nedb from 'nedb';

//This IStudent interface will contain all the variables that each student registered in the system has to aquire(which can be modified for extra information later on)
export interface IStudent {
    _id: string, studentName: string, studentID: string, mark: number, email: string
}

//The following lines in the block define the database we are dealing with and linking it the folder it is located at.
const db: Nedb= new Nedb({
    filename: path.join(__dirname, 'examGrades.db'),
    autoload: true
});
export class Student {
    //the following function returns a promise of all students regestered in the database as an array of object IStudent, which will be returning error or the student himself
    public listStudents(): Promise<IStudent[]> {
        return new Promise((inResolve, inReject) => {
            db.find({},
                (inError: Error | null, studentsList: IStudent[]) => {
                    if (inError) {
                        inReject(inError);
                    } else {
                        inResolve(studentsList);
                    }
                }
            );
        });
    }

    //the following function returns a promise of the student that is regestered in the database that aquire the same ID, which will be returning error or the student himself
    public getStudent(id: string): Promise<IStudent> {
        return new Promise((inResolve, inReject) => {
            db.findOne({ _id: id },
                (inError: Error | null, student: IStudent) => {
                    if (inError) {
                        inReject(inError);
                    } else {
                        inResolve(student);
                    }
                }
            );
        });
    }

    //the following function returns a promise of the average of students marks that is regestered in the database, which will be returning error or the double number itself
    public averageMarks(): Promise<number> {
        return new Promise((inResolve, inReject) => {
            db.find({},
                (inError: Error | null, studentsList: IStudent[]) => {
                    if (inError) {
                        inReject('An error has accrued');
                    } else {
                        let marksSummation: number = 0;
                        for (let i: number = 0; i < studentsList.length; i++) {
                            marksSummation += +studentsList.at(i)!.mark;
                        }
                        inResolve(marksSummation / studentsList.length);
                    }
                }
            );
        });
    }

    public onFindPassedRate(): Promise<number> {
        return new Promise((inResolve, inReject) => {
            db.find({},
                (inError: Error | null, studentsList: IStudent[]) => {
                    if (inError) {
                        inReject('An error has accrued');
                    } else {
                        let passedMarksStudents: number = 0;
                        for (let i: number = 0; i < studentsList.length; i++) {
                            if(studentsList.at(i)!.mark>=50)
                                passedMarksStudents ++;
                        }
                        console.log(passedMarksStudents)
                        console.log(studentsList.length)
                        inResolve(passedMarksStudents / studentsList.length*100);
                    }
                }
            );
        });
    }
    

    //this function performs validatoins on each student added or edited.
    public async validator(student: IStudent, id: string) {
        function formatValidations(): string {
            var errorMessage = errorMessageTemplate;

            //studentName format validations
            var minimumLength = 4;
            var maximumLength = 25;
            if (student.studentName == null)
                errorMessage += '- StudentName field is empty \n';
            else if (student.studentName.length < minimumLength)
                errorMessage += '- StudentName length should not be less than ' + minimumLength + ' characters \n';
            else if (student.studentName.length > maximumLength)
                errorMessage += '- StudentName length should not be exceed ' + maximumLength + ' characters \n';

            //studentID format validations
            if (student.studentID == null)
                errorMessage += '- StudentID field is empty \n';
            else if (isNaN(+student.studentID) || student.studentID.length != 5) {
                errorMessage += '- Student ID must be 5 digit numerical value, no changed has been done \n';
            }

            //mark format validations
            var minimumValue = 0;
            var maximumValue = 100;
            if (student.mark == null)
                errorMessage += '- Mark field is empty \n';
            else if (student.mark < minimumValue)
                errorMessage += '- Mark value should not be less than ' + minimumValue + ' \n';
            else if (student.mark > maximumValue)
                errorMessage += '- Mark value should not be exceed ' + maximumValue + ' \n';

            //email format validations
            const emailExpression: RegExp = /^\S+@\S+\.\S+$/;
            if (student.email == null)
                errorMessage += '- Email field is empty \n';
            else if (!emailExpression.test(student.email))
                errorMessage += '- The email is not valid \n';

            return errorMessage;
        }

        // if there's any errors that was appended to the string, return them.
        let errorMessageTemplate: string = "You have the following errors: \n";
        let resultErrorMessage = formatValidations();
        if (resultErrorMessage !== errorMessageTemplate)
            return new Promise((_, inReject) => {
                inReject(resultErrorMessage);
            });

        //validations regarding studentID (assuring studentID to remain unique)
        await new Promise((inResolve, inReject) => {
            db.findOne({ studentID: student.studentID },
                (inError: Error | null, fetchedStudent: IStudent) => {
                    if (fetchedStudent != null && (id == "NoIdProvided" || fetchedStudent._id != id)) {
                        inReject('Student ID already exist, no changed has been done');
                    }
                    else if (inError) {
                        inReject(inError);
                    } else {
                        inResolve(student);
                    }
                });
        })
    }

    //The following method performs the actual add to the database, after it gets the approval of all the fulfill to all the requirments from the validator.
    public async addStudent(student: IStudent): Promise<IStudent> {
        await this.validator(student, "NoIdProvided");

        return new Promise((inResolve, inReject) => {
            db.insert(student, (inError: Error | null) => {
                if (inError) {
                    inReject(inError);
                } else {
                    inResolve(student);
                }
            }
            );
        });
    }

    //The following method performs the actual edit to the database, after it gets the approval of all the fulfill to all the requirments from the validator.
    public async updateStudent(id: string, student: IStudent): Promise<IStudent> {
        await this.validator(student, id);

        return new Promise((inResolve, inReject) => {
            db.update({ _id: id }, student, {}, function (inError) {
                if (inError) {
                    inReject(inError);
                }
                else {
                    inResolve(student);
                }
            }
            );
        });
    }

    //The following method performs the actual delete to the record on the database.
    public deleteStudent(id: string): Promise<void> {
        return new Promise((inResolve, inReject) => {
            db.remove({ _id: id }, {},
                (inError: Error | null, _) => {
                    if (inError) {
                        inReject(inError);
                    }
                    else {
                        inResolve();
                    }
                }
            );
        });
    }
}