const HtmlWebPackPlugin = require("html-webpack-plugin");

module.exports = {

  entry : "./src/code/main.tsx",

  resolve: {
    extensions: [".tsx", ".ts", ".jsx", ".js", "..."],
    fallback: { "http": false, "browser": false, "https": false, "zlib": false,
      "stream": false, "url": false, "buffer": false, "timers": false, "assert": false, "axios": false 
    }
  },

  module : {
    rules : [
      {
        test : /\.html$/,
        use : { loader : "html-loader" }
      },
      {
        test : /\.css$/,
        use : [ "style-loader", "css-loader"]
      },
      {
        test : /\.tsx?$/,
        loader: 'awesome-typescript-loader'
      }
    ]

  },

  plugins : [
    new HtmlWebPackPlugin({ template : "./src/index.html", filename : "./index.html" })
  ],

  performance : { hints : false },
  watch : true,
  devtool : "source-map"

};
