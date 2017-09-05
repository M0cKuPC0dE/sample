const DEV_LOGIN_URL = 'http://localhost:8080/'

import axios from 'axios'
import https from 'https'

var http = axios.create({
  baseURL: DEV_LOGIN_URL,
  httpsAgent: new https.Agent({
    keepAlive: true,
    rejectUnauthorized: false
  })
})

export default http
