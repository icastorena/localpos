# LocalPOS

**LocalPOS** is an open-source, modular, and offline-ready Point of Sale system tailored for retail stores and restaurants. It is designed to run entirely in a private or local network environment, making it ideal for internal business operations without dependency on cloud services.

---

## 🚀 Project Structure

```bash
localpos/
├── backend/
│   ├── localpos-user-service/
│   ├── localpos-product-service/
│   ├── localpos-order-service/
│   ├── localpos-inventory-service/
│   └── localpos-reporting-service/
│
├── frontend/
│   ├── localpos-web-app/
│   └── localpos-mobile-app/
│
├── infrastructure/
│   ├── docker/
│   └── nginx/
│
├── shared-libraries/
│   └── localpos-common/
│
├── docs/
│   ├── openapi/
│   ├── diagrams/
│   └── manuals/
│
├── scripts/
└── README.md
