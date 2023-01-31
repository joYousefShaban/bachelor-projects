import React, { useState } from "react";
import * as ReactDOM from "react-dom";
import AddStudentForm from "./addStudentForm";
import EditStudentForm from "./editStudentForm";
import StudentTable from "./studentsTable";
import { IStudent } from "./interface";
import axios from 'axios';
import "./css/styles.css";
import logo from "./assets/logo.png"



const defaultStudents: Array<IStudent> = [];
const initCurrentStudent: IStudent = { studentName: '', studentID: '', mark: NaN, email: '', _id: '' };
//refreshSeconds variable will contain the number of seconds for each refresh retrieved from the serverside/database.
var refreshSeconds: number = 1;
//editRefreshPause will make sure that the system will pause the retriving more data from the database, untill the user submits his new credientials
var editRefreshPause: boolean = false;

//getListStudents will retrieve all the students regirstered in the database/serverside
export async function getListStudents() {
    let studentList: IStudent[] = defaultStudents;
    await axios({
        method: 'get',
        url: 'student/',
    }).then((response) => {
        if (response.status === 200) //200 means there is a value returned and it was accepted (may contain null)
            studentList = response.data;
        else //not returning a variable means that the system is currently shut, and since we will keep refreshing each 3 seconds, the user won't be able to do any operations till it gets back online
            alert("Server is down, please try again later");
    }, (error) => {
        alert("Server is down, please try again later");
    }
    );
    return studentList;
}

//axios will be used the defined API's from serverside, through the port of it (localhost:8080)
axios.defaults.baseURL = 'http://127.0.0.1:8080';

function App() {
    const [students, setStudents] = useState(defaultStudents);
    const [editStudent, setEditStudent] = useState(initCurrentStudent);
    const [editing, setEdit] = useState(false);


    //Scan the already registered students in the database in the first launch
    window.onload = function () {
        refreshServersideStudents();
        ResetRefreshTimer();
    }

    function ResetRefreshTimer() {
        setInterval(() => {
            if (!editRefreshPause)
                refreshServersideStudents();
        }, refreshSeconds * 1000)
    }

    async function refreshServersideStudents() {
        setStudents(await getListStudents());
    }


    //if addStudentForm.tsx requests to add the student, the axios will pass the values to the serverside, which will return a promise if to add or not
    const onAddStudent = (newStudent: IStudent) => {
        const _id = newStudent._id;

        axios({
            method: 'post',
            url: 'student/',
            data: {
                studentName: newStudent.studentName,
                studentID: newStudent.studentID,
                mark: newStudent.mark,
                email: newStudent.email,
                _id: _id
            }
        }).then((response) => {
            if (response.status === 200)
                setStudents([...students, { ...newStudent, _id }]);
            else
                alert("Server couldn't handle the request, Student wasn't added.\n")
        }, (error) => {
            console.log(error);
            alert("Server couldn't handle the request, Student wasn't added.\n")
        }
        );
    };

    //this method gets called when requesting accsses to see the editStudentForm menu.
    const onCurrentStudent = (student: IStudent) => {
        setEditStudent(student);
        setEdit(true);
        editRefreshPause = true;
    };

    //if editStudentForm.tsx requests to edit the student, the axios will pass the values to the serverside, which will return a promise if to edit or not
    const onUpdateStudent = (_id: string, newStudent: IStudent) => {
        axios({
            method: 'put',
            url: 'student/' + newStudent._id,
            data: {
                studentName: newStudent.studentName,
                studentID: newStudent.studentID,
                mark: newStudent.mark,
                email: newStudent.email,
                _id: newStudent._id,
            }
        }).then((response) => {
            setEdit(false);
            editRefreshPause = false;
            if (response.status === 200) {
                setStudents(students.map((i: IStudent) => (i._id === _id ? newStudent : i)));
            }
            else
                alert("Servercouldn't handle the request, Student wasn't modified.\n Please contact an admin")
        }, (error) => {
            console.log(error);
            alert("Servercouldn't handle the request, Student wasn't modified.\n Please contact an admin")
        }
        );
    };

    //if studentsTable.tsx requests to delete a student through actions menu, the axios will pass the values to the serverside, which will return a promise if to delete or not
    const onDeleteStudent = (currentStudent: IStudent) => {

        axios({
            method: 'delete',
            url: 'student/' + currentStudent._id,
        }).then((response) => {
            if (response.status === 200)
                setStudents(students.filter(i => i._id !== currentStudent._id));
            else
                alert("Servercouldn't handle the request, Student wasn't deleted.\n Please contact an admin")
        }, (error) => {
            console.log(error);
            alert("something went wrong");
        }
        );
    };

    //This will be the main react template
    return (
        <div className="App">
            <img src={logo} alt="harry potter" style={{ marginLeft: 'auto',marginRight: 'auto',marginBottom: '50px', display:'block'}} />
            <div className="student-flex-wrapper">
                {editing ? (
                    <EditStudentForm
                        student={editStudent}
                        onUpdateStudent={onUpdateStudent}
                        setEdit={setEdit}
                    />
                ) : (
                    <AddStudentForm onAddStudent={onAddStudent} />
                )}
                <StudentTable
                    students={students}
                    onEdit={onCurrentStudent}
                    onDelete={onDeleteStudent}
                />
            </div>
        </div>
    );
}

const rootElement = document.getElementById("root");
ReactDOM.render(<App />, rootElement);