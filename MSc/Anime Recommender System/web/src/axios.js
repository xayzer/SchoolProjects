import axios from 'axios'

const protocol = process.env.VUE_APP_HTTP_PROTOCOL || 'http'
const port = process.env.VUE_APP_API_PORT || 3080

export const http = axios.create({
    baseURL: `${protocol}://${location.hostname}:${port}/api/`
})