export async function getUsersFromApi() {
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

    return users;
};