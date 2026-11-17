# Deploy BillFlow AI on Render

The repository includes a Render Blueprint (`render.yaml`) for a public demo deployment. It creates a React static site, a Spring Boot API, and PostgreSQL.

## Before starting

- Push the deployment changes to the GitHub `main` branch.
- Sign in to Render and create a new Blueprint from `mohiuddin2k2/multitenant-ai-bill-platform`.
- When Render asks for `BOOTSTRAP_ADMIN_PASSWORD`, enter a strong password of at least 12 characters. Save it securely. Sign in as `admin@shaksham.com` with that password.

The deployment disables the pre-seeded sample users and replaces the shared sample admin password with the secret provided to Render. Use the admin account to create your clerk and officer accounts.

## Demo data and limits

This Blueprint uses Render's free plans. Free web services can sleep when idle, and the free PostgreSQL instance is temporary and has a 30-day lifetime. The backend's local upload directory is also temporary, so uploaded files can disappear after a redeploy or restart. This setup is suitable for a disposable demo; before using real bills or relying on saved records, upgrade to persistent paid hosting and storage.

The web URL will be `https://billflow-web.onrender.com` if that service name is available in Render. If Render assigns another hostname, update `APP_CORS_ALLOWED_ORIGINS` on `billflow-api` and `VITE_API_BASE_URL` on `billflow-web` to match, then redeploy both services.
