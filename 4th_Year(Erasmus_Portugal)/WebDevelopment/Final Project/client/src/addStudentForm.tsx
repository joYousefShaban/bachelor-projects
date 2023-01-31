import React, { useState } from "react";
import { IStudent } from "./interface";
import validator, { noErrors, FormErrors } from "./validator";

//will contain the funcion from index.html to add the student himself 
interface IProps {
  onAddStudent: (student: IStudent) => void;
}
const initStudent = { studentName: '', studentID: '', mark: '', email: '' };
const AddStudentForm: React.FunctionComponent<IProps> = props => {
  const [formValue, setFormValue] = useState(initStudent);
  const [errors, setErrors] = useState<FormErrors>({});
  const onFormSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    //call the validator to make sure the inputs are in the correct format
    validator(
      formValue,
      false,
      (errors: any): any => {
        if (noErrors(errors)) {
          props.onAddStudent(formValue as any);
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
    setFormValue({ ...formValue, [name]: value });
  };

  //This will show the adding student table which will be shown to the left of the page later on.
  return (
    <div className="student-form">
      <h1>Add Student</h1>
      <form className="form-edit" onSubmit={onFormSubmit}>
        <div className="form-row">
          <label>Student Name:</label>
          <input
            type="text"
            placeholder="please input name"
            name="studentName"
            value={formValue.studentName}
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
            value={formValue.studentID}
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
            value={formValue.mark}
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
            value={formValue.email}
            onChange={onInputChange}
          />
          {errors["email"] && errors["email"].length > 0 && (
            <div className="form-error">{errors["email"].join(",")}</div>
          )}
        </div>
        <div className="form-row">
          <button>Add new student</button>
        </div>
      </form>
    </div>
  );
};
export default AddStudentForm;