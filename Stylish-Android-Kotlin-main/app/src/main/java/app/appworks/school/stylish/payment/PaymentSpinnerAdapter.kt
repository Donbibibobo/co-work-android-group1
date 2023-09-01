package app.appworks.school.stylish.payment

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import app.appworks.school.stylish.databinding.ItemPaymentSpinnerBinding

/**
 * Created by Wayne Chen in Jul. 2019.
 */
class PaymentSpinnerAdapter(private val strings: Array<String>) : BaseAdapter() {

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val binding = ItemPaymentSpinnerBinding.inflate(LayoutInflater.from(parent?.context), parent, false)
        binding.title = strings[position]
        return binding.root
    }

    override fun getItem(position: Int): Any {
        return strings[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return strings.size
    }
}
