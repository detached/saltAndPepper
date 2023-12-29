import { setupServer } from 'msw/node'
import { handlers } from './handlers.js'

export const server = setupServer(...handlers);

server.events.on('response:mocked', ({ request, response }) => {
    console.log(
        '%s %s received %s %s',
        request.method,
        request.url,
        response.status,
        response.statusText
    )
});

server.events.on('unhandledException', ({ request, error }) => {
    console.log('%s %s errored! See details below.', request.method, request.url)
    console.error(error)
})