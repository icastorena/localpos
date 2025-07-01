import React from "react";
import {useIdleTimer} from "react-idle-timer";
import {useNavigate} from "react-router-dom";
import Swal from "sweetalert2";
import "@sweetalert2/theme-material-ui/material-ui.css";
import {useTranslation} from "react-i18next";
import {useAuth} from "../context/useAuth";

const AutoLogout: React.FC = () => {
    const navigate = useNavigate();
    const {t} = useTranslation("autoLogout");
    const {logout} = useAuth();

    const handleOnIdle = async () => {
        logout();

        await Swal.fire({
            icon: "warning",
            title: t("title", "Session expired"),
            text: t("message", "You have been logged out due to inactivity."),
            confirmButtonText: t("ok", "OK"),
            timer: 4000,
            timerProgressBar: true,
        });

        navigate("/login");
    };

    useIdleTimer({
        timeout: 1000 * 60 * 15,
        onIdle: handleOnIdle,
        debounce: 500,
    });

    return null;
};

export default AutoLogout;
