import React, { useState } from 'react';
import api from '../services/api';
import { useNavigate } from 'react-router-dom';
import { 
  UploadCloud, 
  FileCheck, 
  Sparkles, 
  AlertTriangle, 
  CheckCircle2, 
  FileText,
  DollarSign,
  Calendar,
  Layers,
  ArrowRight
} from 'lucide-react';
import AnomalyBadge from '../components/AnomalyBadge';

export default function UploadBill() {
  const [file, setFile] = useState(null);
  const [uploading, setUploading] = useState(false);
  const [jobStatus, setJobStatus] = useState(null);
  const [extractedData, setExtractedData] = useState(null);
  const [anomalyAnalysis, setAnomalyAnalysis] = useState(null);
  const [submitting, setSubmitting] = useState(false);
  const [notes, setNotes] = useState('');
  const navigate = useNavigate();

  const handleFileChange = (e) => {
    if (e.target.files && e.target.files[0]) {
      setFile(e.target.files[0]);
    }
  };

  const handleUploadAndExtract = async () => {
    if (!file) return;
    setUploading(true);
    setJobStatus({ status: 'PENDING' });
    setExtractedData(null);
    setAnomalyAnalysis(null);

    const formData = new FormData();
    formData.append('file', file);

    try {
      const res = await api.post('/ai/extract-bill', formData, {
        headers: { 'Content-Type': 'multipart/form-data' }
      });
      const jobId = res.data.jobId;
      pollJobStatus(jobId);
    } catch (err) {
      alert(err.response?.data?.message || 'Upload failed');
      setUploading(false);
    }
  };

  const pollJobStatus = (jobId) => {
    const interval = setInterval(async () => {
      try {
        const res = await api.get(`/ai/jobs/${jobId}`);
        setJobStatus(res.data);

        if (res.data.status === 'COMPLETED') {
          clearInterval(interval);
          setUploading(false);
          setExtractedData(res.data.extractedData);
          setAnomalyAnalysis(res.data.anomalyAnalysis);
        } else if (res.data.status === 'FAILED') {
          clearInterval(interval);
          setUploading(false);
          alert('AI extraction failed: ' + res.data.errorMessage);
        }
      } catch (err) {
        clearInterval(interval);
        setUploading(false);
      }
    }, 1000);
  };

  const handleConfirmSubmission = async () => {
    if (!extractedData) return;
    setSubmitting(true);
    try {
      const payload = {
        invoiceNumber: extractedData.invoiceNumber,
        vendorName: extractedData.vendorName,
        vendorTaxId: extractedData.vendorTaxId,
        category: extractedData.category,
        billDate: extractedData.billDate,
        dueDate: extractedData.dueDate,
        currency: extractedData.currency || 'INR',
        subtotalAmount: extractedData.subtotalAmount,
        taxAmount: extractedData.taxAmount,
        totalAmount: extractedData.totalAmount,
        aiConfidenceScore: extractedData.confidenceScore,
        notes: notes,
        items: extractedData.items || []
      };

      await api.post('/bills', payload);
      navigate('/bills');
    } catch (err) {
      alert(err.response?.data?.message || 'Bill creation failed');
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <div className="max-w-4xl mx-auto space-y-6">
      {/* Title */}
      <div>
        <h1 className="text-2xl font-bold text-white tracking-tight flex items-center gap-2">
          <Sparkles className="w-6 h-6 text-indigo-400" />
          <span>AI Bill Ingestion Engine</span>
        </h1>
        <p className="text-slate-400 text-sm">
          Upload any PDF invoice or image. Spring AI extracts line items, validates tax data, and triggers anomaly detection in the background.
        </p>
      </div>

      {/* File Dropzone */}
      <div className="p-8 border-2 border-dashed border-slate-700 hover:border-indigo-500/60 rounded-2xl bg-slate-900/40 text-center transition-all">
        <UploadCloud className="w-12 h-12 text-indigo-400 mx-auto mb-3" />
        <h3 className="text-sm font-semibold text-slate-200 mb-1">
          {file ? file.name : 'Select or drag your bill file here'}
        </h3>
        <p className="text-xs text-slate-500 mb-4">Supports PDF, PNG, JPG (Max 25MB)</p>

        <label className="inline-flex items-center px-4 py-2 bg-slate-800 hover:bg-slate-700 text-slate-200 rounded-xl text-xs font-semibold cursor-pointer border border-slate-700 transition-all">
          <span>Choose File</span>
          <input type="file" onChange={handleFileChange} accept=".pdf,image/*" className="hidden" />
        </label>

        {file && !uploading && !extractedData && (
          <div className="mt-4">
            <button
              onClick={handleUploadAndExtract}
              className="px-6 py-2.5 bg-indigo-600 hover:bg-indigo-500 text-white rounded-xl text-sm font-semibold shadow-lg shadow-indigo-600/20 transition-all"
            >
              Start Async AI Extraction
            </button>
          </div>
        )}

        {/* Live Processing Indicator */}
        {uploading && (
          <div className="mt-6 p-4 bg-indigo-950/40 border border-indigo-500/30 rounded-xl max-w-md mx-auto text-center space-y-2">
            <div className="flex items-center justify-center space-x-2 text-indigo-300 font-semibold text-sm">
              <span className="w-2.5 h-2.5 rounded-full bg-indigo-400 animate-ping" />
              <span>AI Worker Processing Queue...</span>
            </div>
            <p className="text-xs text-slate-400">
              Parsing PDF text, invoking Spring AI model, executing duplicate & statistical anomaly scans.
            </p>
          </div>
        )}
      </div>

      {/* Extracted Data Form & Anomaly Alerts */}
      {extractedData && (
        <div className="p-6 bg-slate-900/70 border border-slate-800 rounded-2xl space-y-6 shadow-2xl">
          <div className="flex flex-col sm:flex-row sm:items-center justify-between pb-4 border-b border-slate-800 gap-2">
            <div>
              <h2 className="text-lg font-bold text-white flex items-center gap-2">
                <FileCheck className="w-5 h-5 text-emerald-400" />
                <span>Extracted Bill Details</span>
              </h2>
              <p className="text-xs text-slate-400">Review auto-populated fields before submitting to approval pipeline</p>
            </div>

            <div className="flex items-center gap-2">
              <span className="text-xs px-2.5 py-1 rounded-full bg-emerald-500/20 text-emerald-400 border border-emerald-500/30 font-semibold">
                AI Confidence: {extractedData.confidenceScore}%
              </span>
            </div>
          </div>

          {/* Anomaly & Duplicate Diagnostic Card */}
          {anomalyAnalysis && anomalyAnalysis.anomaly && (
            <div className="p-4 bg-rose-500/10 border border-rose-500/40 rounded-xl space-y-2">
              <div className="flex items-center space-x-2 text-rose-400 font-bold text-sm">
                <AlertTriangle className="w-4 h-4 shrink-0" />
                <span>Risk Diagnostics Flagged: {anomalyAnalysis.severity}</span>
              </div>
              <ul className="text-xs text-rose-300 list-disc list-inside space-y-1">
                {anomalyAnalysis.anomalyReasons?.map((r, i) => (
                  <li key={i}>{r}</li>
                ))}
              </ul>
            </div>
          )}

          {/* Form Fields Preview */}
          <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
            <div>
              <label className="block text-xs font-medium text-slate-400 mb-1">Vendor Name</label>
              <input
                type="text"
                value={extractedData.vendorName}
                onChange={(e) => setExtractedData({ ...extractedData, vendorName: e.target.value })}
                className="w-full bg-slate-800 border border-slate-700 rounded-lg p-2 text-sm text-white"
              />
            </div>

            <div>
              <label className="block text-xs font-medium text-slate-400 mb-1">Invoice Number</label>
              <input
                type="text"
                value={extractedData.invoiceNumber}
                onChange={(e) => setExtractedData({ ...extractedData, invoiceNumber: e.target.value })}
                className="w-full bg-slate-800 border border-slate-700 rounded-lg p-2 text-sm text-white font-mono"
              />
            </div>

            <div>
              <label className="block text-xs font-medium text-slate-400 mb-1">Category</label>
              <input
                type="text"
                value={extractedData.category}
                onChange={(e) => setExtractedData({ ...extractedData, category: e.target.value })}
                className="w-full bg-slate-800 border border-slate-700 rounded-lg p-2 text-sm text-white"
              />
            </div>

            <div>
              <label className="block text-xs font-medium text-slate-400 mb-1">Bill Date</label>
              <input
                type="date"
                value={extractedData.billDate}
                onChange={(e) => setExtractedData({ ...extractedData, billDate: e.target.value })}
                className="w-full bg-slate-800 border border-slate-700 rounded-lg p-2 text-sm text-white"
              />
            </div>

            <div>
              <label className="block text-xs font-medium text-slate-400 mb-1">Due Date</label>
              <input
                type="date"
                value={extractedData.dueDate || ''}
                onChange={(e) => setExtractedData({ ...extractedData, dueDate: e.target.value })}
                className="w-full bg-slate-800 border border-slate-700 rounded-lg p-2 text-sm text-white"
              />
            </div>

            <div>
              <label className="block text-xs font-medium text-slate-400 mb-1">Total Amount (INR)</label>
              <input
                type="number"
                value={extractedData.totalAmount}
                onChange={(e) => setExtractedData({ ...extractedData, totalAmount: e.target.value })}
                className="w-full bg-slate-800 border border-slate-700 rounded-lg p-2 text-sm text-white font-bold font-mono"
              />
            </div>
          </div>

          {/* Notes */}
          <div>
            <label className="block text-xs font-medium text-slate-400 mb-1">Submission Notes</label>
            <textarea
              rows={2}
              value={notes}
              onChange={(e) => setNotes(e.target.value)}
              placeholder="Optional notes or context for the approver..."
              className="w-full bg-slate-800 border border-slate-700 rounded-lg p-2 text-xs text-slate-200"
            />
          </div>

          {/* Confirmation Button */}
          <div className="flex justify-end pt-4 border-t border-slate-800">
            <button
              onClick={handleConfirmSubmission}
              disabled={submitting}
              className="px-6 py-2.5 bg-indigo-600 hover:bg-indigo-500 text-white font-semibold text-sm rounded-xl shadow-lg shadow-indigo-600/25 flex items-center space-x-2 transition-all"
            >
              <span>{submitting ? 'Submitting...' : 'Confirm & Submit Bill'}</span>
              <ArrowRight className="w-4 h-4" />
            </button>
          </div>
        </div>
      )}
    </div>
  );
}

