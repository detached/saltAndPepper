import axios from "axios";

export const saltAndPepperClient = axios.create({
  withCredentials: true,
  //baseURL: "/api/",
  //baseURL: "http://localhost:8080/api/",
  baseURL: "/",
});
