package `in`.devco.cr.ui.crimelist

import `in`.devco.cr.R
import `in`.devco.cr.data.model.Report
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_report.view.*

class ReportAdapter : RecyclerView.Adapter<ReportAdapter.ViewHolder>() {
    private val list = ArrayList<Report>()
    private var listener: AdapterListener? = null

    fun setList(list: List<Report>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    fun setListener(listener: AdapterListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_report, parent, false))

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]

        holder.itemView.descriptionTextView.text = item.description
        holder.itemView.statusTextView.text = item.status

        holder.itemView.attachButton.setOnClickListener { listener?.alert(item.id) }
        holder.itemView.acceptButton.setOnClickListener { listener?.accept(item) }
        holder.itemView.setOnClickListener {
            if (item.files.isNotEmpty()) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(item.files.first()))
                holder.itemView.context.startActivity(intent)
            }
        }
    }

    interface AdapterListener {
        fun accept(report: Report)
        fun alert(reportId: String)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
}