from pathlib import Path
import shutil


OUTPUT = Path(__file__).resolve().parents[1] / "frontend" / "public" / "sample-bills"


SAMPLES = {
    "tcs_it_services_normal.pdf": [
        "SHAKSHAM SAMPLE VENDOR - TATA CONSULTANCY SERVICES",
        "TAX INVOICE | Category: IT Services",
        "Invoice Number: TCS-TEST-1001 | Invoice Date: 2026-10-02",
        "Due Date: 2026-11-01",
        "Bill To: Shaksham Global Enterprises",
        "Description: Monthly application support and maintenance",
        "Quantity: 1 | Unit Price: INR 69700.00 | Line Total: INR 69700.00",
        "Subtotal: INR 69700.00 | GST: INR 15300.00 | Total: INR 85000.00",
        "Payment Terms: Net 30 days",
        "DEMO DOCUMENT - No payment is due.",
    ],
    "reliance_power_normal.pdf": [
        "SHAKSHAM SAMPLE VENDOR - RELIANCE POWER LTD",
        "TAX INVOICE | Category: Utilities",
        "Invoice Number: RPL-TEST-2201 | Invoice Date: 2026-10-04",
        "Due Date: 2026-11-03",
        "Bill To: Shaksham Global Enterprises",
        "Description: Electricity supply and distribution charges",
        "Quantity: 1 | Unit Price: INR 39975.00 | Line Total: INR 39975.00",
        "Subtotal: INR 39975.00 | GST: INR 8775.00 | Total: INR 48750.00",
        "Payment Terms: Net 30 days",
        "DEMO DOCUMENT - No payment is due.",
    ],
    "airtel_fiber_normal.pdf": [
        "SHAKSHAM SAMPLE VENDOR - AIRTEL BUSINESS FIBER",
        "TAX INVOICE | Category: Utilities",
        "Invoice Number: ABF-TEST-3102 | Invoice Date: 2026-10-06",
        "Due Date: 2026-11-05",
        "Bill To: Shaksham Global Enterprises",
        "Description: Leased line internet service - October",
        "Quantity: 1 | Unit Price: INR 26240.00 | Line Total: INR 26240.00",
        "Subtotal: INR 26240.00 | GST: INR 5760.00 | Total: INR 32000.00",
        "Payment Terms: Net 30 days",
        "DEMO DOCUMENT - No payment is due.",
    ],
    "amazon_cloud_normal.pdf": [
        "SHAKSHAM SAMPLE VENDOR - AMAZON WEB SERVICES CLOUD",
        "TAX INVOICE | Category: Cloud Infrastructure",
        "Invoice Number: AWS-TEST-4203 | Invoice Date: 2026-10-08",
        "Due Date: 2026-11-07",
        "Bill To: Shaksham Global Enterprises",
        "Description: Cloud compute, storage, and data transfer usage",
        "Quantity: 1 | Unit Price: INR 159900.00 | Line Total: INR 159900.00",
        "Subtotal: INR 159900.00 | GST: INR 35100.00 | Total: INR 195000.00",
        "Payment Terms: Net 30 days",
        "DEMO DOCUMENT - No payment is due.",
    ],
    "office_depot_normal.pdf": [
        "SHAKSHAM SAMPLE VENDOR - OFFICE DEPOT LOGISTICS",
        "TAX INVOICE | Category: Supplies",
        "Invoice Number: ODL-TEST-5304 | Invoice Date: 2026-10-09",
        "Due Date: 2026-11-08",
        "Bill To: Shaksham Global Enterprises",
        "Description: Office chairs, desks, and stationery supplies",
        "Quantity: 1 | Unit Price: INR 52890.00 | Line Total: INR 52890.00",
        "Subtotal: INR 52890.00 | GST: INR 11610.00 | Total: INR 64500.00",
        "Payment Terms: Net 30 days",
        "DEMO DOCUMENT - No payment is due.",
    ],
    "dell_high_value_anomaly.pdf": [
        "SHAKSHAM SAMPLE VENDOR - DELL TECHNOLOGIES INDIA",
        "TAX INVOICE | Category: IT Equipment",
        "Invoice Number: DELL-TEST-9001 | Invoice Date: 2026-10-10",
        "Due Date: 2026-11-09",
        "Bill To: Shaksham Global Enterprises",
        "Description: Enterprise server and data center hardware",
        "Quantity: 1 | Unit Price: INR 615000.00 | Line Total: INR 615000.00",
        "Subtotal: INR 615000.00 | GST: INR 135000.00 | Total: INR 750000.00",
        "Payment Terms: Net 30 days",
        "DEMO ANOMALY - High value sample; no payment is due.",
    ],
    "dell_second_high_value_anomaly.pdf": [
        "SHAKSHAM SAMPLE VENDOR - DELL TECHNOLOGIES INDIA",
        "TAX INVOICE | Category: IT Equipment",
        "Invoice Number: DELL-TEST-9002 | Invoice Date: 2026-10-12",
        "Due Date: 2026-11-11",
        "Bill To: Shaksham Global Enterprises",
        "Description: Storage array expansion and installation",
        "Quantity: 1 | Unit Price: INR 615000.00 | Line Total: INR 615000.00",
        "Subtotal: INR 615000.00 | GST: INR 135000.00 | Total: INR 750000.00",
        "Payment Terms: Net 30 days",
        "DEMO ANOMALY - High value sample; no payment is due.",
    ],
}


