import React, { useState, useEffect } from "react";
import { IStudent } from "./interface";
import validator, { FormErrors, noErrors } from "./validator";

//will contain the student being edited, edit page if open or not, and the funcion from index.html to update the student himself 
interface IProps {
  student: IStudent;
  onUpdateStudent: (_id: string, student: IStudent) => void;
  setEdit: (bool: boolean) => void;
}

const initStudent = { studentName: '', studentID: '', mark: '', email: '' };

export default function EditStudentForm(props: IProps) {
  const [student, setStudent] = useState(props.student);
  useEffect(() => setStudent(props.student), [props]);
  const [, setFormValue] = useState(initStudent);
  const [errors, setErrors] = useState<FormErrors>({});
  const onFormSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    if (!student.studentID || !student.studentName) {
      console.log("em");
      return false;
    }

    //call the validator to make sure the inputs are in the correct format
    validator(
      student,
      true,
      (errors: any): any => {
        if (noErrors(errors)) {
          props.onUpdateStudent(student._id, student);
          setFormValue(initStudent);
          return false;
        }
        setErrors(errors);
      }
    );
  };

  //append any changes happening in the frontend
  const onInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setStudent({ ...student, [name]: value });
  };

  //This will show the editing student table which will be shown to the left of the page later on.
  return (
    <div className="student-form">
      <h1>Edit Student</h1>
      <form className="form-edit" onSubmit={onFormSubmit}>
        <div className="form-row">
          <label>Student Name:</label>
          <input
            type="text"
            placeholder="please input name"
            name="studentName"
            value={student.studentName}
            onChange={onInputChange}
          />
          {errors["studentName"] && errors["studentName"].length > 0 && (
            <div className="form-error">{errors["studentName"].join(",")}</div>
          )}
        </div>
        <div className="form-row">
          <label>Student Number:</label>
          <input
            type="text"
            placeholder="please input student number"
            name="studentID"
            value={student.studentID}
            onChange={onInputChange}
          />
          {errors["studentID"] && errors["studentID"].length > 0 && (
            <div className="form-error">{errors["studentID"].join(",")}</div>
          )}
        </div>
        <div className="form-row">
          <label>Mark:</label>
          <input
            type="number"
            placeholder="please input mark"
            name="mark"
            value={student.mark}
            onChange={onInputChange}
          />
          {errors["mark"] && errors["mark"].length > 0 && (
            <div className="form-error">{errors["mark"].join(",")}</div>
          )}
        </div>
        <div className="form-row">
          <label>Email:</label>
          <input
            type="email"
            placeholder="please input full email"
            name="email"
            value={student.email}
            onChange={onInputChange}
          />
          {errors["email"] && errors["email"].length > 0 && (
            <div className="form-error">{errors["email"].join(",")}</div>
          )}
        </div>
        <div className="form-error">{"You are currently modifiying the information of " + props.student.studentName}</div>
        <div className="form-row">
          <button>Update</button>
          <button onClick={() => props.setEdit(false)}>Cancel</button>
        </div>
      </form>
    </div>
  );
}