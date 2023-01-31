import { getUsersFromApi } from "./apiCallHelper";

export async function getAllUsers() {
    var users = await getUsersFromApi();

    return users;
};

export async function getSomeUsers(cityName: String) {
    var users = await getUsersFromApi();

    users = users.filter((user: any) => user['address']['city'].toLowerCase() === cityName.toLowerCase());
    return users;
};