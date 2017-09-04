const DEV_LOGIN_URL = 'https://compliance.mitrphol.com/'

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
