import axios from 'axios'
import https from 'https'

var http = axios.create({
  baseURL: process.env.baseUrl,
  httpsAgent: new https.Agent({
    keepAlive: true,
    rejectUnauthorized: false
  })
})

export default http
