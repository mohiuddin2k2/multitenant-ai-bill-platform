import React, { useEffect, useState } from 'react';
import api from '../services/api';
import { useAuth } from '../context/AuthContext';
import AnomalyBadge from '../components/AnomalyBadge';
import { 
  CheckCircle, 
  XCircle, 
  Eye, 
  History, 
  Filter, 
  Search,
  AlertOctagon,
  Calendar,
  Building
} from 'lucide-react';

export default function BillsList() {
  const { isApprover, activeTenantId } = useAuth();
  const [bills, setBills] = useState([]);
  const [loading, setLoading] = useState(true);
  const [activeStatus, setActiveStatus] = useState('');
  const [selectedBill, setSelectedBill] = useState(null);
  const [auditBill, setAuditBill] = useState(null);
  const [rejectingBill, setRejectingBill] = useState(null);
  const [rejectionReason, setRejectionReason] = useState('');
  const [actionLoading, setActionLoading] = useState(false);

  useEffect(() => {
    fetchBills();
  }, [activeStatus, activeTenantId]);

  const fetchBills = () => {
    setLoading(true);
    let url = '/bills';
    if (activeStatus) {
      url += `?status=${activeStatus}`;
    }
    api.get(url)
      .then((res) => {
        setBills(res.data.content || []);
      })
      .catch(console.error)
      .finally(() => setLoading(false));
  };

  const handleApprove = async (billId) => {
    if (!window.confirm('Approve this bill for payment processing?')) return;
    setActionLoading(true);
    try {
      await api.patch(`/bills/${billId}/status`, {
        status: 'APPROVED',
        comment: 'Verified and approved by officer'
      });
      fetchBills();
    } catch (err) {
      alert(err.response?.data?.message || 'Approval failed');
    } finally {
      setActionLoading(false);
    }
  };

  const handleRejectSubmit = async (e) => {
    e.preventDefault();
    if (!rejectionReason.trim()) {
      alert('Please provide a rejection reason');
      return;
    }
    setActionLoading(true);
    try {
      await api.patch(`/bills/${rejectingBill.id}/status`, {
        status: 'REJECTED',
        rejectionReason: rejectionReason,
        comment: rejectionReason
      });
      setRejectingBill(null);
      setRejectionReason('');
      fetchBills();
    } catch (err) {
      alert(err.response?.data?.message || 'Rejection failed');
    } finally {
      setActionLoading(false);
    }
  };

  const formatCurrency = (val) => {
    return new Intl.NumberFormat('en-IN', {
      style: 'currency',
      currency: 'INR',
      maximumFractionDigits: 0,
    }).format(val || 0);
  };

  const getStatusBadge = (status) => {
    switch (status) {
      case 'APPROVED':
        return 'bg-emerald-500/15 text-emerald-400 border-emerald-500/30';
      case 'PAID':
        return 'bg-indigo-500/15 text-indigo-400 border-indigo-500/30';
      case 'PENDING':
        return 'bg-amber-500/15 text-amber-400 border-amber-500/30';
      case 'IN_REVIEW':
        return 'bg-sky-500/15 text-sky-400 border-sky-500/30';
      case 'REJECTED':
        return 'bg-rose-500/15 text-rose-400 border-rose-500/30';
      default:
        return 'bg-slate-500/15 text-slate-400 border-slate-500/30';
    }
  };

  return (
    <div className="space-y-6">
      {/* Header and Filter Tabs */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <h1 className="text-2xl font-bold text-white tracking-tight">Bills & Approval Pipeline</h1>
          <p className="text-slate-400 text-sm">Tenant-isolated bill lifecycle with RBAC approval enforcement</p>
        </div>

        {/* Status Filters */}
        <div className="flex flex-wrap gap-1.5 bg-slate-900 p-1 rounded-xl border border-slate-800 text-xs">
          {[
            { id: '', label: 'All Bills' },
            { id: 'PENDING', label: 'Pending' },
            { id: 'IN_REVIEW', label: 'In Review' },
            { id: 'APPROVED', label: 'Approved' },
            { id: 'REJECTED', label: 'Rejected' },
            { id: 'PAID', label: 'Paid' },
          ].map((tab) => (
            <button
              key={tab.id}
              onClick={() => setActiveStatus(tab.id)}
              className={`px-3 py-1.5 rounded-lg font-medium transition-all ${
                activeStatus === tab.id
                  ? 'bg-indigo-600 text-white shadow'
                  : 'text-slate-400 hover:text-slate-200 hover:bg-slate-800'
              }`}
            >
              {tab.label}
            </button>
          ))}
        </div>
      </div>

      {/* Bills Table */}
      <div className="bg-slate-900/60 border border-slate-800 rounded-2xl overflow-hidden shadow-xl">
        {loading ? (
          <div className="py-20 text-center text-slate-400">Loading bills...</div>
        ) : bills.length === 0 ? (
          <div className="py-20 text-center text-slate-400">
            <Building className="w-8 h-8 mx-auto text-slate-600 mb-2" />
            <p>No bills found matching this filter in the active tenant scope.</p>
          </div>
        ) : (
          <div className="overflow-x-auto">
            <table className="w-full text-left text-sm text-slate-300">
              <thead className="bg-slate-900/90 border-b border-slate-800 text-xs uppercase tracking-wider text-slate-400">
                <tr>
                  <th className="py-3.5 px-4 font-semibold">Invoice #</th>
                  <th className="py-3.5 px-4 font-semibold">Vendor & Category</th>
                  <th className="py-3.5 px-4 font-semibold">Bill Date</th>
                  <th className="py-3.5 px-4 font-semibold">Amount (INR)</th>
                  <th className="py-3.5 px-4 font-semibold">Status</th>
                  <th className="py-3.5 px-4 font-semibold">AI Diagnostic</th>
                  <th className="py-3.5 px-4 font-semibold text-right">Actions</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-slate-800">
                {bills.map((bill) => (
                  <tr key={bill.id} className="hover:bg-slate-800/40 transition-colors">
                    <td className="py-3.5 px-4 font-mono font-medium text-white">
                      {bill.invoiceNumber}
                      <div className="text-[11px] font-sans text-slate-500 font-normal">
                        By: {bill.submittedByUserName || 'System'}
                      </div>
                    </td>

                    <td className="py-3.5 px-4">
                      <div className="font-semibold text-slate-200">{bill.vendorName}</div>
                      <div className="text-xs text-slate-400">{bill.category}</div>
                    </td>

                    <td className="py-3.5 px-4 text-xs text-slate-400 font-mono">
                      {bill.billDate}
                    </td>

                    <td className="py-3.5 px-4 font-bold text-white font-mono">
                      {formatCurrency(bill.totalAmount)}
                    </td>

                    <td className="py-3.5 px-4">
                      <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-semibold border ${getStatusBadge(bill.status)}`}>
                        {bill.status}
                      </span>
                    </td>

                    <td className="py-3.5 px-4">
                      {bill.isAnomaly ? (
                        <AnomalyBadge 
                          isAnomaly={bill.isAnomaly} 
                          severity={bill.anomalySeverity} 
                          reasons={bill.anomalyReasons} 
                        />
                      ) : (
                        <span className="text-xs text-slate-500 flex items-center">
                          <CheckCircle className="w-3.5 h-3.5 text-emerald-500/70 mr-1" />
                          Passed Check
                        </span>
                      )}
                    </td>

                    <td className="py-3.5 px-4 text-right">
                      <div className="flex items-center justify-end space-x-2">
                        {/* View Details */}
                        <button
                          onClick={() => setSelectedBill(bill)}
                          title="View Bill Details"
                          className="p-1.5 rounded-lg text-slate-400 hover:text-indigo-400 hover:bg-slate-800"
                        >
                          <Eye className="w-4 h-4" />
                        </button>

                        {/* View Audit Trail */}
                        <button
                          onClick={() => setAuditBill(bill)}
                          title="View Audit Trail"
                          className="p-1.5 rounded-lg text-slate-400 hover:text-cyan-400 hover:bg-slate-800"
                        >
                          <History className="w-4 h-4" />
                        </button>

                        {/* Approver / Admin Actions */}
                        {isApprover && (bill.status === 'PENDING' || bill.status === 'IN_REVIEW') && (
                          <>
                            <button
                              onClick={() => handleApprove(bill.id)}
                              disabled={actionLoading}
                              title="Approve Bill"
                              className="px-2.5 py-1 rounded-lg text-xs font-semibold bg-emerald-600/20 text-emerald-400 border border-emerald-500/30 hover:bg-emerald-600/30 transition-all"
                            >
                              Approve
                            </button>
                            <button
                              onClick={() => setRejectingBill(bill)}
                              disabled={actionLoading}
                              title="Reject Bill"
                              className="px-2.5 py-1 rounded-lg text-xs font-semibold bg-rose-600/20 text-rose-400 border border-rose-500/30 hover:bg-rose-600/30 transition-all"
                            >
                              Reject
                            </button>
                          </>
                        )}
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>

      {/* Bill Details Modal */}
      {selectedBill && (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/70 backdrop-blur-sm">
          <div className="bg-slate-900 border border-slate-800 rounded-2xl w-full max-w-2xl max-h-[90vh] overflow-y-auto p-6 shadow-2xl">
            <div className="flex items-center justify-between border-b border-slate-800 pb-4 mb-4">
              <div>
                <h3 className="text-lg font-bold text-white">Invoice Details</h3>
                <p className="text-xs text-slate-400">Invoice #{selectedBill.invoiceNumber} • Tenant #{selectedBill.tenantId}</p>
              </div>
              <button onClick={() => setSelectedBill(null)} className="text-slate-400 hover:text-white">✕</button>
            </div>

            <div className="grid grid-cols-2 gap-4 text-xs mb-6">
              <div>
                <span className="text-slate-500">Vendor:</span>
                <p className="text-sm font-semibold text-slate-200">{selectedBill.vendorName}</p>
                <p className="text-slate-400">Tax ID: {selectedBill.vendorTaxId || 'N/A'}</p>
              </div>
              <div>
                <span className="text-slate-500">Category & Dates:</span>
                <p className="text-sm font-semibold text-slate-200">{selectedBill.category}</p>
                <p className="text-slate-400">Date: {selectedBill.billDate} {selectedBill.dueDate ? `• Due: ${selectedBill.dueDate}` : ''}</p>
              </div>
            </div>

            {selectedBill.isAnomaly && (
              <div className="p-3 mb-6 bg-rose-500/10 border border-rose-500/30 rounded-xl text-xs text-rose-300">
                <span className="font-bold flex items-center mb-1">
                  <AlertOctagon className="w-4 h-4 mr-1 text-rose-400" />
                  Anomaly Diagnostics:
                </span>
                <p>{selectedBill.anomalyReasons}</p>
              </div>
            )}

            {/* Line Items Table */}
            <div className="mb-6">
              <h4 className="text-xs font-semibold text-slate-400 uppercase tracking-wider mb-2">Itemized Breakdown</h4>
              <div className="border border-slate-800 rounded-xl overflow-hidden">
                <table className="w-full text-left text-xs">
                  <thead className="bg-slate-800/60 text-slate-400">
                    <tr>
                      <th className="p-2.5">Description</th>
                      <th className="p-2.5 text-right">Qty</th>
                      <th className="p-2.5 text-right">Unit Price</th>
                      <th className="p-2.5 text-right">Total</th>
                    </tr>
                  </thead>
                  <tbody className="divide-y divide-slate-800 text-slate-300">
                    {selectedBill.items?.map((it, idx) => (
                      <tr key={idx}>
                        <td className="p-2.5">{it.description}</td>
                        <td className="p-2.5 text-right font-mono">{it.quantity}</td>
                        <td className="p-2.5 text-right font-mono">{formatCurrency(it.unitPrice)}</td>
                        <td className="p-2.5 text-right font-mono font-semibold">{formatCurrency(it.totalPrice)}</td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </div>

            <div className="flex justify-between items-center pt-4 border-t border-slate-800 text-sm">
              <span className="text-slate-400">Total Invoice Amount:</span>
              <span className="text-xl font-extrabold text-white font-mono">{formatCurrency(selectedBill.totalAmount)}</span>
            </div>
          </div>
        </div>
      )}

      {/* Rejection Reason Modal */}
      {rejectingBill && (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/70 backdrop-blur-sm">
          <div className="bg-slate-900 border border-slate-800 rounded-2xl w-full max-w-md p-6 shadow-2xl">
            <h3 className="text-base font-bold text-white mb-2">Reject Invoice #{rejectingBill.invoiceNumber}</h3>
            <p className="text-xs text-slate-400 mb-4">Please provide a mandatory explanation for rejecting this bill submission.</p>

            <form onSubmit={handleRejectSubmit}>
              <textarea
                value={rejectionReason}
                onChange={(e) => setRejectionReason(e.target.value)}
                placeholder="Reason for rejection (e.g., duplicate charges, vendor discrepancies, missing documentation)..."
                required
                rows={4}
                className="w-full bg-slate-800 border border-slate-700 rounded-xl p-3 text-xs text-slate-200 focus:outline-none focus:border-rose-500 mb-4"
              />

              <div className="flex justify-end space-x-2">
                <button
                  type="button"
                  onClick={() => setRejectingBill(null)}
                  className="px-4 py-2 rounded-xl text-xs font-semibold text-slate-400 hover:bg-slate-800"
                >
                  Cancel
                </button>
                <button
                  type="submit"
                  disabled={actionLoading}
                  className="px-4 py-2 rounded-xl text-xs font-semibold bg-rose-600 hover:bg-rose-500 text-white"
                >
                  Confirm Rejection
                </button>
              </div>
            </form>
          </div>
        </div>
      )}

      {/* Audit Log Modal */}
      {auditBill && (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/70 backdrop-blur-sm">
          <div className="bg-slate-900 border border-slate-800 rounded-2xl w-full max-w-lg p-6 shadow-2xl">
            <div className="flex items-center justify-between border-b border-slate-800 pb-3 mb-4">
              <h3 className="text-base font-bold text-white flex items-center gap-2">
                <History className="w-4 h-4 text-cyan-400" />
                <span>Audit Trail • Invoice #{auditBill.invoiceNumber}</span>
              </h3>
              <button onClick={() => setAuditBill(null)} className="text-slate-400 hover:text-white">✕</button>
            </div>

            <div className="space-y-4 max-h-80 overflow-y-auto">
              {auditBill.auditLogs?.length === 0 ? (
                <p className="text-xs text-slate-400">No audit events recorded yet.</p>
              ) : (
                auditBill.auditLogs?.map((log) => (
                  <div key={log.id} className="p-3 bg-slate-800/40 border border-slate-700/60 rounded-xl text-xs space-y-1">
                    <div className="flex justify-between items-center">
                      <span className="font-semibold text-indigo-300">{log.action}</span>
                      <span className="text-[10px] text-slate-400 font-mono">{log.timestamp}</span>
                    </div>
                    <p className="text-slate-300">{log.comment}</p>
                  </div>
                ))
              )}
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

