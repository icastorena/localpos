import React, {useEffect, useMemo, useState} from "react";
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
    firstName: string;
    lastName: string;
    phone?: string;
    address?: string;
    isActive: boolean;
    roles: { name: string }[];
    stores: { name: string }[];
    createdAt: string;
    updatedAt: string;
}

interface Role {
    id: string;
    name: string;
    description: string;
}

interface Store {
    id: string;
    code: string;
    name: string;
    address: string;
    createdAt: string;
    updatedAt: string;
}

interface FormValues {
    username: string;
    email: string;
    password: string;
    firstName: string;
    lastName: string;
    phone: string;
    address: string;
    roles: Role[];
    stores: Store[];
}

const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;

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
    }, [open, apiUrl, token, t]);

    const initialValues: FormValues = useMemo(() => {
        const empty: FormValues = {
            username: "",
            email: "",
            password: "",
            firstName: "",
            lastName: "",
            phone: "",
            address: "",
            roles: [],
            stores: [],
        };

        if (!userToEdit || !allRoles.length || !allStores.length) return empty;

        return {
            username: userToEdit.username,
            email: userToEdit.email,
            password: "",
            firstName: userToEdit.firstName || "",
            lastName: userToEdit.lastName || "",
            phone: userToEdit.phone || "",
            address: userToEdit.address || "",
            roles: allRoles.filter((r) => userToEdit.roles.some((ur) => ur.name === r.name)),
            stores: allStores.filter((s) => userToEdit.stores.some((us) => us.name === s.name)),
        };
    }, [userToEdit, allRoles, allStores]);

    const validationSchema = Yup.object({
        username: Yup.string().required(t("errors.usernameRequired")),
        email: Yup.string().email(t("errors.emailInvalid")).required(t("errors.emailRequired")),
        firstName: Yup.string().required(t("errors.firstNameRequired")),
        lastName: Yup.string().required(t("errors.lastNameRequired")),
        phone: Yup.string(),
        address: Yup.string(),
        ...(userToEdit
            ? {}
            : {
                password: Yup.string()
                    .required(t("errors.passwordRequired"))
                    .matches(passwordRegex, t("errors.passwordStrong")),
            }),
        roles: Yup.array().min(1, t("errors.rolesRequired")),
        stores: Yup.array().min(1, t("errors.storesRequired")),
    });

    const handleSubmit = async (
        values: FormValues,
        {setSubmitting, setErrors}: FormikHelpers<FormValues>
    ) => {
        try {
            const payload: any = {
                username: values.username,
                email: values.email,
                firstName: values.firstName,
                lastName: values.lastName,
                phone: values.phone,
                address: values.address,
                roleNames: values.roles.map((r) => r.name),
                storeCodes: values.stores.map((s) => s.code),
            };
            if (!userToEdit && values.password) payload.password = values.password;

            const res = userToEdit
                ? await axios.put<User>(`${apiUrl}/api/v1/users/${userToEdit.id}`, payload, {
                    headers: {Authorization: `Bearer ${token}`},
                })
                : await axios.post<User>(`${apiUrl}/api/v1/users`, payload, {
                    headers: {Authorization: `Bearer ${token}`},
                });

            onSaved(res.data);
            onClose();
            await MySwal.fire({
                icon: "success",
                title: userToEdit ? t("messages.userUpdated") : t("messages.userCreated"),
            });
        } catch (error: any) {
            const message = error?.response?.data?.message || error.message || t("messages.errorSaving");
            await MySwal.fire({icon: "error", title: message});
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
                                            fullWidth
                                            {...field}
                                            error={touched.email && Boolean(errors.email)}
                                            helperText={touched.email && errors.email}
                                        />
                                    )}
                                </Field>

                                {!userToEdit && (
                                    <Field name="password">
                                        {({field}: FieldProps) => (
                                            <TextField
                                                label={t("fields.password")}
                                                type="password"
                                                fullWidth
                                                {...field}
                                                error={touched.password && Boolean(errors.password)}
                                                helperText={touched.password && errors.password}
                                            />
                                        )}
                                    </Field>
                                )}

                                <Field name="firstName">
                                    {({field}: FieldProps) => (
                                        <TextField
                                            label={t("fields.firstName")}
                                            fullWidth
                                            {...field}
                                            error={touched.firstName && Boolean(errors.firstName)}
                                            helperText={touched.firstName && errors.firstName}
                                        />
                                    )}
                                </Field>

                                <Field name="lastName">
                                    {({field}: FieldProps) => (
                                        <TextField
                                            label={t("fields.lastName")}
                                            fullWidth
                                            {...field}
                                            error={touched.lastName && Boolean(errors.lastName)}
                                            helperText={touched.lastName && errors.lastName}
                                        />
                                    )}
                                </Field>

                                <Field name="phone">
                                    {({field}: FieldProps) => (
                                        <TextField label={t("fields.phone")} fullWidth {...field} />
                                    )}
                                </Field>

                                <Field name="address">
                                    {({field}: FieldProps) => (
                                        <TextField label={t("fields.address")} fullWidth multiline
                                                   rows={2} {...field} />
                                    )}
                                </Field>

                                <Autocomplete
                                    multiple
                                    options={allRoles}
                                    getOptionLabel={(option) => t(`rolesDisplay.${option.name}`) || option.name}
                                    value={values.roles}
                                    onChange={(_, value) => setFieldValue("roles", value)}
                                    renderInput={(params) => (
                                        <TextField
                                            {...params}
                                            label={t("fields.roles")}
                                            error={touched.roles && Boolean(errors.roles)}
                                            helperText={touched.roles && errors.roles ? String(errors.roles) : ""}
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
                                            helperText={touched.stores && errors.stores ? String(errors.stores) : ""}
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
