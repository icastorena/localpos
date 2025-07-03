import Swal from "sweetalert2";
import withReactContent from "sweetalert2-react-content";
import {darkTheme} from "../theme/darkTheme";

const MySwal = withReactContent(Swal);

const DarkSwal = MySwal.mixin({
    background: darkTheme.palette.background.paper,
    color: darkTheme.palette.text.primary ?? "#fff",
    confirmButtonColor: darkTheme.palette.primary.main,
    cancelButtonColor: darkTheme.palette.secondary.main,
    customClass: {
        popup: "mui-swal-popup",
    },
});

export default DarkSwal;
