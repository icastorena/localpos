import React, {useEffect, useState} from "react";
import {Autocomplete, Box, Button, Dialog, DialogActions, DialogContent, DialogTitle, TextField,} from "@mui/material";
import {useTranslation} from "react-i18next";
import axios from "axios";
import {Field, FieldProps, Form, Formik, FormikHelpers} from "formik";
import * as Yup from "yup";
import Swal from "sweetalert2";
import withReactContent from "sweetalert2-react-content";

const MySwal = withReactContent(Swal);

interface UserFormDialogProps {
    open: boolean;
    onClose: () => void;
    onSaved: (user: User) => void;
    userToEdit?: User | null;
}

interface User {
    id: string;
    username: string;
    email: string;
    roles: { name: string }[];
    stores: { name: string }[];
    createdAt: string;
}

interface Role {
    name: string;
}

interface Store {
    name: string;
}

interface FormValues {
    username: string;
    email: string;
    password: string;
    roles: Role[];
    stores: Store[];
}

const UserFormDialog: React.FC<UserFormDialogProps> = ({
                                                           open,
                                                           onClose,
                                                           onSaved,
                                                           userToEdit,
                                                       }) => {
    const {t} = useTranslation("users");
    const [allRoles, setAllRoles] = useState<Role[]>([]);
    const [allStores, setAllStores] = useState<Store[]>([]);
    const apiUrl = import.meta.env.VITE_API_URL || "http://localhost:8080";
    const token = sessionStorage.getItem("jwt_token");

    const getHelperText = (error: unknown): string => {
        if (!error) return "";
        if (Array.isArray(error)) {
            if (typeof error[0] === "string") return error[0];
            return "";
        }
        if (typeof error === "string") return error;
        return "";
    };

    useEffect(() => {
        if (!open) return;
        const fetchRolesStores = async () => {
            try {
                const [rolesRes, storesRes] = await Promise.all([
                    axios.get<Role[]>(`${apiUrl}/api/v1/roles`, {
                        headers: {Authorization: `Bearer ${token}`},
                    }),
                    axios.get<Store[]>(`${apiUrl}/api/v1/stores`, {
                        headers: {Authorization: `Bearer ${token}`},
                    }),
                ]);
                setAllRoles(rolesRes.data);
                setAllStores(storesRes.data);
            } catch {
                MySwal.fire({icon: "error", title: t("messages.errorLoading")});
            }
        };
        fetchRolesStores();
    }, [open]);

    const initialValues: FormValues = {
        username: userToEdit?.username || "",
        email: userToEdit?.email || "",
        password: "",
        roles: userToEdit?.roles || [],
        stores: userToEdit?.stores || [],
    };

    const validationSchema = Yup.object({
        username: Yup.string().required(t("errors.usernameRequired")),
        email: Yup.string().email(t("errors.emailInvalid")).required(t("errors.emailRequired")),
        password: userToEdit
            ? Yup.string()
            : Yup.string().required(t("errors.passwordRequired")),
        roles: Yup.array().min(1, t("errors.rolesRequired")),
        stores: Yup.array().min(1, t("errors.storesRequired")),
    });

    const handleSubmit = async (
        values: FormValues,
        formikHelpers: FormikHelpers<FormValues>
    ) => {
        const {setSubmitting, setErrors} = formikHelpers;
        try {
            const payload = {
                username: values.username,
                email: values.email,
                password: values.password || undefined,
                roleNames: values.roles.map((r) => r.name),
                storeCodes: values.stores.map((s) => s.name),
            };
            let res;
            if (userToEdit) {
                res = await axios.put<User>(`${apiUrl}/api/v1/users/${userToEdit.id}`, payload, {
                    headers: {Authorization: `Bearer ${token}`},
                });
            } else {
                res = await axios.post<User>(`${apiUrl}/api/v1/users`, payload, {
                    headers: {Authorization: `Bearer ${token}`},
                });
            }
            onSaved(res.data);
            onClose();
        } catch (error: unknown) {
            const message =
                (error as any)?.response?.data?.message ||
                (error as Error).message ||
                t("messages.errorSaving");
            MySwal.fire({icon: "error", title: message});
            setErrors({username: message});
        } finally {
            setSubmitting(false);
        }
    };

    return (
        <Dialog open={open} onClose={onClose} fullWidth maxWidth="sm">
            <DialogTitle>{userToEdit ? t("buttons.edit") : t("buttons.create")}</DialogTitle>
            <Formik
                initialValues={initialValues}
                validationSchema={validationSchema}
                onSubmit={handleSubmit}
                enableReinitialize
            >
                {({values, errors, touched, isSubmitting, setFieldValue}) => (
                    <Form>
                        <DialogContent>
                            <Box display="flex" flexDirection="column" gap={2} mt={1}>
                                <Field name="username">
                                    {({field}: FieldProps) => (
                                        <TextField
                                            label={t("fields.username")}
                                            fullWidth
                                            {...field}
                                            error={touched.username && Boolean(errors.username)}
                                            helperText={touched.username && errors.username}
                                        />
                                    )}
                                </Field>
                                <Field name="email">
                                    {({field}: FieldProps) => (
                                        <TextField
                                            label={t("fields.email")}
                                            type="email"
                                            fullWidth
                                            {...field}
                                            error={touched.email && Boolean(errors.email)}
                                            helperText={touched.email && errors.email}
                                        />
                                    )}
                                </Field>
                                <Field name="password">
                                    {({field}: FieldProps) => (
                                        <TextField
                                            label={t("fields.password")}
                                            type="password"
                                            fullWidth
                                            {...field}
                                            error={touched.password && Boolean(errors.password)}
                                            helperText={
                                                userToEdit
                                                    ? t("helpers.leaveBlankToKeepPassword")
                                                    : touched.password && errors.password
                                            }
                                        />
                                    )}
                                </Field>
                                <Autocomplete
                                    multiple
                                    options={allRoles}
                                    getOptionLabel={(option) => option.name}
                                    value={values.roles}
                                    onChange={(_, value) => setFieldValue("roles", value)}
                                    renderInput={(params) => (
                                        <TextField
                                            {...params}
                                            label={t("fields.roles")}
                                            error={touched.roles && Boolean(errors.roles)}
                                            helperText={
                                                touched.roles && errors.roles ? getHelperText(errors.roles) : ""
                                            }
                                        />
                                    )}
                                />
                                <Autocomplete
                                    multiple
                                    options={allStores}
                                    getOptionLabel={(option) => option.name}
                                    value={values.stores}
                                    onChange={(_, value) => setFieldValue("stores", value)}
                                    renderInput={(params) => (
                                        <TextField
                                            {...params}
                                            label={t("fields.stores")}
                                            error={touched.stores && Boolean(errors.stores)}
                                            helperText={
                                                touched.stores && errors.stores ? getHelperText(errors.stores) : ""
                                            }
                                        />
                                    )}
                                />
                            </Box>
                        </DialogContent>
                        <DialogActions>
                            <Button onClick={onClose} disabled={isSubmitting}>
                                {t("buttons.cancel")}
                            </Button>
                            <Button type="submit" variant="contained" disabled={isSubmitting}>
                                {t("buttons.save")}
                            </Button>
                        </DialogActions>
                    </Form>
                )}
            </Formik>
        </Dialog>
    );
};

export default UserFormDialog;
