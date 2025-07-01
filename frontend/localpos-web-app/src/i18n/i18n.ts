import i18n from "i18next";
import {initReactI18next} from "react-i18next";
import LanguageDetector from "i18next-browser-languagedetector";
import HttpBackend from "i18next-http-backend";

const defaultLang = import.meta.env.VITE_DEFAULT_LANG || "en";

i18n
    .use(HttpBackend)
    .use(LanguageDetector)
    .use(initReactI18next)
    .init({
        fallbackLng: defaultLang,
        interpolation: {
            escapeValue: false,
        },
        backend: {
            loadPath: "/locales/{{lng}}/{{ns}}.json",
        },
        ns: ["common", "login"],
        defaultNS: "common",
        detection: {
            order: ["localStorage"],
            caches: ["localStorage"],
        },
    });

export default i18n;

