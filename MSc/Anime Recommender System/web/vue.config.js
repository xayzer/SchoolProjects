//vue.config.js
module.exports = {
    chainWebpack: config => {
        config
            .plugin('html')
            .tap(args => {
                args[0].title = "COMP7240 RS Proj No Idea Is Fine";
                return args;
            })
    }
}