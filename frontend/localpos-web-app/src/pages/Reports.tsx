import React from "react";
import {Box, Typography} from "@mui/material";
import {useTranslation} from "react-i18next";

const Reports: React.FC = () => {
    const {t} = useTranslation("common");

    return (
        <Box p={4}>
            <Typography variant="h5">{t("menu.reports")}</Typography>
        </Box>
    );
};

export default Reports;
