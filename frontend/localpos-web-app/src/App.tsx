import {Route, Routes} from "react-router-dom";
import Login from "./components/Login";
import Home from "./pages/Home";
import Users from "./pages/Users";
import Orders from "./pages/Orders";
import Inventory from "./pages/Inventory";
import Reports from "./pages/Reports";
import PrivateRoute from "./routes/PrivateRoute";
import AutoLogout from "./components/AutoLogout";

import AppBarMenu from "./components/layout/AppBarMenu";
import {Box} from "@mui/material";

const App = () => (
    <>
        <AutoLogout/>

        <Box sx={{mt: 8}}>
            <AppBarMenu/>
        </Box>

        <Routes>
            <Route path="/" element={<Login/>}/>
            <Route path="/login" element={<Login/>}/>
            <Route
                path="/home"
                element={
                    <PrivateRoute>
                        <Home/>
                    </PrivateRoute>
                }
            />
            <Route
                path="/users"
                element={
                    <PrivateRoute>
                        <Users/>
                    </PrivateRoute>
                }
            />
            <Route
                path="/orders"
                element={
                    <PrivateRoute>
                        <Orders/>
                    </PrivateRoute>
                }
            />
            <Route
                path="/inventory"
                element={
                    <PrivateRoute>
                        <Inventory/>
                    </PrivateRoute>
                }
            />
            <Route
                path="/reports"
                element={
                    <PrivateRoute>
                        <Reports/>
                    </PrivateRoute>
                }
            />
        </Routes>
    </>
);

export default App;
