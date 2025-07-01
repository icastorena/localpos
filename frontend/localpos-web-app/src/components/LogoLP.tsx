import React from "react";

const LogoLP: React.FC<{ size?: number }> = ({ size = 40 }) => {
    return (
        <svg
            width={size}
            height={size}
            viewBox="0 0 100 100"
            role="img"
            aria-label="Local POS Logo"
            xmlns="http://www.w3.org/2000/svg"
            style={{ cursor: "pointer" }}
        >
            <circle cx="50" cy="50" r="48" fill="#1976d2" />
            <text
                x="50%"
                y="55%"
                textAnchor="middle"
                fontFamily="Montserrat, Arial, sans-serif"
                fontWeight="700"
                fontSize="48"
                fill="#fff"
                dominantBaseline="middle"
            >
                LP
            </text>
        </svg>
    );
};

export default LogoLP;
