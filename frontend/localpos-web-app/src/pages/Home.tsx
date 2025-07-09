import React from "react";
import {Box, Grid, Paper, Typography, useTheme} from "@mui/material";
import InventoryIcon from "@mui/icons-material/Inventory";
import PeopleAltIcon from "@mui/icons-material/PeopleAlt";
import ReceiptIcon from "@mui/icons-material/Receipt";
import BarChartIcon from "@mui/icons-material/BarChart";
import {useTranslation} from "react-i18next";
import {useNavigate} from "react-router-dom";
import {useAuth} from "../context/useAuth";

const Home: React.FC = () => {
    const theme = useTheme();
    const {t} = useTranslation("common");
    const navigate = useNavigate();
    const {user} = useAuth();

    if (!user) return null;

    const roles = user.roles;
    const hasAnyRole = (...r: string[]) => roles.some((role) => r.includes(role));

    const cards = [
        {
            label: t("menu.users", "Users"),
            icon: <PeopleAltIcon fontSize="large" color="primary"/>,
            roles: ["OWNER", "ADMIN", "MANAGER"],
            href: "/users",
        },
        {
            label: t("menu.orders", "Orders"),
            icon: <ReceiptIcon fontSize="large" color="primary"/>,
            roles: ["OWNER", "ADMIN", "MANAGER", "CASHIER", "WAITER"],
            href: "/orders",
        },
        {
            label: t("menu.inventory", "Inventory"),
            icon: <InventoryIcon fontSize="large" color="primary"/>,
            roles: ["OWNER", "ADMIN", "MANAGER"],
            href: "/inventory",
        },
        {
            label: t("menu.reports", "Reports"),
            icon: <BarChartIcon fontSize="large" color="primary"/>,
            roles: ["OWNER", "ADMIN", "MANAGER"],
            href: "/reports",
        },
    ];

    return (
        <Box p={4}>
            <Typography variant="h4" gutterBottom>
                {t("welcome", {user: user.sub})}
            </Typography>

            <Grid container spacing={3} columns={{xs: 4, sm: 8, md: 12}}>
                {cards
                    .filter((card) => hasAnyRole(...card.roles))
                    .map((card, index) => (
                        <Grid
                            component={"div"}
                            key={index}
                            size={
                                {
                                    xs: 12,
                                    sm: 6,
                                    md: 3
                                }
                            }
                            sx={{cursor: "pointer"}}
                            onClick={() => navigate(card.href)}
                        >
                            <Paper
                                elevation={4}
                                sx={{
                                    p: 3,
                                    display: "flex",
                                    flexDirection: "column",
                                    alignItems: "center",
                                    justifyContent: "center",
                                    textAlign: "center",
                                    borderRadius: 4,
                                    transition: "transform 0.2s ease-in-out, box-shadow 0.3s",
                                    "&:hover": {
                                        transform: "scale(1.05)",
                                        boxShadow: theme.shadows[10],
                                    },
                                }}
                            >
                                {card.icon}
                                <Typography variant="h6" mt={2}>
                                    {card.label}
                                </Typography>
                            </Paper>
                        </Grid>
                    ))}
            </Grid>
        </Box>
    );
};

export default Home;
