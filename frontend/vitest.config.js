import { defineConfig } from 'vitest/config'
import react from "@vitejs/plugin-react"

export default defineConfig({
  plugins: [react()],
  optimizeDeps: {
    include: ['src/**/*.jsx'],
  },
  test: {
    globals: true,
    css: false,
    setupFiles: ['./vitest.setup.js'],
    environment: 'jsdom'
  },
})