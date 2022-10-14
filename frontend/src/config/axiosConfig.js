import axios from "axios";

export const saltAndPepperClient = axios.create({
  withCredentials: true,
  baseURL: "/",
});
