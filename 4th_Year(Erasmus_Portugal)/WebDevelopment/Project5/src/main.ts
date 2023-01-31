import * as express from 'express';
import { initializeMiddlewares, initializeRoutes } from "./routes";


async function main() {
  const app = express();

  initializeMiddlewares(app);
  initializeRoutes(app);

  app.listen(5000);
}

main();