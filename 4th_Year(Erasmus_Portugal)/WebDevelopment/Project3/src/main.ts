import fetch from 'cross-fetch';

import { renameShallowKey, deleteGeo, changeEmail } from "./myTransformations"
import { writeAsFile } from './myLoadDataDestination'

// Installed the following libraries:
// ==================
// npm i fs
// npm i cross-fetch

async function main() {
  var users: any;

  await fetch('https://jsonplaceholder.typicode.com/users')
    .then(async response => {
      if (response.ok) {
        await response.json().then((data: any) => {
          users = data;
        });
      } else {
        throw 'Something went wrong';
      }
    }).
    catch((error: any) => {
      console.log(error);
    });

  renameShallowKey(users, 'phone', 'phone_number');
  deleteGeo(users);
  changeEmail(users);
  writeAsFile(users, 'myJsonFile');

  console.log('Output');
  console.log('================================================');
  console.log(users);
};

main();