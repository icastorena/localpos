import React from "react";
import {useTranslation} from "react-i18next";
import {MenuItem, Select, SelectChangeEvent} from "@mui/material";

const LanguageSwitcher: React.FC = () => {
    const {i18n} = useTranslation();

    const currentLang = ["en", "es"].includes(i18n.language)
        ? i18n.language
        : "en";

    const changeLanguage = (event: SelectChangeEvent) => {
        i18n.changeLanguage(event.target.value);
    };

    return (
        <Select
            value={currentLang}
            onChange={changeLanguage}
            size="small"
            sx={{minWidth: 100, ml: 2}}
        >
            <MenuItem value="en">English</MenuItem>
            <MenuItem value="es">Español</MenuItem>
        </Select>
    );
};

export default LanguageSwitcher;
