import React from "react";
import {Box, Typography} from "@mui/material";
import {useTranslation} from "react-i18next";

const Inventory: React.FC = () => {
    const {t} = useTranslation("common");

    return (
        <Box p={4}>
            <Typography variant="h5">{t("menu.inventory")}</Typography>
        </Box>
    );
};

export default Inventory;
