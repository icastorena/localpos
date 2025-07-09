import React, {useEffect, useState} from "react";
import {AppBar, Box, Button, IconButton, Menu, MenuItem, Toolbar, Typography,} from "@mui/material";
import {AccountCircle, Language as LanguageIcon} from "@mui/icons-material";
import {useTranslation} from "react-i18next";
import {useNavigate} from "react-router-dom";
import LogoLP from "../LogoLP";
import {useAuth} from "../../context/useAuth";

const AppBarMenu: React.FC = () => {
    const {t, i18n} = useTranslation("common");
    const navigate = useNavigate();
    const {token, user, logout} = useAuth();

    const [anchorUser, setAnchorUser] = useState<null | HTMLElement>(null);
    const [anchorLang, setAnchorLang] = useState<null | HTMLElement>(null);

    useEffect(() => {
        if (!token) {
            setAnchorUser(null);
            setAnchorLang(null);
        }
    }, [token]);

    const handleUserMenuOpen = (event: React.MouseEvent<HTMLElement>) => {
        setAnchorUser(event.currentTarget);
    };

    const handleUserMenuClose = () => setAnchorUser(null);
    const handleLangMenuOpen = (event: React.MouseEvent<HTMLElement>) =>
        setAnchorLang(event.currentTarget);
    const handleLangMenuClose = () => setAnchorLang(null);

    const handleLogout = () => {
        setAnchorUser(null);
        setAnchorLang(null);
        logout();
        navigate("/login");
    };

    const changeLanguage = (lng: string) => {
        i18n.changeLanguage(lng);
        handleLangMenuClose();
    };

    if (!token || !user) return null;

    const roles = user.roles;
    const hasRole = (...allowed: string[]) =>
        roles.some((r) => allowed.includes(r));

    return (
        <AppBar position="fixed" elevation={4}>
            <Toolbar sx={{justifyContent: "space-between"}}>
                <Box
                    sx={{display: "flex", alignItems: "center", cursor: "pointer"}}
                    onClick={() => navigate("/home")}
                >
                    <LogoLP size={40}/>
                    <Typography variant="h6" noWrap sx={{ml: 1, color: "white"}}>
                        LocalPOS
                    </Typography>
                </Box>

                <Box>
                    {hasRole("OWNER", "ADMIN", "MANAGER", "CASHIER", "WAITER") && (
                        <Button color="inherit" href="/home">
                            {t("menu.home")}
                        </Button>
                    )}
                    {hasRole("OWNER", "ADMIN", "MANAGER") && (
                        <Button color="inherit" href="/users">
                            {t("menu.users")}
                        </Button>
                    )}
                    {hasRole("OWNER", "ADMIN", "MANAGER", "CASHIER", "WAITER") && (
                        <Button color="inherit" href="/orders">
                            {t("menu.orders")}
                        </Button>
                    )}
                    {hasRole("OWNER", "ADMIN", "MANAGER") && (
                        <Button color="inherit" href="/inventory">
                            {t("menu.inventory")}
                        </Button>
                    )}
                    {hasRole("OWNER", "ADMIN", "MANAGER") && (
                        <Button color="inherit" href="/reports">
                            {t("menu.reports")}
                        </Button>
                    )}
                </Box>

                <Box display="flex" alignItems="center" gap={1}>
                    <Typography variant="body1" sx={{mr: 2}}>
                        {user.sub}
                    </Typography>

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

                    <IconButton size="large" color="inherit" onClick={handleUserMenuOpen}>
                        <AccountCircle/>
                    </IconButton>
                    <Menu
                        anchorEl={anchorUser}
                        open={Boolean(anchorUser)}
                        onClose={handleUserMenuClose}
                        anchorOrigin={{vertical: "bottom", horizontal: "right"}}
                        transformOrigin={{vertical: "top", horizontal: "right"}}
                    >
                        <MenuItem disabled>{user.sub}</MenuItem>
                        <MenuItem onClick={handleLogout}>{t("logout")}</MenuItem>
                    </Menu>
                </Box>
            </Toolbar>
        </AppBar>
    );
};

export default AppBarMenu;
