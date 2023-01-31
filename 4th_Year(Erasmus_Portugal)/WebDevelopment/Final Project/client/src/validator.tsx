import { getListStudents } from "./index";

export interface FormValue {
    [K: string]: any;
}
export interface FormErrors {
    [K: string]: any[];
}
function isEmpty(value: any) {
    return value === '' || value === null || value === undefined;
}

//here will be all the validations accuring for the client side
const Customrules: rules = [
    { key: "studentName", required: true, label: "studentName" },
    { key: "studentID", required: true, label: "studentID" },
    { key: "mark", required: true, label: "Mark" },
    { key: "email", required: true, label: "Email" },
    { key: "studentName", minLength: 4, label: "studentName" },
    { key: "studentName", maxLength: 25, label: "studentName" },
    { key: "studentID", pattern: /\b\d{5}\b/, label: "Student Number" },
    { key: "studentID", checkID: true, label: "Student Number" },
    { key: "mark", minValue: 0, label: "Mark" },
    { key: "mark", maxValue: 100, label: "Mark" }
];

type rules = Array<FormRules>;
interface FormRules {
    key: string;
    label: string;
    required?: boolean;
    minLength?: number;
    maxLength?: number;
    minValue?: number;
    maxValue?: number;
    pattern?: RegExp;
    checkID?: boolean;
    validator?: (value: string) => OneError;
}
type OneError = string | Promise<string>;

//if there was no errors, that means the string should be empty
export const noErrors = (value: any) => {
    return Object.values(value).length === 0;
};
const validator = async (
    //if any values has to be passed, we shold calssify it here
    formValue: FormValue,
    isEditing: Boolean,
    callback: (errors: any) => void
) => {
    const result: any = {};
    //here we will add all the errors to the results array
    const addErrors = async (key: string, error: OneError) => {
        if (result[key] === undefined) {
            result[key] = [];
        }
        result[key].push(error);
    };

    //here we itterate through all the custom rules we made and check if they are valid, if not we add a new error
    await Promise.all(Customrules.map(async (r) => {
        let value = formValue[r.key];
        if (r.validator) {
            const promise = await r.validator(value);
            addErrors(r.key, promise);
        }
        if (isEmpty(value) && r.required) {
            addErrors(r.key, `${r.label} shouldn't be empty`);
        }
        if (!isEmpty(value) && value.length < r.minLength!) {
            addErrors(r.key, `${r.label} shouldn't be less than ${r.minLength!} characters`);
        }
        if (!isEmpty(value) && value.length > r.maxLength!) {
            addErrors(r.key, `${r.label} shouldn't have more than ${r.maxLength!} characters`);
        }
        if (!isEmpty(value) && parseInt(value, 10) < r.minValue!) {
            addErrors(r.key, `${r.label} should be higher than ${r.minValue!}`);
        }
        if (!isEmpty(value) && parseInt(value, 10) > r.maxValue!) {
            addErrors(r.key, `${r.label} shouldn't Exceed ${r.maxValue!}`);
        }
        if (!isEmpty(value) && r.pattern && !r.pattern.test(value)) {
            addErrors(r.key, `${r.label} has to be exactly 5 number digits`);
        }
        //validation to insure that the studentID remains unique
        if (r.checkID && r.key === "studentID") {
            const students = await getListStudents();
            for (var i = 0; i < students.length; i++) {
                if (value === students.at(i)!.studentID) {
                    if (!isEditing || (isEditing && (students.at(i)!._id !== formValue._id))) {
                        addErrors(r.key, `The studentID of ${r.label} already exist`);
                    }
                }
            }
        }
    }
    ));

    const errors = Object.keys(result).map(k =>
        result[k].map((promise: OneError) => [k, promise])
    );
    const newPromises: any = flat(errors).map(([key, promiseOrString]) =>
        (promiseOrString instanceof Promise
            ? promiseOrString
            : Promise.reject(promiseOrString)
        ).then(() => [key, undefined], reason => [key, reason])
    );
    Promise.all(newPromises).then(results => {
        callback(zip(results.filter(i => i[1])));
    });
};

function flat(array: any[]) {
    const result: any = [];
    for (let i = 0; i < array.length; i++) {
        if (array[i] instanceof Array) {
            result.push(...array[i]);
        } else {
            result.push(array[i]);
        }
    }
    return result;
}

function zip(list: Array<string[]>) {
    const result = {};
    list.forEach(([key, value]) => {
        result[key] = result[key] || [];
        result[key].push(value);
    });
    return result;
}
export default validator;