import React, {useContext, useEffect, useMemo} from "react";
import {Alert, Box, Button, IconButton, Menu, MenuItem, TextField, Typography} from "@mui/material";
import {Form, Formik} from "formik";
import * as Yup from "yup";
import axios from "axios";
import {useTranslation} from "react-i18next";
import LanguageIcon from "@mui/icons-material/Language";
import {useNavigate} from "react-router-dom";
import {AuthContext} from "../context/AuthContext";

interface LoginFormValues {
    username: string;
    password: string;
}

const initialValues: LoginFormValues = {username: "", password: ""};

const Login: React.FC = () => {
    const {t, i18n} = useTranslation("login");
    const navigate = useNavigate();
    const {login} = useContext(AuthContext);

    const [error, setError] = React.useState<string | null>(null);
    const [anchorLang, setAnchorLang] = React.useState<null | HTMLElement>(null);

    const validationSchema = useMemo(() => {
        return Yup.object().shape({
            username: Yup.string().required(t("errors.usernameRequired")),
            password: Yup.string().required(t("errors.passwordRequired")),
        });
    }, [t]);

    const handleLangMenuOpen = (event: React.MouseEvent<HTMLElement>) => {
        setAnchorLang(event.currentTarget);
    };
    const handleLangMenuClose = () => setAnchorLang(null);

    const changeLanguage = (lng: string) => {
        i18n.changeLanguage(lng);
        handleLangMenuClose();
    };

    const apiUrl = import.meta.env.VITE_AUTH_API_URL || "http://localhost:8081";

    const handleSubmit = async (values: LoginFormValues) => {
        setError(null);
        try {
            const response = await axios.post(`${apiUrl}/auth/login`, values);
            const token = response.data.token;
            login(token);
            navigate("/home");
        } catch (err: any) {
            setError(err.response?.data?.message || t("errors.loginFailed"));
        }
    };

    return (
        <Box
            maxWidth={400}
            mx="auto"
            mt={8}
            p={4}
            boxShadow={3}
            borderRadius={2}
            bgcolor="background.paper"
            position="relative"
        >
            <Box position="absolute" top={8} right={8}>
                <IconButton size="large" color="inherit" onClick={handleLangMenuOpen}>
                    <LanguageIcon/>
                </IconButton>
                <Menu
                    anchorEl={anchorLang}
                    open={Boolean(anchorLang)}
                    onClose={handleLangMenuClose}
                    anchorOrigin={{vertical: "bottom", horizontal: "right"}}
                    transformOrigin={{vertical: "top", horizontal: "right"}}
                >
                    <MenuItem onClick={() => changeLanguage("es")}>Español</MenuItem>
                    <MenuItem onClick={() => changeLanguage("en")}>English</MenuItem>
                </Menu>
            </Box>

            <Typography variant="h5" mb={3} align="center">
                {t("title")}
            </Typography>

            {error && (
                <Alert severity="error" sx={{mb: 2}}>
                    {error}
                </Alert>
            )}

            <Formik
                initialValues={initialValues}
                validationSchema={validationSchema}
                onSubmit={handleSubmit}
            >
                {({errors, touched, handleChange, values, validateForm}) => {
                    useEffect(() => {
                        validateForm();
                    }, [i18n.language, validateForm]);

                    return (
                        <Form>
                            <TextField
                                fullWidth
                                margin="normal"
                                id="username"
                                name="username"
                                label={t("username")}
                                value={values.username}
                                onChange={handleChange}
                                error={touched.username && Boolean(errors.username)}
                                helperText={touched.username && errors.username ? errors.username : ""}
                            />

                            <TextField
                                fullWidth
                                margin="normal"
                                id="password"
                                name="password"
                                label={t("password")}
                                type="password"
                                value={values.password}
                                onChange={handleChange}
                                error={touched.password && Boolean(errors.password)}
                                helperText={touched.password && errors.password ? errors.password : ""}
                            />

                            <Button
                                type="submit"
                                variant="contained"
                                color="primary"
                                fullWidth
                                sx={{mt: 3}}
                            >
                                {t("submit")}
                            </Button>
                        </Form>
                    );
                }}
            </Formik>
        </Box>
    );
};

export default Login;
