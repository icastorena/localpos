declare namespace NodeJS {
    interface ProcessEnv {
        readonly VITE_API_URL?: string;
    }
}

interface Process {
    env: NodeJS.ProcessEnv;
}

declare var process: Process;
