import { instantiate } from './wasm.uninstantiated.mjs';

await wasmSetup;
instantiate({ skia: Module['asm'] });