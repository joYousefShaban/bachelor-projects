import * as fs from 'fs';

export async function writeAsFile(_json: any, filname: string) {
    const content = JSON.stringify(_json, null, '\t');

    try {
        await fs.promises.writeFile(`./${filname}.json`, content, 'utf8');
        console.log('The file is saved');
    } catch (e) {
        if (typeof e === "string") {
            console.log(`Something went wrong, details: ${e}`)
        } else if (e instanceof Error) {
            console.log(`Something went wrong, details: ${e.message}`)
        };
    }
}