/// <reference types="vite/client" />

// TypeScript declarations for runtime env injection
interface Window {
  __VITE_API_URL__?: string;
}

declare var window: Window;

interface ImportMetaEnv {
  readonly VITE_API_URL?: string;
}

interface ImportMeta {
  readonly env: ImportMetaEnv;
}
