import { getAllUsers, getSomeUsers } from "./externalAPI/usersService";
import * as express from 'express';
import * as node_cache from 'node-cache';
import * as compression from 'compression';


export async function initializeMiddlewares(app: express.Express, shouldCache: boolean = true, shouldCompress: boolean = true) {
    const cryptoCache = new node_cache({ stdTTL: 60 * 5 });

    function cachePayload(req, res, next) {
        try {
            let key = '__express__' + req.originalUrl || req.url
            if (cryptoCache.has(key) && shouldCache) {
                return res.send(cryptoCache.get(key)).status(200);
            }

            res.sendResponse = res.send
            res.send = (body) => {
                cryptoCache.set(key, body);
                res.sendResponse(body)
            }
            next()
        } catch (err) {
            console.log(err);
            throw err;
        }
    }

    app.use(cachePayload);

    if (shouldCompress && app.request.headers['Accept-Encoding']) {
        app.use(compression());
    }
};

export async function initializeRoutes(app: express.Express) {
    const router = express.Router();

    router.get('/', async (_request, response) => {
        var responseBody = await getAllUsers();
        response.send(responseBody);
    });

    router.get('/city/:nameCity', async (request, response) => {
        let cityName = request.params.nameCity as String;
        var responseBody = await getSomeUsers(cityName);
        response.send(responseBody);
    });

    app.use(router);
};