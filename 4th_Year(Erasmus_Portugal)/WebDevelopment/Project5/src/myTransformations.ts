export function renameShallowKey(_json: any, from: string, to: string) {
    _json.forEach(obj => {
        obj[to] = obj[from];
        delete obj[from];
    });
}

export function deleteGeo(_json: any) {
    _json.forEach(obj => {
        delete obj['address']['geo'];
    });
}

export function changeEmail(_json: any) {
    _json.forEach(obj => {
        obj['email'] = obj['email'].replace(/@.*$/, '@ualg.pt');
    });
}