import './assets/main.css'


import { createApp } from 'vue'
import { createPinia } from 'pinia'

import App from './App.vue'
import router from './router'
import axios from "axios";


axios.defaults.baseURL = "http://localhost:8080"; // TODO: Change this to the actual server URL
axios.defaults.headers.common['Access-Control-Allow-Origin'] = 'http://localhost:5173';
// axios.defaults.headers.common['Access-Control-Allow-Origin'] = 'https:localhost:8080';
axios.defaults.headers.common['Access-Control-Allow-Methods'] = 'GET, POST, PUT, DELETE, OPTIONS, HEAD';
axios.defaults.headers.common['Access-Control-Allow-Headers'] = 'Origin, X-Requested-With, Content-Type, Accept, Authorization, X-Auth-Token';
axios.defaults.headers["x-api-token"] = localStorage.getItem("token");
axios.defaults.headers['withCredentials'] = true;
const app = createApp(App)

app.use(createPinia())
app.use(router)

app.mount('#app')
