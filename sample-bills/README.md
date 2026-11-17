# BillFlow sample invoices

Download the PDFs from the Upload page's **Sample invoices** section, or open `frontend/public/sample-bills` directly.

## Workflow walkthrough

1. Sign in as `admin@shaksham.com` with `Password@123`.
2. In **User Management**, create a Clerk, Assistant Officer, and Higher Officer in the active tenant.
3. Upload one of the normal PDFs and confirm the extracted bill. New bills enter the admin's unassigned `PENDING` queue.
4. In **Bills & Approvals**, assign it to the Clerk.
5. Sign in as that Clerk, check/edit details, and submit it for Assistant Officer review.
6. Sign in as the Assistant Officer and forward it to the Higher Officer.
7. Sign in as the Higher Officer and approve it. The Admin can then mark it paid after an external payment is made.

## Samples

| File | Expected demo behavior |
| --- | --- |
| `tcs_it_services_normal.pdf` | Normal IT support invoice, INR 85,000 |
| `reliance_power_normal.pdf` | Normal utilities invoice, INR 48,750 |
| `airtel_fiber_normal.pdf` | Normal internet service invoice, INR 32,000 |
| `amazon_cloud_normal.pdf` | Normal cloud invoice, INR 195,000 |
| `office_depot_normal.pdf` | Normal supplies invoice, INR 64,500 |
| `dell_high_value_anomaly.pdf` | Dell hardware invoice above INR 500,000; expected HIGH anomaly |
| `dell_second_high_value_anomaly.pdf` | A second high-value Dell invoice; expected HIGH anomaly |
| `duplicate_upload_same_file.pdf` | Byte-for-byte copy of the TCS PDF. Submit the TCS PDF first, then upload this copy to trigger duplicate-file detection. |

The local mock extractor uses vendor keywords plus simple text parsing. The Dell samples cross the app's INR 500,000 threshold. The duplicate sample only triggers after the identical TCS file has already been submitted and stored. These are fictional demo invoices and must not be used for real payment.