def pdf_escape(value: str) -> str:
    return value.replace("\\", "\\\\").replace("(", "\\(").replace(")", "\\)")


def create_pdf(path: Path, lines: list[str]) -> None:
    text_commands = ["BT", "/F1 11 Tf"]
    y = 790
    for index, line in enumerate(lines):
        text_commands.append(f"1 0 0 1 52 {y} Tm ({pdf_escape(line)}) Tj")
        y -= 25 if index == 0 else 20
    text_commands.append("ET")
    stream = "\n".join(text_commands).encode("ascii")

    objects = [
        b"<< /Type /Catalog /Pages 2 0 R >>",
        b"<< /Type /Pages /Kids [3 0 R] /Count 1 >>",
        b"<< /Type /Page /Parent 2 0 R /MediaBox [0 0 612 842] /Resources << /Font << /F1 4 0 R >> >> /Contents 5 0 R >>",
        b"<< /Type /Font /Subtype /Type1 /BaseFont /Helvetica >>",
        b"<< /Length " + str(len(stream)).encode("ascii") + b" >>\nstream\n" + stream + b"\nendstream",
    ]

    data = bytearray(b"%PDF-1.4\n%\xe2\xe3\xcf\xd3\n")
    offsets = [0]
    for number, body in enumerate(objects, start=1):
        offsets.append(len(data))
        data.extend(f"{number} 0 obj\n".encode("ascii"))
        data.extend(body)
        data.extend(b"\nendobj\n")

    xref_offset = len(data)
    data.extend(f"xref\n0 {len(offsets)}\n".encode("ascii"))
    data.extend(b"0000000000 65535 f \n")
    for offset in offsets[1:]:
        data.extend(f"{offset:010d} 00000 n \n".encode("ascii"))
    data.extend(
        f"trailer\n<< /Size {len(offsets)} /Root 1 0 R >>\nstartxref\n{xref_offset}\n%%EOF\n".encode("ascii")
    )
    path.write_bytes(data)


def main() -> None:
    OUTPUT.mkdir(parents=True, exist_ok=True)
    for filename, lines in SAMPLES.items():
        create_pdf(OUTPUT / filename, lines)
    # Re-uploading these exact same bytes demonstrates file-hash duplicate detection.
    shutil.copyfile(OUTPUT / "tcs_it_services_normal.pdf", OUTPUT / "duplicate_upload_same_file.pdf")
    print(f"Generated {len(SAMPLES) + 1} sample PDFs in {OUTPUT}")


if __name__ == "__main__":
    main()
