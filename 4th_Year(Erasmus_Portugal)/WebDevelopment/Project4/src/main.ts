import * as fs from 'fs';

// Installed the following libraries:
// ==================
// npm i fs

function writeToFileBatches(
  text: String,
  filePath: string = 'output.txt'
): Promise<boolean> {
  return new Promise((resolve, reject) => {
    const writeStream = fs.createWriteStream(filePath, { flags: 'a' });
    writeStream.write(text);
    writeStream.end();
    writeStream.on("finish", () => { resolve(true); });
    writeStream.on("error", reject);
  });
}

async function getApiAsChunksWithWrite(
  url: string,
  filePath: string = 'output.txt'
): Promise<ReadableStream> {
  return fetch(url)
    .then((response) => {
      var counter: number = 0;
      const reader = response.body!.getReader();
      if (fs.existsSync(filePath)) {
        fs.unlinkSync(filePath)
      }

      return new ReadableStream({
        start(controller) {
          return pump()
          function pump() {
            return reader.read().then(async ({ done, value }) => {
              if (done) {
                console.log('Done');
                controller.close()
                return;
              }

              await writeToFileBatches(
                `\nCHUNK NO. ${counter}\n-------------------------\n`
                + new TextDecoder().decode(value),
                filePath = filePath
              ).then((result) => {
                if (result) {
                  console.log(`Finished writing chunk No. ${++counter}`);
                } else {
                  console.log(`Failed writing chunk No. ${++counter}`);
                }
              });

              controller.enqueue(value);
              return pump();
            })
          }
        }
      })
    });
}

async function main() {
  getApiAsChunksWithWrite('https://jigsaw.w3.org/HTTP/ChunkedScript')
};

main()