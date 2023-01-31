import React from "react";
import { IStudent } from "./interface";
import axios from "axios";

interface IProps {
    students: Array<IStudent>;
    onEdit: (student: IStudent) => void;
    onDelete: (student: IStudent) => void;
}

const StudentTable: React.FunctionComponent<IProps> = props => {
    function confirmDeleteDialog(i: IStudent) {
        var result = window.confirm("I'm aware that the information of the student " + i.studentName + " will be permanantly deleted");
        if (result) {
            props.onDelete(i);
        }
    }

    //if studentsTable.tsx requests to retrieve the avg of marks from students, which will return a promise if succsses or not
    function onFindAverage() {
        axios({
            method: 'get',
            url: 'student/avg',
        }).then((response) => {
            if (response.status === 200)
                alert("The average of marks for the " + props.students.length + " students registered is: " + response.data);
            else
                alert("Server is down, please try again later")
        }, (error) => {
            console.log(error);
            alert("Server is down, please try again later");
        }
        );
    };

    function onFindTotalNumber() {
        alert("The number of students registered on the system is: " + props.students.length);
    };

    function onFindPassedRate() {
        axios({
            method: 'get',
            url: 'student/prate',
        }).then((response) => {
            if (response.status === 200)
                alert("The passing rate for the " + props.students.length + " students registered is: " + response.data + "%");
            else
                alert("Server is down, please try again later")
        }, (error) => {
            console.log(error);
            alert("Server is down, please try again later");
        }
        );
    }

    //change each a row colour if two rows accur immediatly after each other (colour the table with white and grey)
    var backgroudClourflag = true;
    function tableBackgroundColour() {
        backgroudClourflag = !backgroudClourflag;
        if (backgroudClourflag === true) {
            const style = { "background": "#ebeff1" };
            return style;
        }
    }

    //change each a mark colour two match up if he passed or not
    function markNumberColour(mark: number) {
        var style: React.CSSProperties;
        if (mark >= 50)
            style = { "color": "white", "background": "green" };
        else
            style = { "color": "white", "background": "red" }
        return style;
    }

    //This will be the registered student table which will be shown to the right of the page later on.
    return (
        <div className="student-table">
            <h1>View Students</h1>
            <table>
                <thead>
                    <tr>
                        <th>Name</th>
                        <th>No.</th>
                        <th>Mark</th>
                        <th>Email</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                    {props.students.length > 0 ? (
                        props.students.map(i => (
                            <tr key={i._id} style={tableBackgroundColour()}>
                                <td>{i["studentName"]}</td>
                                <td>{"a" + i["studentID"]}</td>
                                <td style={markNumberColour(i.mark)}>{i["mark"]}</td>
                                <td>{i["email"]}</td>
                                <td>
                                    <button onClick={() => props.onEdit(i)}>edit</button>
                                    <button onClick={() => confirmDeleteDialog(i)}>delete</button>
                                </td>
                            </tr>
                        ))
                    ) : (
                        <tr>
                            <td colSpan={3}>No students registered</td>
                        </tr>
                    )}
                </tbody>
            </table>
            <br></br>
            <div className="form-row">
                <button onClick={() => onFindTotalNumber()}>Find Total Number Of Students</button>
                <div></div>
                <button onClick={() => onFindPassedRate()}>Find Pass Rate</button>
                <div></div>
                <button onClick={() => onFindAverage()}>Find Average</button>
            </div>
        </div>
    );
};
export default StudentTable;