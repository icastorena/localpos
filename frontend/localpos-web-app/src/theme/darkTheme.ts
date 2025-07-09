import {createTheme} from "@mui/material";

export const darkTheme = createTheme({
    palette: {
        mode: "dark",
        primary: {main: "#00bcd4"},
        secondary: {main: "#ff9800"},
        background: {
            default: "#121212",
            paper: "#1e1e1e",
        },
    },
    shape: {borderRadius: 12},
});
