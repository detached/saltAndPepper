import { server } from './mocks/node.js'
import {mockConfig} from "./src/config/mockConfig";
import {saltAndPepperClient} from "./src/config/axiosConfig";
import '@testing-library/jest-dom';
import './src/i18n.js';
import { cleanup } from '@testing-library/react';
import {handlers} from "./mocks/handlers";

beforeAll(() => {
  mockConfig.enabled = false;
  saltAndPepperClient.baseURL = '/api/';
  server.listen()
})

afterEach(() => {
  server.resetHandlers(...handlers)
})

afterAll(() => {
  server.close()
  cleanup();
})